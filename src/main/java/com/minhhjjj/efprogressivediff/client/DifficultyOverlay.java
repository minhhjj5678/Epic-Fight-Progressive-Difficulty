package com.minhhjjj.efprogressivediff.client;

import com.minhhjjj.efprogressivediff.capability.PlayerDataCapability;
import com.minhhjjj.efprogressivediff.config.PDClientConfig;
import com.minhhjjj.efprogressivediff.config.PDConfig;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class DifficultyOverlay {
    @SuppressWarnings("null")
	public static final IGuiOverlay INSTANCE = (gui, poseStack, partialTick, screenWidth, screenHeight) -> {
        Minecraft mc = Minecraft.getInstance();
        if(!PDClientConfig.showHud) return;
        if(mc.player == null) return;

        mc.player.getCapability(PlayerDataCapability.INSTANCE).ifPresent(cap -> {
            double currentDiff = cap.getDifficulty();
            double maxDiff = PDConfig.maxDifficultyCap;
            double percentage = Math.min(maxDiff > 0 ? currentDiff / maxDiff : 0, 1);
            
            int barWidth = 100;
            int barHeight = 30;
            int posX = PDClientConfig.posX;
            int posY = PDClientConfig.posY;

            int filledWidth = (int) (barWidth * percentage);

            poseStack.fill(posX-1, screenHeight-posY-barHeight-1, posX+barWidth+1, screenHeight-posY+1, 0xFF000000);
            poseStack.fill(posX, screenHeight-posY-barHeight, posX+barWidth, screenHeight-posY, 0xFF555555);
            if (filledWidth > 0) {
                poseStack.fill(posX, screenHeight-posY-barHeight, posX+filledWidth, screenHeight-posY, 0xFFAA0000);
            }
            String text = String.format("%.2f / %.0f", currentDiff, maxDiff);
            poseStack.drawString(mc.font, text, posX-1, screenHeight-posY-barHeight-2, 0xFFFFFF, true);
        });
    };
}