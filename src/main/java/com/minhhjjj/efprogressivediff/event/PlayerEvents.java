package com.minhhjjj.efprogressivediff.event;

import com.minhhjjj.efprogressivediff.EFProgressiveDiff;
import com.minhhjjj.efprogressivediff.attachment.PlayerDataAttachment;
import com.minhhjjj.efprogressivediff.config.PDConfig;
import static com.minhhjjj.efprogressivediff.attachment.PlayerDataAttachment.type;
import com.minhhjjj.efprogressivediff.network.DifficultySyncPacket;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

@EventBusSubscriber(modid = EFProgressiveDiff.MODID)
public class PlayerEvents {

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        syncDifficulty(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        player.getData(type()).addDifficulty(PDConfig.getRespawnIncrement());
        syncDifficulty(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        syncDifficulty(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerWakeUp(PlayerWakeUpEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        player.getData(type()).addDifficulty(PDConfig.getWakeupIncrement());
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        Entity victim = event.getEntity();
        Entity source = event.getSource().getEntity();
        if (source instanceof ServerPlayer player) {
            if (victim instanceof Enemy) player.getData(type()).addDifficulty(PDConfig.getHostileIncrement());
            else if (victim instanceof NeutralMob) player.getData(type()).addDifficulty(PDConfig.getNeutralIncrement());
            player.getData(type()).addDifficulty(PDConfig.getMobIncrement(BuiltInRegistries.ENTITY_TYPE.getKey(victim.getType())));
        }
    }

    public static void syncDifficulty(Entity entity) {
        if(entity instanceof ServerPlayer serverPlayer) {
            PlayerDataAttachment data = ((Player)serverPlayer).getData(type());
            DifficultySyncPacket msg = new DifficultySyncPacket(data.getDifficulty(), data.getAroundDifficulty(), PDConfig.getMaxDiff());
            PacketDistributor.sendToPlayer(serverPlayer, msg);
        }
    }

}