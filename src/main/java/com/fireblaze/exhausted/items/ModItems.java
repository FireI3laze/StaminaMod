package com.fireblaze.exhausted.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.eventbus.api.IEventBus;
import com.fireblaze.exhausted.StaminaMod;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, StaminaMod.MODID);

    public static final RegistryObject<Item> STEPUP_BOOTS = ITEMS.register("stepup_boots",
            () -> new StepUpBoots(ArmorMaterials.LEATHER, ArmorItem.Type.BOOTS,
                    new Item.Properties()));

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
