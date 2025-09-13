package com.fireblaze.exhausted.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public record JumpS2CPacket(Vec3 motion) {

    public static void encode(JumpS2CPacket pkt, FriendlyByteBuf buf) {
        buf.writeDouble(pkt.motion.x);
        buf.writeDouble(pkt.motion.y);
        buf.writeDouble(pkt.motion.z);
    }

    public static JumpS2CPacket decode(FriendlyByteBuf buf) {
        return new JumpS2CPacket(new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()));
    }

    public static void handle(JumpS2CPacket pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var player = net.minecraft.client.Minecraft.getInstance().player;
            if (player != null) {
                player.setDeltaMovement(pkt.motion);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
