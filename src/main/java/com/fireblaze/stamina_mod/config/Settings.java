package com.fireblaze.stamina_mod.config;

public class Settings {

    // Mining
    public static double getMiningCost(String tool, boolean isShort) {
        return switch (tool.toLowerCase()) {
            case "hand" -> isShort ? StaminaConfig.MINING_HAND_SHORT.get() : StaminaConfig.MINING_HAND_LONG.get();
            case "pickaxe" -> isShort ? StaminaConfig.MINING_PICKAXE_SHORT.get() : StaminaConfig.MINING_PICKAXE_LONG.get();
            case "axe" -> isShort ? StaminaConfig.MINING_AXE_SHORT.get() : StaminaConfig.MINING_AXE_LONG.get();
            case "shovel" -> isShort ? StaminaConfig.MINING_SHOVEL_SHORT.get() : StaminaConfig.MINING_SHOVEL_LONG.get();
            case "hoe" -> isShort ? StaminaConfig.MINING_HOE_SHORT.get() : StaminaConfig.MINING_HOE_LONG.get();
            case "shears" -> isShort ? StaminaConfig.MINING_SHEARS_SHORT.get() : StaminaConfig.MINING_SHEARS_LONG.get();
            default -> isShort ? StaminaConfig.MINING_UNKNOWN_SHORT.get() : StaminaConfig.MINING_UNKNOWN_LONG.get();
        };
    }

    public static StaminaConfig.Difficulty getMiningAllDifficulty() {
        return StaminaConfig.MINING_ALL.get();
    }

    // Movement
    public static double getMovementCost(String type, boolean isShort) {
        return switch (type.toLowerCase()) {
            case "sprint" -> isShort ? StaminaConfig.MOVEMENT_SPRINT_SHORT.get() : StaminaConfig.MOVEMENT_SPRINT_LONG.get();
            case "walk" -> isShort ? StaminaConfig.MOVEMENT_WALK_SHORT.get() : StaminaConfig.MOVEMENT_WALK_LONG.get();
            case "swim" -> isShort ? StaminaConfig.MOVEMENT_SWIM_SHORT.get() : StaminaConfig.MOVEMENT_SWIM_LONG.get();
            case "crouch" -> isShort ? StaminaConfig.MOVEMENT_CROUCH_SHORT.get() : StaminaConfig.MOVEMENT_CROUCH_LONG.get();
            case "jump" -> isShort ? StaminaConfig.MOVEMENT_JUMP_SHORT.get() : StaminaConfig.MOVEMENT_JUMP_LONG.get();
            default -> isShort ? 0.00005 : 0;
        };
    }
    public static double getCombatCost(String type, boolean isShort) {
        return switch (type.toLowerCase()) {
            case "hit" -> isShort ? StaminaConfig.COMBAT_HITTING_SHORT.get() : StaminaConfig.COMBAT_HITTING_LONG.get();
            case "block" -> isShort ? StaminaConfig.COMBAT_BLOCKING_SHORT.get() : StaminaConfig.COMBAT_BLOCKING_LONG.get();
            case "damagetaken" -> isShort ? StaminaConfig.COMBAT_DAMAGE_TAKEN_SHORT.get() : StaminaConfig.COMBAT_DAMAGE_TAKEN_LONG.get();
            default -> isShort ? 0.00005 : 0;
        };
    }

    public static StaminaConfig.Difficulty getMovementAllDifficulty() {
        return StaminaConfig.MOVEMENT_ALL.get();
    }
    // todo

    // Interact
    public static double getInteractCost(String type, boolean isShort) {
        return switch (type.toLowerCase()) {
            case "place" -> isShort ? StaminaConfig.INTERACT_PLACE_SHORT.get() : StaminaConfig.INTERACT_PLACE_LONG.get();
            case "click" -> isShort ? StaminaConfig.INTERACT_CLICK_SHORT.get() : StaminaConfig.INTERACT_CLICK_LONG.get();
            default -> isShort ? 0.01 : 0;
        };
    }

    public static double getRegenerationConfigs(String type) {
        return switch (type.toLowerCase()) {
            case "baseregenshort" -> StaminaConfig.BASE_REGEN_SHORT.get();
            case "baseregenlong" -> StaminaConfig.BASE_REGEN_LONG.get();
            case "sitbonus" -> StaminaConfig.SIT_BONUS.get();
            case "campfirebonus" -> StaminaConfig.CAMPFIRE_BONUS.get();
            case "foodfactor" -> StaminaConfig.FOOD_FACTOR.get();
            default -> 0;
        };
    }

    public static StaminaConfig.Difficulty getInteractAllDifficulty() {
        return StaminaConfig.INTERACT_ALL.get();
    }

    // Schwierigkeitsgrad global
    public static StaminaConfig.Difficulty getDifficulty() {
        return StaminaConfig.DIFFICULTY.get();
    }

    // Thresholds
    public static double getPositiveEffectThreshold() {
        return StaminaConfig.POSITIVE_EFFECT_THRESHOLD.get();
    }

    public static double getNegativeEffect1Threshold() {
        return StaminaConfig.NEGATIVE_EFFECT_1_THRESHOLD.get();
    }

    public static double getNegativeEffect2Threshold() {
        return StaminaConfig.NEGATIVE_EFFECT_2_THRESHOLD.get();
    }

    public static double getNegativeEffect3Threshold() {
        return StaminaConfig.NEGATIVE_EFFECT_3_THRESHOLD.get();
    }
}
