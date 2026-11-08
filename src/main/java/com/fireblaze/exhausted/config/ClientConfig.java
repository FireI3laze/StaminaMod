package com.fireblaze.exhausted.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

    public static final ForgeConfigSpec.Builder BUILDER =
            new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec SPEC;


    // === UI ===

    public static final ForgeConfigSpec.ConfigValue<Integer>
            UI_X_COORDINATE_STAMINA_BAR;

    public static final ForgeConfigSpec.ConfigValue<Integer>
            UI_Y_COORDINATE_STAMINA_BAR;

    public static final ForgeConfigSpec.ConfigValue<Integer>
            UI_WIDTH_STAMINA_BAR;

    public static final ForgeConfigSpec.BooleanValue
            UI_HIDE_STAMINA_BAR;

    public static final ForgeConfigSpec.DoubleValue STAMINA_LEVEL_ICON_SCALE;

    public static final ForgeConfigSpec.IntValue
            STAMINA_LEVEL_ICON_OFFSET_X;

    public static final ForgeConfigSpec.IntValue
            STAMINA_LEVEL_ICON_OFFSET_Y;

    // === Action Bar Notification ===

    public static final ForgeConfigSpec.BooleanValue
            EXHAUSTION_THRESHOLD;
    public static final ForgeConfigSpec.ConfigValue<String>
            EXHAUSTION_THRESHOLD_TEXT;

    // === Sound ===

    public static final ForgeConfigSpec.BooleanValue
            SOUND_BREATHING;


    // === Animation ===

    public static final ForgeConfigSpec.BooleanValue
            ANIMATION_BREATHING;



    static {

        BUILDER.push("UI");


        UI_X_COORDINATE_STAMINA_BAR =
                BUILDER.comment(
                        "0 = Center"
                ).define(
                        "xCoordinateOfStaminaBar",
                        0
                );


        UI_Y_COORDINATE_STAMINA_BAR =
                BUILDER.comment(
                        "0 = bottom edge"
                ).define(
                        "yCoordinateOfStaminaBar",
                        54
                );


        UI_WIDTH_STAMINA_BAR =
                BUILDER.comment(
                        "Base = 182"
                ).define(
                        "widthOfStaminaBar",
                        182
                );


        UI_HIDE_STAMINA_BAR =
                BUILDER.comment(
                        "Hide bar above 90% stamina"
                ).define(
                        "allowHideStaminaBar",
                        true
                );

        STAMINA_LEVEL_ICON_SCALE = BUILDER
                .comment("Scale of the stamina icon")
                .defineInRange(
                        "staminaLevelIconScale",
                        1.0,
                        0.25,
                        4.0
                );

        STAMINA_LEVEL_ICON_OFFSET_X =
                BUILDER.defineInRange(
                        "staminaLevelIconOffsetX",
                        -20,
                        -2000,
                        2000
                );


        STAMINA_LEVEL_ICON_OFFSET_Y =
                BUILDER.defineInRange(
                        "staminaLevelIconOffsetY",
                        6,
                        -2000,
                        2000
                );


        BUILDER.pop();

        BUILDER.push("Action-Bar-Notifications");

        EXHAUSTION_THRESHOLD = BUILDER.comment(
                "Exhaustion notification (notifies the player on the threshold from which comfort requirements start to lower. The default is 60% and can be changed in common > General-Settings > Comfort > Advanced-Comfort-Settings)"
        ).define(
                "exhaustionNotificationThreshold",
                false
        );

        EXHAUSTION_THRESHOLD_TEXT = BUILDER.comment(
                "The text (and  color) displayed when the threshold above is reached"
        ).define(
                "exhaustionNotificationText",
                "§eYou start feeling exhausted"
        );



        BUILDER.push("Sounds");


        SOUND_BREATHING =
                BUILDER.comment(
                        "Enable breath sound"
                ).define(
                        "allowBreathSound",
                        true
                );


        BUILDER.pop();



        BUILDER.push("Animation");


        ANIMATION_BREATHING =
                BUILDER.comment(
                        "Enable breathing animation"
                ).define(
                        "allowBreathAnimation",
                        true
                );


        BUILDER.pop();



        SPEC = BUILDER.build();
    }
}