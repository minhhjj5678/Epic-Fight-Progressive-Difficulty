package com.minhhjjj.efprogressivediff.network;

import java.util.function.Supplier;

import com.minhhjjj.efprogressivediff.client.ClientHandler;

import com.minhhjjj.efprogressivediff.config.PDConfig;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class DifficultySyncPacket {
    public double difficulty;
    public double aroundDifficulty;
    public double maxDiff;

    public DifficultySyncPacket () {}

    public DifficultySyncPacket(double difficulty, double aroundDifficulty, double maxDifficulty) {
        this.difficulty = difficulty;
        this.aroundDifficulty = aroundDifficulty;
        this.maxDiff = maxDifficulty;
    }

    public static DifficultySyncPacket fromBytes(FriendlyByteBuf buf) {
        DifficultySyncPacket msg = new DifficultySyncPacket();
        msg.difficulty = buf.readDouble();
        msg.aroundDifficulty = buf.readDouble();
        msg.maxDiff = buf.readDouble();
        return msg;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(this.difficulty);
        buf.writeDouble(this.aroundDifficulty);
        buf.writeDouble(this.maxDiff);
    }

    public static void handle(DifficultySyncPacket msg, Supplier<NetworkEvent.Context> contextSupplier) {
        ClientHandler.handle(msg, contextSupplier);
    }
}
