package com.minhhjjj.efprogressivediff.attachment;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import javax.annotation.Nonnull;

import com.minhhjjj.efprogressivediff.EFProgressiveDiff;
import com.minhhjjj.efprogressivediff.config.PDConfig;
import com.minhhjjj.efprogressivediff.event.MobEvents;
import com.minhhjjj.efprogressivediff.network.DifficultySyncPacket;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.netty.buffer.ByteBuf;

@SuppressWarnings("null")
public class PlayerDataAttachment implements INBTSerializable<CompoundTag> {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES ,EFProgressiveDiff.MODID);
    public static final String NAME = "player_difficulty";
    public static final Codec<PlayerDataAttachment> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.DOUBLE.fieldOf("difficulty").forGetter(PlayerDataAttachment::getDifficulty),
        Codec.DOUBLE.fieldOf("aroundDifficulty").forGetter(PlayerDataAttachment::getAroundDifficulty)
    ).apply(instance, (diff, aroundDiff) -> new PlayerDataAttachment(diff, aroundDiff)));
    public static final StreamCodec<ByteBuf ,PlayerDataAttachment> streamCodec = ByteBufCodecs.fromCodec(CODEC);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerDataAttachment>> PLAYER_DIFFICULTY = 
        ATTACHMENT_TYPES.register(NAME, () -> AttachmentType.builder(() -> new PlayerDataAttachment())
            .serialize(PlayerDataAttachment.CODEC)
            .copyOnDeath()
        .build());

    public static final double DEVATION_THRESHOLD = 0.1;

    private double difficulty = 0;
    private double aroundDifficulty = 0;
    private int tickCounter = 0;
    private int debugCounter = 0;
    private double lastSentDifficulty = 0;

    private BlockPos lastPos;
    private int idleTime = 0;

    public PlayerDataAttachment() {
    }

    public PlayerDataAttachment(double difficulty, double aroundDifficulty) {
        this.difficulty = difficulty;
        this.aroundDifficulty = aroundDifficulty;
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

    public void copyFrom(PlayerDataAttachment source) {
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
                PacketDistributor.sendToPlayer(player, msg);
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
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("difficulty", difficulty);
        tag.putDouble("aroundDifficulty", aroundDifficulty);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        if (nbt.contains("difficulty")) {
            difficulty = nbt.getDouble("difficulty");
            aroundDifficulty = nbt.getDouble("aroundDifficulty");
        }
    }

    public static @Nonnull AttachmentType<PlayerDataAttachment> type() {
        return PlayerDataAttachment.PLAYER_DIFFICULTY.get();
    }
}
