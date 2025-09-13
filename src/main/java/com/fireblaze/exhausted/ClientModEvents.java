package com.fireblaze.exhausted;

import com.fireblaze.exhausted.entity.client.SeatRenderer;
import com.fireblaze.exhausted.entity.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Exhausted.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            net.minecraft.client.renderer.entity.EntityRenderers.register(
                    ModEntities.SEAT.get(),
                    SeatRenderer::new
            );
        });
    }
}

