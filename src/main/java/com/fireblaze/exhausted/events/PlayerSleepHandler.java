package com.fireblaze.exhausted.events;

import com.fireblaze.exhausted.capability.StaminaProvider;
import com.fireblaze.exhausted.networking.ModMessages;
import com.fireblaze.exhausted.networking.packet.StaminaDataSyncS2CPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.common.MinecraftForge;

import net.minecraft.server.level.ServerLevel;

public class PlayerSleepHandler {

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new PlayerSleepHandler());
    }

    @SubscribeEvent
    public void onSleepFinished(SleepFinishedTimeEvent event) {
        ServerLevel level = (ServerLevel) event.getLevel();

        for (ServerPlayer player : level.getPlayers(p -> true)) {
            if (player.isCreative() || player.isSpectator()) return;
            player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
                float MAX_STAMINA = stamina.resetStamina();
                ModMessages.sendToPlayer(new StaminaDataSyncS2CPacket(MAX_STAMINA), player);
            });
        }
    }
}
