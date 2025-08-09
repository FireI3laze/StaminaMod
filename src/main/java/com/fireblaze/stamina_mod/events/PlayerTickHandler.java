package com.fireblaze.stamina_mod.events;

import com.fireblaze.stamina_mod.capability.StaminaProvider;
import ichttt.mods.firstaid.api.CapabilityExtendedHealthSystem;
import ichttt.mods.firstaid.api.damagesystem.AbstractPlayerDamageModel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PlayerTickHandler {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Player player = event.player;
        // wounded management
        //float legDamage = getBodyPartDamage(player, "leg");
        //float armDamage = getBodyPartDamage(player, "arm");
        float legDamage = 0.0f;
        float armDamage = 0.0f;

        player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
            // stamina management
            if (player.isSprinting()) {
                float shortStaminaConsumption = 1f;
                float longStaminaConsumption = 0.05f;
                stamina.consume(shortStaminaConsumption, longStaminaConsumption, legDamage);
            }

            if (player.isCrouching()) {

            }

            if (player.isSwimming()) {

            }

            double reachDistance = 5.0;
            BlockHitResult hitResult = (BlockHitResult) player.pick(reachDistance, 0.0f, false);

            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos blockPos = hitResult.getBlockPos();
                BlockState blockState = player.level().getBlockState(blockPos);
                float hardness = blockState.getDestroySpeed(player.level(), blockPos);

                ItemStack heldItem = player.getMainHandItem();
                Item item = heldItem.getItem();

                if (player.swinging && player.getMainHandItem().isCorrectToolForDrops(blockState)) {
                    if (item instanceof DiggerItem) {
                        if (item instanceof PickaxeItem) {
                            // Spitzhacke
                            stamina.consume(0.3f, 0.025f, armDamage, hardness);
                        } else if (item instanceof AxeItem) {
                            // Axt
                            stamina.consume(0.266f, 0.02f, armDamage, hardness);
                        } else if (item instanceof ShovelItem) {
                            // Schaufel
                            stamina.consume(0.233f, 0.015f, armDamage, hardness);
                        } else if (item instanceof HoeItem) {
                            // Hacke
                            stamina.consume(0.2f, 0.01f, armDamage, hardness);
                        }
                    }
                    else {
                        stamina.consume(0.25f, 0.03f, armDamage, hardness);
                    }
                }
            }

            stamina.tick(player, armDamage + legDamage);

            // Low Stamina Punishment
            float shortStam = stamina.getShortStamina();
            float longStam = stamina.getLongStamina();

            if (shortStam <= 5) {
                int amplifier = 2;
                applyEffects(player, amplifier);
            } else if (shortStam <= 15) {
                int amplifier = 1;
                applyEffects(player, amplifier);
            } else if (shortStam <= 30) {
                int amplifier = 0;
                applyEffects(player, amplifier);
            }
        });
    }

    public static void applyEffects(Player player, int amplifier) {
        int duration = 40; // 2 Sekunden
        refreshEffect(player, MobEffects.MOVEMENT_SLOWDOWN, amplifier, duration);
        refreshEffect(player, MobEffects.DIG_SLOWDOWN, amplifier, duration);
        refreshEffect(player, MobEffects.WEAKNESS, amplifier, duration);
    }

    private static void refreshEffect(Player player, MobEffect effect, int amplifier, int duration) {
        MobEffectInstance current = player.getEffect(effect);
        if (current == null || current.getDuration() <= 10 || current.getAmplifier() != amplifier) {
            player.addEffect(new MobEffectInstance(effect, duration, amplifier, false, false));
        }
    }

    @SubscribeEvent
    public void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        // wounded management
        //float armDamage = getBodyPartDamage(player, "arm");
        float armDamage = 0;

        if (event.getEntity() instanceof Player) {
            BlockState placedBlock = event.getPlacedBlock();

            player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
                stamina.consume(0.25f, 0.005f, armDamage);
            });
        }
    }

    /*
    private static float getBodyPartDamage(Player player, String part) {
        LazyOptional<AbstractPlayerDamageModel> opt =
                player.getCapability(CapabilityExtendedHealthSystem.INSTANCE);

        return opt.map(model -> {
            // Vermutlich existiert eine Methode wie diese im Modell:
            return model.getDamageFor(part);
        }).orElse(0f);
    }
    */
}
