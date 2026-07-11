package com.minhhjjj.efprogressivediff.network;

import com.minhhjjj.efprogressivediff.EFProgressiveDiff;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("null")
public record DifficultySyncPacket(double difficulty, double aroundDifficulty, double maxDifficulty) implements CustomPacketPayload {
    public static final Type<DifficultySyncPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(EFProgressiveDiff.MODID, "difficulty_sync"));
    public static final StreamCodec<ByteBuf, DifficultySyncPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.DOUBLE, DifficultySyncPacket::difficulty, ByteBufCodecs.DOUBLE, DifficultySyncPacket::aroundDifficulty, ByteBufCodecs.DOUBLE, DifficultySyncPacket::maxDifficulty, DifficultySyncPacket::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
