package com.fireblaze.exhausted.items;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum ModArmorMaterials implements ArmorMaterial {
    HIKING(
            "hiking",
            15,                             // Base durability multiplier
            new int[]{1, 2, 3, 1},          // Protection values: [boots, leggings, chest, helmet]
            10,                             // Enchantability
            SoundEvents.ARMOR_EQUIP_LEATHER,// Equip sound
            0.0F,                           // Toughness
            0.0F,                           // Knockback resistance
            () -> Ingredient.of(ModItems.HIKING_BOOTS.get()) // Repair item
    );

    private final String name;
    private final int durabilityMultiplier;
    private final int[] protectionAmounts = {1, 2, 3, 1};         // gleiche Reihenfolge
    private final int enchantability;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairIngredient;

    ModArmorMaterials(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantability,
                      SoundEvent equipSound, float toughness, float knockbackResistance,
                      Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.enchantability = enchantability;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient;
    }



    private static final int[] BASE_DURABILITY = {13, 15, 16, 11};

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return switch (type) {
            case BOOTS -> BASE_DURABILITY[0] * 15;      // Multiplier wie vorher
            case LEGGINGS -> BASE_DURABILITY[1] * 15;
            case CHESTPLATE -> BASE_DURABILITY[2] * 15;
            case HELMET -> BASE_DURABILITY[3] * 15;
        };
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return switch (type) {
            case BOOTS -> protectionAmounts[0];
            case LEGGINGS -> protectionAmounts[1];
            case CHESTPLATE -> protectionAmounts[2];
            case HELMET -> protectionAmounts[3];
        };
    }


    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override
    public String getName() {
        return "exhausted:" + this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}
