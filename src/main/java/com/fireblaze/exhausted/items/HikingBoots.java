package com.fireblaze.exhausted.items;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import java.util.UUID;
import com.fireblaze.exhausted.ModAttributes;

public class HikingBoots extends ArmorItem {

    private static final UUID STEP_UP_UUID = UUID.fromString("8a7f4d8d-1a2b-4c3d-9e0f-123456789abc");

    public HikingBoots() {
        super(ModArmorMaterials.HIKING, Type.BOOTS, new Properties());
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
        modifiers.putAll(super.getAttributeModifiers(slot, stack));

        if (slot == EquipmentSlot.FEET) {
            Attribute stepUpAttr = ModAttributes.STEP_UP.get();
            AttributeModifier stepUpModifier = new AttributeModifier(
                    STEP_UP_UUID,
                    "exhausted.step_up",
                    1.0D,
                    AttributeModifier.Operation.ADDITION
            );
            modifiers.put(stepUpAttr, stepUpModifier);
        }

        return modifiers;
    }
}
