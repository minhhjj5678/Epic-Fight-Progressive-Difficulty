package com.minhhjjj.efprogressivediff.data;

import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class DifficultyModifier {
    public static final String FILE = "difficulty";
    public final boolean disabled;
    public final double maxDifficulty;
    public final double difficultyIncrement;
    public final double afkIncrement;
    public final int afkTime;
    public final double wakeupIncrement;
    public final double respawnIncrement;
    public final double hostileIncrement;
    public final double neutralIncrement;
    public final List<MobIncrement> mobIncrements = new ArrayList<>();

    public static class MobIncrement {
        public final double increment;
        public final List<ResourceLocation> mobs = new ArrayList<>();
        public MobIncrement(double increment, List<ResourceLocation> mobs) {
            this.increment = increment;
            this.mobs.addAll(mobs);
        }
    }

    public DifficultyModifier(
            boolean disabled,
            double maxDifficulty,
            double difficultyIncrement,
            double afkIncrement,
            int afkTime,
            double wakeupIncrement,
            double respawnIncrement,
            double hostileIncrement,
            double neutralIncrement,
            List<MobIncrement> mobIncrements
    ) {
        this.disabled = disabled;
        this.maxDifficulty = maxDifficulty;
        this.difficultyIncrement = difficultyIncrement;
        this.afkIncrement = afkIncrement;
        this.afkTime = afkTime;
        this.wakeupIncrement = wakeupIncrement;
        this.respawnIncrement = respawnIncrement;
        this.hostileIncrement = hostileIncrement;
        this.neutralIncrement = neutralIncrement;
        this.mobIncrements.addAll(mobIncrements);
    }
}
