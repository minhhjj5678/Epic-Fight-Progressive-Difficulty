package com.minhhjjj.efprogressivediff.event;

import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

import java.util.List;

import com.minhhjjj.efprogressivediff.EFProgressiveDiff;
import com.minhhjjj.efprogressivediff.capability.PlayerDataCapability;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;

import com.minhhjjj.efprogressivediff.config.PDConfig;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;

@Mod.EventBusSubscriber(modid = EFProgressiveDiff.MODID)
public class MobEvents {
	public static final int RADIUS = 64;
	
	@SuppressWarnings("null")
	@SubscribeEvent
	public static void onSpawn(MobSpawnEvent.FinalizeSpawn event) {
		Mob entity = event.getEntity();
		if(event.getLevel().isClientSide()) return;
		if(!(entity.level() instanceof ServerLevel)) return;
		
		double averageDifficulty = getDifficultyAround(entity);
		if (averageDifficulty < 0) return;

		AttributeInstance weight = entity.getAttribute(EpicFightAttributes.WEIGHT.get());
		AttributeInstance impact = entity.getAttribute(EpicFightAttributes.IMPACT.get());
		AttributeInstance stunArmor = entity.getAttribute(EpicFightAttributes.STUN_ARMOR.get());
		AttributeInstance maxStrikes = entity.getAttribute(EpicFightAttributes.MAX_STRIKES.get());
		AttributeInstance armorNegation = entity.getAttribute(EpicFightAttributes.ARMOR_NEGATION.get());
		double amount;
		double WEIGHT_BASE_VALUE = PDConfig.weightBaseValue;
		double IMPACT_BASE_VALUE = PDConfig.impactBaseValue;
		double STUN_ARMOR_BASE_VALUE = PDConfig.stunArmorBaseValue;
		double MAX_STRIKES_BASE_VALUE = PDConfig.maxStrikesBaseValue;
		double ARMOR_NEGATION_BASE_VALUE = PDConfig.armorNegationBaseValue;

		if(weight != null && impact != null && stunArmor != null && maxStrikes != null && armorNegation != null) {
			amount = impact.getBaseValue() + IMPACT_BASE_VALUE*(1+PDConfig.impactMultiply*averageDifficulty);
			impact.setBaseValue(amount);
			amount = stunArmor.getBaseValue() + STUN_ARMOR_BASE_VALUE*(1+PDConfig.stunArmorMultiply*averageDifficulty);
			stunArmor.setBaseValue(amount);
			amount = maxStrikes.getBaseValue() + (MAX_STRIKES_BASE_VALUE*(1+PDConfig.maxStrikesMultiply*averageDifficulty));
			maxStrikes.setBaseValue(amount);
			amount = armorNegation.getBaseValue() + (ARMOR_NEGATION_BASE_VALUE*(1+PDConfig.armorNegationMultiply*averageDifficulty));
			armorNegation.setBaseValue(amount);

			if(weight.getBaseValue() == 0.0D) {
				EntityDimensions dims = entity.getDimensions(Pose.STANDING);
				double newWeight = dims.width * dims.height * LivingEntityPatch.WEIGHT_CORRECTION;
				weight.setBaseValue(newWeight);
			}
			amount = weight.getBaseValue() + WEIGHT_BASE_VALUE*(1+PDConfig.weightMultiply*averageDifficulty);
			weight.setBaseValue(amount);
		}
	}

	public static double getDifficultyAround(Entity entity) {
		return getDifficultyAround(entity, RADIUS);
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
		double averageDifficulty = Math.min(totalWeight > 0 ? totalDifficulty / totalWeight : 0, PDConfig.maxDifficultyCap);
		return averageDifficulty;
	}
}