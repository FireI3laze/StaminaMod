package com.fireblaze.stamina_mod;

import com.fireblaze.stamina_mod.capability.Stamina;
import com.fireblaze.stamina_mod.capability.StaminaProvider;
import com.fireblaze.stamina_mod.comfort.ComfortCalculator;
import com.fireblaze.stamina_mod.comfort.ComfortProvider;
import com.fireblaze.stamina_mod.comfort.ComfortUtils;
import com.fireblaze.stamina_mod.config.Settings;
import com.fireblaze.stamina_mod.config.StaminaConfig;
import com.fireblaze.stamina_mod.entity.ModEntities;
import com.fireblaze.stamina_mod.entity.SeatEntity;
import com.fireblaze.stamina_mod.networking.ModMessages;
import com.fireblaze.stamina_mod.networking.packet.StaminaDataSyncS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.EntityMountEvent;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = StaminaMod.MODID)
public class ModEvents {
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            // Verhindert Doppel-Registrierung
            if (!player.getCapability(StaminaProvider.PLAYER_STAMINA).isPresent()) {
                event.addCapability(ResourceLocation.fromNamespaceAndPath(StaminaMod.MODID, "properties"), new StaminaProvider());
                System.out.println("Attaching Stamina Capability from onAttachCapabilitiesPlayer");
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        event.getOriginal().reviveCaps();

        event.getOriginal().getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(oldStore -> {
            event.getEntity().getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(newStore -> {

                boolean keepLevel = StaminaConfig.KEEP_LEVEL_ON_DEATH.get();
                boolean keepStamina = StaminaConfig.KEEP_STAMINA_ON_DEATH.get();

                if (keepLevel && keepStamina) {
                    // Alles übernehmen
                    newStore.copyFrom(oldStore);
                } else if (keepLevel) {
                    // Nur Level & Exp übernehmen
                    newStore.setStaminaLvl(oldStore.getStaminaLvl());
                    newStore.setStaminaExp(oldStore.getStaminaExp());
                } else if (keepStamina) {
                    // Alles außer Level & Exp übernehmen
                    newStore.setShortStamina(oldStore.getShortStamina());
                    newStore.setLongStamina(oldStore.getLongStamina());
                    // Level & Exp bleiben unverändert
                } else {
                    // Nichts übernehmen, alles bleibt default
                }

            });
        });

        event.getOriginal().invalidateCaps();
    }



    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(Stamina.class);
    }

    // crashes in singleplayer
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        Level level = player.level();
        if (!level.isClientSide && event.getHand() == InteractionHand.MAIN_HAND && player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {

            BlockPos clickedPos = event.getPos();
            BlockState clickedBlock = level.getBlockState(clickedPos);
            BlockPos blockUnderFeet = player.blockPosition();

            // Klick nur direkt auf Boden oder Block unter Füßen
            if (!clickedPos.equals(blockUnderFeet) && !clickedPos.equals(blockUnderFeet.below())) return;

            VoxelShape shape = clickedBlock.getCollisionShape(level, clickedPos);
            if (shape.isEmpty()) return; // kein Sitz auf Block ohne Kollisionsbox

            // Höhe des Blocks ermitteln
            double blockHeight = clickedBlock.getShape(level, clickedPos).max(net.minecraft.core.Direction.Axis.Y);
            double sitHeight = clickedPos.getY() + blockHeight - 0.2;

            // Sitz-Entity erstellen
            SeatEntity seat = new SeatEntity(ModEntities.SEAT.get(), level);
            seat.moveTo(clickedPos.getX() + 0.5, sitHeight, clickedPos.getZ() + 0.5);
            level.addFreshEntity(seat);

            // Spieler hinsetzen
            double comfortThreshold = Settings.getComfortThresholdSit();
            ComfortCalculator.ComfortResult comfortResult = ComfortCalculator.calculateComfort(player.serverLevel(), player);
            double comfort = comfortResult.comfort;
            List<String> issues = comfortResult.issues;

            if (comfort < comfortThreshold) {
                player.displayClientMessage(ComfortUtils.getComfortMessage(player, comfort, comfortThreshold, issues), true);
                seat.discard();
                return;
            } else {
                player.displayClientMessage(ComfortUtils.formatComfort(comfort), true);
            }
            player.startRiding(seat);

            // Comfort nur einmal berechnen
            player.getCapability(ComfortProvider.COMFORT_CAP).ifPresent(cap -> {
                cap.setComfortLevel(comfort);
                cap.setSitting(true); // optional Flag für Tick
            });

            // Event abbrechen
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }

}
