package com.fireblaze.exhausted.stepUp;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public class ClientStepUpHandler {

    public static void setStepUp(float stepUpHeight) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            player.setMaxUpStep(stepUpHeight);
        }
    }
}
