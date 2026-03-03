package com.minhhjjj.efprogressivediff.event;

import com.minhhjjj.efprogressivediff.EFProgressiveDiff;
import com.minhhjjj.efprogressivediff.attachment.PlayerDataAttachment;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = EFProgressiveDiff.MODID)
public class DataAttachmentEventHandler {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (PlayerDataAttachment.PLAYER_DIFFICULTY != null) player.getData(PlayerDataAttachment.PLAYER_DIFFICULTY).tick(player);
    }

    // @SubscribeEvent
    // public static void onVillagerTick(LivingTickEvent event) {
    //     if (event.getEntity().level().isClientSide()) return;
    //     if (event.getEntity() instanceof Villager villager) {
    //         villager.getCapability(PlayerDataCapability.INSTANCE).ifPresent(cap -> {
    //             cap.tick();
    //             cap.debug();
    //         });
    //     }
    // }

    // @SuppressWarnings("null")
    // @SubscribeEvent
    // public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
    //     if (event.getTarget() instanceof Villager villager) {
    //         if (villager.level().isClientSide()) return;
    //         villager.getCapability(PlayerDataCapability.INSTANCE).ifPresent(villagerCap -> {
    //             event.getEntity().sendSystemMessage(Component.literal("Dân làng tên " + villager.getName() + " đang có độ khó " + villagerCap.getDifficulty()));
    //         });
    //     }
    // }
}