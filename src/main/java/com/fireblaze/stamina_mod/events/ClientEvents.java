package com.fireblaze.stamina_mod.events;

import com.fireblaze.stamina_mod.StaminaMod;
import com.fireblaze.stamina_mod.capability.StaminaProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = StaminaMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEvents {

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        if (!event.getOverlay().id().equals(VanillaGuiOverlay.EXPERIENCE_BAR.id())) return;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        player.getCapability(StaminaProvider.PLAYER_STAMINA).ifPresent(stamina -> {
            float shortStamCur = stamina.getShortStamina();
            float shortStamMax = stamina.getCurrentShortStaminaCap();
            float longStamCur = stamina.getLongStamina();
            float longStamMax = stamina.getLongStaminaCap();

            GuiGraphics graphics = event.getGuiGraphics();

            int width = mc.getWindow().getGuiScaledWidth();
            int height = mc.getWindow().getGuiScaledHeight();

            int barWidth = 100;
            int barHeight = 10;
            int x = (width - barWidth) / 2;
            int y = height - 40;

            int fillWidth = (int) (barWidth * (shortStamCur / shortStamMax));

            graphics.fill(x, y, x + barWidth, y + barHeight, 0x88000000); // Hintergrund
            graphics.fill(x, y, x + fillWidth, y + barHeight, 0xFF00FF00); // Füllung
        });
    }
}
