package com.fireblaze.exhausted.Sounds;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlaySoundPacket {
    private final int threshold;

    // Konstruktor
    public PlaySoundPacket(int threshold) {
        this.threshold = threshold;
    }

    // Encoder: schreibt Daten in den Buffer
    public static void encode(PlaySoundPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.threshold);
    }

    // Decoder: liest Daten aus dem Buffer
    public static PlaySoundPacket decode(FriendlyByteBuf buf) {
        return new PlaySoundPacket(buf.readInt());
    }

    // Handler: führt auf Client aus
    public static void handle(PlaySoundPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientSoundHandler.playThresholdSound(msg.threshold);
        });

        ctx.get().setPacketHandled(true);
    }
}
