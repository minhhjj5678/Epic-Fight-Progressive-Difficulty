package com.minhhjjj.efprogressivediff.client;

import org.lwjgl.glfw.GLFW;

import com.minhhjjj.efprogressivediff.EFProgressiveDiff;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EFProgressiveDiff.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
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
    public static void tick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        if (tickCounter < TIME_APPEAR) {
            tickCounter++;
        }
    }
}
