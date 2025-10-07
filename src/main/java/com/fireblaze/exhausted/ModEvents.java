package com.fireblaze.exhausted;

import com.fireblaze.exhausted.capability.Stamina;
import com.fireblaze.exhausted.capability.StaminaProvider;
import com.fireblaze.exhausted.comfort.ComfortCalculator;
import com.fireblaze.exhausted.comfort.ComfortProvider;
import com.fireblaze.exhausted.comfort.ComfortUtils;
import com.fireblaze.exhausted.commands.StaminaCommands;
import com.fireblaze.exhausted.config.Settings;
import com.fireblaze.exhausted.config.StaminaConfig;
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

                boolean keepLevel = StaminaConfig.KEEP_LEVEL_ON_DEATH.get();
                boolean keepStamina = StaminaConfig.KEEP_STAMINA_ON_DEATH.get();

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

    @SubscribeEvent public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
    {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        Level level = player.level();
        if (!level.isClientSide && event.getHand() == InteractionHand.MAIN_HAND && player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty())
        {
            BlockPos clickedPos = event.getPos(); BlockState clickedBlock = level.getBlockState(clickedPos);
            BlockPos blockUnderFeet = player.blockPosition();
            if (!clickedPos.equals(blockUnderFeet) && !clickedPos.equals(blockUnderFeet.below())) return;
            VoxelShape shape = clickedBlock.getCollisionShape(level, clickedPos);
            if (shape.isEmpty()) return;
            double blockHeight;
            if (clickedBlock.getBlock() instanceof StairBlock) {
                blockHeight = 0.5;
            } else {
                blockHeight = clickedBlock.getShape(level, clickedPos).max(Direction.Axis.Y);
            }
            double sitHeight = clickedPos.getY() + blockHeight -0.2;

            // Create Seat-Entity
            SeatEntity seat = new SeatEntity(ModEntities.SEAT.get(), level);
            seat.moveTo(clickedPos.getX() + 0.5, sitHeight, clickedPos.getZ() + 0.5); level.addFreshEntity(seat);

            // Comfort calculation
            double comfortThreshold = Settings.getComfortThresholdSit();
            ComfortCalculator.ComfortResult comfortResult = ComfortCalculator.calculateComfort(player.serverLevel(), player, clickedPos);
            double comfort = comfortResult.comfort;
            List<String> issues = comfortResult.issues;

            if (comfort < comfortThreshold) {
                player.displayClientMessage(ComfortUtils.getComfortMessage(player, comfort, comfortThreshold, issues), true);
                seat.discard(); return; } else { player.displayClientMessage(ComfortUtils.formatComfort(comfort), true);
            } player.startRiding(seat);

            player.getCapability(ComfortProvider.COMFORT_CAP).ifPresent(cap -> {
                cap.setComfortLevel(comfort);
                cap.setSitting(true);
                });
            // Cancel Event
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }

}
