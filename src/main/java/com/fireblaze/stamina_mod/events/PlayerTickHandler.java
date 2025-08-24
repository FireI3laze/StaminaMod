package com.fireblaze.stamina_mod.events;

import com.fireblaze.stamina_mod.capability.StaminaProvider;
import com.fireblaze.stamina_mod.comfort.ComfortProvider;
import com.fireblaze.stamina_mod.comfort.ComfortUtils;
import com.fireblaze.stamina_mod.config.Settings;
import com.fireblaze.stamina_mod.config.StaminaConfig;
import com.fireblaze.stamina_mod.entity.SeatEntity;
import com.fireblaze.stamina_mod.networking.ModMessages;
import com.fireblaze.stamina_mod.networking.packet.StaminaDataSyncS2CPacket;
import com.fireblaze.stamina_mod.comfort.ComfortCalculator;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

import static net.minecraft.world.item.ArmorMaterials.*;

@Mod.EventBusSubscriber
public class PlayerTickHandler {
    private static final Map<UUID, Float> shortStaminaMap = new HashMap<>();
    private static final Map<UUID, Float> longStaminaMap = new HashMap<>();
    private static final Map<UUID, Float> maxStaminaMap = new HashMap<>();
    private static final Map<UUID, Float> staminaExpMap = new HashMap<>();
    private static final Map<UUID, Integer> staminaLvlMap = new HashMap<>();

    private static final Map<UUID, Double> lastPlayerPosX = new HashMap<>();
    private static final Map<UUID, Double> lastPlayerPosZ = new HashMap<>();
    private static final Map<UUID, BlockPos> miningPlayers = new HashMap<>();
    private static final Map<UUID, Integer> tickCounter = new HashMap<>();
    private static final int tickMultiplier = 5;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide()) return;

        Player player = event.player;
        UUID id = player.getUUID();

        // Tick-Counter hochzählen
        int counter = tickCounter.getOrDefault(id, 0) + 1;
        tickCounter.put(id, counter);

        // Nur alle 5 Ticks ausführen
        if (counter < tickMultiplier) return;
        tickCounter.put(id, 0); // reset

        player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
            if (player.isSprinting()) {
                stamina.consume(
                        (float) Settings.getMovementCost("sprint", true) * tickMultiplier,
                        (float) Settings.getMovementCost("sprint", false) * tickMultiplier,
                        getArmorStaminaMultiplier(player, "movement"), player
                );
            } else if (isPlayerWalking(player)) {
                stamina.consume(
                        (float) Settings.getMovementCost("walk", true) * tickMultiplier,
                        (float) Settings.getMovementCost("walk", false) * tickMultiplier,
                        getArmorStaminaMultiplier(player, "movement"), player
                );
            }

            if (player.isCrouching() || player.isSwimming()) {
                stamina.consume(
                        (float) Settings.getMovementCost("crouch", true) * tickMultiplier,
                        (float) Settings.getMovementCost("crouch", false) * tickMultiplier,
                        getArmorStaminaMultiplier(player, "movement"), player
                );
            }

            if (player.getDeltaMovement().y > 0 && !player.onGround()) {
                stamina.consume(
                        (float) Settings.getMovementCost("jump", true) * tickMultiplier,
                        (float) Settings.getMovementCost("jump", false) * tickMultiplier,
                        getArmorStaminaMultiplier(player, "movement"), player
                );
            }

            if (player.isBlocking()) {
                stamina.consume(
                        (float) Settings.getCombatCost("block", true) * tickMultiplier,
                        (float) Settings.getCombatCost("block", false) * tickMultiplier,
                        getArmorStaminaMultiplier(player, "action"), player
                );
            }

            // Mining-Stamina (mit Faktor 5)
            BlockPos blockPos = miningPlayers.get(id);
            if (blockPos != null) {
                BlockState blockState = player.level().getBlockState(blockPos);
                if (!blockState.isAir()) {
                    float hardness = blockState.getDestroySpeed(player.level(), blockPos);
                    ItemStack heldItem = player.getMainHandItem();
                    // float armorFactor = 1.0f;
                    // if (!(StaminaConfig.POTATO_MODE.get() || StaminaConfig.ARMOR_DISABLED.get())) armorFactor = getArmorStaminaMultiplier(player, "action");
                    float armorFactor = getArmorStaminaMultiplier(player, "action");
                    if (heldItem.isCorrectToolForDrops(blockState)) {
                        if (blockState.is(BlockTags.MINEABLE_WITH_PICKAXE))
                            stamina.consume((float) Settings.getMiningCost("pickaxe", true) * tickMultiplier,
                                    (float) Settings.getMiningCost("pickaxe", false) * tickMultiplier,
                                    armorFactor, hardness, player);
                        else if (blockState.is(BlockTags.MINEABLE_WITH_AXE))
                            stamina.consume((float) Settings.getMiningCost("axe", true) * tickMultiplier,
                                    (float) Settings.getMiningCost("axe", false) * tickMultiplier,
                                    armorFactor, hardness, player);
                        else if (blockState.is(BlockTags.MINEABLE_WITH_SHOVEL))
                            stamina.consume((float) Settings.getMiningCost("shovel", true) * tickMultiplier,
                                    (float) Settings.getMiningCost("shovel", false) * tickMultiplier,
                                    armorFactor, hardness, player);
                        else if (blockState.is(BlockTags.MINEABLE_WITH_HOE))
                            stamina.consume((float) Settings.getMiningCost("hoe", true) * tickMultiplier,
                                    (float) Settings.getMiningCost("hoe", false) * tickMultiplier,
                                    armorFactor, hardness, player);
                        else
                            stamina.consume((float) Settings.getMiningCost("unknown", true) * tickMultiplier,
                                    (float) Settings.getMiningCost("unknown", false) * tickMultiplier,
                                    armorFactor, hardness, player);
                    } else {
                        // Faust / falsches Werkzeug
                        stamina.consume((float) Settings.getMiningCost("hand", true) * tickMultiplier,
                                (float) Settings.getMiningCost("hand", false) * tickMultiplier,
                                1f, hardness, player);
                    }
                }
            }

            // Potion-Effekte & Synchronisation (alle 5 Ticks)
            float shortStam = stamina.getShortStamina();
            float longStam = stamina.getLongStamina();

            // Negative / Positive Effects
            if (shortStam <= Settings.getNegativeEffect3Threshold())
                applyNegativeEffects(player, 2);
            else if (shortStam <= Settings.getNegativeEffect2Threshold())
                applyNegativeEffects(player, 1);
            else if (shortStam <= Settings.getNegativeEffect1Threshold())
                applyNegativeEffects(player, 0);
            else if (shortStam >= Settings.getPositiveEffectThreshold())
                applyPositiveEffects(player, 0);

            stamina.tick(player, tickMultiplier);

            // Stamina Synchronisation
            ModMessages.sendToPlayer(new StaminaDataSyncS2CPacket(
                    shortStam, longStam, stamina.getLongStaminaCap(), stamina.getStaminaExp(), stamina.getStaminaLvl()
            ), (ServerPlayer) player);

        });
    }

    // -- Hilfsmethoden --
    private static boolean isPlayerWalking(Player player) {
        UUID id = player.getUUID();
        double x = player.getX();
        double z = player.getZ();
        double lastX = lastPlayerPosX.getOrDefault(id, x);
        double lastZ = lastPlayerPosZ.getOrDefault(id, z);
        double distanceMoved = Math.sqrt(Math.pow(x - lastX, 2) + Math.pow(z - lastZ, 2));

        lastPlayerPosX.put(id, x);
        lastPlayerPosZ.put(id, z);

        return distanceMoved > 0.001 && !player.isSprinting();
    }

    public static void applyNegativeEffects(Player player, int amplifier) {
        int duration = 100;
        refreshEffect(player, MobEffects.MOVEMENT_SLOWDOWN, amplifier, duration);
        refreshEffect(player, MobEffects.DIG_SLOWDOWN, amplifier, duration);
        refreshEffect(player, MobEffects.WEAKNESS, amplifier, duration);

        if (amplifier >= 1) refreshEffect(player, MobEffects.HUNGER, 0, duration);
        if (amplifier == 2) {
            refreshEffect(player, MobEffects.BLINDNESS, 0, duration);
            refreshEffect(player, MobEffects.JUMP, 200, duration);
            Objects.requireNonNull(player.getAttribute(Attributes.JUMP_STRENGTH)).setBaseValue(Objects.requireNonNull(player.getAttribute(Attributes.JUMP_STRENGTH)).getBaseValue() - 0.5);
        }
    }

    public static void applyPositiveEffects(Player player, int amplifier) {
        int duration = 100;
        refreshEffect(player, MobEffects.MOVEMENT_SPEED, amplifier, duration);
        refreshEffect(player, MobEffects.DIG_SPEED, amplifier, duration);
        refreshEffect(player, MobEffects.DAMAGE_BOOST, amplifier, duration);
    }

    private static void refreshEffect(Player player, MobEffect effect, int amplifier, int duration) {
        MobEffectInstance current = player.getEffect(effect);
        if (current == null || current.getDuration() <= 40 || current.getAmplifier() != amplifier) {
            player.addEffect(new MobEffectInstance(effect, duration, amplifier, false, false));
        }
    }

    public static float getArmorStaminaMultiplier(Player player, String activity) {
        float multiplier = 1.0f;

        if (Objects.equals(activity, "action")) {
            ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
            ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
            ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
            ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
            if (helmet.getItem() instanceof ArmorItem helmetArmor) {
                ArmorMaterial material = helmetArmor.getMaterial();
                multiplier += pieceMultiplier(materialMultiplier(material), (float) Settings.getArmorCost("helmetaction"));
            }
            if (chest.getItem() instanceof ArmorItem chestArmor) {
                ArmorMaterial material = chestArmor.getMaterial();
                multiplier += pieceMultiplier(materialMultiplier(material), (float) Settings.getArmorCost("chestplateaction"));
            }
            if (legs.getItem() instanceof ArmorItem legArmor) {
                ArmorMaterial material = legArmor.getMaterial();
                multiplier += pieceMultiplier(materialMultiplier(material), (float) Settings.getArmorCost("leggingsaction"));
            }
            if (boots.getItem() instanceof ArmorItem bootArmor) {
                ArmorMaterial material = bootArmor.getMaterial();
                multiplier += pieceMultiplier(materialMultiplier(material), (float) Settings.getArmorCost("bootsaction"));
            }
        } else if (Objects.equals(activity, "movement")) {
            ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
            ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
            ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
            ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
            if (helmet.getItem() instanceof ArmorItem helmetArmor) {
                ArmorMaterial material = helmetArmor.getMaterial();
                multiplier += pieceMultiplier(materialMultiplier(material), (float) Settings.getArmorCost("helmetmovement"));
            }
            if (chest.getItem() instanceof ArmorItem chestArmor) {
                ArmorMaterial material = chestArmor.getMaterial();
                multiplier += pieceMultiplier(materialMultiplier(material), (float) Settings.getArmorCost("chestplatemovement"));
            }
            if (legs.getItem() instanceof ArmorItem legArmor) {
                ArmorMaterial material = legArmor.getMaterial();
                multiplier += pieceMultiplier(materialMultiplier(material), (float) Settings.getArmorCost("leggingsmovement"));
            }
            if (boots.getItem() instanceof ArmorItem bootArmor) {
                ArmorMaterial material = bootArmor.getMaterial();
                multiplier += pieceMultiplier(materialMultiplier(material), (float) Settings.getArmorCost("bootsmovement"));
            }
        }
        return multiplier;
    }
    public static float pieceMultiplier(float multiplier, float relevanceFactor) {
        return multiplier * relevanceFactor;
    }

    public static float materialMultiplier(ArmorMaterial material) {
        if (material.equals(LEATHER)) {
            return (float) Settings.getArmorCost("leather");
        } else if (material.equals(CHAIN)) {
            return (float) Settings.getArmorCost("chain");
        } else if (material.equals(IRON)) {
            return (float) Settings.getArmorCost("iron");
        } else if (material.equals(GOLD)) {
            return (float) Settings.getArmorCost("gold");
        } else if (material.equals(DIAMOND)) {
            return (float) Settings.getArmorCost("diamond");
        } else if (material.equals(NETHERITE)) {
            return (float) Settings.getArmorCost("netherite");
        } else return (float) Settings.getArmorCost("unknown");
    }


    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
            stamina.consume((float) Settings.getCombatCost("hit", true), (float) Settings.getCombatCost("hit", false), getArmorStaminaMultiplier(player, "action"), player);
            System.out.println("hit enemy and consumed " + Settings.getCombatCost("hit", true) + " Stamina");
        });
    }
    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            float damage = event.getAmount();

            player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
                // stamina.consume(damage, 0.1f * damage, 1, player);
                stamina.consume((float) Settings.getCombatCost("hit", true) * damage, (float) Settings.getCombatCost("hit", false) * damage, 0, player);
            });
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
            BlockState blockState = event.getLevel().getBlockState(event.getPos());
            if (!blockState.isAir()) {
                // stamina.consume(0.175f, 0.015f, 0, player);
                stamina.consume((float) Settings.getInteractCost("click", true), (float) Settings.getInteractCost("click", false), getArmorStaminaMultiplier(player, "action"), player);
            }
        });
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide()) {
            miningPlayers.put(player.getUUID(), event.getPos());
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        miningPlayers.remove(event.getPlayer().getUUID());
    }

    @SubscribeEvent
    public static void onPlayerStopSwing(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide()) return;
        if (!event.player.swinging) miningPlayers.remove(event.player.getUUID());
    }

    @SubscribeEvent
    public static void onPlayerTrySleep(PlayerSleepInBedEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (player.isCreative() || player.isSpectator()) return;

        double comfortThreshold = Settings.getComfortThresholdSleep();
        ComfortCalculator.ComfortResult comfortResult = ComfortCalculator.calculateComfort((ServerLevel) player.level(), player);
        double comfort = comfortResult.comfort;
        List<String> issues = comfortResult.issues;

        if (comfort < comfortThreshold) {
            event.setResult(Player.BedSleepingProblem.OTHER_PROBLEM);
            player.displayClientMessage(ComfortUtils.getComfortMessage(player, comfort, comfortThreshold, issues), true);
        } else {
            player.displayClientMessage(ComfortUtils.formatComfort(comfort), true);
        }
    }


    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        float armDamage = 0;
        player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
            // stamina.consume(0.0025f, 0.0005f, armDamage, player);
            stamina.consume((float) (Settings.getInteractCost("place", true) - Settings.getInteractCost("click", true)), (float) (Settings.getInteractCost("place", false) - Settings.getInteractCost("click", false)), getArmorStaminaMultiplier(player, "action"), player);
        });
    }

    @SubscribeEvent
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player)) return;

        ItemStack stack = event.getItem();
        Item item = stack.getItem();

        // Prüfen, ob es ein Nahrungsmittel ist
        if (item.isEdible()) {
            FoodProperties food = item.getFoodProperties();

            if (food != null) {
                int nutrition = food.getNutrition();              // Hungerpunkte
                float saturation = food.getSaturationModifier();  // Sättigungswert

                // Stamina erhöhen
                player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
                    stamina.foodEaten(player, nutrition, saturation);
                });
            }
        }
    }

    private static final Map<Entity, Vec3> lastPositions = new HashMap<>();
    private static final Map<UUID, Integer> vehicleTickCounter = new HashMap<>();
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
            UUID id = player.getUUID();

            int counter = vehicleTickCounter.getOrDefault(id, 0) + 1;
            vehicleTickCounter.put(id, counter);
            if (counter < tickMultiplier) continue;
            vehicleTickCounter.put(id, 0);

            Entity vehicle = player.getVehicle();
            if (vehicle == null) continue;

            if (vehicle instanceof SeatEntity) {
                return;
            }
            else if (vehicle instanceof Boat || vehicle instanceof AbstractHorse) {
                Vec3 lastPos = lastPositions.get(vehicle);
                Vec3 currentPos = vehicle.position();

                if (lastPos == null || !lastPos.equals(currentPos)) {
                    // Entity bewegt sich
                    player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
                        if (vehicle instanceof AbstractHorse) {
                            stamina.consume(
                                    (float) Settings.getMovementCost("horseRide", true) * tickMultiplier,
                                    (float) Settings.getMovementCost("horseRide", false) * tickMultiplier,
                                    getArmorStaminaMultiplier(player, "action"), player
                            );
                        }

                        if (vehicle instanceof Boat) {
                            stamina.consume(
                                    (float) Settings.getMovementCost("boatDrive", true) * tickMultiplier,
                                    (float) Settings.getMovementCost("boatDrive", false) * tickMultiplier,
                                    getArmorStaminaMultiplier(player, "action"), player
                            );
                        }
                    });
                }

                // Position für nächsten Tick speichern
                lastPositions.put(vehicle, currentPos);
            }
            // 3. Modded Sitz
            else {
                // Nur anwenden, wenn Komfort >= Threshold
                player.getCapability(ComfortProvider.COMFORT_CAP).ifPresent(cap -> {
                    if (cap.getComfortLevel() >= Settings.getComfortThresholdSit()) {
                        applyRest(player);
                    }
                });
            }
        }
    }

    // Hilfsmethode für Rest-Funktion
    private static void applyRest(Player player) {
        player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
            player.getCapability(ComfortProvider.COMFORT_CAP).ifPresent(cap -> {
                double comfort = cap.getComfortLevel();
                double hardnessFactor = 1.0f / 0.5;
                stamina.rest(player, (float)(comfort * Settings.getRegenerationConfigs("comfortRegMultiplier")), (float) (hardnessFactor / 1.5));
            });
        });
    }
}
