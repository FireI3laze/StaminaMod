package com.fireblaze.stamina_mod.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class StaminaConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    // === Global Options ===

        // === Difficulty ===
    public static final ForgeConfigSpec.EnumValue<Difficulty> DIFFICULTY;
    public static final ForgeConfigSpec.BooleanValue CUSTOM_SETTINGS;

        // === Death Resets ===
    public static final ForgeConfigSpec.BooleanValue KEEP_LEVEL_ON_DEATH;
    public static final ForgeConfigSpec.BooleanValue KEEP_STAMINA_ON_DEATH;

        // === Comfort ===
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> COMFORT_BLOCKS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> COMFORT_BLOCK_GROUPS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> DIMENSION_BOOST_BLOCKS;
    public static final ForgeConfigSpec.BooleanValue COMFORT_ALLOW_STORAGE_BLOCKS;
    public static final ForgeConfigSpec.ConfigValue<Integer> COMFORT_MALUS_MONSTERS;
    public static final ForgeConfigSpec.ConfigValue<Integer> COMFORT_BONUS_CEILING;
    public static final ForgeConfigSpec.ConfigValue<Integer> COMFORT_BONUS_SPACE;
    public static final ForgeConfigSpec.ConfigValue<Integer> COMFORT_BONUS_WALLS;
    public static final ForgeConfigSpec.ConfigValue<Integer> COMFORT_BONUS_LIGHT;
    public static final ForgeConfigSpec.ConfigValue<Integer> COMFORT_MALUS_FOREIGN_DIMENSION;
    public static final ForgeConfigSpec.ConfigValue<Integer> COMFORT_BONUS_ANIMAL;
    public static final ForgeConfigSpec.ConfigValue<Integer> COMFORT_BONUS_FOOD;
    public static final ForgeConfigSpec.ConfigValue<Integer> COMFORT_BONUS_BLOCK;
    public static final ForgeConfigSpec.ConfigValue<Double> COMFORT_BONUS_PER_BLOCK;
    public static final ForgeConfigSpec.ConfigValue<Integer> COMFORT_BONUS_BOOST_BLOCK;
    public static final ForgeConfigSpec.ConfigValue<Integer> COMFORT_MALUS_IN_RAIN;
    public static final ForgeConfigSpec.ConfigValue<Integer> COMFORT_MALUS_HURT;
    public static final ForgeConfigSpec.ConfigValue<Integer> COMFORT_MALUS_HUNGRY;
    public static final ForgeConfigSpec.ConfigValue<Integer> MONSTER_RANGE_TOLERANCE;
    public static final ForgeConfigSpec.ConfigValue<Integer> COMFORT_THRESHOLD_SIT;
    public static final ForgeConfigSpec.ConfigValue<Integer> COMFORT_THRESHOLD_SLEEP;

    // === Advanced Settings ===

    // === Regeneration ===
    public static final ForgeConfigSpec.ConfigValue<Double> BASE_REGEN_SHORT;
    public static final ForgeConfigSpec.ConfigValue<Double> BASE_REGEN_LONG;
    public static final ForgeConfigSpec.ConfigValue<Double> COMFORT_REG_MULTIPLIER;
    public static final ForgeConfigSpec.ConfigValue<Double> FOOD_FACTOR;

    // === Mining ===
    public static final ForgeConfigSpec.ConfigValue<Double> MINING_HAND_SHORT;
    public static final ForgeConfigSpec.ConfigValue<Double> MINING_HAND_LONG;
    public static final ForgeConfigSpec.ConfigValue<Double> MINING_PICKAXE_SHORT;
    public static final ForgeConfigSpec.ConfigValue<Double> MINING_PICKAXE_LONG;
    public static final ForgeConfigSpec.ConfigValue<Double> MINING_AXE_SHORT;
    public static final ForgeConfigSpec.ConfigValue<Double> MINING_AXE_LONG;
    public static final ForgeConfigSpec.ConfigValue<Double> MINING_SHOVEL_SHORT;
    public static final ForgeConfigSpec.ConfigValue<Double> MINING_SHOVEL_LONG;
    public static final ForgeConfigSpec.ConfigValue<Double> MINING_HOE_SHORT;
    public static final ForgeConfigSpec.ConfigValue<Double> MINING_HOE_LONG;
    public static final ForgeConfigSpec.ConfigValue<Double> MINING_SHEARS_SHORT;
    public static final ForgeConfigSpec.ConfigValue<Double> MINING_SHEARS_LONG;
    public static final ForgeConfigSpec.ConfigValue<Double> MINING_UNKNOWN_SHORT;
    public static final ForgeConfigSpec.ConfigValue<Double> MINING_UNKNOWN_LONG;

    // === Interact / Place ===
    public static final ForgeConfigSpec.ConfigValue<Double> INTERACT_PLACE_SHORT;
    public static final ForgeConfigSpec.ConfigValue<Double> INTERACT_PLACE_LONG;
    public static final ForgeConfigSpec.ConfigValue<Double> INTERACT_CLICK_SHORT;
    public static final ForgeConfigSpec.ConfigValue<Double> INTERACT_CLICK_LONG;

    // === Movement ===
    public static final ForgeConfigSpec.ConfigValue<Double> MOVEMENT_SPRINT_SHORT;
    public static final ForgeConfigSpec.ConfigValue<Double> MOVEMENT_SPRINT_LONG;
    public static final ForgeConfigSpec.ConfigValue<Double> MOVEMENT_WALK_SHORT;
    public static final ForgeConfigSpec.ConfigValue<Double> MOVEMENT_WALK_LONG;
    public static final ForgeConfigSpec.ConfigValue<Double> MOVEMENT_SWIM_SHORT;
    public static final ForgeConfigSpec.ConfigValue<Double> MOVEMENT_SWIM_LONG;
    public static final ForgeConfigSpec.ConfigValue<Double> MOVEMENT_CROUCH_SHORT;
    public static final ForgeConfigSpec.ConfigValue<Double> MOVEMENT_CROUCH_LONG;
    public static final ForgeConfigSpec.ConfigValue<Double> MOVEMENT_JUMP_SHORT;
    public static final ForgeConfigSpec.ConfigValue<Double> MOVEMENT_JUMP_LONG;
    public static final ForgeConfigSpec.ConfigValue<Double> MOVEMENT_HORSE_RIDE_SHORT;
    public static final ForgeConfigSpec.ConfigValue<Double> MOVEMENT_HORSE_RIDE_LONG;
    public static final ForgeConfigSpec.ConfigValue<Double> MOVEMENT_BOAT_DRIVE_SHORT;
    public static final ForgeConfigSpec.ConfigValue<Double> MOVEMENT_BOAT_DRIVE_LONG;

    // === Combat ===
    public static final ForgeConfigSpec.ConfigValue<Double> COMBAT_HITTING_SHORT;
    public static final ForgeConfigSpec.ConfigValue<Double> COMBAT_HITTING_LONG;
    public static final ForgeConfigSpec.ConfigValue<Double> COMBAT_BLOCKING_SHORT;
    public static final ForgeConfigSpec.ConfigValue<Double> COMBAT_BLOCKING_LONG;
    public static final ForgeConfigSpec.ConfigValue<Double> COMBAT_DAMAGE_TAKEN_SHORT;
    public static final ForgeConfigSpec.ConfigValue<Double> COMBAT_DAMAGE_TAKEN_LONG;
    // === Combat ===
    public static final ForgeConfigSpec.ConfigValue<Double> ARMOR_MATERIAL_LEATHER;
    public static final ForgeConfigSpec.ConfigValue<Double> ARMOR_MATERIAL_CHAIN;
    public static final ForgeConfigSpec.ConfigValue<Double> ARMOR_MATERIAL_IRON;
    public static final ForgeConfigSpec.ConfigValue<Double> ARMOR_MATERIAL_GOLD;
    public static final ForgeConfigSpec.ConfigValue<Double> ARMOR_MATERIAL_DIAMOND;
    public static final ForgeConfigSpec.ConfigValue<Double> ARMOR_MATERIAL_NETHERITE;
    public static final ForgeConfigSpec.ConfigValue<Double> ARMOR_MATERIAL_UNKOWN;
    public static final ForgeConfigSpec.ConfigValue<Double> ARMOR_HELMET_MULTIPLIER_ACTION;
    public static final ForgeConfigSpec.ConfigValue<Double> ARMOR_CHESTPLATE_MULTIPLIER_ACTION;
    public static final ForgeConfigSpec.ConfigValue<Double> ARMOR_LEGGINGS_MULTIPLIER_ACTION;
    public static final ForgeConfigSpec.ConfigValue<Double> ARMOR_BOOTS_MULTIPLIER_ACTION;
    public static final ForgeConfigSpec.ConfigValue<Double> ARMOR_HELMET_MULTIPLIER_MOVEMENT;
    public static final ForgeConfigSpec.ConfigValue<Double> ARMOR_CHESTPLATE_MULTIPLIER_MOVEMENT;
    public static final ForgeConfigSpec.ConfigValue<Double> ARMOR_LEGGINGS_MULTIPLIER_MOVEMENT;
    public static final ForgeConfigSpec.ConfigValue<Double> ARMOR_BOOTS_MULTIPLIER_MOVEMENT;

    // === Thresholds ===
    public static final ForgeConfigSpec.ConfigValue<Integer> POSITIVE_EFFECT_THRESHOLD;
    public static final ForgeConfigSpec.ConfigValue<Integer> NEGATIVE_EFFECT_1_THRESHOLD;
    public static final ForgeConfigSpec.ConfigValue<Integer> NEGATIVE_EFFECT_2_THRESHOLD;
    public static final ForgeConfigSpec.ConfigValue<Integer> NEGATIVE_EFFECT_3_THRESHOLD;

    static {
        BUILDER.comment("Stamina Mod Settings");

        BUILDER.push("Global-Settings");

            BUILDER.push("Difficulty");
            DIFFICULTY = BUILDER
                    .comment("While Easy is barely noticeable, Hard can quickly become frustrating and result in much afk time. Medium tries to hit the perfect balance")
                    .defineEnum("Difficulty", Difficulty.MEDIUM);
            CUSTOM_SETTINGS = BUILDER
                    .comment("If true, the custom settings below the global settings are used and override the difficulty setting here.")
                    .define("Custom Settings", true);
            BUILDER.pop();

            BUILDER.push("Death-Resets");
            BUILDER.comment("Toggle here if you want to keep your stamina level upon death");
            KEEP_LEVEL_ON_DEATH = BUILDER.define("Keep Level On Death", true);
            BUILDER.comment("Enable this option if you want to be revived with the stamina you had when you died, or disable it to respawn with full stamina");
            KEEP_STAMINA_ON_DEATH = BUILDER.define("Keep Stamina On Death", false);
            BUILDER.pop();

            BUILDER.push("Comfort");
            BUILDER.comment("Here you can add/change specific blocks designed to provide comfort");
            COMFORT_BLOCKS = BUILDER.defineList("comfortBlocks", List.of("minecraft:crafting_table"), obj -> obj instanceof String);
            BUILDER.comment("Here you can add/change block groups or mods designed to provide comfort. E.g. with 'door', oak_door, birch_door, oak_trapdoor, (...) will work. With 'furniture', all blocks of all mods that have 'furniture' in their namae provide comfort.");
            COMFORT_BLOCK_GROUPS = BUILDER.defineList("comfortBlockGroups", List.of("door","carpet","shelf", "glass", "potted","furniture"), obj -> obj instanceof String);
            BUILDER.comment("Considers all blocks with internal storage as comfort blocks (chests, furnaces, etc.)");
            COMFORT_ALLOW_STORAGE_BLOCKS = BUILDER.define("allowStorageBlocks", true);
            BUILDER.comment("Each Dimension also has one boost block that provides additional comfort. Here you can change the boost block for the corresponding dimension");
            DIMENSION_BOOST_BLOCKS = BUILDER.defineList(
                    "dimensionBoostBlocks",
                    List.of(
                            "minecraft:overworld=minecraft:campfire",
                            "minecraft:the_end=minecraft:campfire",
                            "minecraft:the_nether=minecraft:water_cauldron"
                    ),
                    obj -> obj instanceof String
            );
            COMFORT_BONUS_CEILING = defineIntComment("comfortBonusCeiling", 5, "When your on top of you are at least 3 air blocks");
            COMFORT_BONUS_SPACE = defineIntComment("comfortBonusSpace", 15, "When your room contains at least 24 air blocks.");
            COMFORT_BONUS_WALLS = defineIntComment("comfortBonusWalls", 20, "When your room/closer range has blocks that are not only stone, dirt etc");
            COMFORT_BONUS_LIGHT = defineIntComment("comfortBonusLight", 10, "When your room/closer range is properly lit up");
            COMFORT_BONUS_ANIMAL = defineIntComment("comfortBonusAnimal", 5, "When you have a friendly animal in your room/closer range");
            COMFORT_BONUS_FOOD = defineIntComment("comfortBonusFood", 5, "When you have food in a chest in your room/closer range");
            COMFORT_BONUS_BLOCK = defineIntComment("comfortBonusBlockMax", 30, "The max amount of comfort you can get from comfort blocks");
            BUILDER.comment("The amount comfort a single comfort block provides");
            COMFORT_BONUS_PER_BLOCK = defineDoubleInRange("comfortBonusPerBlock", 5, 0.0, 100.0);
            COMFORT_BONUS_BOOST_BLOCK = defineIntComment("comfortBonusBoostBlock", 10, "The comfort provided by the boost block (not stackable)");
            COMFORT_MALUS_IN_RAIN = defineIntComment("comfortMalusInRain", -50, "Comfort reduction when in rain");
            COMFORT_MALUS_HURT = defineIntComment("comfortMalusHurt", -5, "Comfort reduction when hurt");
            COMFORT_MALUS_HUNGRY = defineIntComment("comfortMalusHungry", -10, "Comfort reduction when hungry (< 16/20)");
            COMFORT_MALUS_FOREIGN_DIMENSION = defineIntComment("comfortMalusForeignDimension", -10, "When you are in the overworld");
            COMFORT_MALUS_MONSTERS = defineIntComment("comfortMalusMonsters", -20, "When you have no monsters in a certain range around you");
            MONSTER_RANGE_TOLERANCE = defineIntComment("monsterRangeTolerance", 10, "The range scanned around the player to spot monster triggering the monster malus");
            COMFORT_THRESHOLD_SIT = defineIntComment("comfortThresholdSit", 40, "The required comfort for players to be able to sit");
            COMFORT_THRESHOLD_SLEEP = defineIntComment("comfortThresholdSleep", 75, "The required comfort for players to be able to sleep");
            BUILDER.pop();

        BUILDER.pop();

        BUILDER.push("Advanced-Settings");
        BUILDER.comment("For reference, the following numbers represent the medium settings.\nShort = active consumption, Long = temporary stamina cap");

        // --- Regeneration ---
        BUILDER.push("Regeneration");
        BASE_REGEN_SHORT = defineDoubleInRange("baseRegenShort", 0.0002, 0.0, 1.0, "Always active per tick");
        BASE_REGEN_LONG = defineDoubleInRange("baseRegenLong", 0.001, 0.0, 1.0, "Active while sitting per tick");
        COMFORT_REG_MULTIPLIER = defineDoubleInRange("comfortRegMultiplier", 0.0325, 0.0, 10.0, "Multiplier for comfort");
        FOOD_FACTOR = defineDoubleInRange("foodFactor", 0.075, 0.0, 10.0, "Multiplies with nutrition+saturation");
        BUILDER.pop();

        // --- Mining ---
        BUILDER.push("Mining");
        MINING_HAND_SHORT = defineDoubleInRange("handShort", 0.125, 0.0, 10.0);
        MINING_HAND_LONG = defineDoubleInRange("handLong", 0.0125, 0.0, 10.0);
        MINING_PICKAXE_SHORT = defineDoubleInRange("pickaxeShort", 0.07, 0.0, 10.0);
        MINING_PICKAXE_LONG = defineDoubleInRange("pickaxeLong", 0.005, 0.0, 10.0);
        MINING_AXE_SHORT = defineDoubleInRange("axeShort", 0.068, 0.0, 10.0);
        MINING_AXE_LONG = defineDoubleInRange("axeLong", 0.0025, 0.0, 10.0);
        MINING_SHOVEL_SHORT = defineDoubleInRange("shovelShort", 0.066, 0.0, 10.0);
        MINING_SHOVEL_LONG = defineDoubleInRange("shovelLong", 0.0001, 0.0, 10.0);
        MINING_HOE_SHORT = defineDoubleInRange("hoeShort", 0.065, 0.0, 10.0);
        MINING_HOE_LONG = defineDoubleInRange("hoeLong", 0.0008, 0.0, 10.0);
        MINING_SHEARS_SHORT = defineDoubleInRange("shearsShort", 1.0, 0.0, 10.0);
        MINING_SHEARS_LONG = defineDoubleInRange("shearsLong", 1.0, 0.0, 10.0);
        MINING_UNKNOWN_SHORT = defineDoubleInRange("unknownToolShort", 0.07, 0.0, 10.0);
        MINING_UNKNOWN_LONG = defineDoubleInRange("unknownToolLong", 0.007, 0.0, 10.0);
        BUILDER.pop();

        // --- Interact ---
        BUILDER.push("Interact");
        INTERACT_PLACE_SHORT = defineDoubleInRange("placeShort", 0.0075, 0.0, 10.0);
        INTERACT_PLACE_LONG = defineDoubleInRange("placeLong", 0.00065, 0.0, 10.0);
        INTERACT_CLICK_SHORT = defineDoubleInRange("clickShort", 0.175, 0.0, 10.0);
        INTERACT_CLICK_LONG = defineDoubleInRange("clickLong", 0.015, 0.0, 10.0);
        BUILDER.pop();

        // --- Movement ---
        BUILDER.push("Movement");
        MOVEMENT_SPRINT_SHORT = defineDoubleInRange("sprintShort", 0.05, 0.0, 10.0);
        MOVEMENT_SPRINT_LONG = defineDoubleInRange("sprintLong", 0.002, 0.0, 10.0);
        MOVEMENT_WALK_SHORT = defineDoubleInRange("walkShort", 0.0075, 0.0, 10.0);
        MOVEMENT_WALK_LONG = defineDoubleInRange("walkLong", 0.0005, 0.0, 10.0);
        MOVEMENT_SWIM_SHORT = defineDoubleInRange("swimShort", 0.0075, 0.0, 10.0);
        MOVEMENT_SWIM_LONG = defineDoubleInRange("swimLong", 0.001, 0.0, 10.0);
        MOVEMENT_CROUCH_SHORT = defineDoubleInRange("crouchShort", 0.0075, 0.0, 10.0);
        MOVEMENT_CROUCH_LONG = defineDoubleInRange("crouchLong", 0.001, 0.0, 10.0);
        MOVEMENT_JUMP_SHORT = defineDoubleInRange("jumpShort", 0.125, 0.0, 10.0);
        MOVEMENT_JUMP_LONG = defineDoubleInRange("jumpLong", 0.005, 0.0, 10.0);
        MOVEMENT_HORSE_RIDE_SHORT = defineDoubleInRange("horseRideShort", 0.01, 0.0, 10.0);
        MOVEMENT_HORSE_RIDE_LONG = defineDoubleInRange("horseRideLong", 0.0009, 0.0, 10.0);
        MOVEMENT_BOAT_DRIVE_SHORT = defineDoubleInRange("boatDriveShort", 0.018, 0.0, 10.0);
        MOVEMENT_BOAT_DRIVE_LONG = defineDoubleInRange("boatDriveLong", 0.0015, 0.0, 10.0);
        BUILDER.pop();

        // --- Combat ---
        BUILDER.push("Combat");
        COMBAT_HITTING_SHORT = defineDoubleInRange("hitShort", 0.5, 0.0, 10.0);
        COMBAT_HITTING_LONG = defineDoubleInRange("hitLong", 0.05, 0.0, 10.0);
        COMBAT_BLOCKING_SHORT = defineDoubleInRange("blockShort", 0.0075, 0.0, 10.0);
        COMBAT_BLOCKING_LONG = defineDoubleInRange("blockLong", 0.001, 0.0, 10.0);
        COMBAT_DAMAGE_TAKEN_SHORT = defineDoubleInRange("damageTakenShort", 1.0, 0.0, 10.0);
        COMBAT_DAMAGE_TAKEN_LONG = defineDoubleInRange("damageTakenLong", 0.1, 0.0, 10.0);
        BUILDER.pop();

        // --- Armor ---
        BUILDER.push("Armor");
        BUILDER.comment("In the following you can configure the additional stamina consumption based on your armor. The additional consumption is based on the armor material and your activity. E.g *walking* -> materialMultiplier * armorpieceActivityMultiplier");
        BUILDER.comment("Multiplier for the corresponding material");
        ARMOR_MATERIAL_LEATHER = defineDoubleInRange("leatherMultiplier", 0.03, 0.0, 10.0);
        ARMOR_MATERIAL_CHAIN = defineDoubleInRange("chainMultiplier", 0.05, 0.0, 10.0);
        ARMOR_MATERIAL_IRON = defineDoubleInRange("ironMultiplier", 0.1, 0.0, 10.0);
        ARMOR_MATERIAL_GOLD = defineDoubleInRange("goldMultiplier", 0.1, 0.0, 10.0);
        ARMOR_MATERIAL_DIAMOND = defineDoubleInRange("diamondMultiplier", 0.125, 0.0, 10.0);
        ARMOR_MATERIAL_NETHERITE = defineDoubleInRange("netheriteMultiplier", 0.15, 0.0, 10.0);
        ARMOR_MATERIAL_UNKOWN = defineDoubleInRange("unknownMultiplier", 0.3, 0.0, 10.0);
        BUILDER.comment("Multiplier for the corresponding armor piece, based on activity");
        ARMOR_HELMET_MULTIPLIER_ACTION = defineDoubleInRange("helmetActionMultiplier", 0.0, 0.0, 1.0);
        ARMOR_CHESTPLATE_MULTIPLIER_ACTION = defineDoubleInRange("chestplateActionMultiplier", 1.0, 0.0, 1.0);
        ARMOR_LEGGINGS_MULTIPLIER_ACTION = defineDoubleInRange("leggingsActionMultiplier", 0.5, 0.0, 1.0);
        ARMOR_BOOTS_MULTIPLIER_ACTION = defineDoubleInRange("bootsActionMultiplier", 0.25, 0.0, 1.0);
        ARMOR_HELMET_MULTIPLIER_MOVEMENT = defineDoubleInRange("helmetMovementMultiplier", 0.25, 0.0, 1.0);
        ARMOR_CHESTPLATE_MULTIPLIER_MOVEMENT = defineDoubleInRange("chestplateMovementMultiplier", 0.5, 0.0, 1.0);
        ARMOR_LEGGINGS_MULTIPLIER_MOVEMENT = defineDoubleInRange("leggingsMovementMultiplier", 1.0, 0.0, 1.0);
        ARMOR_BOOTS_MULTIPLIER_MOVEMENT = defineDoubleInRange("bootsMovementMultiplier", 0.75, 0.0, 1.0);
        BUILDER.pop();

        // --- Thresholds ---
        BUILDER.push("Potion-Effect-Thresholds");
        POSITIVE_EFFECT_THRESHOLD = defineIntComment("positiveEffect", 80, "Min stamina to receive positive effects");
        NEGATIVE_EFFECT_1_THRESHOLD = defineIntComment("negativeEffect1", 15, "Min stamina to suffer negative effects");
        NEGATIVE_EFFECT_2_THRESHOLD = defineIntComment("negativeEffect2", 10, "Min stamina to suffer negative effects");
        NEGATIVE_EFFECT_3_THRESHOLD = defineIntComment("negativeEffect3", 5, "Min stamina to suffer negative effects");
        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    private static ForgeConfigSpec.ConfigValue<Double> defineDoubleInRange(String name, double def, double min, double max) {
        return BUILDER.defineInRange(name, def, min, max);
    }

    private static ForgeConfigSpec.ConfigValue<Double> defineDoubleInRange(String name, double def, double min, double max, String comment) {
        return BUILDER.comment(comment).defineInRange(name, def, min, max);
    }

    private static ForgeConfigSpec.ConfigValue<Integer> defineIntComment(String name, int def, String comment) {
        if (comment == null) comment = "";
        return BUILDER.comment(comment).defineInRange(name, def, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD
    }
}