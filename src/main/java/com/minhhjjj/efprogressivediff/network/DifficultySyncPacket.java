package com.minhhjjj.efprogressivediff.network;

import java.util.function.Supplier;

import com.minhhjjj.efprogressivediff.client.ClientHandler;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class DifficultySyncPacket {
    public double difficulty;

    public DifficultySyncPacket () {}

    public DifficultySyncPacket(double difficulty) {
        this.difficulty = difficulty;
    }

    public static DifficultySyncPacket fromBytes(FriendlyByteBuf buf) {
        DifficultySyncPacket msg = new DifficultySyncPacket();
        msg.difficulty = buf.readDouble();
        return msg;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(this.difficulty);
    }

    public static void handle(DifficultySyncPacket msg, Supplier<NetworkEvent.Context> contextSupplier) {
        ClientHandler.handle(msg, contextSupplier);
    }
}
