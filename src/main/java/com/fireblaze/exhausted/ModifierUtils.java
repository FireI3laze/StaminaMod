package com.fireblaze.exhausted;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class ModifierUtils {

    // Präfix für deine Mod
    private static final String MOD_PREFIX = "clensed_";

    /**
     * Entfernt alle AttributeModifier eines Spielers, die zu deiner Mod gehören.
     */
    public static void removeAllCustomModifiers(Player player) {
        removeCustomModifiersFromAttribute(player, player.getAttribute(Attributes.MOVEMENT_SPEED));
        removeCustomModifiersFromAttribute(player, player.getAttribute(Attributes.ATTACK_DAMAGE));
        removeCustomModifiersFromAttribute(player, player.getAttribute(Attributes.ATTACK_SPEED));
        removeCustomModifiersFromAttribute(player, player.getAttribute(Attributes.KNOCKBACK_RESISTANCE));
        // Weitere Attribute hinzufügen falls nötig
    }

    private static void removeCustomModifiersFromAttribute(Player player, AttributeInstance attribute) {
        if (attribute == null) return;

        attribute.getModifiers().stream()
                .filter(mod -> mod.getName().startsWith(MOD_PREFIX))
                .forEach(attribute::removeModifier);
    }

    /**
     * Hilfsmethode zum Hinzufügen eines eigenen Modifiers mit MOD_PREFIX.
     */
    public static void addCustomModifier(Player player, Attribute attribute, String name, double value, AttributeModifier.Operation operation, UUID uuid) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance == null) return;

        AttributeModifier modifier = new AttributeModifier(uuid, MOD_PREFIX + name, value, operation);
        instance.addTransientModifier(modifier);
    }

    public static void addCustomModifier(Player player, Attribute attribute, String name, double value, AttributeModifier.Operation operation) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance == null) return;

        AttributeModifier modifier = new AttributeModifier(MOD_PREFIX + name, value, operation);
        instance.addTransientModifier(modifier);
    }
}
