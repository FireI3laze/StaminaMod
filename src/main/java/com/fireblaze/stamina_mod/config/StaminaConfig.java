package com.fireblaze.stamina_mod.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class StaminaConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    // Config-Werte
    public static final ForgeConfigSpec.BooleanValue KEEP_LEVEL_ON_DEATH;

    static {
        BUILDER.comment("Stamina Mod Settings");

        KEEP_LEVEL_ON_DEATH = BUILDER
                .comment("Wenn true, behält der Spieler nach dem Tod sein Stamina-Level und XP.")
                .define("keepLevelOnDeath", false);

        SPEC = BUILDER.build();
    }
}
