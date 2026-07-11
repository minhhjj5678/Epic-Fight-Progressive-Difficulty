package com.minhhjjj.efprogressivediff.network;

import com.minhhjjj.efprogressivediff.EFProgressiveDiff;
import com.minhhjjj.efprogressivediff.attachment.PlayerDataAttachment;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@SuppressWarnings("removal")
@EventBusSubscriber(modid = EFProgressiveDiff.MODID, bus = EventBusSubscriber.Bus.MOD)
public class PacketHandler {
    public static final String PROTOCOL_VERSION = "3";

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);

        CustomPacketPayload.Type<DifficultySyncPacket> TYPE = DifficultySyncPacket.TYPE;
        StreamCodec<ByteBuf, DifficultySyncPacket> STREAM_CODEC = DifficultySyncPacket.STREAM_CODEC;
        if (TYPE == null || STREAM_CODEC == null) return;
        registrar.playToClient(TYPE, STREAM_CODEC, PacketHandler::DifficultySyncHandle);       
    }

    public static void DifficultySyncHandle(DifficultySyncPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            AttachmentType<PlayerDataAttachment> data = PlayerDataAttachment.PLAYER_DIFFICULTY.get();
            if (data != null) {
                player.getData(data).setMaxDifficulty(packet.maxDifficulty());
                player.getData(data).setDifficulty(packet.difficulty());
                player.getData(data).setAroundDifficulty(packet.aroundDifficulty());
            }
        });
    }
}