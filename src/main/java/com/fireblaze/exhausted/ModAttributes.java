package com.fireblaze.exhausted;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.eventbus.api.IEventBus;

public class ModAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Exhausted.MODID);

    // name = registry-name, description id = the string used for translations
    public static final RegistryObject<Attribute> STEP_UP =
            ATTRIBUTES.register("step_up",
                    () -> new RangedAttribute("attribute.name.exhausted.step_up", 0.0D, -1024.0D, 1024.0D)
            );


    public static void register(IEventBus bus) {
        ATTRIBUTES.register(bus);
    }
}
