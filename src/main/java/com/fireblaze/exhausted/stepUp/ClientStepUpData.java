package com.fireblaze.exhausted.stepUp;

public class ClientStepUpData {
    private static float stepUpHeight = 0.6f;

    public static void setStepUpHeight(float multiplier) {
        stepUpHeight = multiplier;
    }

    public static float getStepUpHeight() {
        return stepUpHeight;
    }
}
