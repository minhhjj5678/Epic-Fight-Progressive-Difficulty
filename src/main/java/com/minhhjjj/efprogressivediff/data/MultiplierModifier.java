package com.minhhjjj.efprogressivediff.data;

import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class MultiplierModifier {
    public static final String FILE = "multipliers";
    public final boolean disabled;
    public final double expBonus;
    public final double groupBonus;
    public final int groupRadius;
    public final List<DimensionMultiplier> dimensionMultipliers =  new ArrayList<>();
    public final List<BiomeMultiplier> biomeMultipliers  =  new ArrayList<>();

    public MultiplierModifier(boolean disabled, double expBonus, double groupBonus, int groupRadius, List<DimensionMultiplier> dimensionMultipliers, List<BiomeMultiplier> biomeMultipliers) {
        this.disabled = disabled;
        this.expBonus = expBonus;
        this.groupBonus = groupBonus;
        this.groupRadius = groupRadius;
        this.dimensionMultipliers.addAll(dimensionMultipliers);
        this.biomeMultipliers.addAll(biomeMultipliers);
    }

    public static class DimensionMultiplier {
        public final double dimensionScale;
        public final List<ResourceLocation> dimensions = new ArrayList<>();
        public DimensionMultiplier(double dimensionScale, List<ResourceLocation> dimensions) {
            this.dimensionScale = dimensionScale;
            this.dimensions.addAll(dimensions);
        }
    }

    public static class BiomeMultiplier {
        public final double biomeScale;
        public final List<ResourceLocation> biomes = new ArrayList<>();
        public BiomeMultiplier(double biomeScale, List<ResourceLocation> biomes) {
            this.biomeScale = biomeScale;
            this.biomes.addAll(biomes);
        }
    }

}