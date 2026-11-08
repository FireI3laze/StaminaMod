package com.fireblaze.exhausted.events;

import com.fireblaze.exhausted.Exhausted;
import com.fireblaze.exhausted.client.ClientStaminaData;
import com.fireblaze.exhausted.config.ClientConfig;
import com.fireblaze.exhausted.config.CommonConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(
        modid = Exhausted.MODID,
        value = Dist.CLIENT
)
public class StaminaLevelOverlay {
    @SubscribeEvent
    public static void onScreenRender(ScreenEvent.Render.Post event) {

        if (!(event.getScreen() instanceof InventoryScreen screen)) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null) {
            return;
        }

        GuiGraphics gui = event.getGuiGraphics();

        int level = (int) ClientStaminaData.getPlayerStaminaLvl();
        float exp = ClientStaminaData.getPlayerStaminaExp();

        float requiredExp = (float) (
                CommonConfig.XP_BASE.get()
                        * Math.pow(level, CommonConfig.XP_EXPONENT.get())
        );

        float expProgress = requiredExp > 0.0F
                ? (exp / requiredExp) * 100.0F
                : 0.0F;

        expProgress = Math.max(0.0F, Math.min(100.0F, expProgress));
        float currentStamina = ClientStaminaData.getPlayerShortStamina();

        StaminaState state = getStaminaState(currentStamina);

        ResourceLocation icon = switch (state) {
            case HIGH -> ResourceLocation.fromNamespaceAndPath(
                    "minecraft",
                    "textures/mob_effect/speed.png"
            );

            case AVERAGE -> ResourceLocation.fromNamespaceAndPath(
                    "minecraft",
                    "textures/mob_effect/regeneration.png"
            );

            case LOW -> ResourceLocation.fromNamespaceAndPath(
                    "minecraft",
                    "textures/mob_effect/slowness.png"
            );
        };


        int x = screen.getGuiLeft()
                + screen.getXSize()
                + ClientConfig.STAMINA_LEVEL_ICON_OFFSET_X.get();

        int y = screen.getGuiTop()
                + ClientConfig.STAMINA_LEVEL_ICON_OFFSET_Y.get();

        float scale = ClientConfig.STAMINA_LEVEL_ICON_SCALE.get().floatValue();


        gui.pose().pushPose();

        gui.pose().translate(x, y, 0);
        gui.pose().scale(scale, scale, 1.0f);

        gui.blit(
                icon,
                0,
                0,
                0,
                0,
                12,
                12,
                12,
                12
        );

        gui.pose().popPose();

        int scaledSize = (int) (12 * scale);

        if (isMouseOver(
                event.getMouseX(),
                event.getMouseY(),
                x,
                y,
                scaledSize,
                scaledSize
        )) {
            List<Component> tooltip = new ArrayList<>();

            tooltip.add(Component.literal("Stamina Effects"));
            tooltip.add(Component.literal(""));

            for (Component effect : getEffectTooltip(currentStamina)) {
                tooltip.add(effect);
            }

            tooltip.add(Component.literal(""));

            String progressText = String.format(
                    java.util.Locale.ROOT,
                    "%.1f",
                    expProgress
            );

            if (Screen.hasShiftDown()) {
                tooltip.add(Component.literal(
                        "Stamina Level " + level + " (" + progressText + "%)"
                ));
            } else {
                tooltip.add(Component.literal("Stamina Level " + level));
            }

            gui.renderTooltip(
                    mc.font,
                    tooltip,
                    Optional.empty(),
                    event.getMouseX(),
                    event.getMouseY()
            );
        }
    }

    private static List<Component> getEffectTooltip(float stamina) {
        List<Component> lines = new ArrayList<>();

        if (stamina <= CommonConfig.NEGATIVE_EFFECT_3_THRESHOLD.get()) {

            lines.add(Component.literal("Severe Exhaustion")
                    .withStyle(ChatFormatting.GOLD));

            lines.add(Component.literal(formatNumber(CommonConfig.NEGATIVE_EFFECT_3_MOVEMENT_SPEED_PERCENTAGE.get() * 100) + "% Movement Speed")
                    .withStyle(ChatFormatting.RED));

            lines.add(Component.literal("-" + formatNumber(CommonConfig.NEGATIVE_EFFECT_3_DAMAGE_PERCENTAGE.get() * 100) + "% Attack Damage")
                    .withStyle(ChatFormatting.RED));

            lines.add(Component.literal("-" + formatNumber((1 - CommonConfig.NEGATIVE_EFFECT_3_MINING_SPEED_MULTIPLIER.get()) * 100) + "% Mining Speed")
                    .withStyle(ChatFormatting.RED));

            lines.add(Component.literal("Hunger Drain")
                    .withStyle(ChatFormatting.RED));

            if (CommonConfig.NEGATIVE_EFFECT_3_BLINDNESS.get()) {
                lines.add(Component.literal("Blindness")
                        .withStyle(ChatFormatting.RED));
            }

        } else if (stamina <= CommonConfig.NEGATIVE_EFFECT_2_THRESHOLD.get()) {

            lines.add(Component.literal("Exhaustion")
                    .withStyle(ChatFormatting.GOLD));

            lines.add(Component.literal(formatNumber(CommonConfig.NEGATIVE_EFFECT_2_MOVEMENT_SPEED_PERCENTAGE.get() * 100) + "% Movement Speed")
                    .withStyle(ChatFormatting.RED));

            lines.add(Component.literal(formatNumber(CommonConfig.NEGATIVE_EFFECT_2_DAMAGE_PERCENTAGE.get() * 100) + "% Attack Damage")
                    .withStyle(ChatFormatting.RED));

            lines.add(Component.literal("-" + formatNumber((1 - CommonConfig.NEGATIVE_EFFECT_2_MINING_SPEED_MULTIPLIER.get()) * 100) + "% Mining Speed")
                    .withStyle(ChatFormatting.RED));

            lines.add(Component.literal("Hunger Drain")
                    .withStyle(ChatFormatting.RED));

        } else if (stamina <= CommonConfig.NEGATIVE_EFFECT_1_THRESHOLD.get()) {

            lines.add(Component.literal("Fatigued")
                    .withStyle(ChatFormatting.GOLD));

            lines.add(Component.literal(formatNumber(CommonConfig.NEGATIVE_EFFECT_1_MOVEMENT_SPEED_PERCENTAGE.get() * 100) + "% Movement Speed")
                    .withStyle(ChatFormatting.RED));

            lines.add(Component.literal(formatNumber(CommonConfig.NEGATIVE_EFFECT_1_DAMAGE_PERCENTAGE.get() * 100) + "% Attack Damage")
                    .withStyle(ChatFormatting.RED));

            lines.add(Component.literal("-" + formatNumber((1 - CommonConfig.NEGATIVE_EFFECT_1_MINING_SPEED_MULTIPLIER.get()) * 100) + "% Mining Speed")
                    .withStyle(ChatFormatting.RED));

        } else if (stamina >= CommonConfig.POSITIVE_EFFECT_THRESHOLD.get()) {

            lines.add(Component.literal("Well Rested")
                    .withStyle(ChatFormatting.GOLD));

            lines.add(Component.literal("+" + formatNumber(CommonConfig.POSITIVE_EFFECT_SPEED_PERCENTAGE.get() * 100) + "% Movement Speed")
                    .withStyle(ChatFormatting.BLUE));

            lines.add(Component.literal("+" + formatNumber(CommonConfig.POSITIVE_EFFECT_DAMAGE_FLAT.get()) + " Attack Damage")
                    .withStyle(ChatFormatting.BLUE));

            lines.add(Component.literal("+" + formatNumber(CommonConfig.POSITIVE_EFFECT_MINING_SPEED_MULTIPLIER.get() * 100 - 100) + "% Mining Speed")
                    .withStyle(ChatFormatting.BLUE));

        } else {

            lines.add(Component.literal("No active effects")
                    .withStyle(ChatFormatting.GOLD));
        }

        return lines;
    }

    private static String formatNumber(double value) {
        if (value == Math.floor(value)) {
            return String.valueOf((long) value);
        }

        return String.valueOf(value);
    }

    private static boolean isMouseOver(
            double mouseX,
            double mouseY,
            int x,
            int y,
            int width,
            int height
    ) {
        return mouseX >= x
                && mouseX <= x + width
                && mouseY >= y
                && mouseY <= y + height;
    }

    private static StaminaState getStaminaState(float currentStamina) {

        if (currentStamina >= CommonConfig.POSITIVE_EFFECT_THRESHOLD.get()) {
            return StaminaState.HIGH;
        }

        if (currentStamina >= CommonConfig.NEGATIVE_EFFECT_1_THRESHOLD.get()) {
            return StaminaState.AVERAGE;
        }

        return StaminaState.LOW;
    }

    public enum StaminaState {
        HIGH,
        AVERAGE,
        LOW
    }
}