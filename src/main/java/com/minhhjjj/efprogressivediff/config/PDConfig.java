package com.minhhjjj.efprogressivediff.config;

import com.minhhjjj.efprogressivediff.data.AttributeModifier;
import com.minhhjjj.efprogressivediff.data.DataListener;
import com.minhhjjj.efprogressivediff.data.DifficultyModifier;
import com.minhhjjj.efprogressivediff.data.MultiplierModifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import com.minhhjjj.efprogressivediff.EFProgressiveDiff;

@Mod.EventBusSubscriber(modid = EFProgressiveDiff.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PDConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.DoubleValue MAX_DIFFICULTY_CAP;
    private static final ForgeConfigSpec.DoubleValue DIFFICULTY_INCREMENT;
    private static final ForgeConfigSpec.DoubleValue AFK_INCREMENT;
    private static final ForgeConfigSpec.IntValue AFK_TIME;
    private static final ForgeConfigSpec.DoubleValue WAKE_UP_INCREMENT;
    private static final ForgeConfigSpec.DoubleValue RESPAWN_INCREMENT;
    private static final ForgeConfigSpec.DoubleValue HOSTILE_INCREMENT;
    private static final ForgeConfigSpec.DoubleValue NEUTRAL_INCREMENT;
    private static final ForgeConfigSpec.DoubleValue EXP_BONUS;
    private static final ForgeConfigSpec.DoubleValue GROUP_BONUS;
    private static final ForgeConfigSpec.IntValue GROUP_RADIUS;
    private static final ForgeConfigSpec.DoubleValue OVERWORLD_BONUS;
    private static final ForgeConfigSpec.DoubleValue NETHER_BONUS;
    private static final ForgeConfigSpec.DoubleValue THEEND_BONUS;
    private static final ForgeConfigSpec.DoubleValue OTHER_BONUS;

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

    private static final ForgeConfigSpec.BooleanValue IS_V120_FIXED;

    static {
        BUILDER.comment("General settings")
                .push("General");
                BUILDER.comment("Settings related to difficulty calculation and increments.")
                .push("Difficulty");
                        // DIFFICULTY
                MAX_DIFFICULTY_CAP = BUILDER
                        .defineInRange("maxDifficultyCap", 100, 0d, Double.MAX_VALUE);

                DIFFICULTY_INCREMENT = BUILDER
                        .comment("The amount of difficulty added per second. This is the main way difficulty increases, and is applied when the player is active.")
                        .defineInRange("difficultyIncrement", 0.00083d, -Double.MAX_VALUE, Double.MAX_VALUE);

                AFK_INCREMENT = BUILDER
                        .comment("The amount of difficulty added per second when the player is AFK. This is applied when the player is idle for more than afkTime seconds.")
                        .defineInRange("afkIncrement", 0.000083d, -Double.MAX_VALUE, Double.MAX_VALUE);

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
                        .defineInRange("hostileIncrement", 0.003d, -Double.MAX_VALUE, Double.MAX_VALUE);

                NEUTRAL_INCREMENT = BUILDER
                        .comment("The amount of difficulty added when the player kills a neutral mob.")
                        .defineInRange("neutralIncrement", 0.0d, -Double.MAX_VALUE, Double.MAX_VALUE);
                BUILDER.pop();

                BUILDER.comment("Settings related to difficulty bonuses based on player grouping and dimension.")
                .push("Difficulty Bonus");
                EXP_BONUS = BUILDER
                        .comment("The experience multiplier per difficulty point. Formula for dropped exp: base_exp * (1 + difficulty * exp_bonus).")
                        .defineInRange("expBonus", 0.005, 0, Double.MAX_VALUE);
                GROUP_BONUS = BUILDER
                        .comment("Additional difficulty multiplier based on the number of nearby players. For example, a value of 0.1 means each additional player increases the average difficulty by 10%.")
                        .defineInRange("groupBonus", 0.05, 0.0d, Double.MAX_VALUE);

                GROUP_RADIUS = BUILDER
                        .comment("The radius around the newly spawned mob (in blocks) to check for nearby players for the group bonus.")
                        .defineInRange("groupRadius", 64, 0, Integer.MAX_VALUE);

                OVERWORLD_BONUS = BUILDER
                        .comment("Additional difficulty multiplier for being in the Overworld. For example, a value of 0.1 means the difficulty is increased by 10% in the Overworld.")
                        .defineInRange("overworldBonus", 0.0d, 0.0d, Double.MAX_VALUE);

                NETHER_BONUS = BUILDER
                        .comment("Additional difficulty multiplier for being in the Nether. For example, a value of 0.1 means the difficulty is increased by 10% in the Nether.")
                        .defineInRange("netherBonus", 0.5d, 0.0d, Double.MAX_VALUE);

                THEEND_BONUS = BUILDER
                        .comment("Additional difficulty multiplier for being in The End. For example, a value of 0.15 means the difficulty is increased by 15% in The End.")
                        .defineInRange("theendBonus", 1.25d, 0.0d, Double.MAX_VALUE);

                OTHER_BONUS = BUILDER
                        .comment("Additional difficulty multiplier for being in other dimensions (modded dimensions). For example, a value of 0.1 means the difficulty is increased by 10% in other dimensions.")
                        .defineInRange("otherBonus", 0.5d, 0.0d, Double.MAX_VALUE);
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
                        .defineInRange("weightMultiply", 0.1, 0, 1);
                BUILDER.pop();

                BUILDER.push("Impact");
                IMPACT_BASE_VALUE = BUILDER
                        .comment("Base value added to impact at difficulty 0.")
                        .defineInRange("impactBaseValue", 1, 0.0d, Double.MAX_VALUE);

                IMPACT_MULTIPLY = BUILDER
                        .comment("How much the added impact increases per difficulty. For example, a value of 0.05 means the added impact increases by 5% per difficulty.")
                        .defineInRange("impactMultiply", 0.25, 0, 1);
                BUILDER.pop();

                BUILDER.push("Stun Armor");
                STUN_ARMOR_BASE_VALUE = BUILDER
                        .comment("Base value added to stun armor at difficulty 0.")
                        .defineInRange("stunArmorBaseValue", 3, 0.0d, Double.MAX_VALUE);

                STUN_ARMOR_MULTIPLY = BUILDER
                        .comment("How much the added stun armor increases per difficulty. For example, a value of 0.05 means the added stun armor increases by 5% per difficulty.")
                        .defineInRange("stunArmorMultiply", 0.25, 0, 1);
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

                IS_V120_FIXED = BUILDER.comment("Internal flag for bug fixes. Do not modify.").define("is_v120_fixed", false);
        BUILDER.pop();
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    private static double maxDifficultyCap;
    private static double difficultyIncrement;
    private static double afkIncrement;
    private static int afkTime;
    private static double wakeUpIncrement;
    private static double respawnIncrement;
    private static double hostileIncrement;
    private static double neutralIncrement;

    private static double expBonus;
    private static double groupBonus;
    private static int groupRadius;
    private static double overworldBonus;
    private static double netherBonus;
    private static double theendBonus;
    private static double otherBonus;

    private static double weightBaseValue;
    private static double weightMultiply;
    private static double impactBaseValue;
    private static double impactMultiply;
    private static double stunArmorBaseValue;
    private static double stunArmorMultiply;
    private static double armorNegationBaseValue;
    private static double armorNegationMultiply;
    private static double maxStrikesBaseValue;
    private static double maxStrikesMultiply;

//     private static boolean validateItemName(final Object obj)
//     {
//         return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
//     }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        if (event.getConfig().getSpec() == PDConfig.SPEC) {
                fixV120(event);
                maxDifficultyCap = MAX_DIFFICULTY_CAP.get();
                difficultyIncrement = DIFFICULTY_INCREMENT.get();
                afkIncrement = AFK_INCREMENT.get();
                afkTime = AFK_TIME.get();
                wakeUpIncrement = WAKE_UP_INCREMENT.get();
                respawnIncrement = RESPAWN_INCREMENT.get();
                hostileIncrement = HOSTILE_INCREMENT.get();
                neutralIncrement = NEUTRAL_INCREMENT.get();

                expBonus = EXP_BONUS.get();
                groupBonus = GROUP_BONUS.get();
                groupRadius = GROUP_RADIUS.get();
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

    private static final double WRONG_VALUE = 0.00005d;
    private static final double FIX_VALUE = 0.00083d;
    private static void fixV120(ModConfigEvent event) {
        if (!IS_V120_FIXED.get()) {
            IS_V120_FIXED.set(true);
            if (PDConfig.DIFFICULTY_INCREMENT.get().equals(WRONG_VALUE)) {
                PDConfig.DIFFICULTY_INCREMENT.set(FIX_VALUE);
            }
            event.getConfig().save();
        }
    }

    private static final ResourceLocation RL_DIFF = ResourceLocation.fromNamespaceAndPath(EFProgressiveDiff.MODID, DifficultyModifier.FILE);
    private static final ResourceLocation RL_MULTIPLIERS = ResourceLocation.fromNamespaceAndPath(EFProgressiveDiff.MODID, MultiplierModifier.FILE);
    private static final ResourceLocation RL_ATTRIBUTE = ResourceLocation.fromNamespaceAndPath(EFProgressiveDiff.MODID, AttributeModifier.FILE);
    static boolean isDifficultyPackLoaded() {
        return DataListener.DIFFICULTY_MODIFIER.containsKey(RL_DIFF)
                && !DataListener.DIFFICULTY_MODIFIER.get(RL_DIFF).disabled;
    }

    static boolean isMultiplierPackLoaded() {
        return DataListener.MULTIPLIER_MODIFIER.containsKey(RL_MULTIPLIERS)
                && !DataListener.MULTIPLIER_MODIFIER.get(RL_MULTIPLIERS).disabled;
    }

    static boolean isAttributePackLoaded() {
        return DataListener.ATTRIBUTE_MODIFIER.containsKey(RL_ATTRIBUTE)
                && !DataListener.ATTRIBUTE_MODIFIER.get(RL_ATTRIBUTE).disabled;
    }

    public static double getMaxDiff() {
        return isDifficultyPackLoaded() ? DataListener.DIFFICULTY_MODIFIER.get(RL_DIFF).maxDifficulty : maxDifficultyCap;
    }

    public static double getDifficultyIncrement() {
        return isDifficultyPackLoaded() ? DataListener.DIFFICULTY_MODIFIER.get(RL_DIFF).difficultyIncrement : difficultyIncrement;
    }

    public static int getAfkTime() {
        return isDifficultyPackLoaded() ? DataListener.DIFFICULTY_MODIFIER.get(RL_DIFF).afkTime : afkTime;
    }

    public static double getAfkIncrement() {
        return isDifficultyPackLoaded() ? DataListener.DIFFICULTY_MODIFIER.get(RL_DIFF).afkIncrement : afkIncrement;
    }

    public static double getWakeupIncrement() {
        return isDifficultyPackLoaded() ? DataListener.DIFFICULTY_MODIFIER.get(RL_DIFF).wakeupIncrement : wakeUpIncrement;
    }

    public static double getRespawnIncrement() {
        return isDifficultyPackLoaded() ? DataListener.DIFFICULTY_MODIFIER.get(RL_DIFF).respawnIncrement : respawnIncrement;
    }

    public static double getHostileIncrement() {
        return isDifficultyPackLoaded() ? DataListener.DIFFICULTY_MODIFIER.get(RL_DIFF).hostileIncrement : hostileIncrement;
    }

    public static double getNeutralIncrement() {
        return isDifficultyPackLoaded() ? DataListener.DIFFICULTY_MODIFIER.get(RL_DIFF).neutralIncrement : neutralIncrement;
    }

    public static double getMobIncrement(ResourceLocation rl) {
        if (isDifficultyPackLoaded() && rl != null) {
            for (DifficultyModifier.MobIncrement mobModifier : DataListener.DIFFICULTY_MODIFIER.get(RL_DIFF).mobIncrements) {
                if (mobModifier.mobs.contains(rl)) {
                    return mobModifier.increment;
                }
            }
        }
        return 0.0d;
    }

    public static double getExpBonus() {
        return isMultiplierPackLoaded() ? DataListener.MULTIPLIER_MODIFIER.get(RL_MULTIPLIERS).expBonus : expBonus;
    }

    public static double getGroupBonus() {
        return isMultiplierPackLoaded() ? DataListener.MULTIPLIER_MODIFIER.get(RL_MULTIPLIERS).groupBonus : groupBonus;
    }

    public static int getGroupRadius() {
        return isMultiplierPackLoaded() ? DataListener.MULTIPLIER_MODIFIER.get(RL_MULTIPLIERS).groupRadius : groupRadius;
    }

    public static double getDimensionBonus(ResourceLocation rl) {
        if (rl == null) return 0.0d;
        if (isMultiplierPackLoaded()) {
            double scaleFactor = 0.0;
            boolean found = false;
            for (MultiplierModifier.DimensionMultiplier dim : DataListener.MULTIPLIER_MODIFIER.get(RL_MULTIPLIERS).dimensionMultipliers) {
                if (dim.dimensions.contains(rl)) {
                    if (found) {
                        DataListener.LOGGER.warn("Multiple scales detected for multiplier {}, using the last scale from the file", rl.toString());
                    }
                    scaleFactor = dim.dimensionScale;
                    found = true;
                }
            }
            return scaleFactor;
        }

        if (rl.equals(Level.OVERWORLD.location())) {
            return overworldBonus;
        } else if (rl.equals(Level.NETHER.location())) {
            return netherBonus;
        } else if (rl.equals(Level.END.location())) {
            return theendBonus;
        } else {
            return otherBonus;
        }
    }

    public static double getBiomeBonus(ResourceLocation rl) {
        double scaleFactor = 0.0;
        if (isMultiplierPackLoaded() && rl != null) {
            boolean found = false;
            for (MultiplierModifier.BiomeMultiplier bio : DataListener.MULTIPLIER_MODIFIER.get(RL_MULTIPLIERS).biomeMultipliers) {
                if (bio.biomes.contains(rl)) {
                    if (found) {
                        DataListener.LOGGER.warn("Multiple scales detected for multiplier {}, using the last scale from the file", rl.toString());
                    }
                    scaleFactor = bio.biomeScale;
                    found = true;
                }
            }
        }
        return scaleFactor;
    }

    public static double getWeightBaseValue() {
        return isAttributePackLoaded() ? DataListener.ATTRIBUTE_MODIFIER.get(RL_ATTRIBUTE).weightBase : weightBaseValue;
    }

    public static double getWeightMultiplierValue() {
        return isAttributePackLoaded() ? DataListener.ATTRIBUTE_MODIFIER.get(RL_ATTRIBUTE).weightMultiply : weightMultiply;
    }

    public static double getImpactBaseValue() {
        return isAttributePackLoaded() ? DataListener.ATTRIBUTE_MODIFIER.get(RL_ATTRIBUTE).impactBase : impactBaseValue;
    }

    public static double getImpactMultiplierValue() {
        return isAttributePackLoaded() ? DataListener.ATTRIBUTE_MODIFIER.get(RL_ATTRIBUTE).impactMultiply : impactMultiply;
    }

    public static double getStunArmorBaseValue() {
        return isAttributePackLoaded() ? DataListener.ATTRIBUTE_MODIFIER.get(RL_ATTRIBUTE).stunArmorBase : stunArmorBaseValue;
    }

    public static double getStunArmorMultiplierValue() {
        return isAttributePackLoaded() ? DataListener.ATTRIBUTE_MODIFIER.get(RL_ATTRIBUTE).stunArmorMultiply : stunArmorMultiply;
    }

    public static double getArmorNegationBaseValue() {
        return isAttributePackLoaded() ? DataListener.ATTRIBUTE_MODIFIER.get(RL_ATTRIBUTE).armorNegationBase : armorNegationBaseValue;
    }

    public static double getArmorNegationMultiplierValue() {
        return isAttributePackLoaded() ? DataListener.ATTRIBUTE_MODIFIER.get(RL_ATTRIBUTE).armorNegationMultiply : armorNegationMultiply;
    }

    public static double getMaxStrikesBaseValue() {
        return isAttributePackLoaded() ? DataListener.ATTRIBUTE_MODIFIER.get(RL_ATTRIBUTE).maxStrikesBase : maxStrikesBaseValue;
    }

    public static double getMaxStrikesMultiplierValue() {
        return isAttributePackLoaded() ? DataListener.ATTRIBUTE_MODIFIER.get(RL_ATTRIBUTE).maxStrikesMultiply : maxStrikesMultiply;
    }
}