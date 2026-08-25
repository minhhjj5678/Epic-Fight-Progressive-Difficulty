package com.minhhjjj.efprogressivediff.event;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import yesman.epicfight.registry.entries.EpicFightAttributes;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.level.Level;

import java.util.*;

import com.minhhjjj.efprogressivediff.EFProgressiveDiff;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;

import com.minhhjjj.efprogressivediff.config.PDConfig;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import static com.minhhjjj.efprogressivediff.attachment.PlayerDataAttachment.type;

@EventBusSubscriber(modid = EFProgressiveDiff.MODID)
public class MobEvents {
	private static final String DIFFICULTY_STORED = EFProgressiveDiff.MODID + ":difficulty_stored";

	public static double getDifficultyAround(Entity entity) {
		return getDifficultyAround(entity, PDConfig.getGroupRadius());
	}

	@SuppressWarnings("null")
	@SubscribeEvent
	public static void onMobFirstTick(EntityTickEvent.Post event) {
		if (!(event.getEntity() instanceof Mob mob)) return;
		if (mob.level().isClientSide()) return;
		if (!(mob.level() instanceof ServerLevel)) return;
		if (mob.getPersistentData().contains(DIFFICULTY_STORED)) return;

		AttributeInstance weight = mob.getAttribute(attributeHolder(EpicFightAttributes.WEIGHT));
		if (weight == null) return;

		double averageDifficulty = getDifficultyAround(mob);
		if (averageDifficulty < 0) return;
		applyDifficultyScaling(mob, averageDifficulty);

	}

	@SubscribeEvent
	public static void onXpDrop(LivingExperienceDropEvent event) {
		if (!(event.getEntity() instanceof Mob mob)) return;
		if (mob.level().isClientSide()) return;
		if (event.getAttackingPlayer() instanceof ServerPlayer) {
			double exp = event.getDroppedExperience();
			int newEXP = (int) Math.round(exp * (1 + PDConfig.getExpBonus() * mob.getPersistentData().getDouble(DIFFICULTY_STORED)));
			event.setDroppedExperience(Math.max(0, newEXP));
		}
	}

	@SuppressWarnings("null")
	private static void applyDifficultyScaling(Mob entity, double averageDifficulty) {
		AttributeInstance weight = entity.getAttribute(attributeHolder(EpicFightAttributes.WEIGHT));
		AttributeInstance impact = entity.getAttribute(attributeHolder(EpicFightAttributes.IMPACT));
		AttributeInstance stunArmor = entity.getAttribute(attributeHolder(EpicFightAttributes.STUN_ARMOR));
		AttributeInstance maxStrikes = entity.getAttribute(attributeHolder(EpicFightAttributes.MAX_STRIKES));
		AttributeInstance armorNegation = entity.getAttribute(attributeHolder(EpicFightAttributes.ARMOR_NEGATION));
		if(weight == null || impact == null || stunArmor == null || maxStrikes == null || armorNegation == null) return;

		double amount;
		double WEIGHT_BASE_VALUE = PDConfig.getWeightBaseValue();
		double IMPACT_BASE_VALUE = PDConfig.getImpactBaseValue();
		double STUN_ARMOR_BASE_VALUE = PDConfig.getStunArmorBaseValue();
		double MAX_STRIKES_BASE_VALUE = PDConfig.getMaxStrikesBaseValue();
		double ARMOR_NEGATION_BASE_VALUE = PDConfig.getArmorNegationBaseValue();

		amount = impact.getBaseValue() + IMPACT_BASE_VALUE*(1+PDConfig.getImpactMultiplierValue()*averageDifficulty);
		impact.setBaseValue(amount);
		amount = stunArmor.getBaseValue() + STUN_ARMOR_BASE_VALUE*(1+PDConfig.getStunArmorMultiplierValue()*averageDifficulty);
		stunArmor.setBaseValue(amount);
		amount = maxStrikes.getBaseValue() + (MAX_STRIKES_BASE_VALUE*(1+PDConfig.getMaxStrikesMultiplierValue()*averageDifficulty));
		maxStrikes.setBaseValue(amount);
		amount = armorNegation.getBaseValue() + (ARMOR_NEGATION_BASE_VALUE*(1+PDConfig.getArmorNegationMultiplierValue()*averageDifficulty));
		armorNegation.setBaseValue(amount);

		if(weight.getBaseValue() == 0.0D) {
			EntityDimensions dims = entity.getDimensions(Pose.STANDING);
			double newWeight = dims.width() * dims.height() * LivingEntityPatch.WEIGHT_CORRECTION;
			weight.setBaseValue(newWeight);
		}
		amount = weight.getBaseValue() + WEIGHT_BASE_VALUE*(1+PDConfig.getWeightMultiplierValue()*averageDifficulty);
		weight.setBaseValue(amount);

		entity.getPersistentData().putDouble(DIFFICULTY_STORED, averageDifficulty);
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
			totalDifficulty += player.getData(type()).getDifficulty() * weight;
			totalWeight += weight;
		}
		double averageDifficulty = totalWeight > 0 ? totalDifficulty / totalWeight : 0;
		averageDifficulty += averageDifficulty * (PDConfig.getGroupBonus() * Math.max(0, nearbyPlayers.size()-1));

		ResourceKey<Level> dimension = entity.level().dimension();
		double dimensionBonus = PDConfig.getDimensionBonus(dimension.location());
		double biomeBonus = 0.0d;
		Optional<ResourceKey<Biome>> biomeKey = entity.level().getBiome(mobPos).unwrapKey();
		if (biomeKey.isPresent()) {
			biomeBonus = PDConfig.getBiomeBonus(biomeKey.get().location());
		}
		averageDifficulty *= 1 + dimensionBonus + biomeBonus;
		return Math.min(averageDifficulty, PDConfig.getMaxDiff());
	}

	private static Holder<net.minecraft.world.entity.ai.attributes.Attribute> attributeHolder(Holder<net.minecraft.world.entity.ai.attributes.Attribute> attribute) {
		return attribute;
	}
}