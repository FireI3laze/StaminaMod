package com.fireblaze.exhausted.networking.packet;

import com.fireblaze.exhausted.client.ClientStaminaData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class StaminaDataSyncS2CPacket {
    private final float shortStamina;
    private final float longStamina;
    private final float maxStamina;
    private final float staminaExp;
    private final int staminaLvl;

    public StaminaDataSyncS2CPacket(float shortStamina, float longStamina, float maxStamina, float staminaExp, int staminaLvl) {
        this.shortStamina = shortStamina;
        this.longStamina = longStamina;
        this.maxStamina = maxStamina;
        this.staminaExp = staminaExp;
        this.staminaLvl = staminaLvl;
    }
    public StaminaDataSyncS2CPacket(float maxStamina) {
        this.shortStamina = maxStamina;
        this.longStamina = maxStamina;
        this.maxStamina = maxStamina;
        this.staminaExp = 9990;
        this.staminaLvl = 10;
    }

    public StaminaDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.shortStamina = buf.readFloat();
        this.longStamina = buf.readFloat();
        this.maxStamina = buf.readFloat();
        this.staminaExp = buf.readFloat();
        this.staminaLvl = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeFloat(shortStamina);
        buf.writeFloat(longStamina);
        buf.writeFloat(maxStamina);
        buf.writeFloat(staminaExp);
        buf.writeInt(staminaLvl);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ClientStaminaData.set(shortStamina, longStamina, maxStamina, staminaExp, staminaLvl);
        });
        return true;
    }
}
