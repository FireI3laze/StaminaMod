package com.fireblaze.exhausted.comfort;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import javax.annotation.Nullable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;


public class ComfortProvider implements ICapabilityProvider, ICapabilitySerializable<net.minecraft.nbt.CompoundTag> {

    public static final Capability<ComfortCapability> COMFORT_CAP = CapabilityManager.get(new CapabilityToken<>() {});
    private ComfortCapability comfort = new ComfortCapability();
    private final LazyOptional<ComfortCapability> optional = LazyOptional.of(() -> comfort);

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        return cap == COMFORT_CAP ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public net.minecraft.nbt.CompoundTag serializeNBT() {
        return comfort.serializeNBT();
    }

    @Override
    public void deserializeNBT(net.minecraft.nbt.CompoundTag nbt) {
        comfort.deserializeNBT(nbt);
    }

    public ComfortCapability getComfort() {
        return comfort;
    }
}
