package com.minhhjjj.efprogressivediff.client;

import com.minhhjjj.efprogressivediff.EFProgressiveDiff;
import com.minhhjjj.efprogressivediff.capability.PlayerDataCapability;
import com.minhhjjj.efprogressivediff.config.PDClientConfig;
import com.minhhjjj.efprogressivediff.config.PDConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EFProgressiveDiff.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class DifficultyOverlay {
    public static final int BLACK = 0xFF000000;
    public static final int DARK_GRAY = 0xFF555555;
    public static final int RED = 0xFFAA0000;
    public static final int GREEN = 0xFF00AA00;
    public static final int PURPLE = 0xFFAA00AA;

    private static final ResourceLocation SKULL_NORMAL = ResourceLocation.fromNamespaceAndPath(EFProgressiveDiff.MODID, "textures/gui/skull_normal.png");
    private static final ResourceLocation SKULL_HARD = ResourceLocation.fromNamespaceAndPath(EFProgressiveDiff.MODID, "textures/gui/skull_hard.png");

    @SuppressWarnings("null")
	public static final IGuiOverlay INSTANCE = (gui, poseStack, partialTick, screenWidth, screenHeight) -> {
        Minecraft mc = Minecraft.getInstance();
    if (mc.screen instanceof ChatScreen) return;
        if(!PDClientConfig.showHud && !(KeyInputHandler.tickCounter < KeyInputHandler.TIME_APPEAR)) return;
        if(mc.player == null) return;

        mc.player.getCapability(PlayerDataCapability.INSTANCE).ifPresent(cap -> {
            double currentDiff = cap.getAroundDifficulty();
            double maxDiff = PDConfig.maxDifficultyCap;
            double percentage = Math.min(maxDiff > 0 ? currentDiff / maxDiff : 0, 1);
            
            int barWidth = 100;
            int barHeight = 10;
            int posX = PDClientConfig.posX;
            int posY = PDClientConfig.posY;

            int filledWidth = (int) (barWidth * percentage);

            poseStack.fill(posX-1, screenHeight-posY-barHeight-1, posX+barWidth+1, screenHeight-posY+1, BLACK);
            poseStack.fill(posX, screenHeight-posY-barHeight, posX+barWidth, screenHeight-posY, DARK_GRAY);

            ResourceLocation skullTexture = percentage < 0.33 ? SKULL_NORMAL : SKULL_HARD;
            if (filledWidth > 0) {
                if (percentage < 0.33) {
                    poseStack.fill(posX, screenHeight-posY-barHeight, posX+filledWidth, screenHeight-posY, GREEN);
                } else if (percentage < 0.66) {
                    poseStack.fill(posX, screenHeight-posY-barHeight, posX+filledWidth, screenHeight-posY, RED);
                } else {
                    poseStack.fill(posX, screenHeight-posY-barHeight, posX+filledWidth, screenHeight-posY, PURPLE);
                }
            }
            poseStack.blit(skullTexture, posX+filledWidth-8, screenHeight-posY-barHeight-4, 0.0F, 0.0F, 16, 16, 16, 16);
            String text = String.format("%.2f / %.0f", currentDiff, maxDiff);
            poseStack.drawString(mc.font, text, posX-1, screenHeight-posY+2, 0xFFFFFF, true);
        });
    };

    @SuppressWarnings("null")
    @SubscribeEvent
    public static void registerOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll(EFProgressiveDiff.MODID, DifficultyOverlay.INSTANCE);
    }
}