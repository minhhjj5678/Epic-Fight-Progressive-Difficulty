package com.minhhjjj.efprogressivediff.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import com.minhhjjj.efprogressivediff.EFProgressiveDiff;

@Mod.EventBusSubscriber(modid = EFProgressiveDiff.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PDConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue DISABLE;
    private static final ForgeConfigSpec.DoubleValue MAX_DIFFICULTY_CAP;
    private static final ForgeConfigSpec.DoubleValue DIFFICULTY_INCREMENT;

    private static final ForgeConfigSpec.DoubleValue WEIGHT_BASE_VALUE;
    private static final ForgeConfigSpec.DoubleValue WEIGHT_MULTIPLY;

    private static final ForgeConfigSpec.DoubleValue IMPACT_BASE_VALUE;
    private static final ForgeConfigSpec.DoubleValue IMPACT_MULTIPLY;

    private static final ForgeConfigSpec.DoubleValue STUN_ARMOR_BASE_VALUE;
    private static final ForgeConfigSpec.DoubleValue STUN_ARMOR_MULTIPLY;

    private static final ForgeConfigSpec.DoubleValue ARMOR_NEGATION_BASE_VALUE;
    private static final ForgeConfigSpec.DoubleValue ARMOR_NEGATION_MULTIPLY;

    private static final ForgeConfigSpec.DoubleValue MAX_STRIKES_BASE_VALUE;
    private static final ForgeConfigSpec.DoubleValue MAX_STRIKES_MULTIPLY;

    static {
        BUILDER.comment("Common Configurations")
                .push("General");
                DISABLE = BUILDER
                        .comment("Set to true to disable mod")
                        .define("disable", false);

                        // DIFFICULTY
                MAX_DIFFICULTY_CAP = BUILDER
                        .defineInRange("maxDifficultyCap", 100, 0d, Double.MAX_VALUE);

                DIFFICULTY_INCREMENT = BUILDER
                        .defineInRange("difficultyIncrement", 0.0083d, 0d, Double.MAX_VALUE);
        BUILDER.pop();

        BUILDER.comment(
                "",
                "Final attribute value = Original Attribute Value + Base Value * (1 + multiply * difficulty)",
                "Base Value is the amount added to the attribute at difficulty 0. ",
                "Multiply is how much the added amount increases per difficulty. ")
                .push("Attribute");

                BUILDER.push("Weight");
                WEIGHT_BASE_VALUE = BUILDER
                        .defineInRange("weightBaseValue", 50, 0.0d, Double.MAX_VALUE);

                WEIGHT_MULTIPLY = BUILDER
                        .defineInRange("weightMultiply", 0.07, 0, 1);
                BUILDER.pop();

                BUILDER.push("Impact");
                IMPACT_BASE_VALUE = BUILDER
                        .defineInRange("impactBaseValue", 1, 0.0d, Double.MAX_VALUE);

                IMPACT_MULTIPLY = BUILDER
                        .defineInRange("impactMultiply", 0.05, 0, 1);
                BUILDER.pop();

                BUILDER.push("Stun Armor");
                STUN_ARMOR_BASE_VALUE = BUILDER
                        .defineInRange("stunArmorBaseValue", 1, 0.0d, Double.MAX_VALUE);

                STUN_ARMOR_MULTIPLY = BUILDER
                        .defineInRange("stunArmorMultiply", 0.05, 0, 1);
                BUILDER.pop();

                BUILDER.push("Armor Negation");
                ARMOR_NEGATION_BASE_VALUE = BUILDER
                        .defineInRange("armorNegationBaseValue", 5, 0.0d, Double.MAX_VALUE);

                ARMOR_NEGATION_MULTIPLY = BUILDER
                        .defineInRange("armorNegationMultiply", 0.05, 0, 1);
                BUILDER.pop();

                BUILDER.push("Max Strikes");
                MAX_STRIKES_BASE_VALUE = BUILDER
                        .defineInRange("maxStrikesBaseValue", 1, 0.0d, Double.MAX_VALUE);

                MAX_STRIKES_MULTIPLY = BUILDER
                        .defineInRange("maxStrikesMultiply", 0.03, 0, 1);
                BUILDER.pop();
        BUILDER.pop();
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean disable;
    public static double weightBaseValue;
    public static double weightMultiply;
    public static double impactBaseValue;
    public static double impactMultiply;
    public static double stunArmorBaseValue;
    public static double stunArmorMultiply;
    public static double armorNegationBaseValue;
    public static double armorNegationMultiply;
    public static double maxStrikesBaseValue;
    public static double maxStrikesMultiply;
    public static double maxDifficultyCap;
    public static double difficultyIncrement;

//     private static boolean validateItemName(final Object obj)
//     {
//         return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
//     }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        if (event.getConfig().getSpec() == PDConfig.SPEC) {
                disable = DISABLE.get();
                weightMultiply = WEIGHT_MULTIPLY.get();
                impactMultiply = IMPACT_MULTIPLY.get();
                stunArmorMultiply = STUN_ARMOR_MULTIPLY.get();
                armorNegationMultiply = ARMOR_NEGATION_MULTIPLY.get();
                maxStrikesMultiply = MAX_STRIKES_MULTIPLY.get();
                maxDifficultyCap = MAX_DIFFICULTY_CAP.get();
                difficultyIncrement = DIFFICULTY_INCREMENT.get();
                weightBaseValue = WEIGHT_BASE_VALUE.get();
                impactBaseValue = IMPACT_BASE_VALUE.get();
                stunArmorBaseValue = STUN_ARMOR_BASE_VALUE.get();
                armorNegationBaseValue = ARMOR_NEGATION_BASE_VALUE.get();
                maxStrikesBaseValue = MAX_STRIKES_BASE_VALUE.get();
        }
    }
}