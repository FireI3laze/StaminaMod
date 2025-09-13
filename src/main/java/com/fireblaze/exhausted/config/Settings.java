package com.fireblaze.exhausted.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Settings {
    private static Set<String> comfortWallBlocksBlacklist = new HashSet<>();
    private static Set<String> comfortBlocks = new HashSet<>();
    private static Set<String> comfortBlockGroups = new HashSet<>();
    private static boolean comfortAllowStorage = false;

    // ----- UI -----
    public static int getUiScalings(String uiPart) {
        return switch (uiPart.toLowerCase()) {
            case "x" -> StaminaConfig.UI_X_COORDINATE_STAMINA_BAR.get();
            case "y" -> StaminaConfig.UI_Y_COORDINATE_STAMINA_BAR.get();
            case "width" -> StaminaConfig.UI_WIDTH_STAMINA_BAR.get();
            default -> 0;
        };
    }

    public static boolean getAllowFading() {
        return StaminaConfig.UI_HIDE_STAMINA_BAR.get();
    }

    // ----- Mining -----
    public static double getMiningCost(String tool, boolean isShort) {
        if (StaminaConfig.CUSTOM_SETTINGS.get()) {
            return switch (tool.toLowerCase()) {
                case "hand" -> isShort ? StaminaConfig.MINING_HAND_SHORT.get() : StaminaConfig.MINING_HAND_LONG.get();
                case "pickaxe" -> isShort ? StaminaConfig.MINING_PICKAXE_SHORT.get() : StaminaConfig.MINING_PICKAXE_LONG.get();
                case "axe" -> isShort ? StaminaConfig.MINING_AXE_SHORT.get() : StaminaConfig.MINING_AXE_LONG.get();
                case "shovel" -> isShort ? StaminaConfig.MINING_SHOVEL_SHORT.get() : StaminaConfig.MINING_SHOVEL_LONG.get();
                case "hoe" -> isShort ? StaminaConfig.MINING_HOE_SHORT.get() : StaminaConfig.MINING_HOE_LONG.get();
                case "shears" -> isShort ? StaminaConfig.MINING_SHEARS_SHORT.get() : StaminaConfig.MINING_SHEARS_LONG.get();
                case "break" -> isShort ? StaminaConfig.MINING_BLOCK_BREAK_SHORT.get() : StaminaConfig.MINING_BLOCK_BREAK_LONG.get();
                default -> isShort ? StaminaConfig.MINING_UNKNOWN_SHORT.get() : StaminaConfig.MINING_UNKNOWN_LONG.get();
            };
        } else {
            // Custom Settings aus: Base-Werte * Difficulty-Multiplikator
            double m = getDifficultyMultiplier();
            return switch (tool.toLowerCase()) {
                case "hand" -> isShort ? 0.125 * m : 0.01 * m;
                case "pickaxe" -> isShort ? 0.07 * m : 0.005 * m;
                case "axe" -> isShort ? 0.068 * m : 0.004 * m;
                case "shovel" -> isShort ? 0.066 * m : 0.0036 * m;
                case "hoe" -> isShort ? 0.065 * m : 0.0034 * m;
                case "shears" -> isShort ? m : 0.1 * m;
                case "break" -> isShort ? 0.03 * m : 0.001 * m;
                default -> isShort ? 0.07 * m : 0.005 * m;
            };
        }
    }

    // ----- Movement -----
    public static double getMovementCost(String type, boolean isShort) {
        if (StaminaConfig.CUSTOM_SETTINGS.get()) {
            return switch (type.toLowerCase()) {
                case "sprint" -> isShort ? StaminaConfig.MOVEMENT_SPRINT_SHORT.get() : StaminaConfig.MOVEMENT_SPRINT_LONG.get();
                case "walk" -> isShort ? StaminaConfig.MOVEMENT_WALK_SHORT.get() : StaminaConfig.MOVEMENT_WALK_LONG.get();
                case "swim" -> isShort ? StaminaConfig.MOVEMENT_SWIM_SHORT.get() : StaminaConfig.MOVEMENT_SWIM_LONG.get();
                case "crouch" -> isShort ? StaminaConfig.MOVEMENT_CROUCH_SHORT.get() : StaminaConfig.MOVEMENT_CROUCH_LONG.get();
                case "jump" -> isShort ? StaminaConfig.MOVEMENT_JUMP_SHORT.get() : StaminaConfig.MOVEMENT_JUMP_LONG.get();
                case "horseride" -> isShort ? StaminaConfig.MOVEMENT_HORSE_RIDE_SHORT.get() : StaminaConfig.MOVEMENT_HORSE_RIDE_LONG.get();
                case "boatdrive" -> isShort ? StaminaConfig.MOVEMENT_BOAT_DRIVE_SHORT.get() : StaminaConfig.MOVEMENT_BOAT_DRIVE_LONG.get();
                default -> isShort ? 0.00005 : 0;
            };
        } else {
            double m = getDifficultyMultiplier();
            return switch (type.toLowerCase()) {
                case "sprint" -> isShort ? 0.05 * m : 0.0018 * m;
                case "walk" -> isShort ? 0.0075 * m : 0.001 * m;
                case "swim" -> isShort ? 0.0075 * m : 0.001 * m;
                case "crouch" -> isShort ? 0.0075 * m : 0.001 * m;
                case "jump" -> isShort ? 0.15 * m : 0.0075 * m;
                default -> isShort ? 0.00005 * m : 0;
            };
        }
    }

    // ----- Combat -----
    public static double getCombatCost(String type, boolean isShort) {
        if (StaminaConfig.CUSTOM_SETTINGS.get()) {
            return switch (type.toLowerCase()) {
                case "hit" -> isShort ? StaminaConfig.COMBAT_HITTING_SHORT.get() : StaminaConfig.COMBAT_HITTING_LONG.get();
                case "block" -> isShort ? StaminaConfig.COMBAT_BLOCKING_SHORT.get() : StaminaConfig.COMBAT_BLOCKING_LONG.get();
                case "damagetaken" -> isShort ? StaminaConfig.COMBAT_DAMAGE_TAKEN_SHORT.get() : StaminaConfig.COMBAT_DAMAGE_TAKEN_LONG.get();
                default -> isShort ? 0.00005 : 0;
            };
        } else {
            double m = getDifficultyMultiplier();
            return switch (type.toLowerCase()) {
                case "hit" -> isShort ? 0.5 * m : 0.05 * m;
                case "block" -> isShort ? 0.0075 * m : 0.001 * m;
                case "damagetaken" -> isShort ? 1.0 * m : 0.1 * m;
                default -> isShort ? 0.00005 * m : 0;
            };
        }
    }

    // ----- Armor -----
    public static double getArmorCost(String type) {
        if (StaminaConfig.CUSTOM_SETTINGS.get()) {
            return switch (type.toLowerCase()) {
                case "leather" -> StaminaConfig.ARMOR_MATERIAL_LEATHER.get();
                case "chain" -> StaminaConfig.ARMOR_MATERIAL_CHAIN.get();
                case "iron" -> StaminaConfig.ARMOR_MATERIAL_IRON.get();
                case "gold" -> StaminaConfig.ARMOR_MATERIAL_GOLD.get();
                case "diamond" -> StaminaConfig.ARMOR_MATERIAL_DIAMOND.get();
                case "netherite" -> StaminaConfig.ARMOR_MATERIAL_NETHERITE.get();
                case "unknown" -> StaminaConfig.ARMOR_MATERIAL_UNKOWN.get();
                case "helmetaction" -> StaminaConfig.ARMOR_HELMET_MULTIPLIER_ACTION.get();
                case "chestplateaction" -> StaminaConfig.ARMOR_CHESTPLATE_MULTIPLIER_ACTION.get();
                case "leggingsaction" -> StaminaConfig.ARMOR_LEGGINGS_MULTIPLIER_ACTION.get();
                case "bootsaction" -> StaminaConfig.ARMOR_BOOTS_MULTIPLIER_ACTION.get();
                case "helmetmovement" -> StaminaConfig.ARMOR_HELMET_MULTIPLIER_MOVEMENT.get();
                case "chestplatemovement" -> StaminaConfig.ARMOR_CHESTPLATE_MULTIPLIER_MOVEMENT.get();
                case "leggingsmovement" -> StaminaConfig.ARMOR_LEGGINGS_MULTIPLIER_MOVEMENT.get();
                case "bootsmovement" -> StaminaConfig.ARMOR_BOOTS_MULTIPLIER_MOVEMENT.get();
                default -> 0;
            };
        } else {
            double m = getDifficultyMultiplier();
            return switch (type.toLowerCase()) {
            case "leather" -> 0.03 * m;
            case "chain" -> 0.05;
            case "iron" -> 0.1;
            case "gold" -> 0.1;
            case "diamond" -> 0.125;
            case "netherite" -> 0.15;
            case "unknown" -> 0.175;
            case "helmetaction" -> 0.0;
            case "chestplateaction" -> 1.0;
            case "leggingsaction" -> 0.5;
            case "bootsaction" -> 0.25;
            case "helmetmovement" -> 0.25;
            case "chestplatemovement" -> 0.5;
            case "leggingsmovement" -> 1.0;
            case "bootsmovement" -> 0.75;
                default -> 0;
            };
        }
    }

    // ----- Interact -----
    public static double getInteractCost(String type, boolean isShort) {
        if (StaminaConfig.CUSTOM_SETTINGS.get()) {
            return switch (type.toLowerCase()) {
                case "place" -> isShort ? StaminaConfig.INTERACT_PLACE_SHORT.get() : StaminaConfig.INTERACT_PLACE_LONG.get();
                case "click" -> isShort ? StaminaConfig.INTERACT_CLICK_SHORT.get() : StaminaConfig.INTERACT_CLICK_LONG.get();
                default -> isShort ? 0.01 : 0;
            };
        } else {
            double m = getDifficultyMultiplier();
            return switch (type.toLowerCase()) {
                case "place" -> isShort ? 0.0025 * m : 0.0005 * m;
                case "click" -> isShort ? 0.175 * m : 0.015 * m;
                default -> isShort ? 0.01 * m : 0;
            };
        }
    }

    // ----- Regeneration -----
    public static double getRegenerationConfigs(String type) {
        if (StaminaConfig.CUSTOM_SETTINGS.get()) {
            return switch (type.toLowerCase()) {
                case "baseregenshort" -> StaminaConfig.BASE_REGEN_SHORT.get();
                case "baseregenlong" -> StaminaConfig.BASE_REGEN_LONG.get();
                case "comfortregmultiplier" -> StaminaConfig.COMFORT_REG_MULTIPLIER.get();
                case "foodfactor" -> StaminaConfig.FOOD_FACTOR.get();
                default -> 0;
            };
        } else {
            double m = 2 - getDifficultyMultiplier();
            return switch (type.toLowerCase()) {
                case "baseregenshort" -> 0.0002 * m;
                case "baseregenlong" -> 0.0015 * m;
                case "comfortregmultiplier" -> 0.0325 * m;
                case "foodfactor" -> 0.075 * m;
                default -> 0;
            };
        }
    }

    // ----- Comfort -----
    public static void loadComfortSettings() {
        comfortWallBlocksBlacklist.clear();
        comfortWallBlocksBlacklist.addAll(StaminaConfig.COMFORT_WALL_BLOCKS_BLACKLIST.get());
        comfortBlocks.clear();
        comfortBlocks.addAll(StaminaConfig.COMFORT_BLOCKS_WHITELIST.get());
        comfortBlockGroups.clear();
        comfortBlockGroups.addAll(StaminaConfig.COMFORT_BLOCK_GROUPS.get());

        comfortAllowStorage = StaminaConfig.COMFORT_ALLOW_STORAGE_BLOCKS.get();
    }
    public static Set<String> getWallBlocksBlacklist() {
        return comfortWallBlocksBlacklist;
    }
    public static Set<String> getComfortBlocks() {
        return comfortBlocks;
    }

    public static Set<String> getComfortBlockGroups() {
        return comfortBlockGroups;
    }

    public static boolean isComfortStorageAllowed() {
        return comfortAllowStorage;
    }

    public static double getComfortBonus(String type) {
        return switch (type.toLowerCase()) {
            case "ceiling" -> StaminaConfig.COMFORT_BONUS_CEILING.get();
            case "space" -> StaminaConfig.COMFORT_BONUS_SPACE.get();
            case "walls" -> StaminaConfig.COMFORT_BONUS_WALLS.get();
            case "light" -> StaminaConfig.COMFORT_BONUS_LIGHT.get();
            case "animal" -> StaminaConfig.COMFORT_BONUS_ANIMAL.get();
            case "food" -> StaminaConfig.COMFORT_BONUS_FOOD.get();
            case "block_max" -> StaminaConfig.COMFORT_BONUS_BLOCK.get();
            case "block_per" -> StaminaConfig.COMFORT_BONUS_PER_BLOCK.get();
            case "boost_block" -> StaminaConfig.COMFORT_BONUS_BOOST_BLOCK.get();
            default -> 0;
        };
    }
    public static double getComfortMalus(String type) {
        return switch (type.toLowerCase()) {
            case "in_rain" -> StaminaConfig.COMFORT_MALUS_IN_RAIN.get();
            case "hurt" -> StaminaConfig.COMFORT_MALUS_HURT.get();
            case "hungry" -> StaminaConfig.COMFORT_MALUS_HUNGRY.get();
            case "dimension" -> StaminaConfig.COMFORT_MALUS_FOREIGN_DIMENSION.get();
            case "monsters" -> StaminaConfig.COMFORT_MALUS_MONSTERS.get();
            default -> 0;
        };
    }

    public static double getComfortSettings(String type) {
        return switch (type.toLowerCase()) {
            case "monsterrangetolerance" -> StaminaConfig.MONSTER_RANGE_TOLERANCE.get();
            default -> 0;
        };
    }

    public static int getComfortThresholdSit() {
        return StaminaConfig.COMFORT_THRESHOLD_SIT.get();
    }

    public static int getComfortThresholdSleep() {
        return StaminaConfig.COMFORT_THRESHOLD_SLEEP.get();
    }

    // ----- Thresholds -----
    public static double getPositiveEffectThreshold() {
        return StaminaConfig.POSITIVE_EFFECT_THRESHOLD.get();
    }

    public static double getNegativeEffect1Threshold() {
        return StaminaConfig.NEGATIVE_EFFECT_1_THRESHOLD.get();
    }

    public static double getNegativeEffect2Threshold() {
        return StaminaConfig.NEGATIVE_EFFECT_2_THRESHOLD.get();
    }

    public static double getNegativeEffect3Threshold() {return StaminaConfig.NEGATIVE_EFFECT_3_THRESHOLD.get();}
    public static boolean getBreathVolume() {return StaminaConfig.SOUND_BREATHING.get();}

    // ----- Dimension Boost Blocks -----
    public static Map<String, String> getDimensionBoostBlocks() {
        Map<String, String> map = new HashMap<>();
        for (String entry : StaminaConfig.DIMENSION_BOOST_BLOCKS.get()) {
            String[] parts = entry.split("=");
            if (parts.length == 2) {
                map.put(parts[0].trim(), parts[1].trim());
            }
        }
        return map;
    }

    // ----- Global Difficulty ----
    private static double getDifficultyMultiplier() {
        return switch (StaminaConfig.DIFFICULTY.get()) {
            case EASY -> 0.75;
            case MEDIUM -> 1.0;
            case HARD -> 1.25;
            default -> 1.0;
        };
    }
}