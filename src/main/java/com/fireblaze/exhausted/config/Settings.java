package com.fireblaze.exhausted.config;

import java.util.*;

public class Settings {
    private static Set<String> comfortWallBlocksBlacklist = new HashSet<>();
    private static Set<String> comfortBlocks = new HashSet<>();
    private static Set<String> comfortBlockGroups = new HashSet<>();
    private static boolean comfortAllowStorage = false;

    // ----- UI -----
    public static int getUiScalings(String uiPart) {
        return switch (uiPart.toLowerCase()) {
            case "x" -> ClientConfig.UI_X_COORDINATE_STAMINA_BAR.get();
            case "y" -> ClientConfig.UI_Y_COORDINATE_STAMINA_BAR.get();
            case "width" -> ClientConfig.UI_WIDTH_STAMINA_BAR.get();
            default -> 0;
        };
    }

    public static boolean getAllowFading() {
        return ClientConfig.UI_HIDE_STAMINA_BAR.get();
    }

    // ----- Mining -----
    public static double getMiningCost(String tool, boolean isShort) {
        if (CommonConfig.CUSTOM_SETTINGS.get()) {
            return switch (tool.toLowerCase()) {
                case "hand" -> isShort ? CommonConfig.MINING_HAND_SHORT.get() : CommonConfig.MINING_HAND_LONG.get();
                case "pickaxe" -> isShort ? CommonConfig.MINING_PICKAXE_SHORT.get() : CommonConfig.MINING_PICKAXE_LONG.get();
                case "axe" -> isShort ? CommonConfig.MINING_AXE_SHORT.get() : CommonConfig.MINING_AXE_LONG.get();
                case "shovel" -> isShort ? CommonConfig.MINING_SHOVEL_SHORT.get() : CommonConfig.MINING_SHOVEL_LONG.get();
                case "hoe" -> isShort ? CommonConfig.MINING_HOE_SHORT.get() : CommonConfig.MINING_HOE_LONG.get();
                case "shears" -> isShort ? CommonConfig.MINING_SHEARS_SHORT.get() : CommonConfig.MINING_SHEARS_LONG.get();
                case "break" -> isShort ? CommonConfig.MINING_BLOCK_BREAK_SHORT.get() : CommonConfig.MINING_BLOCK_BREAK_LONG.get();
                default -> isShort ? CommonConfig.MINING_UNKNOWN_SHORT.get() : CommonConfig.MINING_UNKNOWN_LONG.get();
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
        if (CommonConfig.CUSTOM_SETTINGS.get()) {
            return switch (type.toLowerCase()) {
                case "sprint" -> isShort ? CommonConfig.MOVEMENT_SPRINT_SHORT.get() : CommonConfig.MOVEMENT_SPRINT_LONG.get();
                case "walk" -> isShort ? CommonConfig.MOVEMENT_WALK_SHORT.get() : CommonConfig.MOVEMENT_WALK_LONG.get();
                case "swim" -> isShort ? CommonConfig.MOVEMENT_SWIM_SHORT.get() : CommonConfig.MOVEMENT_SWIM_LONG.get();
                case "crouch" -> isShort ? CommonConfig.MOVEMENT_CROUCH_SHORT.get() : CommonConfig.MOVEMENT_CROUCH_LONG.get();
                case "jump" -> isShort ? CommonConfig.MOVEMENT_JUMP_SHORT.get() : CommonConfig.MOVEMENT_JUMP_LONG.get();
                case "horseride" -> isShort ? CommonConfig.MOVEMENT_HORSE_RIDE_SHORT.get() : CommonConfig.MOVEMENT_HORSE_RIDE_LONG.get();
                case "boatdrive" -> isShort ? CommonConfig.MOVEMENT_BOAT_DRIVE_SHORT.get() : CommonConfig.MOVEMENT_BOAT_DRIVE_LONG.get();
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
        if (CommonConfig.CUSTOM_SETTINGS.get()) {
            return switch (type.toLowerCase()) {
                case "hit" -> isShort ? CommonConfig.COMBAT_HITTING_SHORT.get() : CommonConfig.COMBAT_HITTING_LONG.get();
                case "block" -> isShort ? CommonConfig.COMBAT_BLOCKING_SHORT.get() : CommonConfig.COMBAT_BLOCKING_LONG.get();
                case "damagetaken" -> isShort ? CommonConfig.COMBAT_DAMAGE_TAKEN_SHORT.get() : CommonConfig.COMBAT_DAMAGE_TAKEN_LONG.get();
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
        if (CommonConfig.CUSTOM_SETTINGS.get()) {
            return switch (type.toLowerCase()) {
                case "leather" -> CommonConfig.ARMOR_MATERIAL_LEATHER.get();
                case "chain" -> CommonConfig.ARMOR_MATERIAL_CHAIN.get();
                case "iron" -> CommonConfig.ARMOR_MATERIAL_IRON.get();
                case "gold" -> CommonConfig.ARMOR_MATERIAL_GOLD.get();
                case "diamond" -> CommonConfig.ARMOR_MATERIAL_DIAMOND.get();
                case "netherite" -> CommonConfig.ARMOR_MATERIAL_NETHERITE.get();
                case "unknown" -> CommonConfig.ARMOR_MATERIAL_UNKOWN.get();
                case "helmetaction" -> CommonConfig.ARMOR_HELMET_MULTIPLIER_ACTION.get();
                case "chestplateaction" -> CommonConfig.ARMOR_CHESTPLATE_MULTIPLIER_ACTION.get();
                case "leggingsaction" -> CommonConfig.ARMOR_LEGGINGS_MULTIPLIER_ACTION.get();
                case "bootsaction" -> CommonConfig.ARMOR_BOOTS_MULTIPLIER_ACTION.get();
                case "helmetmovement" -> CommonConfig.ARMOR_HELMET_MULTIPLIER_MOVEMENT.get();
                case "chestplatemovement" -> CommonConfig.ARMOR_CHESTPLATE_MULTIPLIER_MOVEMENT.get();
                case "leggingsmovement" -> CommonConfig.ARMOR_LEGGINGS_MULTIPLIER_MOVEMENT.get();
                case "bootsmovement" -> CommonConfig.ARMOR_BOOTS_MULTIPLIER_MOVEMENT.get();
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
        if (CommonConfig.CUSTOM_SETTINGS.get()) {
            return switch (type.toLowerCase()) {
                case "place" -> isShort ? CommonConfig.INTERACT_PLACE_SHORT.get() : CommonConfig.INTERACT_PLACE_LONG.get();
                case "click" -> isShort ? CommonConfig.INTERACT_CLICK_SHORT.get() : CommonConfig.INTERACT_CLICK_LONG.get();
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
        if (CommonConfig.CUSTOM_SETTINGS.get()) {
            return switch (type.toLowerCase()) {
                case "baseregenshort" -> CommonConfig.BASE_REGEN_SHORT.get();
                case "baseregenlong" -> CommonConfig.BASE_REGEN_LONG.get();
                case "comfortregmultiplier" -> CommonConfig.COMFORT_REG_MULTIPLIER.get();
                case "foodfactor" -> CommonConfig.FOOD_FACTOR.get();
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
        comfortWallBlocksBlacklist.addAll(CommonConfig.COMFORT_WALL_BLOCKS_BLACKLIST.get());
        comfortBlocks.clear();
        comfortBlocks.addAll(CommonConfig.COMFORT_BLOCKS_WHITELIST.get());
        comfortBlockGroups.clear();
        comfortBlockGroups.addAll(CommonConfig.COMFORT_BLOCK_GROUPS.get());

        comfortAllowStorage = CommonConfig.COMFORT_ALLOW_STORAGE_BLOCKS.get();
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
            case "ceiling" -> CommonConfig.COMFORT_BONUS_CEILING.get();
            case "space" -> CommonConfig.COMFORT_BONUS_SPACE.get();
            case "walls" -> CommonConfig.COMFORT_BONUS_WALLS.get();
            case "light" -> CommonConfig.COMFORT_BONUS_LIGHT.get();
            case "animal" -> CommonConfig.COMFORT_BONUS_ANIMAL.get();
            case "food" -> CommonConfig.COMFORT_BONUS_FOOD.get();
            case "block_max" -> CommonConfig.COMFORT_BONUS_BLOCK.get();
            case "block_per" -> CommonConfig.COMFORT_BONUS_PER_BLOCK.get();
            case "boost_block" -> CommonConfig.COMFORT_BONUS_BOOST_BLOCK.get();
            default -> 0;
        };
    }
    public static double getComfortMalus(String type) {
        return switch (type.toLowerCase()) {
            case "in_rain" -> CommonConfig.COMFORT_MALUS_IN_RAIN.get();
            case "hurt" -> CommonConfig.COMFORT_MALUS_HURT.get();
            case "hungry" -> CommonConfig.COMFORT_MALUS_HUNGRY.get();
            case "dimension" -> CommonConfig.COMFORT_MALUS_FOREIGN_DIMENSION.get();
            case "monsters" -> CommonConfig.COMFORT_MALUS_MONSTERS.get();
            default -> 0;
        };
    }

    public static double getComfortSettings(String type) {
        return switch (type.toLowerCase()) {
            case "monsterrangetolerance" -> CommonConfig.MONSTER_RANGE_TOLERANCE.get();
            default -> 0;
        };
    }

    public static int getComfortThresholdSit() {
        return CommonConfig.COMFORT_THRESHOLD_SIT.get();
    }

    public static int getComfortThresholdSleep() {
        return CommonConfig.COMFORT_THRESHOLD_SLEEP.get();
    }

    // ----- Thresholds -----
    public static double getPositiveEffectThreshold() {
        return CommonConfig.POSITIVE_EFFECT_THRESHOLD.get();
    }

    public static double getNegativeEffect1Threshold() {
        return CommonConfig.NEGATIVE_EFFECT_1_THRESHOLD.get();
    }

    public static double getNegativeEffect2Threshold() {
        return CommonConfig.NEGATIVE_EFFECT_2_THRESHOLD.get();
    }

    public static double getNegativeEffect3Threshold() {return CommonConfig.NEGATIVE_EFFECT_3_THRESHOLD.get();}
    public static boolean getBreathVolume() {return ClientConfig.SOUND_BREATHING.get();}
    public static boolean getBreathAnimation() {return ClientConfig.ANIMATION_BREATHING.get();}

    // ----- Dimension Boost Blocks -----
    public static Map<String, List<String>> getDimensionBoostBlocks() {

        Map<String, List<String>> result = new HashMap<>();

        for (String entry : CommonConfig.DIMENSION_BOOST_BLOCKS.get()) {

            String[] split = entry.split("=");

            if (split.length != 2)
                continue;

            result
                    .computeIfAbsent(split[0], k -> new ArrayList<>())
                    .add(split[1]);
        }

        return result;
    }

    // ----- Global Difficulty ----
    private static double getDifficultyMultiplier() {
        return switch (CommonConfig.DIFFICULTY.get()) {
            case EASY -> 0.75;
            case MEDIUM -> 1.0;
            case HARD -> 1.25;
            default -> 1.0;
        };
    }
}