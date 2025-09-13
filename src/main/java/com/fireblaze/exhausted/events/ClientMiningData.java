package com.fireblaze.exhausted.events;

public class ClientMiningData {
    private static float speedMultiplier = 1.0f;

    public static void setSpeedMultiplier(float multiplier) {
        speedMultiplier = multiplier;
    }

    public static float getSpeedMultiplier() {
        return speedMultiplier;
    }
}
