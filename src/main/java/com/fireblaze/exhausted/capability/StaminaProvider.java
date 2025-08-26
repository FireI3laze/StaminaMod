package com.fireblaze.exhausted.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public class StaminaProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {

    public static final Capability<Stamina> PLAYER_STAMINA = CapabilityManager.get(new CapabilityToken<Stamina>() {});
    private Stamina stamina = new Stamina(); // direkt initialisiert
    private final LazyOptional<Stamina> optional = LazyOptional.of(() -> stamina);


    private Stamina createStamina() {
        if (stamina == null) {
            stamina = new Stamina(); // immer initialisieren
        }
        return stamina;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        return cap == PLAYER_STAMINA ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createStamina().saveNBTData(nbt); // direkt auf das bestehende Objekt zugreifen
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        stamina.loadNBTData(nbt); // direkt auf das bestehende Objekt zugreifen
    }

    // Optional: Externe Methode, um direkt auf die Stamina zuzugreifen
    public Stamina getStamina() {
        return stamina;
    }
}
