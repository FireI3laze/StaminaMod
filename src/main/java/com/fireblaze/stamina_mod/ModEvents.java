package com.fireblaze.stamina_mod;

import com.fireblaze.stamina_mod.capability.Stamina;
import com.fireblaze.stamina_mod.capability.StaminaProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = StaminaMod.MODID)
public class ModEvents {

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            // Verhindert Doppel-Registrierung
            if (!player.getCapability(StaminaProvider.PLAYER_STAMINA).isPresent() && player.level().isClientSide) {
                event.addCapability(ResourceLocation.fromNamespaceAndPath(StaminaMod.MODID, "properties"), new StaminaProvider());
                System.out.println("Attaching Stamina Capability from onAttachCapabilitiesPlayer");
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()  && event.getEntity().level().isClientSide) {
            event.getOriginal().getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(oldStore -> {
                event.getEntity().getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                    System.out.println("Attaching Stamina Capability from onPlayerCloned");
                });
            });
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(Stamina.class);
        System.out.println("Capability Registered");
    }
}
