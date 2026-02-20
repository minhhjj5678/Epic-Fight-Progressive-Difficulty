package com.minhhjjj.efprogressivediff.config;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import com.minhhjjj.efprogressivediff.EFProgressiveDiff;

@EventBusSubscriber(modid = EFProgressiveDiff.MODID)
public class PDConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue DISABLE = BUILDER
            .comment("Set to true to disable mod")
            .define("disable", false);

    public static final ModConfigSpec.IntValue BASE_VALUE = BUILDER
            .comment(
                "",
                "Final attribute value = Original Attribute Value + Base Value * (1 + multiply * dayCount)")
            .defineInRange("baseValue", 1, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue WEIGHT_MULTIPLY = BUILDER
            .defineInRange("weightMultiply", 0.05, 0, 1);

    public static final ModConfigSpec.DoubleValue IMPACT_MULTIPLY = BUILDER
            .defineInRange("impactMultiply", 0.05, 0, 1);

    public static final ModConfigSpec.DoubleValue STUN_ARMOR_MULTIPLY = BUILDER
            .defineInRange("stunArmorMultiply", 0.05, 0, 1);

    public static final ModConfigSpec.DoubleValue ARMOR_NEGATION_MULTIPLY = BUILDER
            .defineInRange("armorNegationMultiply", 0.05, 0, 1);

    public static final ModConfigSpec.DoubleValue MAX_STRIKES_MULTIPLY = BUILDER
            .defineInRange("maxStrikesMultiply", 0.03, 0, 1);

    public static final ModConfigSpec.IntValue WEIGHT_CAP = BUILDER
            .comment("The maximum additional value mobs can gain")
            .defineInRange("weightCap", 10, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue IMPACT_CAP = BUILDER
            .defineInRange("impactCap", 10, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue STUN_ARMOR_CAP = BUILDER
            .defineInRange("stunArmorCap", 40, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue ARMOR_NEGATION_CAP = BUILDER
            .defineInRange("armorNegationCap", 30, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue MAX_STRIKES_CAP = BUILDER
            .defineInRange("maxStrikesCap", 3, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean disable;
    public static int baseValue;
    public static double weightMultiply;
    public static double impactMultiply;
    public static double stunArmorMultiply;
    public static double armorNegationMultiply;
    public static double maxStrikesMultiply;
    public static int weightCap;
    public static int impactCap;
    public static int stunArmorCap;
    public static int maxStrikesCap;
    public static int armorNegationCap;

    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        disable = DISABLE.get();
        baseValue = BASE_VALUE.get();
        weightMultiply = WEIGHT_MULTIPLY.get();
        impactMultiply = IMPACT_MULTIPLY.get();
        stunArmorMultiply = STUN_ARMOR_MULTIPLY.get();
        armorNegationMultiply = ARMOR_NEGATION_MULTIPLY.get();
        maxStrikesMultiply = MAX_STRIKES_MULTIPLY.get();
        weightCap = WEIGHT_CAP.get();
        impactCap = IMPACT_CAP.get();
        stunArmorCap = STUN_ARMOR_CAP.get();
        maxStrikesCap = MAX_STRIKES_CAP.get();
        armorNegationCap = ARMOR_NEGATION_CAP.get();
    }
}
