package com.fireblaze.exhausted.events;

import com.fireblaze.exhausted.capability.StaminaProvider;
import com.fireblaze.exhausted.comfort.ComfortCalculator;
import com.fireblaze.exhausted.config.CommonConfig;
import com.fireblaze.exhausted.networking.ModMessages;
import com.fireblaze.exhausted.networking.packet.StaminaDataSyncS2CPacket;
import net.minecraft.core.BlockPos;
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

            double comfort = player.getPersistentData().getDouble("comfort");
            double maxComfort = getMaxComfort();

            double missingPercent = (maxComfort - comfort) / maxComfort;
            double staminaPercent = 1.0 - (missingPercent * 0.5);

            player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
                if (CommonConfig.COMFORT_SLEEP_REGENERATION_CAP.get()) {
                    float refreshedStamina = stamina.restoreStaminaPercentage((float) staminaPercent);
                    ModMessages.sendToPlayer(new StaminaDataSyncS2CPacket(refreshedStamina), player);
                }
                else {
                    float MAX_STAMINA = stamina.resetStamina();
                    ModMessages.sendToPlayer(new StaminaDataSyncS2CPacket(MAX_STAMINA), player);
                }
            });
        }
    }

    public static double getMaxComfort() {
        return CommonConfig.COMFORT_BONUS_CEILING.get()
                + CommonConfig.COMFORT_BONUS_SPACE.get()
                + CommonConfig.COMFORT_BONUS_WALLS.get()
                + CommonConfig.COMFORT_BONUS_LIGHT.get()
                + CommonConfig.COMFORT_BONUS_ANIMAL.get()
                + CommonConfig.COMFORT_BONUS_FOOD.get()
                + CommonConfig.COMFORT_BONUS_BLOCK.get()
                + CommonConfig.COMFORT_BONUS_BOOST_BLOCK.get();
    }
}
