package com.fireblaze.stamina_mod.events;

import com.fireblaze.stamina_mod.capability.StaminaProvider;
import com.fireblaze.stamina_mod.config.Settings;
import com.fireblaze.stamina_mod.networking.ModMessages;
import com.fireblaze.stamina_mod.networking.packet.StaminaDataSyncS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide()) return;

        Player player = event.player;
        UUID id = player.getUUID();

        float[] shortStamina = { shortStaminaMap.getOrDefault(id, 0f) };
        float[] longStamina = { longStaminaMap.getOrDefault(id, 0f) };
        float[] maxStamina = { maxStaminaMap.getOrDefault(id, 0f) };
        float[] staminaExp = { staminaExpMap.getOrDefault(id, 0f) };
        int[] staminaLvl = { staminaLvlMap.getOrDefault(id, 0) };

        float legDamage = 0.0f;
        float armDamage = 0.0f;

        player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
            // Bewegungsausdauer
            if (player.isSprinting()) {
                // stamina.consume(0.05f, 0.002f, legDamage, player);
                stamina.consume((float) Settings.getMovementCost("sprint", true), (float) Settings.getMovementCost("sprint", false), armDamage, player);
            } else if (isPlayerWalking(player)) {
                // stamina.consume(0.0075f, 0.001f, legDamage, player);
                stamina.consume((float) Settings.getMovementCost("walk", true), (float) Settings.getMovementCost("walk", false), armDamage, player);
            }

            if (player.isCrouching() || player.isSwimming()) {
                // stamina.consume(0.0075f, 0.001f, legDamage, player);
                stamina.consume((float) Settings.getMovementCost("crouch", true), (float) Settings.getMovementCost("crouch", false), armDamage, player);
            }

            if (player.getDeltaMovement().y > 0 && !player.onGround()) {
                // stamina.consume(0.15f, 0.0075f, legDamage, player);
                stamina.consume((float) Settings.getMovementCost("jump", true), (float) Settings.getMovementCost("jump", false), armDamage, player);
            }

            if (player.isBlocking()) {
                stamina.consume((float) Settings.getCombatCost("block", true), (float) Settings.getCombatCost("block", false), armDamage, player);
            }

            // Block-Abbau Stamina
            BlockHitResult hitResult = (BlockHitResult) player.pick(5.0, 0.0f, false);
            stamina.tick(player, armDamage + legDamage);

            float shortStam = stamina.getShortStamina();
            float longStam = stamina.getLongStamina();

            // Low Stamina Effekte
            if (shortStam <= Settings.getNegativeEffect3Threshold()) applyNegativeEffects(player, 2);
            else if (shortStam <= Settings.getNegativeEffect2Threshold()) applyNegativeEffects(player, 1);
            else if (shortStam <= Settings.getNegativeEffect1Threshold()) applyNegativeEffects(player, 0);
            else if (shortStam >= Settings.getPositiveEffectThreshold()) applyPositiveEffects(player, 0);

            // Stamina Synchronisation
            if (shortStam != shortStamina[0] || longStam != longStamina[0]) {
                shortStamina[0] = shortStam;
                longStamina[0] = longStam;
                maxStamina[0] = stamina.getLongStaminaCap();
                staminaExp[0] = stamina.getStaminaExp();
                staminaLvl[0] = stamina.getStaminaLvl();

                ModMessages.sendToPlayer(new StaminaDataSyncS2CPacket(
                        shortStamina[0], longStamina[0], maxStamina[0], staminaExp[0], staminaLvl[0]
                ), (ServerPlayer) player);
            }

            // Mining Stamina
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos blockPos = miningPlayers.get(id);
                if (blockPos == null) return;

                BlockState blockState = player.level().getBlockState(blockPos);
                if (blockState.isAir()) {
                    miningPlayers.remove(id);
                    return;
                }

                float hardness = blockState.getDestroySpeed(player.level(), blockPos);
                ItemStack heldItem = player.getMainHandItem();

                if (heldItem.isCorrectToolForDrops(blockState)) {
                    if (blockState.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
                        stamina.consume((float) Settings.getMiningCost("pickaxe", true), (float) Settings.getMiningCost("pickaxe", false), armDamage, hardness, player);
                    } else if (blockState.is(BlockTags.MINEABLE_WITH_AXE)) {
                        stamina.consume((float) Settings.getMiningCost("axe", true), (float) Settings.getMiningCost("axe", false), armDamage, hardness, player);
                    } else if (blockState.is(BlockTags.MINEABLE_WITH_SHOVEL)) {
                        stamina.consume((float) Settings.getMiningCost("shovel", true), (float) Settings.getMiningCost("shovel", false), armDamage, hardness, player);
                    } else if (blockState.is(BlockTags.MINEABLE_WITH_HOE)) {
                        stamina.consume((float) Settings.getMiningCost("hoe", true), (float) Settings.getMiningCost("hoe", false), armDamage, hardness, player);
                    } else {
                        stamina.consume((float) Settings.getMiningCost("unknown", true), (float) Settings.getMiningCost("unknown", false), armDamage, hardness, player);
                    }
                } else {
                    // Faust / falsches Werkzeug
                    stamina.consume((float) Settings.getMiningCost("hand", true), (float) Settings.getMiningCost("hand", false), armDamage, hardness, player);
                }

            }
        });

        shortStaminaMap.put(id, shortStamina[0]);
        longStaminaMap.put(id, longStamina[0]);
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
        int duration = 40;
        refreshEffect(player, MobEffects.MOVEMENT_SLOWDOWN, amplifier, duration);
        refreshEffect(player, MobEffects.DIG_SLOWDOWN, amplifier, duration);
        refreshEffect(player, MobEffects.WEAKNESS, amplifier, duration);
        if (amplifier >= 1) refreshEffect(player, MobEffects.HUNGER, 0, duration);
        if (amplifier == 2) refreshEffect(player, MobEffects.BLINDNESS, 0, duration);
    }

    public static void applyPositiveEffects(Player player, int amplifier) {
        int duration = 40;
        refreshEffect(player, MobEffects.MOVEMENT_SPEED, amplifier, duration);
        refreshEffect(player, MobEffects.DIG_SPEED, amplifier, duration);
        refreshEffect(player, MobEffects.DAMAGE_BOOST, amplifier, duration);
    }

    private static void refreshEffect(Player player, MobEffect effect, int amplifier, int duration) {
        MobEffectInstance current = player.getEffect(effect);
        if (current == null || current.getDuration() <= 20 || current.getAmplifier() != amplifier) {
            player.addEffect(new MobEffectInstance(effect, duration, amplifier, false, false));
        }
    }

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
            stamina.consume((float) Settings.getCombatCost("hit", true), (float) Settings.getCombatCost("hit", false), 0, player);
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
                stamina.consume((float) Settings.getInteractCost("click", true), (float) Settings.getInteractCost("click", false), 0, player);
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
    public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        float armDamage = 0;
        player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
            // stamina.consume(0.0025f, 0.0005f, armDamage, player);
            stamina.consume((float) (Settings.getInteractCost("place", true) - Settings.getInteractCost("click", true)), (float) (Settings.getInteractCost("place", false) - Settings.getInteractCost("click", false)), 0, player);
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
                    stamina.foodEaten(nutrition, saturation);
                });
            }
        }
    }
}
