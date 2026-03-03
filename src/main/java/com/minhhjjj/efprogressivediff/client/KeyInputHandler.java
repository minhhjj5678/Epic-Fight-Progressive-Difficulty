package com.minhhjjj.efprogressivediff.client;

import org.lwjgl.glfw.GLFW;

import com.minhhjjj.efprogressivediff.EFProgressiveDiff;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(modid = EFProgressiveDiff.MODID, value = Dist.CLIENT)
public class KeyInputHandler {
    public static int tickCounter = 0;
    public static final int TIME_APPEAR = 200; // 200 ticks = 10 seconds

    @SubscribeEvent
    public static void onKeyDown(InputEvent.Key event) {
        if (event.getKey() != KeyBindings.TOGGLE_GUI.getKey().getValue()) return;
        if (event.getAction() != GLFW.GLFW_PRESS) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        tickCounter = 0;
    }

    @SubscribeEvent
    public static void tick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        if (tickCounter < TIME_APPEAR) {
            tickCounter++;
        }
    }
}
