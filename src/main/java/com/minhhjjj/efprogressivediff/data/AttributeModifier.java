package com.minhhjjj.efprogressivediff.data;

public class AttributeModifier {
    public static final String FILE = "attributes";
    public final boolean disabled;
    public final double weightBase;
    public final double weightMultiply;
    public final double impactBase;
    public final double impactMultiply;
    public final double stunArmorBase;
    public final double stunArmorMultiply;
    public final double armorNegationBase;
    public final double armorNegationMultiply;
    public final double maxStrikesBase;
    public final double maxStrikesMultiply;

    public AttributeModifier(boolean disabled,
                             double weightBase,
                             double weightMultiply,
                             double impactBase,
                             double impactMultiply,
                             double stunArmorBase,
                             double stunArmorMultiply,
                             double armorNegationBase,
                             double armorNegationMultiply,
                             double maxStrikesBase,
                             double maxStrikesMultiply) {
        this.disabled = disabled;
        this.weightBase = weightBase;
        this.weightMultiply = weightMultiply;
        this.impactBase = impactBase;
        this.impactMultiply = impactMultiply;
        this.stunArmorBase = stunArmorBase;
        this.stunArmorMultiply = stunArmorMultiply;
        this.armorNegationBase = armorNegationBase;
        this.armorNegationMultiply = armorNegationMultiply;
        this.maxStrikesBase = maxStrikesBase;
        this.maxStrikesMultiply = maxStrikesMultiply;
    }
}