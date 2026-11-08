package com.fireblaze.exhausted.events;

import com.fireblaze.exhausted.capability.StaminaProvider;
import com.fireblaze.exhausted.config.CommonConfig;
import com.fireblaze.exhausted.config.Settings;
import com.fireblaze.exhausted.networking.ModMessages;
import com.fireblaze.exhausted.networking.packet.MiningspeedS2CPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber
public class MiningSpeedModifyHandler {
    private static final Map<Player, Integer> tickCounters = new WeakHashMap<>();

    @SubscribeEvent
    public static void onBlockBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
            float shortStam = stamina.getShortStamina();
            float speedMultiplier = 1.0f;

            if (shortStam >= Settings.getPositiveEffectThreshold()) {
                speedMultiplier = CommonConfig.POSITIVE_EFFECT_MINING_SPEED_MULTIPLIER.get().floatValue();
            } else if (shortStam <= Settings.getNegativeEffect3Threshold()) {
                speedMultiplier = CommonConfig.NEGATIVE_EFFECT_3_MINING_SPEED_MULTIPLIER.get().floatValue();
            } else if (shortStam <= Settings.getNegativeEffect2Threshold()) {
                speedMultiplier = CommonConfig.NEGATIVE_EFFECT_2_MINING_SPEED_MULTIPLIER.get().floatValue();
            } else if (shortStam <= Settings.getNegativeEffect1Threshold()) {
                speedMultiplier = CommonConfig.NEGATIVE_EFFECT_1_MINING_SPEED_MULTIPLIER.get().floatValue();
            }

            event.setNewSpeed(event.getNewSpeed() * speedMultiplier);

            tickCounters.putIfAbsent(player, 0);
            int ticks = tickCounters.get(player);
            tickCounters.put(player, ticks + 1);

            if (ticks % 5 == 0) {
                ModMessages.sendToPlayer(
                        new MiningspeedS2CPacket(speedMultiplier), (ServerPlayer) player
                );
            }
        });
    }

}
