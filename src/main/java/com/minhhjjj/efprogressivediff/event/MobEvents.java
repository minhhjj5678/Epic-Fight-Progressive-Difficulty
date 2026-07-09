package com.minhhjjj.efprogressivediff.event;

import net.minecraft.world.entity.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

import java.util.List;
import java.util.Optional;

import com.minhhjjj.efprogressivediff.EFProgressiveDiff;
import com.minhhjjj.efprogressivediff.capability.PlayerDataCapability;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;

import com.minhhjjj.efprogressivediff.config.PDConfig;

@Mod.EventBusSubscriber(modid = EFProgressiveDiff.MODID)
public class MobEvents {
	private static final String WEIGHT_INIT_TAG = EFProgressiveDiff.MODID + ":weight_initialized";
	
	@SuppressWarnings("null")
	@SubscribeEvent
	public static void onSpawn(MobSpawnEvent.FinalizeSpawn event) {
		Mob entity = event.getEntity();
		if(event.getLevel().isClientSide()) return;
		if(!(entity.level() instanceof ServerLevel)) return;
		
		double averageDifficulty = getDifficultyAround(entity);
		if (averageDifficulty < 0) return;
		applyDifficultyScaling(entity, averageDifficulty);
	}

	public static double getDifficultyAround(Entity entity) {
		return getDifficultyAround(entity, PDConfig.getGroupRadius());
	}

	@SuppressWarnings("null")
	@SubscribeEvent
	public static void onMobFirstTick(LivingTickEvent event) {
		if (!(event.getEntity() instanceof Mob mob)) return;
		if (mob.level().isClientSide()) return;
		if (!(mob.level() instanceof ServerLevel)) return;
		if (mob.getPersistentData().getBoolean(WEIGHT_INIT_TAG)) return;

		AttributeInstance weight = mob.getAttribute(EpicFightAttributes.WEIGHT.get());
		if (weight == null) {
			mob.getPersistentData().putBoolean(WEIGHT_INIT_TAG, true);
			return;
		}

		if (weight.getBaseValue() == 0.0D) {
			double averageDifficulty = Math.max(0.0D, getDifficultyAround(mob));
			applyDifficultyScaling(mob, averageDifficulty);
		}

		mob.getPersistentData().putBoolean(WEIGHT_INIT_TAG, true);
	}

	@SubscribeEvent
	public static void onXpDrop(LivingExperienceDropEvent event) {
		if (!(event.getEntity() instanceof Mob mob)) return;
		if (mob.level().isClientSide()) return;
		if (event.getAttackingPlayer() instanceof ServerPlayer serverPlayer) {
			double exp = event.getDroppedExperience();
			serverPlayer.getCapability(PlayerDataCapability.INSTANCE).ifPresent(cap -> {
				int newEXP = (int) Math.round(exp * (1 + PDConfig.getExpBonus() * cap.getAroundDifficulty()));
				event.setDroppedExperience(Math.max(0, newEXP));
			});
		}
	}

	private static void applyDifficultyScaling(Mob entity, double averageDifficulty) {
		AttributeInstance weight = entity.getAttribute(EpicFightAttributes.WEIGHT.get());
		AttributeInstance impact = entity.getAttribute(EpicFightAttributes.IMPACT.get());
		AttributeInstance stunArmor = entity.getAttribute(EpicFightAttributes.STUN_ARMOR.get());
		AttributeInstance maxStrikes = entity.getAttribute(EpicFightAttributes.MAX_STRIKES.get());
		AttributeInstance armorNegation = entity.getAttribute(EpicFightAttributes.ARMOR_NEGATION.get());
		if(weight == null || impact == null || stunArmor == null || maxStrikes == null || armorNegation == null) return;

		double amount;
		double WEIGHT_BASE_VALUE = PDConfig.getWeightBaseValue();
		double IMPACT_BASE_VALUE = PDConfig.getImpactBaseValue();
		double STUN_ARMOR_BASE_VALUE = PDConfig.getStunArmorBaseValue();
		double MAX_STRIKES_BASE_VALUE = PDConfig.getMaxStrikesBaseValue();
		double ARMOR_NEGATION_BASE_VALUE = PDConfig.getArmorNegationBaseValue();

		amount = impact.getBaseValue() + IMPACT_BASE_VALUE*(1+PDConfig.getImpactMultiplierValue()*averageDifficulty);
		impact.setBaseValue(Math.max(0, amount));
		amount = stunArmor.getBaseValue() + STUN_ARMOR_BASE_VALUE*(1+PDConfig.getStunArmorMultiplierValue()*averageDifficulty);
		stunArmor.setBaseValue(Math.max(0, amount));
		amount = maxStrikes.getBaseValue() + (MAX_STRIKES_BASE_VALUE*(1+PDConfig.getMaxStrikesMultiplierValue()*averageDifficulty));
		maxStrikes.setBaseValue(Math.max(0, amount));
		amount = armorNegation.getBaseValue() + (ARMOR_NEGATION_BASE_VALUE*(1+PDConfig.getArmorNegationMultiplierValue()*averageDifficulty));
		armorNegation.setBaseValue(Math.max(0, amount));

		if(weight.getBaseValue() == 0.0D) {
			EntityDimensions dims = entity.getDimensions(Pose.STANDING);
			double newWeight = dims.width * dims.height * LivingEntityPatch.WEIGHT_CORRECTION;
			weight.setBaseValue(Math.max(0, newWeight));
		}
		amount = weight.getBaseValue() + WEIGHT_BASE_VALUE*(1+PDConfig.getWeightMultiplierValue()*averageDifficulty);
		weight.setBaseValue(Math.max(0, amount));
	}

	@SuppressWarnings("null")
	public static double getDifficultyAround(Entity entity, int radius) {
		if (entity == null) return -1;
		if (!(entity.level() instanceof ServerLevel level)) return -1;
		BlockPos mobPos = entity.blockPosition();
		AABB spawnArea = new AABB(mobPos).inflate(radius);
		List<ServerPlayer> nearbyPlayers = level.getEntitiesOfClass(ServerPlayer.class, spawnArea);
		if(nearbyPlayers.isEmpty()) return -1;
		double totalDifficulty = 0;
		double totalWeight = 0;
		for(ServerPlayer player : nearbyPlayers) {
			double distance = Math.sqrt(player.distanceToSqr(entity.getX(), entity.getY(), entity.getZ()));
			double weight = Mth.clamp(1 - (distance / radius), 0.0, 1.0);
			totalDifficulty += player.getCapability(PlayerDataCapability.INSTANCE).map(PlayerDataCapability::getDifficulty).orElse(0.0) * weight;
			totalWeight += weight;
		}
		double averageDifficulty = totalWeight > 0 ? totalDifficulty / totalWeight : 0;
		averageDifficulty += averageDifficulty * (PDConfig.getGroupBonus() * Math.max(0, nearbyPlayers.size()-1));

		double dimensionBonus = PDConfig.getDimensionBonus(entity.level().dimension().location());
		double biomeBonus = 0.0d;
		Optional<ResourceKey<Biome>> biomeKey = entity.level().getBiome(mobPos).unwrapKey();
		if (biomeKey.isPresent()) {
			biomeBonus = PDConfig.getBiomeBonus(biomeKey.get().location());
		}
		averageDifficulty *= 1 + dimensionBonus + biomeBonus;
		return Math.min(averageDifficulty, PDConfig.getMaxDiff());
	}
}