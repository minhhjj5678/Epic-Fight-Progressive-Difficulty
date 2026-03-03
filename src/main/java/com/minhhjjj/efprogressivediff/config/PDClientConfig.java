package com.minhhjjj.efprogressivediff.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;

import com.minhhjjj.efprogressivediff.EFProgressiveDiff;

@EventBusSubscriber(modid = EFProgressiveDiff.MODID, value = Dist.CLIENT)
public class PDClientConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue SHOW_DIFFICULTY_OVERLAY = BUILDER
    .comment("Set to true to always show difficulty bar")
    .define("showHud", false);
    private static final ModConfigSpec.IntValue OVERLAY_POSITION_X = BUILDER
    .comment("Distance from the left of the window")
    .defineInRange("posX", 20, 0, Integer.MAX_VALUE);
    private static final ModConfigSpec.IntValue OVERLAY_POSITION_Y = BUILDER
    .comment("Distance from the bottom of the window")
    .defineInRange("posY", 30, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean showHud;
    public static int posX;
    public static int posY;

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
        if (event.getConfig().getSpec() == PDClientConfig.SPEC) {
            showHud = SHOW_DIFFICULTY_OVERLAY.get();
            posX = OVERLAY_POSITION_X.get();
            posY = OVERLAY_POSITION_Y.get();
        }
    }
}