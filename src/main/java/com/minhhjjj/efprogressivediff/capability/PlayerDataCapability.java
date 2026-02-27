package com.minhhjjj.efprogressivediff.capability;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import com.minhhjjj.efprogressivediff.config.PDConfig;

public class PlayerDataCapability implements ICapabilitySerializable<CompoundTag> {
    public static Capability<PlayerDataCapability> INSTANCE = CapabilityManager.get(new CapabilityToken<PlayerDataCapability>() {});
    private final LazyOptional<PlayerDataCapability> holder = LazyOptional.of(() -> this);
    
    private double difficulty = 0;
    private int tickCounter = 0;
    private int debugCounter = 0;

    public PlayerDataCapability() {
    }

    public double getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(double difficulty) {
        if(Double.compare(this.difficulty, difficulty) != 0) {
            this.difficulty = difficulty;
            this.difficulty = Mth.clamp(this.difficulty, 0d, PDConfig.maxDifficultyCap);
        }
    }

    public void addDifficulty(double amount) {
        setDifficulty(this.difficulty + amount);
    }

    public void copyFrom(PlayerDataCapability source) {
        this.difficulty = source.difficulty;
    }

    public void tick() {
        tickCounter++;
        if(tickCounter >= 20) {
            addDifficulty(PDConfig.difficultyIncrement);
            tickCounter = 0;
        }
    }

    public void debug() {
        debugCounter++;
        if(debugCounter >= 200) {
            System.out.println("Current Difficulty: " + difficulty);
            debugCounter = 0;
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("difficulty", difficulty);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (nbt.contains("difficulty")) {
            difficulty = nbt.getDouble("difficulty");
        }
    }

	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction arg1) {
		return INSTANCE.orEmpty(cap, this.holder);
	}
    
}
