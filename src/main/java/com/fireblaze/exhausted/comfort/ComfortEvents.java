package com.fireblaze.exhausted.comfort;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraft.resources.ResourceLocation;
import com.fireblaze.exhausted.Exhausted;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Exhausted.MODID)
public class ComfortEvents {

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(ComfortCapability.class);
    }

    // Capability anhängen
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            if (!player.getCapability(ComfortProvider.COMFORT_CAP).isPresent()) {
                event.addCapability(
                        ResourceLocation.fromNamespaceAndPath(Exhausted.MODID, "comfort"),
                        new ComfortProvider()
                );
                //System.out.println("Attaching ComfortCapability to player");
            }
        }
    }



    // Spieler-Clone (bei Tod/Dimensionwechsel) → Comfort-Daten kopieren
    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().reviveCaps();
            event.getOriginal().getCapability(ComfortProvider.COMFORT_CAP).ifPresent(oldCap -> {
                event.getEntity().getCapability(ComfortProvider.COMFORT_CAP).ifPresent(newCap -> {
                    newCap.setSitting(oldCap.isSitting());
                    newCap.setComfortLevel(oldCap.getComfortLevel());
                });
            });
            event.getOriginal().invalidateCaps();
        }
    }



    // Nur prüfen wenn Sitting = true
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        /*
        if (event.phase != TickEvent.Phase.END) return;
        if (!(event.player instanceof ServerPlayer player)) return;

        player.getCapability(ComfortProvider.COMFORT_CAP).ifPresent(cap -> {
            if (cap.isSitting()) {
                double comfort = ComfortCalculator.calculateComfort(player.serverLevel(), player);
                cap.setComfortLevel(comfort);

                System.out.println("Comfort: " + comfort + "%");
                // Debug-Ausgabe
                player.connection.send(
                        new ClientboundSystemChatPacket(
                                Component.literal("Comfort: " + comfort + "%"), false
                        )
                );
            }
        });
        */
    }
}
