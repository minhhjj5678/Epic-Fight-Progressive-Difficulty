package com.minhhjjj.efprogressivediff.event;

import com.minhhjjj.efprogressivediff.EFProgressiveDiff;
import com.minhhjjj.efprogressivediff.capability.PlayerDataCapability;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

@Mod.EventBusSubscriber(modid = EFProgressiveDiff.MODID)
public class CapabilityEventHandler {
    @SuppressWarnings("removal")
	@SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            if (!player.getCapability(PlayerDataCapability.INSTANCE).isPresent()) {
                event.addCapability(new ResourceLocation(EFProgressiveDiff.MODID, "player_diff"), new PlayerDataCapability());
            }
        }
        // else if (event.getObject() instanceof Villager villager) {
        //     if (!villager.getCapability(PlayerDataCapability.INSTANCE).isPresent()) {
        //         event.addCapability(new ResourceLocation(EFProgressiveDiff.MODID, "player_diff"), new PlayerDataCapability());
        //     }
        // }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        event.getOriginal().reviveCaps();
        event.getOriginal().getCapability(PlayerDataCapability.INSTANCE).ifPresent(originalCap -> {
            event.getEntity().getCapability(PlayerDataCapability.INSTANCE).ifPresent(newCap -> {
                newCap.copyFrom(originalCap);
            });
        });
        event.getOriginal().invalidateCaps();
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!event.side.isServer()) return;
        if (event.phase != TickEvent.Phase.END) return;
        event.player.getCapability(PlayerDataCapability.INSTANCE).ifPresent(cap -> {
            cap.tick((ServerPlayer)event.player);
            cap.debug();
        });
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
