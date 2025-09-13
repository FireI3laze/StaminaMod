package com.fireblaze.exhausted.networking.packet;

import com.fireblaze.exhausted.events.ClientMiningData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record MiningspeedS2CPacket(float multiplier) {

    public static void encode(MiningspeedS2CPacket pkt, FriendlyByteBuf buf) {
        buf.writeFloat(pkt.multiplier);
    }

    public static MiningspeedS2CPacket decode(FriendlyByteBuf buf) {
        return new MiningspeedS2CPacket(buf.readFloat());
    }

    public static void handle(MiningspeedS2CPacket pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Wert auf Client-Seite speichern
            ClientMiningData.setSpeedMultiplier(pkt.multiplier);
        });
        ctx.get().setPacketHandled(true);
    }
}
