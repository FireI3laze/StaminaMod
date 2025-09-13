package com.fireblaze.exhausted.items;

import com.fireblaze.exhausted.ModAttributes;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;

import java.util.UUID;
import net.minecraft.world.item.ItemStack;

public class HikingBoots extends ArmorItem {

    public HikingBoots(ArmorMaterial material, ArmorItem.Type slot, Item.Properties properties) {
        super(material, slot, properties);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        // create mutable copy
        Multimap<net.minecraft.world.entity.ai.attributes.Attribute, AttributeModifier> modifiers = HashMultimap.create();
        modifiers.putAll(super.getAttributeModifiers(slot, stack));

        if (slot == EquipmentSlot.FEET) {
            // feste UUID für diesen Modifier (einmal generieren und konstant halten)
            UUID STEP_UP_UUID = UUID.fromString("8a7f4d8d-1a2b-4c3d-9e0f-123456789abc");

            net.minecraft.world.entity.ai.attributes.Attribute stepUpAttr = ModAttributes.STEP_UP.get();
            AttributeModifier stepUpModifier = new AttributeModifier(
                    STEP_UP_UUID,
                    "exhausted:step_up",
                    1.0D,
                    AttributeModifier.Operation.ADDITION
            );

            modifiers.put(stepUpAttr, stepUpModifier);
        }

        return modifiers;
    }

}
