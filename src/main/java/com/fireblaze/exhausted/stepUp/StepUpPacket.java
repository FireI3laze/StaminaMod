package com.fireblaze.exhausted.stepUp;

import com.fireblaze.exhausted.stepUp.ClientStepUpHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class StepUpPacket {
    private final float stepUpHeight;

    // Konstruktor
    public StepUpPacket(float stepUpHeight) {
        this.stepUpHeight = stepUpHeight;
    }

    public static void encode(StepUpPacket msg, FriendlyByteBuf buf) {
        buf.writeFloat(msg.stepUpHeight);
    }

    public static StepUpPacket decode(FriendlyByteBuf buf) {
        return new StepUpPacket(buf.readInt());
    }

    public static void handle(StepUpPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientStepUpHandler.setStepUp(msg.stepUpHeight);
        });

        ctx.get().setPacketHandled(true);
    }
}
