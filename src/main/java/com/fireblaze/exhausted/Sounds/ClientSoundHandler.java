package com.fireblaze.exhausted.Sounds;

import com.fireblaze.exhausted.config.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;

import java.util.ArrayDeque;
import java.util.Queue;

public class ClientSoundHandler {
    private static int currentThreshold = -1;
    private static AbstractSoundInstance currentSound;

    // Queue für SoundEvents
    private static final Queue<Integer> pendingThresholds = new ArrayDeque<>();

    public static void playThresholdSound(int newThreshold) {
        // Check if sound is registered
        SoundEvent soundEvent = getSoundEvent(newThreshold);
        if (soundEvent == null && newThreshold != -1) {
            System.out.println("NEGATIVE_" + newThreshold + " noch nicht registriert, in Queue setzen!");
            pendingThresholds.add(newThreshold);
            // Try again later
            Minecraft.getInstance().execute(ClientSoundHandler::tryPendingSounds);
            return;
        }

        // Wenn registriert, Sound sofort abspielen
        if (newThreshold >= 0) playSound(soundEvent, newThreshold);
        currentThreshold = newThreshold;
    }

    private static void tryPendingSounds() {
        while (!pendingThresholds.isEmpty()) {
            int threshold = pendingThresholds.peek();
            SoundEvent soundEvent = getSoundEvent(threshold);
            if (soundEvent == null) break; // noch nicht registriert
            pendingThresholds.poll(); // entfernen aus Queue
            playSound(soundEvent, threshold);
            currentThreshold = threshold;
        }
    }

    private static SoundEvent getSoundEvent(int threshold) {
        return switch (threshold) {
            case 0 -> ModSounds.NEGATIVE_1.isPresent() ? ModSounds.NEGATIVE_1.get() : null;
            case 1, 2 -> ModSounds.NEGATIVE_2.isPresent() ? ModSounds.NEGATIVE_2.get() : null;
            default -> null;
        };
    }

    private static void playSound(SoundEvent soundEvent, int threshold) {
        if (!Settings.getBreathVolume()) return;
        Minecraft mc = Minecraft.getInstance();
        SoundManager manager = mc.getSoundManager();

        if (threshold > currentThreshold || currentThreshold == -1) {
            if (currentSound != null) {
                manager.stop(currentSound);
                currentSound = null;
            }

            currentSound = SimpleSoundInstance.forLocalAmbience(soundEvent, 1.0f, 1.0f);
            mc.getSoundManager().play(currentSound);
        }
    }
}
