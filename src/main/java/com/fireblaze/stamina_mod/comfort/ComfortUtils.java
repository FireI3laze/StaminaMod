package com.fireblaze.stamina_mod.comfort;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ComfortUtils {


    public static Component formatComfort(double comfort) {
        String color = comfort >= 75 ? "§a" : comfort >= 40 ? "§e" : "§c";

        DecimalFormat df = new DecimalFormat("0.##");
        String formatted = df.format(comfort);

        return Component.literal(color + "Comfort: " + formatted);
    }

    public static Component getComfortMessage(Player player, double comfort, double comfortThreshold, List<String> issues) {
        if (!player.isCrouching()) {
            List<String> messages = new ArrayList<>();
            for (String issue : issues) {
                messages.addAll(getMessagesForIssue(issue));
            }
            if (!messages.isEmpty()) {
                String random = messages.get((int)(Math.random() * messages.size()));

                DecimalFormat df = new DecimalFormat("0.##");
                String formattedComfort = df.format(comfort);
                String formattedThreshold = df.format(comfortThreshold);

                return Component.literal("§c" + random + " (" + formattedComfort + "/" + formattedThreshold + " comfort)");
            }
        }
        // fallback → formatComfort
        return formatComfort(comfort);
    }


    public static List<String> getMessagesForIssue(String issue) {
        List<String> messages = new ArrayList<>();
        switch (issue) {
            case "monsters" -> {
                messages.add("The monsters are scary");
                messages.add("I can hear monsters");
            }
            case "low_ceiling" -> messages.add("I feel cramped here");
            case "not_enough_space" -> {
                messages.add("Not enough space to sit comfortably");
                messages.add("I can hardly stretch my legs");
            }
            case "dark" -> {
                messages.add("It's too dark to relax");
                messages.add("It's pitch black");
            }
            case "lonely" -> messages.add("I wish I had a friend");
            case "no_food_stuck" -> {
                messages.add("What should I eat tomorrow?");
                messages.add("I have no food stocked");
            }
            case "wet" -> {
                messages.add("I'm getting wet");
                messages.add("The rain is tedious");
            }
            case "hurt" -> messages.add("I'm hurt and uneasy");
            case "hungry" -> {
                messages.add("I'm hungry and can't relax");
                messages.add("My stomach is growling");
            }
        }
        return messages;
    }
}
