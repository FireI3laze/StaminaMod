package com.fireblaze.exhausted.networking.packet;

import com.fireblaze.exhausted.stepUp.ClientStepUpData;
import com.fireblaze.exhausted.stepUp.ClientStepUpHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record StepUpS2CPacket(float stepUpHeight) {

    public static void encode(StepUpS2CPacket pkt, FriendlyByteBuf buf) {
        buf.writeFloat(pkt.stepUpHeight);
    }

    public static StepUpS2CPacket decode(FriendlyByteBuf buf) {
        return new StepUpS2CPacket(buf.readFloat());
    }

    public static void handle(StepUpS2CPacket pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Wert auf Client-Seite speichern
            ClientStepUpData.setStepUpHeight(pkt.stepUpHeight);
            ClientStepUpHandler.setStepUp(pkt.stepUpHeight);
        });
        ctx.get().setPacketHandled(true);
    }
}
