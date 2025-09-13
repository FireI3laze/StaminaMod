package com.fireblaze.exhausted.events;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ClientMiningSpeedHandler {
    @SubscribeEvent
    public static void onBlockBreakSpeed(PlayerEvent.BreakSpeed event) {
        float multiplier = ClientMiningData.getSpeedMultiplier();
        event.setNewSpeed(event.getNewSpeed() * multiplier);
    }
}

