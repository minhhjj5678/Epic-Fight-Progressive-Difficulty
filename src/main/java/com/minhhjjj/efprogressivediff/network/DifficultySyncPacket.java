package com.minhhjjj.efprogressivediff.network;

import java.util.function.Supplier;

import com.minhhjjj.efprogressivediff.client.ClientHandler;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class DifficultySyncPacket {
    public double difficulty;
    public double aroundDifficulty;

    public DifficultySyncPacket () {}

    public DifficultySyncPacket(double difficulty, double aroundDifficulty) {
        this.difficulty = difficulty;
        this.aroundDifficulty = aroundDifficulty;
    }

    public static DifficultySyncPacket fromBytes(FriendlyByteBuf buf) {
        DifficultySyncPacket msg = new DifficultySyncPacket();
        msg.difficulty = buf.readDouble();
        msg.aroundDifficulty = buf.readDouble();
        return msg;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(this.difficulty);
        buf.writeDouble(this.aroundDifficulty);
    }

    public static void handle(DifficultySyncPacket msg, Supplier<NetworkEvent.Context> contextSupplier) {
        ClientHandler.handle(msg, contextSupplier);
    }
}
