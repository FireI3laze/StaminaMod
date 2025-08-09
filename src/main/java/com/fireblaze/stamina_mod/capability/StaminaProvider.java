package com.fireblaze.stamina_mod.capability;

import com.fireblaze.stamina_mod.StaminaMod;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public class StaminaProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
    public static Capability<Stamina> PLAYER_STAMINA = CapabilityManager.get(new CapabilityToken<Stamina>() {});
    private Stamina stamina = null;
    private final LazyOptional<Stamina> optional = LazyOptional.of(this::createStamina);

    private Stamina createStamina() {
        if (this.stamina == null) {
            this.stamina = new Stamina();
        }

        return this.stamina;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_STAMINA) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createStamina().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createStamina().loadNBTData(nbt);
    }
}
