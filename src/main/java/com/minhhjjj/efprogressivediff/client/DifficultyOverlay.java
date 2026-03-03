package com.minhhjjj.efprogressivediff.client;

import com.minhhjjj.efprogressivediff.EFProgressiveDiff;
import com.minhhjjj.efprogressivediff.attachment.PlayerDataAttachment;
import static com.minhhjjj.efprogressivediff.attachment.PlayerDataAttachment.type;
import com.minhhjjj.efprogressivediff.config.PDClientConfig;
import com.minhhjjj.efprogressivediff.config.PDConfig;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw.Layer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import javax.annotation.Nonnull;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

@EventBusSubscriber(modid = EFProgressiveDiff.MODID, value = Dist.CLIENT)
public class DifficultyOverlay implements Layer {
    public static final int BLACK = 0xFF000000;
    public static final int WHITE = 0xFFFFFFFF;
    public static final int DARK_GRAY = 0xFF555555;
    public static final int RED = 0xFFAA0000;
    public static final int GREEN = 0xFF00AA00;
    public static final int PURPLE = 0xFFAA00AA;

    public static double lastDiff = -1;
    public static String text;

    @Override
    public void render(@Nonnull GuiGraphics guiGraphics, @Nonnull DeltaTracker delt) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null || mc.options.hideGui) return;
        if (!PDClientConfig.showHud && !(KeyInputHandler.tickCounter < KeyInputHandler.TIME_APPEAR)) return;

        PlayerDataAttachment data = player.getData(type());
        double currentDiff = data.getAroundDifficulty();
        double maxDiff = PDConfig.maxDifficultyCap;
        double percentage = Math.min(maxDiff>0 ? currentDiff/maxDiff : 0, 1);
        int barHeight = 10;
        int barWidth = 100;
        int posX = PDClientConfig.posX;
        int posY = PDClientConfig.posY;

        int HEIGHT = guiGraphics.guiHeight();
        guiGraphics.fill(posX-1, HEIGHT-posY-barHeight-1, posX+barWidth+1, HEIGHT-posY+1, BLACK);
        guiGraphics.fill(posX, HEIGHT-posY-barHeight, posX+barWidth, HEIGHT-posY, DARK_GRAY);
        if (percentage > 0.0) {
            int STAGE_COLOR = percentage<0.3333 ? GREEN : percentage < 0.6666 ? RED : PURPLE;
            guiGraphics.fill(posX, HEIGHT-posY-barHeight, (int)(posX+((int)barWidth*percentage)), HEIGHT-posY, STAGE_COLOR);
        }
        if (lastDiff < 0 || text == null || Double.compare(lastDiff, currentDiff) != 0) {
            text = String.format("%.2f / %.0f", currentDiff, maxDiff);
            lastDiff = currentDiff;
        }
        var font = mc.font;
        if(font == null) return;
        guiGraphics.drawString(font, text, posX-1, HEIGHT - posY - 2, WHITE);
    }

    @SuppressWarnings("null")
    @SubscribeEvent
    public static void registerOverlays(RegisterGuiLayersEvent event) {
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(EFProgressiveDiff.MODID, "difficulty_overlay"), new DifficultyOverlay());
    }
}