package com.minhhjjj.efprogressivediff.data;

public class DifficultyModifier {
    public static final String FILE = "difficulty";
    public final boolean disabled;
    public final double maxDifficulty;
    public final double difficultyIncrement;
    public final double afkIncrement;
    public final int afkTime;
    public final double wakeupIncrement;
    public final double respawnIncrement;
    public final double groupBonus;
    public final int groupRadius;

    public DifficultyModifier(
            boolean disabled,
            double maxDifficulty,
            double difficultyIncrement,
            double afkIncrement,
            int afkTime,
            double wakeupIncrement,
            double respawnIncrement,
            double groupBonus,
            int groupRadius
    ) {
        this.disabled = disabled;
        this.maxDifficulty = maxDifficulty;
        this.difficultyIncrement = difficultyIncrement;
        this.afkIncrement = afkIncrement;
        this.afkTime = afkTime;
        this.wakeupIncrement = wakeupIncrement;
        this.respawnIncrement = respawnIncrement;
        this.groupBonus = groupBonus;
        this.groupRadius = groupRadius;
    }
}
