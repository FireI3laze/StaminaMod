package com.fireblaze.exhausted.items;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.eventbus.api.IEventBus;
import com.fireblaze.exhausted.Exhausted;
import com.fireblaze.exhausted.items.HikingBoots;
import com.fireblaze.exhausted.items.ModArmorMaterials;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Exhausted.MODID);

    public static final RegistryObject<Item> HIKING_BOOTS = ITEMS.register("hiking",
            HikingBoots::new);

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
