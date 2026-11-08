
package com.fireblaze.exhausted.utils;

import com.fireblaze.exhausted.config.CommonConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.ArrayList;
import java.util.List;

public final class FoodStaminaRegeneration {

    private FoodStaminaRegeneration() {}

    // Alle Werte sind Prozentwerte des maximalen Stamina-Werts (100).
    // Ein Ziel ohne # bezeichnet ein einzelnes Item.
    // Ein Ziel mit # bezeichnet ein Item-Tag.

    private static List<Entry> loadEntriesFromConfig() {
        List<Entry> entries = new ArrayList<>();

        for (String value : CommonConfig.FOOD_STAMINA_ENTRIES.get()) {
            String[] parts = value.split(";");

            if (parts.length != 3) {
                continue;
            }

            try {
                String target = parts[0].trim();
                float shortPercent = Float.parseFloat(parts[1].trim());
                float longPercent = Float.parseFloat(parts[2].trim());

                entries.add(new Entry(target, shortPercent, longPercent));
            } catch (NumberFormatException e) {
                // Ungültige Zahlenwerte in der Config überspringen.
            }
        }

        return entries;
    }

    public static class Entry {
        private final String target;
        private final float shortPercent;
        private final float longPercent;

        public Entry(String target, float shortPercent, float longPercent) {
            this.target = target;
            this.shortPercent = shortPercent;
            this.longPercent = longPercent;
        }

        public String getTarget() {
            return target;
        }

        public float getShortPercent() {
            return shortPercent;
        }

        public float getLongPercent() {
            return longPercent;
        }
    }

    /**
     * Sucht zuerst nach einem exakten Item-Eintrag.
     * Falls keiner existiert, werden passende Item-Tags geprüft.
     */
    public static Entry find(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }

        ResourceLocation itemId =
                BuiltInRegistries.ITEM.getKey(stack.getItem());

        List<Entry> entries = loadEntriesFromConfig();

        // 1. Exakte Item-Zuordnung hat Vorrang.
        for (Entry entry : entries) {
            if (entry.target.startsWith("#")) {
                continue;
            }

            ResourceLocation targetId =
                    ResourceLocation.tryParse(entry.target);

            if (targetId != null && targetId.equals(itemId)) {
                return entry;
            }
        }

        // 2. Erst danach passende Tags prüfen.
        for (Entry entry : entries) {
            if (!entry.target.startsWith("#")) {
                continue;
            }

            ResourceLocation tagId =
                    ResourceLocation.tryParse(entry.target.substring(1));

            if (tagId == null) {
                continue;
            }

            TagKey<Item> tag = TagKey.create(
                    Registries.ITEM,
                    tagId
            );

            if (stack.is(tag)) {
                return entry;
            }
        }

        return null;
    }
}
