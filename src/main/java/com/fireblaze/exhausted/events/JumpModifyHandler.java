package com.fireblaze.exhausted.events;

import com.fireblaze.exhausted.capability.StaminaProvider;
import com.fireblaze.exhausted.config.Settings;
import com.fireblaze.exhausted.networking.ModMessages;
import com.fireblaze.exhausted.networking.packet.JumpS2CPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class JumpModifyHandler {
    @SubscribeEvent
    public static void onPlayerJump(LivingEvent.LivingJumpEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        // Nur auf Serverseite
        if (player.level().isClientSide()) return;

        // Stamina auslesen
        player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
            double verticalBoost;
            double horizontalBoost;
            float shortStam = stamina.getShortStamina();
            Vec3 motion = player.getDeltaMovement();
            Vec3 newMotion;

            if (shortStam <= Settings.getNegativeEffect3Threshold()) {
                verticalBoost = 0.25;
                horizontalBoost = 0.25;

            } else if (shortStam <= Settings.getNegativeEffect2Threshold()) {
                verticalBoost = 0.5;
                horizontalBoost = 0.5;
            } else {
                return;
            }

            newMotion = new Vec3(motion.x, motion.y * verticalBoost, motion.z);

            /*
            if (player.isSprinting()) {
                newMotion = new Vec3(motion.x * horizontalBoost, newMotion.y, motion.z * horizontalBoost);
            }
            */
            player.setDeltaMovement(newMotion);

            ModMessages.sendToPlayer(
                    new JumpS2CPacket(newMotion), (ServerPlayer) player
            );

        });
    }
}
