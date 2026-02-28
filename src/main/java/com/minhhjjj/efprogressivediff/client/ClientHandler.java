package com.minhhjjj.efprogressivediff.client;

import java.util.function.Supplier;

import com.minhhjjj.efprogressivediff.capability.PlayerDataCapability;
import com.minhhjjj.efprogressivediff.network.DifficultySyncPacket;

import net.minecraft.client.Minecraft;
import net.minecraftforge.network.NetworkEvent;

public class ClientHandler {

    public static void handle(DifficultySyncPacket msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        Minecraft mc = Minecraft.getInstance();
        if(mc.player == null) return;
        mc.player.getCapability(PlayerDataCapability.INSTANCE).ifPresent(cap -> {
            cap.setDifficulty(msg.difficulty);
        });
        context.setPacketHandled(true);
    }
}
