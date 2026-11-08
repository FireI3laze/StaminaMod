package com.fireblaze.exhausted.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class CommonConfig {


    public static final ForgeConfigSpec.Builder BUILDER =
            new ForgeConfigSpec.Builder();


    public static final ForgeConfigSpec SPEC;



    // === Global Options ===

    // === Difficulty ===
    public static final ForgeConfigSpec.EnumValue<CommonConfig.Difficulty> DIFFICULTY;
    public static final ForgeConfigSpec.BooleanValue CUSTOM_SETTINGS;


    // === Experience ===
    public static final ForgeConfigSpec.ConfigValue<Boolean> DISABLE_EXPERIENCE_GAIN;
    public static final ForgeConfigSpec.ConfigValue<Double> XP_BASE;
    public static final ForgeConfigSpec.ConfigValue<Double> XP_EXPONENT;
    public static final ForgeConfigSpec.ConfigValue<Double> EXPERIENCE_GAIN_PERCENTAGE;

    // === Death Resets ===
    public static final ForgeConfigSpec.BooleanValue KEEP_LEVEL_ON_DEATH;
    public static final ForgeConfigSpec.BooleanValue KEEP_STAMINA_ON_DEATH;

    // === Comfort ===
        // === Comfort Bonus ===
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
    public static final ForgeConfigSpec.ConfigValue<Integer> COMFORT_THRESHOLD_SIT;
    public static final ForgeConfigSpec.ConfigValue<Integer> COMFORT_THRESHOLD_SLEEP;
    public static final ForgeConfigSpec.BooleanValue COMFORT_SLEEP_REGENERATION_CAP;
    public static final ForgeConfigSpec.BooleanValue DYNAMIC_COMFORT_REQUIREMENT_CAP;
    public static final ForgeConfigSpec.ConfigValue<Integer> DYNAMIC_COMFORT_REQUIREMENT_STAMINA_THRESHOLD;
    public static final ForgeConfigSpec.ConfigValue<Double> DYNAMIC_COMFORT_REQUIREMENT_REDUCTION_MULTIPLIER;


        // === Requirements ===
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> COMFORT_WALL_BLOCKS_BLACKLIST;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> COMFORT_BLOCKS_WHITELIST;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> COMFORT_BLOCK_GROUPS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> DIMENSION_BOOST_BLOCKS;
    public static final ForgeConfigSpec.ConfigValue<Integer> CEILING_HEIGHT_REQUIREMENT;
    public static final ForgeConfigSpec.ConfigValue<Integer> ROOM_AIR_BLOCK_AMOUNT_REQUIREMENT;
    public static final ForgeConfigSpec.ConfigValue<Integer> FOOD_REQUIREMENT;
    public static final ForgeConfigSpec.ConfigValue<Integer> MONSTER_RANGE_TOLERANCE;

    // === Advanced Settings ===

    // === Regeneration ===
    public static final ForgeConfigSpec.ConfigValue<Double> BASE_REGEN_SHORT;
    public static final ForgeConfigSpec.ConfigValue<Double> BASE_REGEN_LONG;
    public static final ForgeConfigSpec.ConfigValue<Double> COMFORT_REG_MULTIPLIER;
    public static final ForgeConfigSpec.ConfigValue<Double> FOOD_FACTOR;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>>
            FOOD_STAMINA_ENTRIES;

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
    public static final ForgeConfigSpec.ConfigValue<Double> MINING_BLOCK_BREAK_SHORT;
    public static final ForgeConfigSpec.ConfigValue<Double> MINING_BLOCK_BREAK_LONG;

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

    // === Armor ===
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

    // === Thresholds & Buffs ===
    public static final ForgeConfigSpec.ConfigValue<Integer> POSITIVE_EFFECT_THRESHOLD;
    public static final ForgeConfigSpec.ConfigValue<Integer> NEGATIVE_EFFECT_1_THRESHOLD;
    public static final ForgeConfigSpec.ConfigValue<Integer> NEGATIVE_EFFECT_2_THRESHOLD;
    public static final ForgeConfigSpec.ConfigValue<Integer> NEGATIVE_EFFECT_3_THRESHOLD;
    public static final ForgeConfigSpec.ConfigValue<Double> POSITIVE_EFFECT_DAMAGE_FLAT;
    public static final ForgeConfigSpec.ConfigValue<Double> POSITIVE_EFFECT_SPEED_PERCENTAGE;
    public static final ForgeConfigSpec.ConfigValue<Double> POSITIVE_EFFECT_MINING_SPEED_MULTIPLIER;
    public static final ForgeConfigSpec.ConfigValue<Double> NEGATIVE_EFFECT_1_DAMAGE_PERCENTAGE;
    public static final ForgeConfigSpec.ConfigValue<Double> NEGATIVE_EFFECT_1_MOVEMENT_SPEED_PERCENTAGE;
    public static final ForgeConfigSpec.ConfigValue<Double> NEGATIVE_EFFECT_1_MINING_SPEED_MULTIPLIER;
    public static final ForgeConfigSpec.ConfigValue<Double> NEGATIVE_EFFECT_2_DAMAGE_PERCENTAGE;
    public static final ForgeConfigSpec.ConfigValue<Double> NEGATIVE_EFFECT_2_MOVEMENT_SPEED_PERCENTAGE;
    public static final ForgeConfigSpec.ConfigValue<Double> NEGATIVE_EFFECT_2_MINING_SPEED_MULTIPLIER;
    public static final ForgeConfigSpec.ConfigValue<Integer> NEGATIVE_EFFECT_2_HUNGER_DRAIN_INTERVAL;
    public static final ForgeConfigSpec.ConfigValue<Double> NEGATIVE_EFFECT_3_DAMAGE_PERCENTAGE;
    public static final ForgeConfigSpec.ConfigValue<Double> NEGATIVE_EFFECT_3_MOVEMENT_SPEED_PERCENTAGE;
    public static final ForgeConfigSpec.ConfigValue<Double> NEGATIVE_EFFECT_3_MINING_SPEED_MULTIPLIER;
    public static final ForgeConfigSpec.ConfigValue<Integer> NEGATIVE_EFFECT_3_HUNGER_DRAIN_INTERVAL;
    public static final ForgeConfigSpec.ConfigValue<Boolean> NEGATIVE_EFFECT_3_BLINDNESS;



    static {
        BUILDER.comment("Level & Comfort Settings");
        BUILDER.push("General-Settings");

        BUILDER.push("Difficulty");
        DIFFICULTY = BUILDER
                .comment("While Easy is barely noticeable, Hard can quickly become frustrating and result in much afk time. Medium tries to hit the perfect balance")
                .defineEnum("difficulty", CommonConfig.Difficulty.MEDIUM);
        CUSTOM_SETTINGS = BUILDER
                .comment("If true, the custom settings are used and override the difficulty setting here.")
                .define("customSettings", true);
        BUILDER.pop();

        BUILDER.comment("Formular: xpBase * staminaLevel^xpExponent");
        BUILDER.push("Level");
        DISABLE_EXPERIENCE_GAIN = BUILDER
                .comment("Enable or disable the level system")
                .define("activateLevelSystem", true);
        XP_BASE = defineDoubleInRange("xpBase", 100, 1, 1000000, "Base XP required for leveling");
        XP_EXPONENT = defineDoubleInRange("xpExponent", 2.0, 0.0, 10.0, "XP curve exponent. 1.0 = linear, 2.0 = quadratic, 3.0 = cubic");
        EXPERIENCE_GAIN_PERCENTAGE = defineDoubleInRange("percentageExperienceGain", 1, 0.0, 100.0, "Change the experience gain based on the consumed stamina");
        BUILDER.pop();

        BUILDER.push("Death-Resets");
        BUILDER.comment("Toggle here if you want to keep your stamina level upon death");
        KEEP_LEVEL_ON_DEATH = BUILDER.define("keepLevelOnDeath", true);
        BUILDER.comment("Enable this option if you want to be revived with the stamina you had when you died, or disable it to respawn with full stamina");
        KEEP_STAMINA_ON_DEATH = BUILDER.define("keepStaminaOnDeath", false);
        BUILDER.pop();

        BUILDER.push("Comfort");

        BUILDER.comment("Change the comfort amount you get from the different factors");
        BUILDER.push("Comfort Bonus per Condition");
        COMFORT_BONUS_CEILING = defineIntComment("comfortBonusCeiling", 5, 0, 10000, "When on top of you are enough air blocks");
        COMFORT_BONUS_SPACE = defineIntComment("comfortBonusSpace", 15, 0, 10000, "When your room contains at least 24 air blocks.");
        COMFORT_BONUS_WALLS = defineIntComment("comfortBonusWalls", 20, 0, 10000, "When your room/closer range has blocks that are not only stone, dirt etc");
        COMFORT_BONUS_LIGHT = defineIntComment("comfortBonusLight", 10, 0, 10000, "When your room/closer range is properly lit up");
        COMFORT_BONUS_ANIMAL = defineIntComment("comfortBonusAnimal", 5, 0, 10000, "When you have a friendly animal in your room/closer range");
        COMFORT_BONUS_FOOD = defineIntComment("comfortBonusFood", 5, 0, 10000, "When you have food in a chest in your room/closer range");
        COMFORT_BONUS_BLOCK = defineIntComment("comfortBonusBlockMax", 30, 0, 10000, "The max amount of comfort you can get from comfort blocks");
        BUILDER.comment("The amount comfort a single comfort block provides. Repeatedly using the same block reduces its effectiveness by 50%.");
        COMFORT_BONUS_PER_BLOCK = defineDoubleInRange("comfortBonusPerBlock", 5, 0.0, 1000.0);
        COMFORT_BONUS_BOOST_BLOCK = defineIntComment("comfortBonusBoostBlock", 10, 0, 10000, "The comfort provided by the boost block (not stackable)");
        COMFORT_MALUS_IN_RAIN = defineIntComment("comfortMalusInRain", -50, -10000, 0, "Comfort reduction when in rain");
        COMFORT_MALUS_HURT = defineIntComment("comfortMalusHurt", -5, -10000, 0, "Comfort reduction when hurt");
        COMFORT_MALUS_HUNGRY = defineIntComment("comfortMalusHungry", -10, -10000, 0, "Comfort reduction when hungry (< 16/20)");
        COMFORT_MALUS_FOREIGN_DIMENSION = defineIntComment("comfortMalusForeignDimension", -10, -10000, 0, "When you are in the overworld");
        COMFORT_MALUS_MONSTERS = defineIntComment("comfortMalusMonsters", -20, -10000, 0, "When you have no monsters in a certain range around you");
        BUILDER.pop();

        BUILDER.comment("Change the conditions for the different comfort factors");
        BUILDER.push("Comfort-Condition-Rules");
        BUILDER.comment("Here you can add/change blocks that are not allowed to be built a base out of");
        COMFORT_WALL_BLOCKS_BLACKLIST = BUILDER.defineList("wallBlockBlacklist", List.of(
                "minecraft:stone",
                "minecraft:deepslate",
                "minecraft:gravel",
                "minecraft:dirt",
                "minecraft:tuff",
                "minecraft:netherrack",
                "minecraft:soul_sand",
                "minecraft:soul_soil",
                "minecraft:basalt",
                "minecraft:blackstone",
                "minecraft:end_stone"
        ), obj -> obj instanceof String);
        BUILDER.comment("Here you can add/change specific blocks designed to provide comfort");
        COMFORT_BLOCKS_WHITELIST = BUILDER.defineList("comfortBlocks", List.of(
                "minecraft:crafting_table",
                "minecraft:amethyst_block",
                "minecraft:budding_amethyst",
                "minecraft:amethyst_cluster"
        ), obj -> obj instanceof String);
        BUILDER.comment("Here you can add/change block groups or mods designed to provide comfort. E.g. with 'door', oak_door, birch_door, oak_trapdoor, (...) will work. With 'furniture', all blocks of all mods that have 'furniture' in their name provide comfort.");
        COMFORT_BLOCK_GROUPS = BUILDER.defineList("comfortBlockGroups", List.of("door","carpet","shelf", "glass", "potted", "amethyst_bud", "furniture"), obj -> obj instanceof String);
        BUILDER.comment("Considers all blocks with internal storage as comfort blocks (chests, furnaces, etc.)");
        COMFORT_ALLOW_STORAGE_BLOCKS = BUILDER.define("allowStorageBlocks", true);
        BUILDER.comment("Each Dimension also has one boost block that provides additional comfort. Here you can change the boost block for the corresponding dimension");
        DIMENSION_BOOST_BLOCKS = BUILDER.defineList(
                "dimensionBoostBlocks",
                List.of(
                        "minecraft:overworld=minecraft:campfire",
                        "minecraft:overworld=minecraft:beacon",
                        "minecraft:overworld=cold_sweat:hearth_bottom",

                        "minecraft:the_end=minecraft:campfire",
                        "minecraft:the_end=minecraft:beacon",
                        "minecraft:the_end=cold_sweat:hearth_bottom",

                        "minecraft:the_nether=minecraft:water_cauldron",
                        "minecraft:the_nether=minecraft:soul_campfire",
                        "minecraft:the_nether=cold_sweat:hearth_bottom"
                ),
                obj -> obj instanceof String
        );
        CEILING_HEIGHT_REQUIREMENT = defineIntComment("ceilingHeightRequirement", 2, 0, 100, "The amount of air blocks required between your feet and the ceiling to get the High Ceiling comfort bonus");
        ROOM_AIR_BLOCK_AMOUNT_REQUIREMENT = defineIntComment("roomAirBlockAmountRequirement", 24, 0, 1000, "The amount of air blocks required in a closed room to get the Space comfort bonus");
        FOOD_REQUIREMENT = defineIntComment("foodRequirement", 16, 0, 20, "The required food bar threshold to not be considered hungry (20 is max)");
        MONSTER_RANGE_TOLERANCE = defineIntComment("monsterRangeTolerance", 10, 0, 1000, "The range scanned around the player to spot monster triggering the monster malus");
        BUILDER.pop();

        COMFORT_THRESHOLD_SIT = defineIntComment("comfortThresholdSit", 40, 0, 100000, "The required comfort for players to be able to sit");
        COMFORT_THRESHOLD_SLEEP = defineIntComment("comfortThresholdSleep", 75, 0, 100000, "The required comfort for players to be able to sleep");
        BUILDER.push("Advanced-Comfort-Settings");
        COMFORT_SLEEP_REGENERATION_CAP = BUILDER
                .comment("If disabled, always completely fills the stamina bar after resting. If enabled, fills according to the players current comfort based on the max achievable comfort. Formular: 100% - missingComfort% * 0.5")
                .define("comfortSleepRegenerationCap", true);
        DYNAMIC_COMFORT_REQUIREMENT_CAP = BUILDER
                .comment("If disabled, the comfort requirement is fix and will not be lowered when the stamina passes a certain threshold. Formular: (staminaThreshold - yourCurrentLongStamina) * reductionMultiplier\n " +
                        "Example: Your Long Stamina (gray bar) is at 50%, the requiredStaminaThreshold is set to 60% (value can be changed below) and the reductionMultiplier is set to 2 (value can be changed below), while the comfort requirement to sit is 40 (value can be changed above)\n " +
                        "Calculation: (100% - (60% - 50%) * 2) * 40 Comfort = 32 Comfort\n " +
                        "So the new required Comfort to sit with the default settings and a currently half filled gray stamina bar will be 32.")
                .define("dynamicComfortRequirementCap", true);
        DYNAMIC_COMFORT_REQUIREMENT_STAMINA_THRESHOLD = defineIntComment("dynamicComfortRequirementStaminaThreshold", 60, 0, 100, "The percentage value the long stamina (gray bar) has to be at to lower the comfort requirements");
        DYNAMIC_COMFORT_REQUIREMENT_REDUCTION_MULTIPLIER = defineDoubleInRange("dynamicComfortRequirementReductionMultiplier", 2, 0.0, 100.0, "Multiplier to adjust the comfort requirement");

        BUILDER.pop();
        BUILDER.pop();
        BUILDER.pop();

        BUILDER.comment("Fine Tune the Stamina consumption here. 'Long' refers to the gray stamina bar and 'Short' to the blue one.");
        BUILDER.push("Advanced-Settings");

        // --- Regeneration ---
        BUILDER.comment("Passive and active stamina regeneration");
        BUILDER.push("Regeneration");
        BASE_REGEN_SHORT = defineDoubleInRange("baseRegenShort", 0.0002, 0.0, 1.0, "Always active | per tick");
        BASE_REGEN_LONG = defineDoubleInRange("baseRegenLong", 0.0017, 0.0, 1.0, "Active while sitting | per tick");
        COMFORT_REG_MULTIPLIER = defineDoubleInRange("comfortRegMultiplier", 0.0325, 0.0, 10.0, "Multiplier for comfort");
        FOOD_FACTOR = defineDoubleInRange("foodFactor", 0.075, 0.0, 10.0, "Multiplies with nutrition+saturation");
        FOOD_STAMINA_ENTRIES = BUILDER
                .comment(
                        "Items and item tags that restore stamina when consumed.",
                        "Format: item_id;shortStamina;longStamina",
                        "Prefix item tags with #, e.g. #minecraft:fox_food"
                )
                .defineListAllowEmpty(
                        List.of("food_stamina_entries"),
                        List.of(
                                "minecraft:golden_carrot;5;0",
                                "minecraft:golden_apple;10;10",
                                "minecraft:enchanted_golden_apple;90;90"
                        ),
                        entry -> entry instanceof String
                );
        BUILDER.pop();

        // --- Mining ---
        BUILDER.comment("Stamina consumption through mining");
        BUILDER.push("Mining");
        MINING_HAND_SHORT = defineDoubleInRange("handShort", 0.125, 0.0, 10.0);
        MINING_HAND_LONG = defineDoubleInRange("handLong", 0.01, 0.0, 10.0);
        MINING_PICKAXE_SHORT = defineDoubleInRange("pickaxeShort", 0.07, 0.0, 10.0);
        MINING_PICKAXE_LONG = defineDoubleInRange("pickaxeLong", 0.005, 0.0, 10.0);
        MINING_AXE_SHORT = defineDoubleInRange("axeShort", 0.068, 0.0, 10.0);
        MINING_AXE_LONG = defineDoubleInRange("axeLong", 0.004, 0.0, 10.0);
        MINING_SHOVEL_SHORT = defineDoubleInRange("shovelShort", 0.066, 0.0, 10.0);
        MINING_SHOVEL_LONG = defineDoubleInRange("shovelLong", 0.0036, 0.0, 10.0);
        MINING_HOE_SHORT = defineDoubleInRange("hoeShort", 0.065, 0.0, 10.0);
        MINING_HOE_LONG = defineDoubleInRange("hoeLong", 0.0034, 0.0, 10.0);
        MINING_SHEARS_SHORT = defineDoubleInRange("shearsShort", 1.0, 0.0, 10.0);
        MINING_SHEARS_LONG = defineDoubleInRange("shearsLong", 1.0, 0.0, 10.0);
        MINING_UNKNOWN_SHORT = defineDoubleInRange("unknownToolShort", 0.07, 0.0, 10.0);
        MINING_UNKNOWN_LONG = defineDoubleInRange("unknownToolLong", 0.005, 0.0, 10.0);
        MINING_BLOCK_BREAK_SHORT = defineDoubleInRange("blockBreakShort", 0.03, 0.0, 10.0);
        MINING_BLOCK_BREAK_LONG = defineDoubleInRange("blockBreakLong", 0.001, 0.0, 10.0);
        BUILDER.pop();

        // --- Interact ---
        BUILDER.comment("Stamina consumption through interacting with or placing blocks");
        BUILDER.push("Interact");
        INTERACT_PLACE_SHORT = defineDoubleInRange("placeShort", 0.0075, 0.0, 10.0);
        INTERACT_PLACE_LONG = defineDoubleInRange("placeLong", 0.00065, 0.0, 10.0);
        INTERACT_CLICK_SHORT = defineDoubleInRange("clickShort", 0.175, 0.0, 10.0);
        INTERACT_CLICK_LONG = defineDoubleInRange("clickLong", 0.015, 0.0, 10.0);
        BUILDER.pop();

        // --- Movement ---
        BUILDER.comment("Stamina consumption through movement");
        BUILDER.push("Movement");
        MOVEMENT_SPRINT_SHORT = defineDoubleInRange("sprintShort", 0.05, 0.0, 10.0);
        MOVEMENT_SPRINT_LONG = defineDoubleInRange("sprintLong", 0.0018, 0.0, 10.0);
        MOVEMENT_WALK_SHORT = defineDoubleInRange("walkShort", 0.0075, 0.0, 10.0);
        MOVEMENT_WALK_LONG = defineDoubleInRange("walkLong", 0.0005, 0.0, 10.0);
        MOVEMENT_SWIM_SHORT = defineDoubleInRange("swimShort", 0.0075, 0.0, 10.0);
        MOVEMENT_SWIM_LONG = defineDoubleInRange("swimLong", 0.001, 0.0, 10.0);
        MOVEMENT_CROUCH_SHORT = defineDoubleInRange("crouchShort", 0.0075, 0.0, 10.0);
        MOVEMENT_CROUCH_LONG = defineDoubleInRange("crouchLong", 0.001, 0.0, 10.0);
        MOVEMENT_JUMP_SHORT = defineDoubleInRange("jumpShort", 0.125, 0.0, 10.0);
        MOVEMENT_JUMP_LONG = defineDoubleInRange("jumpLong", 0.008, 0.0, 10.0);
        MOVEMENT_HORSE_RIDE_SHORT = defineDoubleInRange("horseRideShort", 0.01, 0.0, 10.0);
        MOVEMENT_HORSE_RIDE_LONG = defineDoubleInRange("horseRideLong", 0.0009, 0.0, 10.0);
        MOVEMENT_BOAT_DRIVE_SHORT = defineDoubleInRange("boatDriveShort", 0.018, 0.0, 10.0);
        MOVEMENT_BOAT_DRIVE_LONG = defineDoubleInRange("boatDriveLong", 0.0015, 0.0, 10.0);
        BUILDER.pop();

        // --- Combat ---
        BUILDER.comment("Stamina consumption through combat activities");
        BUILDER.push("Combat");
        COMBAT_HITTING_SHORT = defineDoubleInRange("attackShort", 0.5, 0.0, 10.0);
        COMBAT_HITTING_LONG = defineDoubleInRange("attackLong", 0.05, 0.0, 10.0);
        COMBAT_BLOCKING_SHORT = defineDoubleInRange("shieldBlockShort", 0.0075, 0.0, 10.0);
        COMBAT_BLOCKING_LONG = defineDoubleInRange("shieldBlockLong", 0.001, 0.0, 10.0);
        COMBAT_DAMAGE_TAKEN_SHORT = defineDoubleInRange("damageTakenShort", 1.0, 0.0, 10.0);
        COMBAT_DAMAGE_TAKEN_LONG = defineDoubleInRange("damageTakenLong", 0.1, 0.0, 10.0);
        BUILDER.pop();

        // --- Armor ---
        BUILDER.comment("Stamina consumption based on worn armor");
        BUILDER.push("Armor");
        BUILDER.comment("Multiplier for the corresponding material");
        BUILDER.push("Material");
        ARMOR_MATERIAL_LEATHER = defineDoubleInRange("leatherMultiplier", 0.03, 0.0, 10.0);
        ARMOR_MATERIAL_CHAIN = defineDoubleInRange("chainMultiplier", 0.05, 0.0, 10.0);
        ARMOR_MATERIAL_IRON = defineDoubleInRange("ironMultiplier", 0.1, 0.0, 10.0);
        ARMOR_MATERIAL_GOLD = defineDoubleInRange("goldMultiplier", 0.1, 0.0, 10.0);
        ARMOR_MATERIAL_DIAMOND = defineDoubleInRange("diamondMultiplier", 0.125, 0.0, 10.0);
        ARMOR_MATERIAL_NETHERITE = defineDoubleInRange("netheriteMultiplier", 0.15, 0.0, 10.0);
        ARMOR_MATERIAL_UNKOWN = defineDoubleInRange("unknownMultiplier", 0.175, 0.0, 10.0);
        BUILDER.pop();
        BUILDER.comment("Multiplier for the corresponding armor piece, based on activity. 'Action' includes activities that are mostly arm related like mining and horse or boat riding. 'Movement' includes activities that are mostly leg related like walking, running, jumping (...)");
        BUILDER.push("Armor-Piece-And-Activity");
        ARMOR_HELMET_MULTIPLIER_ACTION = defineDoubleInRange("helmetActionMultiplier", 0.0, 0.0, 1.0);
        ARMOR_CHESTPLATE_MULTIPLIER_ACTION = defineDoubleInRange("chestplateActionMultiplier", 1.0, 0.0, 1.0);
        ARMOR_LEGGINGS_MULTIPLIER_ACTION = defineDoubleInRange("leggingsActionMultiplier", 0.5, 0.0, 1.0);
        ARMOR_BOOTS_MULTIPLIER_ACTION = defineDoubleInRange("bootsActionMultiplier", 0.25, 0.0, 1.0);
        ARMOR_HELMET_MULTIPLIER_MOVEMENT = defineDoubleInRange("helmetMovementMultiplier", 0.25, 0.0, 1.0);
        ARMOR_CHESTPLATE_MULTIPLIER_MOVEMENT = defineDoubleInRange("chestplateMovementMultiplier", 0.5, 0.0, 1.0);
        ARMOR_LEGGINGS_MULTIPLIER_MOVEMENT = defineDoubleInRange("leggingsMovementMultiplier", 1.0, 0.0, 1.0);
        ARMOR_BOOTS_MULTIPLIER_MOVEMENT = defineDoubleInRange("bootsMovementMultiplier", 0.75, 0.0, 1.0);
        BUILDER.pop();
        BUILDER.pop();

        // --- Thresholds & Buffs ---
        BUILDER.comment("Set the Stamina Thresholds for positive and negative effects");
        BUILDER.push("Stamina-Effects");
        BUILDER.push("Thresholds");
        POSITIVE_EFFECT_THRESHOLD = defineIntComment("positiveEffect", 80, 0, 101, "Min stamina to receive positive effects");
        NEGATIVE_EFFECT_1_THRESHOLD = defineIntComment("negativeEffect_1", 15, 0, 101, "Min stamina to suffer negative effects");
        NEGATIVE_EFFECT_2_THRESHOLD = defineIntComment("negativeEffect_2", 10, 0, 101, "Min stamina to suffer negative effects");
        NEGATIVE_EFFECT_3_THRESHOLD = defineIntComment("negativeEffect_3", 5, 0, 101, "Min stamina to suffer negative effects");
        BUILDER.pop();

        BUILDER.push("Values");
        POSITIVE_EFFECT_DAMAGE_FLAT = defineDoubleInRange("positiveEffectDamageFlat", 3.0, 0.0, 10.0);
        POSITIVE_EFFECT_SPEED_PERCENTAGE = defineDoubleInRange("positiveEffectSpeedPercentage", 0.2, 0.0, 5.0);
        POSITIVE_EFFECT_MINING_SPEED_MULTIPLIER = defineDoubleInRange("positiveEffectMiningSpeedMultiplier", 1.2, 1.0, 5.0);

        NEGATIVE_EFFECT_1_DAMAGE_PERCENTAGE = defineDoubleInRange("negativeEffect_1_DamagePercentage", -0.25, -1.0, 0.0);
        NEGATIVE_EFFECT_1_MOVEMENT_SPEED_PERCENTAGE = defineDoubleInRange("negativeEffect_1_MovementSpeedPercentage", -0.15, -1.0, 0.0);
        NEGATIVE_EFFECT_1_MINING_SPEED_MULTIPLIER = defineDoubleInRange("negativeEffect_1_MiningSpeedMultiplier", 0.3, 0.0, 1.0);

        NEGATIVE_EFFECT_2_DAMAGE_PERCENTAGE = defineDoubleInRange("negativeEffect_2_DamagePercentage", -0.5, -1.0, 0.0);
        NEGATIVE_EFFECT_2_MOVEMENT_SPEED_PERCENTAGE = defineDoubleInRange("negativeEffect_2_MovementSpeedPercentage", -0.3, -1.0, 0.0);
        NEGATIVE_EFFECT_2_MINING_SPEED_MULTIPLIER = defineDoubleInRange("negativeEffect_2_MiningSpeedMultiplier", 0.09, 0.0, 1.0);
        NEGATIVE_EFFECT_2_HUNGER_DRAIN_INTERVAL = defineIntComment("negativeEffect_2_HungerInterval", 40, 0, 1200, "Interval in ticks 1 hunger is drained");

        NEGATIVE_EFFECT_3_DAMAGE_PERCENTAGE = defineDoubleInRange("negativeEffect_3_DamagePercentage", -0.75, -1.0, 0.0);
        NEGATIVE_EFFECT_3_MOVEMENT_SPEED_PERCENTAGE = defineDoubleInRange("negativeEffect_3_MovementSpeedPercentage", -0.45, -1.0, 0.0);
        NEGATIVE_EFFECT_3_MINING_SPEED_MULTIPLIER = defineDoubleInRange("negativeEffect_3_MiningSpeedMultiplier", 0.0027, 0.0, 1.0);
        NEGATIVE_EFFECT_3_HUNGER_DRAIN_INTERVAL = defineIntComment("negativeEffect_3_HungerInterval", 40, 0, 1200, "Interval in ticks 1 hunger is drained");
        NEGATIVE_EFFECT_3_BLINDNESS = BUILDER.define("negativeEffect_3_GiveBlindness", true);
        BUILDER.pop();
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
    private static ForgeConfigSpec.ConfigValue<Integer> defineIntComment(String name, int def, int min, int max, String comment) {
        if (comment == null) comment = "";
        return BUILDER.comment(comment).defineInRange(name, def, min, max);
    }

    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD
    }
}