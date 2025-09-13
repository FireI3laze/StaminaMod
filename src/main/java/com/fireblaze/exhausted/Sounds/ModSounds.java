package com.fireblaze.exhausted.Sounds;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, "exhausted");

    public static final RegistryObject<SoundEvent> NEGATIVE_1 = registerSound("negative_1");
    public static final RegistryObject<SoundEvent> NEGATIVE_2 = registerSound("negative_2");
    public static final RegistryObject<SoundEvent> NEGATIVE_3 = registerSound("negative_3");

    private static RegistryObject<SoundEvent> registerSound(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath("exhausted", name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
