package com.fireblaze.stamina_mod.events;

import com.fireblaze.stamina_mod.capability.StaminaProvider;
import com.fireblaze.stamina_mod.networking.ModMessages;
import com.fireblaze.stamina_mod.networking.packet.StaminaDataSyncS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.TickEvent;
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
                stamina.consume(0.05f, 0.002f, legDamage, player);
            } else if (isPlayerWalking(player)) {
                stamina.consume(0.0075f, 0.001f, legDamage, player);
            }

            if (player.isCrouching() || player.isSwimming() || player.isBlocking()) {
                stamina.consume(0.0075f, 0.001f, legDamage, player);
            }

            if (player.getDeltaMovement().y > 0 && !player.onGround()) {
                stamina.consume(0.15f, 0.0075f, legDamage, player);
            }

            // Block-Abbau Stamina
            BlockHitResult hitResult = (BlockHitResult) player.pick(5.0, 0.0f, false);
            stamina.tick(player, armDamage + legDamage);

            float shortStam = stamina.getShortStamina();
            float longStam = stamina.getLongStamina();

            // Low Stamina Effekte
            if (shortStam <= 5) applyNegativeEffects(player, 2);
            else if (shortStam <= 10) applyNegativeEffects(player, 1);
            else if (shortStam <= 15) applyNegativeEffects(player, 0);
            else if (shortStam >= 90) applyPositiveEffects(player, 0);

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
                        System.out.println("Pickaxe");
                        stamina.consume(0.06f, 0.005f, armDamage, hardness, player);
                    } else if (blockState.is(BlockTags.MINEABLE_WITH_AXE)) {
                        System.out.println("Axe");
                        stamina.consume(0.055f, 0.0025f, armDamage, hardness, player);
                    } else if (blockState.is(BlockTags.MINEABLE_WITH_SHOVEL)) {
                        System.out.println("Shovel");
                        stamina.consume(0.0525f, 0.0001f, armDamage, hardness, player);
                    } else if (blockState.is(BlockTags.MINEABLE_WITH_HOE)) {
                        System.out.println("Hoe");
                        stamina.consume(0.0525f, 0.001f, armDamage, hardness, player);
                    } else {
                        System.out.println("Unknown Tool");
                        stamina.consume(0.125f, 0.0125f, armDamage, hardness, player);
                    }
                } else {
                    // Faust / falsches Werkzeug
                    stamina.consume(0.125f, 0.0125f, armDamage, hardness, player);
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
    }

    public static void applyPositiveEffects(Player player, int amplifier) {
        int duration = 40;
        refreshEffect(player, MobEffects.MOVEMENT_SPEED, amplifier, duration);
        refreshEffect(player, MobEffects.DIG_SPEED, amplifier, duration);
        refreshEffect(player, MobEffects.DAMAGE_BOOST, amplifier, duration);
    }

    private static void refreshEffect(Player player, MobEffect effect, int amplifier, int duration) {
        MobEffectInstance current = player.getEffect(effect);
        if (current == null || current.getDuration() <= 10 || current.getAmplifier() != amplifier) {
            player.addEffect(new MobEffectInstance(effect, duration, amplifier, false, false));
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
            BlockState blockState = event.getLevel().getBlockState(event.getPos());
            if (!blockState.isAir()) {
                stamina.consume(0.175f, 0.015f, 0, player);
                System.out.println("consumed Stamina for interacting");
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
    public void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        float armDamage = 0;
        player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
            stamina.consume(0.0025f, 0.0005f, armDamage, player);
        });
    }
}
