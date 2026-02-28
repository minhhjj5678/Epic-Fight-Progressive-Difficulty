package com.minhhjjj.efprogressivediff.event;

import com.minhhjjj.efprogressivediff.EFProgressiveDiff;
import com.minhhjjj.efprogressivediff.capability.PlayerDataCapability;
import com.minhhjjj.efprogressivediff.network.DifficultySyncPacket;
import com.minhhjjj.efprogressivediff.network.PacketHandler;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkDirection;

@Mod.EventBusSubscriber(modid = EFProgressiveDiff.MODID)
public class PlayerEvents {

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        syncDifficulty(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        syncDifficulty(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        syncDifficulty(event.getEntity());
    }

    public static void syncDifficulty(Entity entity) {
        if(entity instanceof ServerPlayer serverPlayer) {
            serverPlayer.getCapability(PlayerDataCapability.INSTANCE).ifPresent(cap -> {
                PacketHandler.channel.sendTo(new DifficultySyncPacket(cap.getDifficulty()), serverPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
            });
        }
    }
}
