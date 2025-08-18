package com.fireblaze.stamina_mod.entity.client;

import com.fireblaze.stamina_mod.StaminaMod;
import com.fireblaze.stamina_mod.entity.ModEntities;
import com.fireblaze.stamina_mod.entity.client.SeatRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = StaminaMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        System.out.println("[StaminaMod] Registering SeatEntity renderer: " + ModEntities.SEAT.getId());
        event.registerEntityRenderer(ModEntities.SEAT.get(), SeatRenderer::new);
    }
}
