package com.minhhjjj.efprogressivediff.config;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import com.minhhjjj.efprogressivediff.EFProgressiveDiff;

@Mod.EventBusSubscriber(modid = EFProgressiveDiff.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PDClientConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue SHOW_DIFFICULTY_OVERLAY = BUILDER
    .comment("Set to true to always show difficulty bar")
    .define("showHud", false);
    private static final ForgeConfigSpec.IntValue OVERLAY_POSITION_X = BUILDER
    .comment("Distance from the left of the window")
    .defineInRange("posX", 20, 0, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.IntValue OVERLAY_POSITION_Y = BUILDER
    .comment("Distance from the bottom of the window")
    .defineInRange("posY", 30, 0, Integer.MAX_VALUE);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

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