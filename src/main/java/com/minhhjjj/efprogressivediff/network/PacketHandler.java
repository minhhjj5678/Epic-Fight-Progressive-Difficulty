package com.minhhjjj.efprogressivediff.network;

import java.util.Objects;

import com.minhhjjj.efprogressivediff.EFProgressiveDiff;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry.ChannelBuilder;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    @SuppressWarnings("removal")
    public static final ResourceLocation INSTANCE = new ResourceLocation(EFProgressiveDiff.MODID, "network");
    public static SimpleChannel channel;
    public static final String PROTOCOL_VERSION = "3";

    public PacketHandler() {}

    public static void init() {
        channel = ChannelBuilder.named(INSTANCE).clientAcceptedVersions((s) -> Objects.equals(s, PROTOCOL_VERSION)).serverAcceptedVersions((s) -> Objects.equals(s, PROTOCOL_VERSION)).networkProtocolVersion(() -> PROTOCOL_VERSION).simpleChannel();
        channel.messageBuilder(DifficultySyncPacket.class, 1)
               .decoder(DifficultySyncPacket::fromBytes)
               .encoder(DifficultySyncPacket::toBytes)
               .consumerMainThread(DifficultySyncPacket::handle)
               .add();
    }
}
