package com.fireblaze.stamina_mod.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class StaminaConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    // === Global Options ===
    public static final ForgeConfigSpec.BooleanValue KEEP_LEVEL_ON_DEATH;
    public static final ForgeConfigSpec.EnumValue<Difficulty> DIFFICULTY;
    public static final ForgeConfigSpec.BooleanValue CUSTOM_SETTINGS;

    // === Regeneration ===
    public static final ForgeConfigSpec.EnumValue<Difficulty> REGENERATION_ALL;
    public static final ForgeConfigSpec.ConfigValue<Double> BASE_REGEN_SHORT;
    public static final ForgeConfigSpec.ConfigValue<Double> BASE_REGEN_LONG;
    public static final ForgeConfigSpec.ConfigValue<Double> SIT_BONUS;
    public static final ForgeConfigSpec.ConfigValue<Double> CAMPFIRE_BONUS;
    public static final ForgeConfigSpec.ConfigValue<Double> FOOD_FACTOR;

    // === Mining ===
    public static final ForgeConfigSpec.EnumValue<Difficulty> MINING_ALL;
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
    public static final ForgeConfigSpec.EnumValue<Difficulty> INTERACT_ALL;
    public static final ForgeConfigSpec.ConfigValue<Double> INTERACT_PLACE_SHORT;
    public static final ForgeConfigSpec.ConfigValue<Double> INTERACT_PLACE_LONG;
    public static final ForgeConfigSpec.ConfigValue<Double> INTERACT_CLICK_SHORT;
    public static final ForgeConfigSpec.ConfigValue<Double> INTERACT_CLICK_LONG;

    // === Movement ===
    public static final ForgeConfigSpec.EnumValue<Difficulty> MOVEMENT_ALL;
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

    // === Combat ===
    public static final ForgeConfigSpec.EnumValue<Difficulty> COMBAT_ALL;
    public static final ForgeConfigSpec.ConfigValue<Double> COMBAT_HITTING_SHORT;
    public static final ForgeConfigSpec.ConfigValue<Double> COMBAT_HITTING_LONG;
    public static final ForgeConfigSpec.ConfigValue<Double> COMBAT_BLOCKING_SHORT;
    public static final ForgeConfigSpec.ConfigValue<Double> COMBAT_BLOCKING_LONG;
    public static final ForgeConfigSpec.ConfigValue<Double> COMBAT_DAMAGE_TAKEN_SHORT;
    public static final ForgeConfigSpec.ConfigValue<Double> COMBAT_DAMAGE_TAKEN_LONG;

    // === Thresholds ===
    public static final ForgeConfigSpec.ConfigValue<Integer> POSITIVE_EFFECT_THRESHOLD;
    public static final ForgeConfigSpec.ConfigValue<Integer> NEGATIVE_EFFECT_1_THRESHOLD;
    public static final ForgeConfigSpec.ConfigValue<Integer> NEGATIVE_EFFECT_2_THRESHOLD;
    public static final ForgeConfigSpec.ConfigValue<Integer> NEGATIVE_EFFECT_3_THRESHOLD;

    static {
        BUILDER.comment("Stamina Mod Settings");

        DIFFICULTY = BUILDER
                .comment("Choose Difficulty: EASY, MEDIUM, HARD")
                .defineEnum("Difficulty", Difficulty.MEDIUM);

        CUSTOM_SETTINGS = BUILDER
                .comment("If true, the custom settings below are used and override the difficulty setting above.")
                .define("Custom Settings", false);

        BUILDER.push("Advanced-Settings");
        BUILDER.comment("In the following, 'Short' refers to the active stamina consumption (blue bar, resets over time), while 'Long' refers to the flexible stamina cap (gray bar, resets upon sleep)");

        // === Regeneration ===
        BUILDER.push("Regeneration");
        REGENERATION_ALL = BUILDER.defineEnum("regenerationDifficulty", Difficulty.MEDIUM);
        BASE_REGEN_SHORT = DoubleValueRegeneration("baseRegenShort", 0.000175, "Always active | triggers per tick");
        BASE_REGEN_LONG = DoubleValueRegeneration("baseRegenLong", 0.002f, "Active while sitting | triggers per tick");
        SIT_BONUS = DoubleValueRegeneration("sitBonus", 1.5, "Amplifies the Base Regen Short and activates the Base Regen Long");
        CAMPFIRE_BONUS = DoubleValueTick("campfireBonus", 1.5);
        FOOD_FACTOR = DoubleValueRegeneration("foodFactor", 0.075, "Multiplies with (nutrition + saturation) | increases current Long Stamina");
        BUILDER.comment("Formula: BASE_REGEN * damageMultiplier * (1 + staminaLvl * 0.02) * restFactor * hardnessFactor\n-> if [CAMPFIRE_BONUS] is active: restFactor = sitBonus + campfireBonus\n-> else: restFactor = sitBonus\n-> hardnessFactor is based on the hardness of the block you are sitting on");
        BUILDER.pop();

        // === Mining ===
        BUILDER.push("Mining");
        MINING_ALL = BUILDER.defineEnum("miningDifficulty", Difficulty.MEDIUM);
        MINING_HAND_SHORT = DoubleValueTick("handShort", 0.125);
        MINING_HAND_LONG = DoubleValueTick("handLong", 0.0125);
        MINING_PICKAXE_SHORT = DoubleValueTick("pickaxeShort", 0.06);
        MINING_PICKAXE_LONG = DoubleValueTick("pickaxeLong", 0.005);
        MINING_AXE_SHORT = DoubleValueTick("axeShort", 0.055);
        MINING_AXE_LONG = DoubleValueTick("axeLong", 0.0025);
        MINING_SHOVEL_SHORT = DoubleValueTick("shovelShort", 0.0525);
        MINING_SHOVEL_LONG = DoubleValueTick("shovelLong", 0.0001);
        MINING_HOE_SHORT = DoubleValueTick("hoeShort", 0.05);
        MINING_HOE_LONG = DoubleValueTick("hoeLong", 0.0008);
        MINING_SHEARS_SHORT = DoubleValueTick("shearsShort", 1.0);
        MINING_SHEARS_LONG = DoubleValueTick("shearsLong", 1.0);
        MINING_UNKNOWN_SHORT = DoubleValueTick("unknownToolShort", 0.07);
        MINING_UNKNOWN_LONG = DoubleValueTick("unknownToolLong", 0.007);
        BUILDER.pop();

        // === Interact ===
        BUILDER.push("Interact");
        INTERACT_ALL = BUILDER.defineEnum("interactDifficulty", Difficulty.MEDIUM);
        INTERACT_PLACE_SHORT = DoubleValueTick("placeShort", 0.0025);
        INTERACT_PLACE_LONG = DoubleValueTick("placeLong", 0.0005);
        INTERACT_CLICK_SHORT = DoubleValueTick("clickShort", 0.175);
        INTERACT_CLICK_LONG = DoubleValueTick("clickLong", 0.015);
        BUILDER.pop();

        // === Movement ===
        BUILDER.push("Movement");
        MOVEMENT_ALL = BUILDER.defineEnum("movementDifficulty", Difficulty.MEDIUM);
        MOVEMENT_SPRINT_SHORT = DoubleValueTick("sprintShort", 0.05);
        MOVEMENT_SPRINT_LONG = DoubleValueTick("sprintLong", 0.002);
        MOVEMENT_WALK_SHORT = DoubleValueTick("walkShort", 0.0075);
        MOVEMENT_WALK_LONG = DoubleValueTick("walkLong", 0.001);
        MOVEMENT_SWIM_SHORT = DoubleValueTick("swimShort", 0.0075);
        MOVEMENT_SWIM_LONG = DoubleValueTick("swimLong", 0.001);
        MOVEMENT_CROUCH_SHORT = DoubleValueTick("crouchShort", 0.0075);
        MOVEMENT_CROUCH_LONG = DoubleValueTick("crouchLong", 0.001);
        MOVEMENT_JUMP_SHORT = DoubleValueAction("jumpShort", 0.15);
        MOVEMENT_JUMP_LONG = DoubleValueAction("jumpLong", 0.0075);
        BUILDER.pop();

        // === Combat ===
        BUILDER.push("Combat");
        COMBAT_ALL = BUILDER.defineEnum("combatDifficulty", Difficulty.MEDIUM);
        COMBAT_HITTING_SHORT = DoubleValueAction("hitShort", 0.5);
        COMBAT_HITTING_LONG = DoubleValueAction("hitLong", 0.05);
        COMBAT_BLOCKING_SHORT = DoubleValueTick("blockShort", 0.0075);
        COMBAT_BLOCKING_LONG = DoubleValueTick("blockLong", 0.001);
        COMBAT_DAMAGE_TAKEN_SHORT = DoubleValueAction("damageTakenShort", 1, "multiplies with amount of hearts lost");
        COMBAT_DAMAGE_TAKEN_LONG = DoubleValueAction("damageTakenLong", 0.1, "multiplies with amount of hearts lost");
        BUILDER.pop();

        // === Thresholds ===
        BUILDER.push("Potion-Effect-Thresholds");
        POSITIVE_EFFECT_THRESHOLD = IntegerValueTick("positiveEffect", 90, "Have at least [Positive Effect]% stamina to receive positive effects");
        NEGATIVE_EFFECT_1_THRESHOLD = IntegerValueTick("negativeEffect1", 15, "Have [Negative Effect 1]% or less stamina to suffer from negative effects");
        NEGATIVE_EFFECT_2_THRESHOLD = IntegerValueTick("negativeEffect2", 10, "Have [Negative Effect 2]% or less stamina to suffer from negative effects");
        NEGATIVE_EFFECT_3_THRESHOLD = IntegerValueTick("negativeEffect3", 5,  "Have [Negative Effect 3]% or less stamina to suffer from negative effects");
        BUILDER.pop();

        BUILDER.push("Level-And-Experience");
        KEEP_LEVEL_ON_DEATH = BUILDER
                .comment("If true, player keeps stamina level and experience after death.")
                .define("Keep Level On Death", true);

        SPEC = BUILDER.build();
    }


    private static ForgeConfigSpec.ConfigValue<Double> DoubleValueTick(String name, double def) {
        double rounded = Math.round(def * 10000.0) / 10000.0;
        return BUILDER.comment("Stamina consumption per tick").define(formatString(name), rounded);
    }

    private static ForgeConfigSpec.ConfigValue<Double> DoubleValueAction(String name, double def) {
        double rounded = Math.round(def * 10000.0) / 10000.0;
        return BUILDER.comment("Stamina consumption on Action").define(formatString(name), rounded);
    }
    private static ForgeConfigSpec.ConfigValue<Double> DoubleValueAction(String name, double def, String comment) {
        double rounded = Math.round(def * 10000.0) / 10000.0;
        return BUILDER.comment("Stamina consumption on Action | " + comment).define(formatString(name), rounded);
    }
    private static ForgeConfigSpec.ConfigValue<Double> DoubleValueRegeneration(String name, double def, String comment) {
        double rounded = Math.round(def * 10000.0) / 10000.0;
        return BUILDER.comment(comment).define(formatString(name), rounded);
    }
    private static ForgeConfigSpec.ConfigValue<Integer> IntegerValueTick(String name, int def, String comment) {
        if (comment == null) comment = "Percentage effect trigger threshold | Min = 0 | Max = 100";
        return BUILDER.comment(comment).define(formatString(name), def);
    }

    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD
    }

    public static String formatString(String input) {
        if (input == null || input.isEmpty()) return input;

        // 1. Vor jedem Großbuchstaben oder jeder Zahl eine Leerstelle einfügen (außer am Anfang)
        String spaced = input.replaceAll("(?<!^)(?=[A-Z0-9])", " ");

        // 2. Jedes Wort großschreiben
        String[] words = spaced.split("\\s+");
        StringBuilder sb = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0))); // erster Buchstabe groß
                if (word.length() > 1) {
                    sb.append(word.substring(1).toLowerCase()); // Rest klein
                }
                sb.append(" ");
            }
        }

        return sb.toString().trim(); // führende/folgende Leerzeichen entfernen
    }
}
