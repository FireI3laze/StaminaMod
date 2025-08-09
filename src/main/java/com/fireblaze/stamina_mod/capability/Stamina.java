package com.fireblaze.stamina_mod.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class Stamina {
    private float shortStamina;
    private float longStamina;
    private static final float MIN_SHORT_STAMINA = 0;
    private static final float MIN_LONG_STAMINA = 0;
    private static final float MAX_SHORT_STAMINA = 200;
    private static final float MAX_LONG_STAMINA = 200;
    private float currentShortStaminaCap = MAX_SHORT_STAMINA;
    private float currentLongStaminaCap = MAX_LONG_STAMINA;
    private float armStrength;
    private float legStrength;

    private int tickCounter = 0; // todo temp
    public Stamina() {
        this.shortStamina = MAX_SHORT_STAMINA;
        this.longStamina = MAX_LONG_STAMINA;
    }

    public void tick(Player player, float damage) {
        float damageMultiplier = 1.0f; // todo damage +* constant

        if (shortStamina < currentShortStaminaCap)
            this.shortStamina = Math.min(shortStamina + 0.00025f * currentShortStaminaCap * damageMultiplier, currentShortStaminaCap);

        tickCounter++;
        if (tickCounter % 100 == 0) {
            System.out.println("[regenerating] stamina: " + shortStamina + " / " + currentShortStaminaCap + " + " + Math.min(shortStamina + 0.00025f * currentShortStaminaCap * damageMultiplier, currentShortStaminaCap));
            System.out.println("[current] Long Stamina: " + longStamina);
        }
    }

    public void consume(float shortStamina, float longStamina, float damage) {
        float damageMultiplier = 1.0f; // todo damage +* constant
        this.shortStamina = Math.max(this.shortStamina - shortStamina * damageMultiplier, MIN_SHORT_STAMINA);
        this.longStamina = Math.max(this.longStamina - longStamina, MIN_LONG_STAMINA);
        relativeMaxShortStamina();
        if (tickCounter % 20 == 0) {
            System.out.println( "[moving] stamina: " + this.shortStamina);
        }

    }
    public void consume(float shortStamina, float longStamina, float damage, float blockHardness) {
        float damageMultiplier = 1.0f; // todo damage +* constant
        this.shortStamina = Math.max(0, this.shortStamina - shortStamina * damageMultiplier);
        this.longStamina = Math.max(0, this.longStamina - longStamina);
        relativeMaxShortStamina();
        if (tickCounter % 20 == 0) {
            System.out.println("[mining] stamina: " + this.shortStamina);
        }
    }
    public void relativeMaxShortStamina() {
        currentShortStaminaCap = MAX_LONG_STAMINA - (MAX_LONG_STAMINA - longStamina) / 2;
        //System.out.println(currentShortStaminaCap + " | " + MAX_LONG_STAMINA + " | " + longStamina);
    }
    public void resetLongStamina() {
        this.longStamina = MAX_LONG_STAMINA;
    }

    public float getShortStamina() {
        return shortStamina;
    }
    public float getLongStamina() {
        return longStamina;
    }

    public float getCurrentShortStaminaCap() {
        return currentShortStaminaCap;
    }
    public float getShortStaminaCap() {
        return MAX_SHORT_STAMINA;
    }
    public float getLongStaminaCap() {
        return MAX_LONG_STAMINA;
    }

    public void copyFrom(Stamina source) {
        this.shortStamina = source.shortStamina;
    }
    public void saveNBTData(CompoundTag tag) {
        tag.putFloat("stamina", shortStamina);
        tag.putFloat("longStamina", longStamina);
    }

    public void loadNBTData(CompoundTag tag) {
        this.shortStamina = tag.getFloat("stamina");
        this.longStamina = tag.getFloat("longStamina");
    }
}
