package com.minhhjjj.efprogressivediff.client;

import com.minhhjjj.efprogressivediff.EFProgressiveDiff;
import com.minhhjjj.efprogressivediff.capability.PlayerDataCapability;
import com.minhhjjj.efprogressivediff.config.PDClientConfig;
import com.minhhjjj.efprogressivediff.config.PDConfig;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EFProgressiveDiff.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class DifficultyOverlay {
    @SuppressWarnings("null")
	public static final IGuiOverlay INSTANCE = (gui, poseStack, partialTick, screenWidth, screenHeight) -> {
        Minecraft mc = Minecraft.getInstance();
        if(!PDClientConfig.showHud && !(KeyInputHandler.tickCounter < KeyInputHandler.TIME_APPEAR)) return;
        if(mc.player == null) return;

        mc.player.getCapability(PlayerDataCapability.INSTANCE).ifPresent(cap -> {
            double currentDiff = cap.getDifficulty();
            double maxDiff = PDConfig.maxDifficultyCap;
            double percentage = Math.min(maxDiff > 0 ? currentDiff / maxDiff : 0, 1);
            
            int barWidth = 100;
            int barHeight = 10;
            int posX = PDClientConfig.posX;
            int posY = PDClientConfig.posY;

            int filledWidth = (int) (barWidth * percentage);

            poseStack.fill(posX-1, screenHeight-posY-barHeight-1, posX+barWidth+1, screenHeight-posY+1, 0xFF000000);
            poseStack.fill(posX, screenHeight-posY-barHeight, posX+barWidth, screenHeight-posY, 0xFF555555);
            if (filledWidth > 0) {
                poseStack.fill(posX, screenHeight-posY-barHeight, posX+filledWidth, screenHeight-posY, 0xFFAA0000);
            }
            String text = String.format("%.2f / %.0f", currentDiff, maxDiff);
            poseStack.drawString(mc.font, text, posX-1, screenHeight-posY-2, 0xFFFFFF, true);
        });
    };

    @SuppressWarnings("null")
    @SubscribeEvent
    public static void registerOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll(EFProgressiveDiff.MODID, DifficultyOverlay.INSTANCE);
    }
}