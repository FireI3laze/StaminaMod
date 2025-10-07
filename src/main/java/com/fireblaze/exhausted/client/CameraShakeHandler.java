package com.fireblaze.exhausted.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.CameraType;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = net.minecraftforge.api.distmarker.Dist.CLIENT)
public class CameraShakeHandler {
    private static float currentIntensity = 0f;
    private static float targetIntensity = 0f;
    private static float currentFrequency = 0.6f;
    private static float targetFrequency = 0.6f;

    private static int currentDuration = 0;
    private static long startTime = 0;
    private static long endTime = 0;

    private static boolean active = false;

    private static final float SMOOTH_SPEED = 0.1f;

    public static void startShake(float newIntensity, int durationMs, float newFrequency) {
        long current = System.currentTimeMillis();

        // Immer neuen Effekt starten
        System.out.printf("[CameraShake] ➤ Neuer Effekt gestartet: Intensität=%.2f, Frequenz=%.2f, Dauer=%dms%n",
                newIntensity, newFrequency, durationMs);

        startTime = current;
        endTime = current + durationMs;
        currentDuration = durationMs;

        targetIntensity = newIntensity;
        targetFrequency = newFrequency;
        active = true;
    }

    @SubscribeEvent
    public static void onCameraSetup(ViewportEvent.ComputeCameraAngles event) {
        // 🚫 Nur in First-Person aktiv
        if (Minecraft.getInstance().options.getCameraType() != CameraType.FIRST_PERSON) {
            return;
        }

        long current = System.currentTimeMillis();

        if (current >= endTime || (currentIntensity <= 0.01f && targetIntensity <= 0.01f)) {
            if (active) {
                System.out.println("[CameraShake] ✦ Effekt beendet.");
                active = false;
            }
            return;
        }

        float elapsed = (current - startTime) / (float) currentDuration;
        elapsed = Math.min(Math.max(elapsed, 0f), 1f);

        float fade = 1f - (float) Math.pow(elapsed, 2);

        currentIntensity += (targetIntensity - currentIntensity) * SMOOTH_SPEED;
        currentFrequency += (targetFrequency - currentFrequency) * SMOOTH_SPEED;

        double time = (current - startTime) / 1000.0;
        double breathing = Math.sin(time * Math.PI * 2.0 * currentFrequency);
        double microJitter = Math.sin(time * Math.PI * 6.0) * 0.05;

        double breathPitch = breathing * currentIntensity * fade * 1.2;
        double breathYaw = microJitter * currentIntensity * fade * 0.2;

        event.setPitch((float) (event.getPitch() + breathPitch));
        event.setYaw((float) (event.getYaw() + breathYaw));
    }
}
