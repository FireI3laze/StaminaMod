package com.fireblaze.stamina_mod.events;

import com.fireblaze.stamina_mod.capability.StaminaProvider;
import com.fireblaze.stamina_mod.networking.ModMessages;
import com.fireblaze.stamina_mod.networking.packet.StaminaDataSyncS2CPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.world.entity.player.Player;

import net.minecraft.server.level.ServerLevel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerSleepHandler {
    private static final Map<UUID, Float> StaminaMap = new HashMap<>();

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new PlayerSleepHandler());
    }

    @SubscribeEvent
    public void onSleepFinished(SleepFinishedTimeEvent event) {
        ServerLevel level = (ServerLevel) event.getLevel();

        for (ServerPlayer player : level.getPlayers(p -> true)) {
            player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
                float MAX_STAMINA = stamina.resetStamina();
                ModMessages.sendToPlayer(new StaminaDataSyncS2CPacket(MAX_STAMINA), player);
            });
        }
    }
}
