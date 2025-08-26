package com.fireblaze.exhausted.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import com.fireblaze.exhausted.config.Settings;
import net.minecraft.sounds.SoundSource;

import java.util.Objects;

public class Stamina {
    private float shortStamina = 100.0F;
    private float longStamina = 100.0F;
    private static final float MIN_STAMINA = 0.0F;
    private static final float MAX_STAMINA = 100.0F;
    private float BASE_REGEN;
    private float AMPLIFIED_REGEN;
    private float armStrength;
    private float legStrength;
    private float staminaConsumption;
    private int staminaLvl = 0;
    private static final int MAX_STAMINA_LEVEL = 20;
    private float staminaExp = 0.0F;
    private int tickCounter = 0;

    public Stamina() {
    }

    public void tick(Player player, int tickMultiplier) {
        if (player.isCreative() || player.isSpectator()) return;
        this.regenerate(player, 1.0f, tickMultiplier);
    }

    public void rest(Player player, float restFactor, float hardnessFactor) {
        if (player.isCreative() || player.isSpectator()) return;
        float damageMultiplier = 1.0F;
        this.BASE_REGEN = (float)Settings.getRegenerationConfigs("baseRegenLong");
        this.AMPLIFIED_REGEN = (float)Math.min((double)(this.BASE_REGEN * damageMultiplier) * (1.0 + (double)this.staminaLvl * 0.03) * (double)restFactor * (double)hardnessFactor, 100.0);
        this.longStamina = Math.min(this.longStamina + this.AMPLIFIED_REGEN, 100.0F);
        this.regenerate(player, restFactor, hardnessFactor);
        //System.out.println(restFactor + " | " + hardnessFactor);
    }

    public void regenerate(Player player, float restFactor, float hardnessFactor) {
        float damageMultiplier = 1.0F;
        if (this.shortStamina < this.longStamina) {
            this.BASE_REGEN = (float)Settings.getRegenerationConfigs("baseRegenShort") * this.longStamina;
            this.AMPLIFIED_REGEN = (float)Math.min((double)(this.BASE_REGEN * damageMultiplier) * (1.0 + (double)this.staminaLvl * 0.05) * (double)restFactor * (double)hardnessFactor, (double)this.longStamina);
            this.shortStamina = Math.min(this.shortStamina + this.AMPLIFIED_REGEN, this.longStamina);
        }
        /*
        ++this.tickCounter;
        if (this.tickCounter % 500 == 0) {
            System.out.println("[regenerating] stamina: " + this.shortStamina + " / " + this.longStamina + " + " + this.AMPLIFIED_REGEN);
            System.out.println("[current] Long Stamina: " + this.longStamina);
            System.out.println("[Experience] Current: " + this.staminaExp + "/" + this.getExperienceToNextLevel() + " | " + this.staminaLvl);
        }
        */
    }

    public void consume(float shortStamina, float longStamina, float armorMultiplier, Player player) {
        if (player.isCreative() || player.isSpectator()) return;
        this.shortStamina = (float)Math.max(this.shortStamina - shortStamina * armorMultiplier * (1.0 - this.staminaLvl * 0.015), 0.0);
        this.longStamina = (float)Math.max(this.longStamina - longStamina * armorMultiplier * (1.0 - this.staminaLvl * 0.03), 0.0);
        this.addExperience(shortStamina + longStamina, player);
        /*
        float armor = (float)Math.max(this.shortStamina - shortStamina * (1.0 - this.staminaLvl * 0.015) * armorMultiplier, 0.0);
        float noArmor = (float)Math.max(this.shortStamina - shortStamina * (1.0 - this.staminaLvl * 0.015) * 1, 0.0);
        System.out.println("No Armor: " + noArmor + " | Armor: " + armor + " | Difference: " + (armor - noArmor));
        */
    }

    public void consume(float shortStamina, float longStamina, float armorMultiplier, float blockHardness, Player player) {
        if (player.isCreative() || player.isSpectator()) return;
        this.shortStamina = (float)Math.max(0.0, this.shortStamina - shortStamina * armorMultiplier * (1.0 - this.staminaLvl * 0.0075));
        this.longStamina = (float)Math.max(0.0, this.longStamina - longStamina * armorMultiplier * (1.0 - this.staminaLvl * 0.02));
        if (this.staminaLvl < 20) {
            this.addExperience(shortStamina + longStamina, player);
        }
    }

    public void foodEaten(Player player, int nutrition, float saturation) {
        if (player.isCreative() || player.isSpectator()) return;
        float longStaminaGain = (float)((double)((float)nutrition + saturation) * Settings.getRegenerationConfigs("foodFactor"));
        this.longStamina += longStaminaGain;
    }

    public void addExperience(float exp_gains, Player player) {
        this.staminaExp += exp_gains;

        while(this.staminaExp >= this.getExperienceToNextLevel() && this.staminaLvl < 20) {
            this.staminaExp -= this.getExperienceToNextLevel();
            ++this.staminaLvl;
            this.onLevelUp(player);
        }

        if (this.staminaLvl >= MAX_STAMINA_LEVEL) {
            this.staminaExp = 0.0F;
        }

    }

    private float getExperienceToNextLevel() {
        return (float)(100 * this.staminaLvl * this.staminaLvl);
    }

    private void onLevelUp(Player player) {
        // Nachricht im Chat (sichtbar für diesen Spieler)
        player.displayClientMessage(
                Component.literal("§aYou have reached Stamina Level " + staminaLvl),
                true
        );

        // Optional: Sound abspielen
        player.playNotifySound(SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    public void strength_add_exp(String bodyPart, float exp_gains) {
        if (!Objects.equals(bodyPart, "leg") && Objects.equals(bodyPart, "arm")) {
        }

    }

    public float resetStamina() {
        this.shortStamina = 100.0F;
        this.longStamina = 100.0F;
        return 100.0F;
    }

    public float getShortStamina() {
        return this.shortStamina;
    }

    public float getLongStamina() {
        return this.longStamina;
    }

    public float getLongStaminaCap() {
        return 100.0F;
    }

    public float getStaminaExp() {
        return this.staminaExp;
    }

    public int getStaminaLvl() {
        return this.staminaLvl;
    }

    public void setShortStamina(float shortStamina) {
        this.shortStamina = shortStamina;
    }

    public void setLongStamina(float longStamina) {
        this.longStamina = longStamina;
    }

    public void setStaminaLvl(int staminaLvl) {
        this.staminaLvl = staminaLvl;
    }

    public void setStaminaExp(float staminaExp) {
        this.staminaExp = staminaExp;
    }

    public void copyFrom(Stamina source) {
        this.shortStamina = source.shortStamina;
        this.longStamina = source.longStamina;
        this.staminaLvl = source.staminaLvl;
        this.staminaExp = source.staminaExp;
    }
    public void saveNBTData(CompoundTag tag) {
        tag.putFloat("stamina", shortStamina);
        tag.putFloat("longStamina", longStamina);
        tag.putInt("staminaLvl", staminaLvl);
        tag.putFloat("staminaExp", staminaExp);
    }

    public void loadNBTData(CompoundTag tag) {
        this.shortStamina = tag.getFloat("stamina");
        this.longStamina = tag.getFloat("longStamina");
        this.staminaLvl = tag.contains("staminaLvl") ? tag.getInt("staminaLvl") : 0;
        this.staminaExp = tag.contains("staminaExp") ? tag.getFloat("staminaExp") : 0;
    }
}
