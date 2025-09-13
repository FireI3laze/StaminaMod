package com.fireblaze.exhausted.events;

import com.fireblaze.exhausted.Exhausted;
import com.fireblaze.exhausted.client.ClientStaminaData;
import com.fireblaze.exhausted.entity.ModEntities;
import com.fireblaze.exhausted.entity.client.SeatRenderer;
import com.fireblaze.exhausted.config.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Exhausted.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEvents {

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.SEAT.get(), SeatRenderer::new);
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        if (!event.getOverlay().id().equals(VanillaGuiOverlay.EXPERIENCE_BAR.id())) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.player.isCreative() || mc.player.isSpectator()) return;

        GuiGraphics graphics = event.getGuiGraphics();

        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();

        // === Neue Breite = Hotbar-Breite ===
        int hotbarWidth = Settings.getUiScalings("width"); // Vanilla Hotbar Standardbreite
        int barWidth = hotbarWidth;

        int barHeight = 3;
        int cornerRadius = 6; // optische Orientierung
        int x = ((width - barWidth) / 2) +  Settings.getUiScalings("x"); // horizontal zentriert wie Hotbar
        int y = height + ( Settings.getUiScalings("y") * -1); // bleibt unverändert

        // Werte holen
        float maxStamina = ClientStaminaData.getPlayerMaxStamina();
        float longStamina = ClientStaminaData.getPlayerLongStamina();
        float shortStamina = ClientStaminaData.getPlayerShortStamina();

        // Normierte Füllbreiten
        int longFill = (int) (barWidth * (longStamina / maxStamina));
        int shortFill = (int) (barWidth * (shortStamina / maxStamina));

        // Debuff Threshold als Breite in Pixel
        int debuffThreshold = 15;
        int debuffWidth = (int) (barWidth * (debuffThreshold / maxStamina));

        // --- 1) Füllungen ---
        drawRoundedRectPartial(graphics, x, y, longFill, barHeight, cornerRadius, 0x99CCCCCC);
        drawRoundedRectPartialGradient(graphics, x, y, shortFill, barHeight, cornerRadius, 0xFF4A90E2, 0xFF3366FF);

        if (shortStamina <= debuffThreshold) {
            int redDrawWidth = Math.min(debuffWidth, shortFill);
            drawRoundedRectPartial(graphics, x, y, redDrawWidth, barHeight, cornerRadius, 0x99FF5555);
        }

        // --- 2) Schwarzer Rand ---
        drawBorder(graphics, x, y, barWidth, barHeight, 1, 0xFF000000);
    }

    /**
     * Zeichnet einen einfachen, rechteckigen Rand (Top/Bottom/Left/Right).
     * Die Ecken sind nicht mathematisch perfekt rund, aber der Rand bleibt sauber
     * und die Mitte bleibt transparent (weil wir nichts "ausradieren").
     *
     * @param thickness Breite des Randes in Pixel (z.B. 1 oder 2)
     */
    private static void drawBorder(GuiGraphics graphics, int x, int y, int width, int height, int thickness, int color) {
        // Obere Linie
        graphics.fill(x - thickness, y - thickness, x + width + thickness, y, color);
        // Untere Linie
        graphics.fill(x - thickness, y + height, x + width + thickness, y + height + thickness, color);
        // Linke Linie
        graphics.fill(x - thickness, y, x, y + height, color);
        // Rechte Linie
        graphics.fill(x + width, y, x + width + thickness, y + height, color);

        // Kleine Quadrate an den Ecken (weicherer Look)
        // Top-Left
        graphics.fill(x - thickness, y - thickness, x + thickness, y + thickness, color);
        // Top-Right
        graphics.fill(x + width - thickness, y - thickness, x + width + thickness, y + thickness, color);
        // Bottom-Left
        graphics.fill(x - thickness, y + height - thickness, x + thickness, y + height + thickness, color);
        // Bottom-Right
        graphics.fill(x + width - thickness, y + height - thickness, x + width + thickness, y + height + thickness, color);
    }

    // Zeichnet ein (links abgerundetes) Rechteck mit Breite "fillWidth" (kann kleiner als Gesamtbreite sein)
    // Diese Methode simuliert die linke Abrundung und funktioniert zuverlässig für unsere Füllungen.
    private static void drawRoundedRectPartial(GuiGraphics graphics, int x, int y, int fillWidth, int height, int radius, int color) {
        if (fillWidth <= 0) return;
        if (fillWidth < radius) {
            // Wenn zu schmal, einfach Rechteck
            graphics.fill(x, y, x + fillWidth, y + height, color);
            return;
        }
        if (fillWidth < radius * 2) {
            // halb abgerundet links, rechte Seite eckig
            graphics.fill(x + radius, y, x + fillWidth, y + height, color);
            graphics.fill(x, y, x + radius, y + height, color);
            return;
        }

        // Mitte Rechteck
        graphics.fill(x + radius, y, x + fillWidth, y + height, color);
        // Linker Bereich (simulate rounded left)
        graphics.fill(x, y, x + radius, y + height, color);
    }

    private static void drawRoundedRectPartialGradient(GuiGraphics graphics, int x, int y, int fillWidth, int height, int radius, int topColor, int bottomColor) {
        if (fillWidth <= 0) return;
        if (fillWidth < radius) {
            graphics.fillGradient(x, y, x + fillWidth, y + height, topColor, bottomColor);
            return;
        }
        if (fillWidth < radius * 2) {
            graphics.fillGradient(x + radius, y, x + fillWidth, y + height, topColor, bottomColor);
            graphics.fillGradient(x, y, x + radius, y + height, topColor, bottomColor);
            return;
        }

        // Mitte Rechteck
        graphics.fillGradient(x + radius, y, x + fillWidth, y + height, topColor, bottomColor);
        // Linker Bereich
        graphics.fillGradient(x, y, x + radius, y + height, topColor, bottomColor);
    }


    @Mod.EventBusSubscriber(modid = Exhausted.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            return;
        }
    }
}