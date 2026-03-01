package com.minhhjjj.efprogressivediff.capability;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkDirection;

import com.minhhjjj.efprogressivediff.config.PDConfig;
import com.minhhjjj.efprogressivediff.event.MobEvents;
import com.minhhjjj.efprogressivediff.network.DifficultySyncPacket;
import com.minhhjjj.efprogressivediff.network.PacketHandler;

public class PlayerDataCapability implements ICapabilitySerializable<CompoundTag> {
    public static Capability<PlayerDataCapability> INSTANCE = CapabilityManager.get(new CapabilityToken<PlayerDataCapability>() {});
    private final LazyOptional<PlayerDataCapability> holder = LazyOptional.of(() -> this);
    public static final double DEVATION_THRESHOLD = 0.1;

    private double difficulty = 0;
    private double aroundDifficulty = 0;
    private int tickCounter = 0;
    private int debugCounter = 0;
    private double lastSentDifficulty = 0;

    private BlockPos lastPos;
    private int idleTime = 0;

    public PlayerDataCapability() {
    }

    public double getDifficulty() {
        return difficulty;
    }

    public double getAroundDifficulty() {
        return aroundDifficulty;
    }

    public void setAroundDifficulty(ServerPlayer player) {
        aroundDifficulty = MobEvents.getDifficultyAround(player);
    }

    public void setAroundDifficulty(double aroundDifficulty) {
        this.aroundDifficulty = aroundDifficulty;
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
        this.aroundDifficulty = source.aroundDifficulty;
    }

    public void tick(ServerPlayer player) {
        tickCounter++;
        if(tickCounter >= 20) {
            BlockPos currentPos = player.blockPosition();
            if (lastPos != null && currentPos.equals(lastPos)) {
                idleTime++;
            } else {
                lastPos = currentPos;
                idleTime = 0;
            }

            if (idleTime <= PDConfig.afkTime) addDifficulty(PDConfig.difficultyIncrement);
            else addDifficulty(PDConfig.afkIncrement);

            setAroundDifficulty(player);
            double diff = Math.abs(lastSentDifficulty - aroundDifficulty);
            if (diff >= DEVATION_THRESHOLD) {
                lastSentDifficulty = aroundDifficulty;
                DifficultySyncPacket msg = new DifficultySyncPacket(this.difficulty, aroundDifficulty);
                PacketHandler.channel.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
            }
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
        tag.putDouble("aroundDifficulty", aroundDifficulty);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (nbt.contains("difficulty")) {
            difficulty = nbt.getDouble("difficulty");
            aroundDifficulty = nbt.getDouble("aroundDifficulty");
        }
    }

	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction arg1) {
		return INSTANCE.orEmpty(cap, this.holder);
	}
    
}
