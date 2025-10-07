package com.fireblaze.exhausted.comfort;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import com.fireblaze.exhausted.config.Settings;

import java.util.ArrayList;
import java.util.List;

public class ComfortCalculator {
    public static class ComfortResult {
        public double comfort;
        public final List<String> issues = new ArrayList<>();

        public ComfortResult(double comfort) {
            this.comfort = comfort;
        }
    }

    public static ComfortResult calculateComfort(ServerLevel level, Player player, BlockPos pos) {
        double comfort = 0.0;
        ComfortResult result = new ComfortResult(0.0);
        pos = pos.above();

        StringBuilder string = new StringBuilder();
        if (player.isCreative() || player.isSpectator()) {
            result.comfort = 999999;
            return result;
        }

        if (Settings.getComfortBonus("walls") != 0) {
            if (ComfortHelper.hasDecoratedWalls(level, pos)) {
                double bonus = Settings.getComfortBonus("walls");
                comfort += bonus;
                string.append("Has deco walls +").append(bonus).append("\n");
            } else result.issues.add("no_deco_walls");
        }

        if (Settings.getComfortBonus("ceiling") != 0) {
            if (ComfortHelper.hasCeilingHeight(level, pos, 2)) {
                double bonus = Settings.getComfortBonus("ceiling");
                comfort += bonus;
                string.append("Has ceiling +").append(bonus).append("\n");
            } else result.issues.add("low_ceiling");
        }

        if (Settings.getComfortBonus("space") != 0) {
            if (ComfortHelper.hasEnoughSpace(level, pos, 24)) {
                double bonus = Settings.getComfortBonus("space");
                comfort += bonus;
                string.append("Has enough space +").append(bonus).append("\n");
            } else result.issues.add("no_space");
        }

        if (Settings.getComfortBonus("light") != 0) {
            if (ComfortHelper.hasGoodLighting(level, pos)){
                double bonus = Settings.getComfortBonus("light");
                comfort += bonus;
                string.append("Has good lighting +").append(bonus).append("\n");
            } else result.issues.add("dark");
        }

        if (Settings.getComfortBonus("animal") != 0) {
            if (ComfortHelper.hasFriendlyAnimalNearby(level, pos)) {
                double bonus = Settings.getComfortBonus("animal");
                comfort += bonus;
                string.append("Animal nearby +").append(bonus).append("\n");
            } else result.issues.add("lonely");
        }

        if (Settings.getComfortBonus("food") != 0) {
            if (ComfortHelper.chestWithFoodNearby(level, pos)) {
                double bonus = Settings.getComfortBonus("food");
                comfort += bonus;
                string.append("Food around +").append(bonus).append("\n");
            } else result.issues.add("no_food_stock");
        }

        double blockCount = ComfortHelper.countComfortBlocks(level, pos);
        double blockBonus = Math.min(Settings.getComfortBonus("block_max"), blockCount * Settings.getComfortBonus("block_per"));
        comfort += blockBonus;
        string.append("Comfort blocks: ").append(blockCount).append(" => +").append(blockBonus).append("%\n");
        result.issues.add("no_comfort_blocks");

        if (ComfortHelper.hasBoostBlock(level, pos)) {
            double bonus = Settings.getComfortBonus("boost_block");
            comfort += bonus;
            string.append("Boost block present +").append(bonus).append("\n");
            result.issues.add("no_boost_block");
        }

        if (player.isInWaterOrRain()) {
            double malus = Settings.getComfortMalus("in_rain");
            comfort += malus;
            string.append("Wet ").append(malus).append("\n");
            result.issues.add("wet");
        }

        if (player.isHurt()) {
            double malus = Settings.getComfortMalus("hurt");
            comfort += malus;
            string.append("Hurt ").append(malus).append("\n");
            result.issues.add("hurt");
        }

        if (player.getFoodData().getFoodLevel() < 16) {
            double malus = Settings.getComfortMalus("hungry");
            comfort += malus;
            string.append("Hungry ").append(malus).append("\n");
            result.issues.add("hungry");
        }

        if (Settings.getComfortMalus("dimension") != 0 && !level.dimension().location().toString().equals("minecraft:overworld")) {
            double malus = Settings.getComfortMalus("dimension");
            comfort += malus;
            string.append("Not in Overworld ").append(malus).append("\n");
            result.issues.add("foreign_dimension");
        }

        if (Settings.getComfortMalus("monsters") != 0 && ComfortHelper.monstersNearby(level, pos)) {
            double malus = Settings.getComfortMalus("monsters");
            comfort += malus;
            string.append("Monsters around ").append(malus).append("\n");
            result.issues.add("monsters");
        }

        // Optional: Debug
        result.comfort = comfort;
        //System.out.println(string);

        return result;
    }
}
