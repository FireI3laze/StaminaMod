package com.fireblaze.stamina_mod;

import com.fireblaze.stamina_mod.capability.Stamina;
import com.fireblaze.stamina_mod.capability.StaminaProvider;
import com.fireblaze.stamina_mod.config.StaminaConfig;
import com.fireblaze.stamina_mod.entity.ModEntities;
import com.fireblaze.stamina_mod.entity.SeatEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;

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
        if (event.isWasDeath()) {
            // Caps vom Original kurz wiederbeleben, damit wir lesen können
            event.getOriginal().reviveCaps();

            event.getOriginal().getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(oldStore -> {
                event.getEntity().getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(newStore -> {
                    if (StaminaConfig.KEEP_LEVEL_ON_DEATH.get()) {
                        System.out.println("Config says yes");
                        // Alles kopieren (inkl. Level & XP)
                        newStore.copyFrom(oldStore);
                    } else {
                        System.out.println("Config says no");

                        // Standard-Kopie
                        newStore.copyFrom(oldStore);

                        // Falls gewünscht: Stamina-Werte vom Original erhalten
                        newStore.setStaminaLvl(0);
                        newStore.setStaminaExp(0);
                    }
                });
            });

            // Caps wieder invalidieren
            event.getOriginal().invalidateCaps();
        }
    }



    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(Stamina.class);
    }

    // crashes in singleplayer
    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = player.level();
        System.out.println("Right-clicked any block");
        if (!level.isClientSide && event.getHand() == InteractionHand.MAIN_HAND && player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            BlockPos pos = event.getPos().above();

            BlockPos blockPos = event.getPos();
            BlockState clickedBlock = level.getBlockState(blockPos);

            BlockPos blockUnderFeet = player.blockPosition();
            BlockPos clickedPos = event.getPos();
            System.out.println("Right-clicked any block with empty hand");

            if ((!clickedPos.equals(blockUnderFeet) && !clickedPos.equals(blockUnderFeet.below()))) {
                return; // Klick ist nicht erlaubt
            }

            System.out.println("Right-clicked block under feat");
            // Höhe des Blocks ermitteln (0.0 bis 1.0)
            double blockHeight = clickedBlock.getShape(level, blockPos).max(net.minecraft.core.Direction.Axis.Y);

            // Sitzposition berechnen: Blockhöhe minus kleiner Offset
            double sitHeight = pos.getY() + blockHeight - 1.2;

            // Sitz-Entity erstellen
            System.out.println("Trying to load Seat Entity");
            SeatEntity seat = new SeatEntity(ModEntities.SEAT.get(), level);
            System.out.println("Seat Entity Loaded");
            seat.moveTo(pos.getX() + 0.5, sitHeight, pos.getZ() + 0.5);
            System.out.println("1");
            level.addFreshEntity(seat);
            System.out.println("2");

            // Spieler hinsetzen
            player.startRiding(seat);
            System.out.println("3");

            // Rechtsklick-Event abbrechen
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }
}
