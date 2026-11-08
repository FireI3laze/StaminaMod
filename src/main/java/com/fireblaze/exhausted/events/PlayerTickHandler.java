package com.fireblaze.exhausted.events;

import com.fireblaze.exhausted.ModTags;
import com.fireblaze.exhausted.ModifierUtils;
import com.fireblaze.exhausted.capability.StaminaProvider;
import com.fireblaze.exhausted.comfort.ComfortProvider;
import com.fireblaze.exhausted.comfort.ComfortUtils;
import com.fireblaze.exhausted.config.ClientConfig;
import com.fireblaze.exhausted.config.CommonConfig;
import com.fireblaze.exhausted.config.Settings;
import com.fireblaze.exhausted.entity.SeatEntity;
import com.fireblaze.exhausted.networking.ModMessages;
import com.fireblaze.exhausted.networking.packet.StaminaDataSyncS2CPacket;
import com.fireblaze.exhausted.comfort.ComfortCalculator;
import com.fireblaze.exhausted.Sounds.PlaySoundPacket;
import com.fireblaze.exhausted.utils.FoodStaminaRegeneration;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
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
    private static final Map<UUID, Double> lastPlayerPosX = new HashMap<>();
    private static final Map<UUID, Double> lastPlayerPosZ = new HashMap<>();
    private static final Map<UUID, BlockPos> miningPlayers = new HashMap<>();
    private static final Map<UUID, Integer> tickCounter = new HashMap<>();
    private static float previousTickShortStam = 0;
    private static float previousTickLongStam = 0;
    private static int currentTick = 0;
    public static final int tickMultiplier = 5;

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

            /*
            if (player.getDeltaMovement().y > 0 && !player.onGround() && !player.isInWater()) {
                stamina.consume(
                        (float) Settings.getMovementCost("jump", true) * tickMultiplier,
                        (float) Settings.getMovementCost("jump", false) * tickMultiplier,
                        getArmorStaminaMultiplier(player, "movement"), player
                );
            }
            */

            if (player.isBlocking()) {
                stamina.consume(
                        (float) Settings.getCombatCost("block", true) * tickMultiplier,
                        (float) Settings.getCombatCost("block", false) * tickMultiplier,
                        getArmorStaminaMultiplier(player, "action"), player
                );
            }

            // Mining-Stamina (mit Faktor 5)

            // Mining-Stamina (mit Faktor 5)
            BlockPos blockPos = miningPlayers.get(id);

            if (blockPos != null) {
                BlockState blockState = player.level().getBlockState(blockPos);

                if (!blockState.isAir()) {
                    float hardness = blockState.getDestroySpeed(player.level(), blockPos);
                    ItemStack heldItem = player.getMainHandItem();
                    float armorFactor = getArmorStaminaMultiplier(player, "action");

                    // Werkzeug-Kategorie des Blocks bestimmen
                    String toolType = null;

                    if (blockState.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
                        toolType = "pickaxe";
                    } else if (blockState.is(BlockTags.MINEABLE_WITH_AXE)) {
                        toolType = "axe";
                    } else if (blockState.is(BlockTags.MINEABLE_WITH_SHOVEL)) {
                        toolType = "shovel";
                    } else if (blockState.is(BlockTags.MINEABLE_WITH_HOE)) {
                        toolType = "hoe";
                    }

                    if (heldItem.isCorrectToolForDrops(blockState)) {
                        // Korrektes Werkzeug: Kosten der Werkzeug-Kategorie
                        String costType = toolType != null ? toolType : "unknown";

                        stamina.consume(
                                (float) Settings.getMiningCost(costType, true) * tickMultiplier,
                                (float) Settings.getMiningCost(costType, false) * tickMultiplier,
                                armorFactor,
                                hardness,
                                player
                        );
                    } else if (toolType != null) {
                        // Falsches Werkzeug für einen Block mit Werkzeug-Kategorie
                        stamina.consume(
                                (float) Settings.getMiningCost("hand", true) * tickMultiplier,
                                (float) Settings.getMiningCost("hand", false) * tickMultiplier,
                                1f,
                                hardness,
                                player
                        );
                    } else {
                        // Block hat keine Werkzeug-Kategorie, z. B. Glas
                        stamina.consume(
                                (float) Settings.getMiningCost("unknown", true) * tickMultiplier,
                                (float) Settings.getMiningCost("unknown", false) * tickMultiplier,
                                armorFactor,
                                hardness,
                                player
                        );
                    }
                }
            }

            float shortStam = stamina.getShortStamina();
            float longStam = stamina.getLongStamina();

            int notificationThreshold = CommonConfig.DYNAMIC_COMFORT_REQUIREMENT_STAMINA_THRESHOLD.get();

            if (longStam < notificationThreshold && previousTickLongStam > notificationThreshold && CommonConfig.DYNAMIC_COMFORT_REQUIREMENT_CAP.get() && ClientConfig.EXHAUSTION_THRESHOLD.get()) {
                player.displayClientMessage(
                        Component.literal(ClientConfig.EXHAUSTION_THRESHOLD_TEXT.get()),
                        true
                );
            }
            previousTickLongStam = longStam;

            // Negative / Positive Effects
            // Tick-Loop:
            int newThreshold = -1;
            AttributeInstance speed = player.getAttribute(Attributes.MOVEMENT_SPEED);

            // Gültige UUIDs für jeden Modifier
            UUID SPEED_MALUS_3_UUID = UUID.fromString("33333333-3333-3333-3333-333333333333");
            UUID SPEED_MALUS_2_UUID = UUID.fromString("22222222-2222-2222-2222-222222222222");
            UUID SPEED_MALUS_1_UUID = UUID.fromString("11111111-1111-1111-1111-111111111111");
            UUID SPEED_BONUS_1_UUID = UUID.fromString("44444444-4444-4444-4444-444444444444");

            // Prüfen, ob Modifier schon vorhanden sind
            assert speed != null;
            boolean hasMalus3 = speed.getModifiers().stream().anyMatch(mod -> mod.getId().equals(SPEED_MALUS_3_UUID));
            boolean hasMalus2 = speed.getModifiers().stream().anyMatch(mod -> mod.getId().equals(SPEED_MALUS_2_UUID));
            boolean hasMalus1 = speed.getModifiers().stream().anyMatch(mod -> mod.getId().equals(SPEED_MALUS_1_UUID));
            boolean hasBonus1 = speed.getModifiers().stream().anyMatch(mod -> mod.getId().equals(SPEED_BONUS_1_UUID));

            int hungerDrainInterval = 40;
            currentTick++;

            if (shortStam <= Settings.getNegativeEffect3Threshold()) {
                newThreshold = 2;
                hungerSimulation(player, hungerDrainInterval);
                applyNegativeEffects(player);
                if (!hasMalus3) {
                    hungerSimulation(player, CommonConfig.NEGATIVE_EFFECT_3_HUNGER_DRAIN_INTERVAL.get());
                    ModifierUtils.addCustomModifier(player, Attributes.MOVEMENT_SPEED, "Speed malus 3", CommonConfig.NEGATIVE_EFFECT_3_MOVEMENT_SPEED_PERCENTAGE.get(), AttributeModifier.Operation.MULTIPLY_TOTAL, SPEED_MALUS_3_UUID);
                    ModifierUtils.addCustomModifier(player, Attributes.ATTACK_DAMAGE, "Damage malus 3", CommonConfig.NEGATIVE_EFFECT_3_DAMAGE_PERCENTAGE.get(), AttributeModifier.Operation.MULTIPLY_TOTAL);
                }
            } else if (shortStam <= Settings.getNegativeEffect2Threshold()) {
                newThreshold = 1;
                hungerSimulation(player, CommonConfig.NEGATIVE_EFFECT_2_HUNGER_DRAIN_INTERVAL.get());
                if (!hasMalus2) {
                    ModifierUtils.removeAllCustomModifiers(player);
                    ModifierUtils.addCustomModifier(player, Attributes.MOVEMENT_SPEED, "Speed malus 2", CommonConfig.NEGATIVE_EFFECT_2_MOVEMENT_SPEED_PERCENTAGE.get(), AttributeModifier.Operation.MULTIPLY_TOTAL, SPEED_MALUS_2_UUID);
                    ModifierUtils.addCustomModifier(player, Attributes.ATTACK_DAMAGE, "Damage malus 2", CommonConfig.NEGATIVE_EFFECT_2_DAMAGE_PERCENTAGE.get(), AttributeModifier.Operation.MULTIPLY_TOTAL);
                }
            } else if (shortStam <= Settings.getNegativeEffect1Threshold()) {
                newThreshold = 0;
                if (!hasMalus1) {
                    ModifierUtils.removeAllCustomModifiers(player);
                    ModifierUtils.addCustomModifier(player, Attributes.MOVEMENT_SPEED, "Speed malus 1", CommonConfig.NEGATIVE_EFFECT_1_MOVEMENT_SPEED_PERCENTAGE.get(), AttributeModifier.Operation.MULTIPLY_TOTAL, SPEED_MALUS_1_UUID);
                    ModifierUtils.addCustomModifier(player, Attributes.ATTACK_DAMAGE, "Damage malus 1", CommonConfig.NEGATIVE_EFFECT_1_DAMAGE_PERCENTAGE.get(), AttributeModifier.Operation.MULTIPLY_TOTAL);
                }
            } else if (shortStam >= Settings.getPositiveEffectThreshold()) {
                if (!hasBonus1) {
                    ModifierUtils.removeAllCustomModifiers(player);
                    ModifierUtils.addCustomModifier(player, Attributes.MOVEMENT_SPEED, "Speed bonus 1", CommonConfig.POSITIVE_EFFECT_SPEED_PERCENTAGE.get(), AttributeModifier.Operation.MULTIPLY_TOTAL, SPEED_BONUS_1_UUID);
                    ModifierUtils.addCustomModifier(player, Attributes.ATTACK_DAMAGE, "Damage bonus 1", CommonConfig.POSITIVE_EFFECT_DAMAGE_FLAT.get(), AttributeModifier.Operation.ADDITION);
                }
            } else {
                ModifierUtils.removeAllCustomModifiers(player);
            }

            ModMessages.sendToPlayer(new PlaySoundPacket(newThreshold), (ServerPlayer) player);
            stamina.tick(player, tickMultiplier);

            if (previousTickShortStam != shortStam) {
                ModMessages.sendToPlayer(new StaminaDataSyncS2CPacket(
                        shortStam, longStam, stamina.getLongStaminaCap(), stamina.getStaminaExp(), stamina.getStaminaLvl()
                ), (ServerPlayer) player);

                previousTickShortStam = shortStam;
            }
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

    public static void applyNegativeEffects(Player player) {
        int duration = 100;
        if (CommonConfig.NEGATIVE_EFFECT_3_BLINDNESS.get()) refreshEffect(player, MobEffects.BLINDNESS, 0, duration);
    }

    private static void refreshEffect(Player player, MobEffect effect, int amplifier, int duration) {
        MobEffectInstance current = player.getEffect(effect);
        if (current == null || current.getDuration() <= 40 || current.getAmplifier() != amplifier) {
            player.addEffect(new MobEffectInstance(effect, duration, amplifier, false, false));
        }
    }

    private static void hungerSimulation(Player player, int hungerDrainInterval) {
        if (currentTick % hungerDrainInterval == 0) {
            FoodData foodData = player.getFoodData();
            if (foodData.getSaturationLevel() > 0) {
                foodData.setSaturation(Math.max(foodData.getSaturationLevel() - 1, 0));
            } else {
                foodData.setFoodLevel(Math.max(foodData.getFoodLevel() - 1, 0));
            }
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
        });
    }
    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            float damage = event.getAmount();

            player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
                stamina.consume((float) Settings.getCombatCost("hit", true) * damage, (float) Settings.getCombatCost("hit", false) * damage, 0, player);
            });
        }
    }

    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide) return;

        player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
            stamina.consume(
                    (float) 0.65,
                    (float) 0.03,
                    getArmorStaminaMultiplier(player, "movement"),
                    player
            );
        });
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {

        if (event.getLevel().isClientSide()) return;
        if (event.isCanceled()) return;

        BlockState state = event.getLevel().getBlockState(event.getPos());
        if (!consumesStaminaOnInteraction(state)) return;

        Player player = event.getEntity();

        player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
            stamina.consume(
                    (float) Settings.getInteractCost("click", true),
                    (float) Settings.getInteractCost("click", false),
                    getArmorStaminaMultiplier(player, "action"),
                    player
            );
        });
    }

    private static boolean consumesStaminaOnInteraction(BlockState state) {
        return state.is(BlockTags.DOORS)
                || state.is(BlockTags.TRAPDOORS)
                || state.is(BlockTags.BUTTONS)
                || state.is(BlockTags.FENCE_GATES)
                || state.is(BlockTags.WOODEN_DOORS)
                || state.is(BlockTags.WOODEN_TRAPDOORS)
                || state.is(BlockTags.BEDS)
                || state.is(ModTags.Blocks.STAMINA_INTERACTIONS);
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
        Player player = event.getPlayer();
        player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
            stamina.consume((float) Settings.getMiningCost("break", true), (float) Settings.getMiningCost("break", false), getArmorStaminaMultiplier(player, "action"), player);
        });
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

        // Ursprüngliches Comfort Requirement
        double baseComfortThreshold = Settings.getComfortThresholdSleep();
        double comfortThreshold;
        if (CommonConfig.DYNAMIC_COMFORT_REQUIREMENT_CAP.get()) {
            // Aktuelle Long Stamina auslesen
            final double[] longStaminaValue = {100.0};

            player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
                longStaminaValue[0] = stamina.getLongStamina();
            });

            double longStamina = longStaminaValue[0];

            // Berechnen, wie weit die Long Stamina unter dem Grenzwert liegt
            double staminaDeficit = Math.max(
                    0.0,
                    CommonConfig.DYNAMIC_COMFORT_REQUIREMENT_STAMINA_THRESHOLD.get() - longStamina
            );

            // Prozentuale Reduktion des Comfort Requirements
            double reductionPercent = Math.min(
                    100.0,
                    staminaDeficit * CommonConfig.DYNAMIC_COMFORT_REQUIREMENT_REDUCTION_MULTIPLIER.get()
            );

            // Effektives Comfort Requirement
            comfortThreshold = baseComfortThreshold
                    * (1.0 - reductionPercent / 100.0);
        }
        else comfortThreshold = baseComfortThreshold;

        BlockPos bedPos = event.getPos();

        ComfortCalculator.ComfortResult comfortResult =
                ComfortCalculator.calculateComfort(
                        (ServerLevel) player.level(),
                        player,
                        bedPos
                );

        double comfort = comfortResult.comfort;
        List<String> issues = comfortResult.issues;

        if (comfort < comfortThreshold) {
            event.setResult(Player.BedSleepingProblem.OTHER_PROBLEM);

            player.displayClientMessage(
                    ComfortUtils.getComfortMessage(
                            player,
                            comfort,
                            comfortThreshold,
                            issues
                    ),
                    true
            );
        } else {
            player.displayClientMessage(
                    ComfortUtils.formatComfort(comfort),
                    true
            );
        }
    }



    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
            // stamina.consume(0.0025f, 0.0005f, armDamage, player);
            stamina.consume((float) (Settings.getInteractCost("place", true)), (float) (Settings.getInteractCost("place", false)), getArmorStaminaMultiplier(player, "action"), player);
        });
    }


    @SubscribeEvent
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player)) return;

        ItemStack stack = event.getItem();
        Item item = stack.getItem();

        if (!item.isEdible()) return;

        FoodProperties food = item.getFoodProperties(stack, player);

        if (food == null) return;

        int nutrition = food.getNutrition();
        float saturation = food.getSaturationModifier();

        // Optionale zusätzliche Regeneration suchen.
        FoodStaminaRegeneration.Entry entry =
                FoodStaminaRegeneration.find(stack);

        player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {

            // Bestehendes Verhalten bleibt erhalten.
            stamina.foodEaten(player, nutrition, saturation);

            // Zusätzliche Regeneration nur bei konfigurierten Items/Tags.
            if (entry != null) {
                stamina.regenerateFromFoodPercentage(
                        player,
                        entry.getShortPercent(),
                        entry.getLongPercent()
                );
            }
        });
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
            if (vehicle == null)  continue;

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
                float comfort = (float) cap.getComfortLevel();
                double hardnessFactor = 1 / 0.5;
                stamina.rest(
                        player,
                        (float)(comfort * Settings.getRegenerationConfigs("comfortRegMultiplier")) * tickMultiplier,
                        (float)(hardnessFactor / 1.5)
                );
            });
        });
    }

    @SubscribeEvent
    public static void onMount(EntityMountEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide) return;

        if (event.isMounting()) {
            Entity vehicle = event.getEntityBeingMounted();

            if (vehicle instanceof SeatEntity) return;

            if (vehicle instanceof Boat
                    || vehicle instanceof AbstractMinecart
                    || vehicle instanceof AbstractHorse) {
                return;
            }

            BlockPos mountPos = vehicle.blockPosition();

            // Ursprüngliche Comfort-Anforderung für das Sitzen
            double baseComfortThreshold = Settings.getComfortThresholdSit();
            double comfortThreshold;

            if (CommonConfig.DYNAMIC_COMFORT_REQUIREMENT_CAP.get()) {
                final double[] longStaminaValue = {100.0};

                player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
                    longStaminaValue[0] = stamina.getLongStamina();
                });

                double longStamina = longStaminaValue[0];

                double staminaDeficit = Math.max(
                        0.0,
                        CommonConfig.DYNAMIC_COMFORT_REQUIREMENT_STAMINA_THRESHOLD.get()
                                - longStamina
                );

                double reductionPercent = Math.min(
                        100.0,
                        staminaDeficit
                                * CommonConfig.DYNAMIC_COMFORT_REQUIREMENT_REDUCTION_MULTIPLIER.get()
                );

                comfortThreshold = baseComfortThreshold
                        * (1.0 - reductionPercent / 100.0);
            } else {
                comfortThreshold = baseComfortThreshold;
            }

            // Comfort berechnen
            ComfortCalculator.ComfortResult comfortResult =
                    ComfortCalculator.calculateComfort(
                            (ServerLevel) player.level(),
                            player,
                            mountPos
                    );

            double comfort = comfortResult.comfort;
            List<String> issues = comfortResult.issues;

            if (comfort < comfortThreshold) {
                player.displayClientMessage(
                        ComfortUtils.getComfortMessage(
                                player,
                                comfort,
                                comfortThreshold,
                                issues
                        ),
                        true
                );
            } else {
                player.displayClientMessage(
                        ComfortUtils.formatComfort(comfort),
                        true
                );
            }

            player.getCapability(ComfortProvider.COMFORT_CAP).ifPresent(cap -> {
                cap.setComfortLevel(comfort);
                cap.setSitting(true);
            });
        }
    }

}
