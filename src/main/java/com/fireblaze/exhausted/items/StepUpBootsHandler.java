package com.fireblaze.exhausted.items;

import com.fireblaze.exhausted.networking.ModMessages;
import com.fireblaze.exhausted.networking.packet.StepUpS2CPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class StepUpBootsHandler {
    public static void register() {
        MinecraftForge.EVENT_BUS.register(StepUpBootsHandler.class);
    }

    @SubscribeEvent
    public static void onArmorChange(LivingEquipmentChangeEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        if (event.getSlot() == EquipmentSlot.FEET) {
            boolean hasStepUpBoots = player.getInventory().getArmor(0).getItem() instanceof HikingBoots;
            float stepHeight = hasStepUpBoots ? 1.0f : 0.6f;
            ModMessages.sendToPlayer(new StepUpS2CPacket(stepHeight), player);
        }
    }
}
