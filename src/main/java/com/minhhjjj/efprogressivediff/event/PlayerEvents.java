package com.minhhjjj.efprogressivediff.event;

import com.minhhjjj.efprogressivediff.EFProgressiveDiff;
import com.minhhjjj.efprogressivediff.capability.PlayerDataCapability;
import com.minhhjjj.efprogressivediff.config.PDConfig;
import com.minhhjjj.efprogressivediff.network.DifficultySyncPacket;
import com.minhhjjj.efprogressivediff.network.PacketHandler;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = EFProgressiveDiff.MODID)
public class PlayerEvents {

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        syncDifficulty(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        player.getCapability(PlayerDataCapability.INSTANCE).ifPresent(cap -> {
            cap.addDifficulty(PDConfig.getRespawnIncrement());
        });
        syncDifficulty(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        syncDifficulty(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerWakeUp(PlayerWakeUpEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        player.getCapability(PlayerDataCapability.INSTANCE).ifPresent(cap -> {
            cap.addDifficulty(PDConfig.getWakeupIncrement());
        });
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        Entity victim = event.getEntity();
        Entity source = event.getSource().getEntity();
        if (source instanceof ServerPlayer player) {
            player.getCapability(PlayerDataCapability.INSTANCE).ifPresent(cap -> {
                if (victim instanceof Enemy) cap.addDifficulty(PDConfig.getHostileIncrement());
                else if (victim instanceof NeutralMob) cap.addDifficulty(PDConfig.getNeutralIncrement());
                cap.addDifficulty(PDConfig.getMobIncrement(ForgeRegistries.ENTITY_TYPES.getKey(victim.getType())));
            });
        }
    }

    public static void syncDifficulty(Entity entity) {
        if(entity instanceof ServerPlayer serverPlayer) {
            serverPlayer.getCapability(PlayerDataCapability.INSTANCE).ifPresent(cap -> {
                PacketHandler.channel.sendTo(new DifficultySyncPacket(cap.getDifficulty(), cap.getAroundDifficulty(), PDConfig.getMaxDiff()), serverPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
            });
        }
    }
}
