package com.fireblaze.exhausted;

import com.fireblaze.exhausted.capability.Stamina;
import com.fireblaze.exhausted.capability.StaminaProvider;
import com.fireblaze.exhausted.comfort.ComfortCalculator;
import com.fireblaze.exhausted.comfort.ComfortProvider;
import com.fireblaze.exhausted.comfort.ComfortUtils;
import com.fireblaze.exhausted.commands.StaminaCommands;
import com.fireblaze.exhausted.config.Settings;
import com.fireblaze.exhausted.config.CommonConfig;
import com.fireblaze.exhausted.entity.ModEntities;
import com.fireblaze.exhausted.entity.SeatEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.level.block.state.BlockState;


import java.util.*;

@Mod.EventBusSubscriber(modid = Exhausted.MODID)
public class ModEvents {
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            if (!player.getCapability(StaminaProvider.PLAYER_STAMINA).isPresent()) {
                event.addCapability(ResourceLocation.fromNamespaceAndPath(Exhausted.MODID, "properties"), new StaminaProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        StaminaCommands.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        event.getOriginal().reviveCaps();

        event.getOriginal().getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(oldStore -> {
            event.getEntity().getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(newStore -> {

                boolean keepLevel = CommonConfig.KEEP_LEVEL_ON_DEATH.get();
                boolean keepStamina = CommonConfig.KEEP_STAMINA_ON_DEATH.get();

                if (keepLevel && keepStamina) {
                    newStore.copyFrom(oldStore);
                } else if (keepLevel) {
                    newStore.setStaminaLvl(oldStore.getStaminaLvl());
                    newStore.setStaminaExp(oldStore.getStaminaExp());
                } else if (keepStamina) {
                    newStore.setShortStamina(oldStore.getShortStamina());
                    newStore.setLongStamina(oldStore.getLongStamina());
                }
            });
        });
        event.getOriginal().invalidateCaps();
    }



    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(Stamina.class);
    }


    // Maps für Sneak-Tracking


    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        Level level = player.level();

        if (level.isClientSide
                || event.getHand() != InteractionHand.MAIN_HAND
                || !player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            return;
        }

        BlockPos clickedPos = event.getPos();
        BlockState clickedBlock = level.getBlockState(clickedPos);
        BlockPos blockUnderFeet = player.blockPosition();

        if (!clickedPos.equals(blockUnderFeet)
                && !clickedPos.equals(blockUnderFeet.below())) {
            return;
        }

        VoxelShape shape = clickedBlock.getCollisionShape(level, clickedPos);
        if (shape.isEmpty()) return;

        double blockHeight;

        if (clickedBlock.getBlock() instanceof StairBlock) {
            blockHeight = 0.5;
        } else {
            blockHeight = clickedBlock.getShape(level, clickedPos)
                    .max(Direction.Axis.Y);
        }

        double sitHeight = clickedPos.getY() + blockHeight - 0.2;

        // Comfort-Anforderung für das Sitzen
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
                        player.serverLevel(),
                        player,
                        clickedPos
                );

        double comfort = comfortResult.comfort;
        List<String> issues = comfortResult.issues;

        // Prüfen, ob der Comfort ausreicht
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

            return;
        }

        // Seat-Entity erst nach erfolgreicher Comfort-Prüfung erstellen
        SeatEntity seat = new SeatEntity(ModEntities.SEAT.get(), level);
        seat.moveTo(
                clickedPos.getX() + 0.5,
                sitHeight,
                clickedPos.getZ() + 0.5
        );

        if (!level.addFreshEntity(seat)) return;

        player.displayClientMessage(
                ComfortUtils.formatComfort(comfort),
                true
        );

        player.startRiding(seat);

        player.getCapability(ComfortProvider.COMFORT_CAP).ifPresent(cap -> {
            cap.setComfortLevel(comfort);
            cap.setSitting(true);
        });

        // Interaktion erfolgreich behandeln
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }
}
