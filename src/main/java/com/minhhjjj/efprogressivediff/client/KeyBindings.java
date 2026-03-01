package com.minhhjjj.efprogressivediff.client;

import javax.annotation.Nonnull;

import com.minhhjjj.efprogressivediff.EFProgressiveDiff;
import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EFProgressiveDiff.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KeyBindings {
    public static final String KEY_CATEGORY = "key.category." + EFProgressiveDiff.MODID;
    public static final String KEY_TOGGLE_GUI = "key." + EFProgressiveDiff.MODID + ".toggle_gui";

    @Nonnull
    public static final KeyMapping TOGGLE_GUI = new KeyMapping(
        KEY_TOGGLE_GUI,
        KeyConflictContext.IN_GAME,
        InputConstants.Type.KEYSYM,
        InputConstants.KEY_H,
        KEY_CATEGORY
    );

    @SubscribeEvent
    public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
        event.register(TOGGLE_GUI);
    }
}
