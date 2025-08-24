package com.fireblaze.stamina_mod.comfort;

import net.minecraft.nbt.CompoundTag;

public class ComfortCapability {

    private boolean isSitting = false;
    private double comfortLevel = 0.0;

    public boolean isSitting() {
        return isSitting;
    }

    public void setSitting(boolean sitting) {
        this.isSitting = sitting;
    }

    public double getComfortLevel() {
        return comfortLevel;
    }

    public void setComfortLevel(double comfortLevel) {
        this.comfortLevel = comfortLevel;
    }

    // Wichtig: Methoden für Provider
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("isSitting", isSitting);
        tag.putDouble("comfortLevel", comfortLevel);
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        this.isSitting = tag.getBoolean("isSitting");
        this.comfortLevel = tag.getDouble("comfortLevel");
    }
}
