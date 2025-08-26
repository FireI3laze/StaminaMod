package com.fireblaze.exhausted.global;

public class GlobalTickCounter {
    private static int counter = 0;
    public static final int TICK_MULTIPLIER = 5;

    public static boolean shouldProcessThisTick() {
        if (counter >= TICK_MULTIPLIER) {
            counter = 0;
            return true;
        }
        return false;
    }

    public static void tick() {
        counter++;
        if (counter >= TICK_MULTIPLIER) counter = 0;
    }
}
