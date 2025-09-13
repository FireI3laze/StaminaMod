package com.fireblaze.exhausted.networking;

import com.fireblaze.exhausted.Sounds.PlaySoundPacket;
import com.fireblaze.exhausted.Exhausted;
import com.fireblaze.exhausted.networking.packet.*;
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
    private static int packetID = 0;

    public static void register() {
        INSTANCE = NetworkRegistry.ChannelBuilder
                .named(ResourceLocation.fromNamespaceAndPath(Exhausted.MODID, "messages"))
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

        INSTANCE.messageBuilder(PlaySoundPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PlaySoundPacket::encode)
                .decoder(PlaySoundPacket::decode)
                .consumerMainThread(PlaySoundPacket::handle)
                .add();

        INSTANCE.messageBuilder(JumpS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(JumpS2CPacket::encode)
                .decoder(JumpS2CPacket::decode)
                .consumerMainThread(JumpS2CPacket::handle)
                .add();

        INSTANCE.messageBuilder(MiningspeedS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(MiningspeedS2CPacket::encode)
                .decoder(MiningspeedS2CPacket::decode)
                .consumerMainThread(MiningspeedS2CPacket::handle)
                .add();

        INSTANCE.messageBuilder(StepUpS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(StepUpS2CPacket::encode)
                .decoder(StepUpS2CPacket::decode)
                .consumerMainThread(StepUpS2CPacket::handle)
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
