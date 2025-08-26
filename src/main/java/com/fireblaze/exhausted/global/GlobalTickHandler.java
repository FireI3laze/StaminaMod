package com.fireblaze.exhausted.global;

import com.fireblaze.exhausted.StaminaMod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = StaminaMod.MODID)
public class GlobalTickHandler {

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            GlobalTickCounter.tick();
        }
    }
}
