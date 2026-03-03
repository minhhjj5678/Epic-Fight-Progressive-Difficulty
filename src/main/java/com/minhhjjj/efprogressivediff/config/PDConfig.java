package com.minhhjjj.efprogressivediff.config;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import com.minhhjjj.efprogressivediff.EFProgressiveDiff;

@EventBusSubscriber(modid = EFProgressiveDiff.MODID)
public class PDConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.DoubleValue MAX_DIFFICULTY_CAP;
    private static final ModConfigSpec.DoubleValue DIFFICULTY_INCREMENT;
    private static final ModConfigSpec.DoubleValue AFK_INCREMENT;
    private static final ModConfigSpec.IntValue AFK_TIME;
    private static final ModConfigSpec.DoubleValue WAKE_UP_INCREMENT;
    private static final ModConfigSpec.DoubleValue RESPAWN_INCREMENT;
    private static final ModConfigSpec.DoubleValue HOSTILE_INCREMENT;
    private static final ModConfigSpec.DoubleValue NEUTRAL_INCREMENT;
    private static final ModConfigSpec.DoubleValue GROUP_BONUS;
    private static final ModConfigSpec.DoubleValue OVERWORLD_BONUS;
    private static final ModConfigSpec.DoubleValue NETHER_BONUS;
    private static final ModConfigSpec.DoubleValue THEEND_BONUS;
    private static final ModConfigSpec.DoubleValue OTHER_BONUS;

    private static final ModConfigSpec.DoubleValue WEIGHT_BASE_VALUE;
    private static final ModConfigSpec.DoubleValue WEIGHT_MULTIPLY;

    private static final ModConfigSpec.DoubleValue IMPACT_BASE_VALUE;
    private static final ModConfigSpec.DoubleValue IMPACT_MULTIPLY;

    private static final ModConfigSpec.DoubleValue STUN_ARMOR_BASE_VALUE;
    private static final ModConfigSpec.DoubleValue STUN_ARMOR_MULTIPLY;

    private static final ModConfigSpec.DoubleValue ARMOR_NEGATION_BASE_VALUE;
    private static final ModConfigSpec.DoubleValue ARMOR_NEGATION_MULTIPLY;

    private static final ModConfigSpec.DoubleValue MAX_STRIKES_BASE_VALUE;
    private static final ModConfigSpec.DoubleValue MAX_STRIKES_MULTIPLY;

    static {
        BUILDER.comment("General settings")
                .push("General");
                BUILDER.comment("Settings related to difficulty calculation and increments.")
                .push("Difficulty");
                        // DIFFICULTY
                MAX_DIFFICULTY_CAP = BUILDER
                        .defineInRange("maxDifficultyCap", 100, 0d, Double.MAX_VALUE);

                DIFFICULTY_INCREMENT = BUILDER
                        .comment("The amount of difficulty added per tick. This is the main way difficulty increases, and is applied when the player is active.")
                        .defineInRange("difficultyIncrement", 0.00083d, 0d, Double.MAX_VALUE);

                AFK_INCREMENT = BUILDER
                        .comment("The amount of difficulty added per tick when the player is AFK. This is applied when the player is idle for more than afkTime seconds.")
                        .defineInRange("afkIncrement", 0.000083d, 0d, Double.MAX_VALUE);

                AFK_TIME = BUILDER
                        .comment("The amount of idle time in seconds before a player is considered AFK.")
                        .defineInRange("afkTime", 120, 1, Integer.MAX_VALUE);

                WAKE_UP_INCREMENT = BUILDER
                        .comment("The amount of difficulty added when the player wakes up from sleep.")
                        .defineInRange("wakeUpIncrement", 0.0d, -Double.MAX_VALUE, Double.MAX_VALUE);

                RESPAWN_INCREMENT = BUILDER
                        .comment("The amount of difficulty added when the player respawns. Can be negative to reduce difficulty on death.")
                        .defineInRange("respawnIncrement", -5d, -Double.MAX_VALUE, Double.MAX_VALUE);

                HOSTILE_INCREMENT = BUILDER
                        .comment("The amount of difficulty added when the player kills a hostile mob.")
                        .defineInRange("hostileIncrement", 0.0d, -Double.MAX_VALUE, Double.MAX_VALUE);

                NEUTRAL_INCREMENT = BUILDER
                        .comment("The amount of difficulty added when the player kills a neutral mob.")
                        .defineInRange("neutralIncrement", 0.0d, -Double.MAX_VALUE, Double.MAX_VALUE);
                BUILDER.pop();

                BUILDER.comment("Settings related to difficulty bonuses based on player grouping and dimension.")
                .push("Difficulty Bonus");
                GROUP_BONUS = BUILDER
                        .comment("Additional difficulty multiplier based on the number of nearby players. For example, a value of 0.1 means each additional player increases the average difficulty by 10%.")
                        .defineInRange("groupBonus", 0.05, 0.0d, Double.MAX_VALUE);

                OVERWORLD_BONUS = BUILDER
                        .comment("Additional difficulty multiplier for being in the Overworld. For example, a value of 0.1 means the difficulty is increased by 10% in the Overworld.")
                        .defineInRange("overworldBonus", 0.0d, 0.0d, Double.MAX_VALUE);

                NETHER_BONUS = BUILDER
                        .comment("Additional difficulty multiplier for being in the Nether. For example, a value of 0.1 means the difficulty is increased by 10% in the Nether.")
                        .defineInRange("netherBonus", 0.1d, 0.0d, Double.MAX_VALUE);

                THEEND_BONUS = BUILDER
                        .comment("Additional difficulty multiplier for being in The End. For example, a value of 0.15 means the difficulty is increased by 15% in The End.")
                        .defineInRange("theendBonus", 0.15d, 0.0d, Double.MAX_VALUE);

                OTHER_BONUS = BUILDER
                        .comment("Additional difficulty multiplier for being in other dimensions (modded dimensions). For example, a value of 0.1 means the difficulty is increased by 10% in other dimensions.")
                        .defineInRange("otherBonus", 0.1d, 0.0d, Double.MAX_VALUE);
                BUILDER.pop();
        BUILDER.pop();

        BUILDER.comment("Final attribute value = Original Attribute Value + Base Value * (1 + multiply * difficulty)")
                .push("Attribute");

                BUILDER.push("Weight");
                WEIGHT_BASE_VALUE = BUILDER
                        .comment("Base value added to weight at difficulty 0.")
                        .defineInRange("weightBaseValue", 10, 0.0d, Double.MAX_VALUE);

                WEIGHT_MULTIPLY = BUILDER
                        .comment("How much the added weight increases per difficulty. For example, a value of 0.05 means the added weight increases by 5% per difficulty.")
                        .defineInRange("weightMultiply", 0.07, 0, 1);
                BUILDER.pop();

                BUILDER.push("Impact");
                IMPACT_BASE_VALUE = BUILDER
                        .comment("Base value added to impact at difficulty 0.")
                        .defineInRange("impactBaseValue", 1, 0.0d, Double.MAX_VALUE);

                IMPACT_MULTIPLY = BUILDER
                        .comment("How much the added impact increases per difficulty. For example, a value of 0.05 means the added impact increases by 5% per difficulty.")
                        .defineInRange("impactMultiply", 0.05, 0, 1);
                BUILDER.pop();

                BUILDER.push("Stun Armor");
                STUN_ARMOR_BASE_VALUE = BUILDER
                        .comment("Base value added to stun armor at difficulty 0.")
                        .defineInRange("stunArmorBaseValue", 3, 0.0d, Double.MAX_VALUE);

                STUN_ARMOR_MULTIPLY = BUILDER
                        .comment("How much the added stun armor increases per difficulty. For example, a value of 0.05 means the added stun armor increases by 5% per difficulty.")
                        .defineInRange("stunArmorMultiply", 0.05, 0, 1);
                BUILDER.pop();

                BUILDER.push("Armor Negation");
                ARMOR_NEGATION_BASE_VALUE = BUILDER
                        .comment("Base value added to armor negation at difficulty 0.")
                        .defineInRange("armorNegationBaseValue", 5, 0.0d, Double.MAX_VALUE);

                ARMOR_NEGATION_MULTIPLY = BUILDER
                        .comment("How much the added armor negation increases per difficulty. For example, a value of 0.05 means the added armor negation increases by 5% per difficulty.")
                        .defineInRange("armorNegationMultiply", 0.05, 0, 1);
                BUILDER.pop();

                BUILDER.push("Max Strikes");
                MAX_STRIKES_BASE_VALUE = BUILDER
                        .comment("Base value added to max strikes at difficulty 0.")
                        .defineInRange("maxStrikesBaseValue", 1, 0.0d, Double.MAX_VALUE);

                MAX_STRIKES_MULTIPLY = BUILDER
                        .comment("How much the added max strikes increases per difficulty. For example, a value of 0.03 means the added max strikes increases by 3% per difficulty.")
                        .defineInRange("maxStrikesMultiply", 0.03, 0, 1);
                BUILDER.pop();
        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static double maxDifficultyCap;
    public static double difficultyIncrement;
    public static double afkIncrement;
    public static int afkTime;
    public static double wakeUpIncrement;
    public static double respawnIncrement;
    public static double hostileIncrement;
    public static double neutralIncrement;
    public static double groupBonus;
    public static double overworldBonus;
    public static double netherBonus;
    public static double theendBonus;
    public static double otherBonus;

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

//     private static boolean validateItemName(final Object obj)
//     {
//         return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
//     }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        if (event.getConfig().getSpec() == PDConfig.SPEC) {
                maxDifficultyCap = MAX_DIFFICULTY_CAP.get();
                difficultyIncrement = DIFFICULTY_INCREMENT.get();
                afkIncrement = AFK_INCREMENT.get();
                afkTime = AFK_TIME.get();
                wakeUpIncrement = WAKE_UP_INCREMENT.get();
                respawnIncrement = RESPAWN_INCREMENT.get();
                hostileIncrement = HOSTILE_INCREMENT.get();
                neutralIncrement = NEUTRAL_INCREMENT.get();
                groupBonus = GROUP_BONUS.get();
                overworldBonus = OVERWORLD_BONUS.get();
                netherBonus = NETHER_BONUS.get();
                theendBonus = THEEND_BONUS.get();
                otherBonus = OTHER_BONUS.get();

                weightMultiply = WEIGHT_MULTIPLY.get();
                impactMultiply = IMPACT_MULTIPLY.get();
                stunArmorMultiply = STUN_ARMOR_MULTIPLY.get();
                armorNegationMultiply = ARMOR_NEGATION_MULTIPLY.get();
                maxStrikesMultiply = MAX_STRIKES_MULTIPLY.get();
                weightBaseValue = WEIGHT_BASE_VALUE.get();
                impactBaseValue = IMPACT_BASE_VALUE.get();
                stunArmorBaseValue = STUN_ARMOR_BASE_VALUE.get();
                armorNegationBaseValue = ARMOR_NEGATION_BASE_VALUE.get();
                maxStrikesBaseValue = MAX_STRIKES_BASE_VALUE.get();
        }
    }
}