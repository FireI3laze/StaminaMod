package com.fireblaze.stamina_mod.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

public class Stamina {
    private float shortStamina;
    private float longStamina;
    private static final float MIN_STAMINA = 0;
    private static final float MAX_STAMINA = 100;
    private float BASE_REGEN;
    private float AMPLIFIED_REGEN;
    private float armStrength;
    private float legStrength;
    private float staminaConsumption;
    private int staminaLvl;
    private static final int MAX_STAMINA_LEVEL = 20;
    private float staminaExp;

    private int tickCounter = 0; // todo temp
    public Stamina() {
        this.shortStamina = MAX_STAMINA;
        this.longStamina = MAX_STAMINA;
        this.staminaLvl = 0;
        this.staminaExp = 0;
    }

    public void tick(Player player, float damage) {
        float longStamina = 0.0f;
        float factor = 1.0f;
        regenerate(factor);

    }

    public void rest(float sitFactor, float hardnessFactor) {
        float damageMultiplier = 1.0f; // todo damage +* constant
        BASE_REGEN = 0.002f;
        AMPLIFIED_REGEN = (float) Math.min(BASE_REGEN * damageMultiplier * (1 + staminaLvl * 0.02) * sitFactor * hardnessFactor, MAX_STAMINA);
        this.longStamina = Math.min(longStamina + AMPLIFIED_REGEN, MAX_STAMINA);
        regenerate(sitFactor * hardnessFactor);
    }

    public void regenerate(float factor) {
        float damageMultiplier = 1.0f; // todo damage +* constant
        if (shortStamina < longStamina) {
            BASE_REGEN = 0.000175f * longStamina;
            AMPLIFIED_REGEN = (float) Math.min(BASE_REGEN * damageMultiplier * (1 + staminaLvl * 0.05) * factor, longStamina);
            this.shortStamina = Math.min(shortStamina + AMPLIFIED_REGEN, longStamina);
        }

        tickCounter++;
        if (tickCounter % 100 == 0) {
            System.out.println("[regenerating] stamina: " + shortStamina + " / " + longStamina + " + " + AMPLIFIED_REGEN);
            System.out.println("[current] Long Stamina: " + longStamina);
            System.out.println("[Experience] Current: " + staminaExp + "/" + getExperienceToNextLevel() + " | " + staminaLvl);
        }
    }

    public void consume(float shortStamina, float longStamina, float damage, Player player) {
        float damageMultiplier = 1.0f; // todo damage +* constant
        this.shortStamina = (float) Math.max(this.shortStamina - shortStamina * (1 - staminaLvl * 0.01) * damageMultiplier, MIN_STAMINA);
        this.longStamina = (float) Math.max(this.longStamina - longStamina * (1 - staminaLvl * 0.01) * damageMultiplier, MIN_STAMINA);
        //this.shortStamina = Math.max(this.shortStamina - shortStamina * damageMultiplier, MIN_STAMINA);
        //this.longStamina = Math.max(this.longStamina - longStamina, MIN_STAMINA);
        addExperience(shortStamina + longStamina, player);
        if (tickCounter % 20 == 0) {
            System.out.println( "[moving] stamina: " + this.shortStamina);
        }

    }
    public void consume(float shortStamina, float longStamina, float damage, float blockHardness, Player player) {
        float damageMultiplier = 1.0f; // todo damage +* constant
        this.shortStamina = (float) Math.max(0, this.shortStamina - shortStamina * (1 - staminaLvl * 0.005) * damageMultiplier);
        this.longStamina = (float) Math.max(0, this.longStamina - longStamina * (1 - staminaLvl * 0.01) * damageMultiplier);
        if (staminaLvl < MAX_STAMINA_LEVEL) {
            addExperience(shortStamina + longStamina, player);
        }
        if (tickCounter % 20 == 0) {
            System.out.println("[mining] stamina: " + this.shortStamina);
        }
    }
    public void addExperience(float exp_gains, Player player) {
        staminaExp += exp_gains;
        while (staminaExp >= getExperienceToNextLevel()) {
            staminaExp -= getExperienceToNextLevel();
            staminaLvl++;
            onLevelUp(player);
        }
    }
    private float getExperienceToNextLevel() {
        // Exponentiell steigende XP-Kosten
        return (100 * staminaLvl * staminaLvl);
    }

    private void onLevelUp(Player player) {
        // Nachricht im Chat (sichtbar für diesen Spieler)
        player.sendSystemMessage(Component.literal("§aYou have reached Level " + staminaLvl));

        // Optional: Sound abspielen
        player.playSound(SoundEvents.PLAYER_LEVELUP, 100.0F, 1.0F);
    }

    public void strength_add_exp(String bodyPart, float exp_gains) {
        if (Objects.equals(bodyPart, "leg")) {

        }
        else if (Objects.equals(bodyPart, "arm")) {

        }
    }
    public float resetStamina() {
        this.shortStamina = MAX_STAMINA;
        this.longStamina = MAX_STAMINA;
        return  MAX_STAMINA;
    }

    public float getShortStamina() {
        return shortStamina;
    }
    public float getLongStamina() {
        return longStamina;
    }
    public float getLongStaminaCap() {
        return MAX_STAMINA;
    }
    public float getStaminaExp() { return staminaExp; }
    public int getStaminaLvl() { return staminaLvl; }
    public void setShortStamina(float shortStamina) {
        this.shortStamina = shortStamina;
    }

    public void setLongStamina(float longStamina) {
        this.longStamina = longStamina;
    }

    public void setStaminaLvl(int staminaLvl) {
        this.staminaLvl = staminaLvl;
    }

    public void setStaminaExp(int staminaExp) {
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
