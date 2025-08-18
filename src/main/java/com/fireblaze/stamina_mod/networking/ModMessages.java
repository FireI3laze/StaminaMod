package com.fireblaze.stamina_mod.networking;

import com.fireblaze.stamina_mod.StaminaMod;
import com.fireblaze.stamina_mod.networking.packet.StaminaC2SPacket;
import com.fireblaze.stamina_mod.networking.packet.StaminaDataSyncS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    public static SimpleChannel INSTANCE; // public damit andere darauf zugreifen können

    private static int packetId = 0;
    private static int id() { return packetId++; }

    public static void register() {
        INSTANCE = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(StaminaMod.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        // Packets registrieren
        INSTANCE.messageBuilder(StaminaC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(StaminaC2SPacket::new)
                .encoder(StaminaC2SPacket::toBytes)
                .consumerMainThread(StaminaC2SPacket::handle)
                .add();

        INSTANCE.messageBuilder(StaminaDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(StaminaDataSyncS2CPacket::new)
                .encoder(StaminaDataSyncS2CPacket::toBytes)
                .consumerMainThread(StaminaDataSyncS2CPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        if (INSTANCE != null)
            INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        if (INSTANCE != null)
            INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
