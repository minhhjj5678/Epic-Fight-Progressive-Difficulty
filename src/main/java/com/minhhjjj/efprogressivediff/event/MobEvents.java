package com.minhhjjj.efprogressivediff.event;

import net.minecraft.world.entity.Mob;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import yesman.epicfight.registry.entries.EpicFightAttributes;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.level.Level;

import java.util.List;

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
	public static final int RADIUS = 64;
	private static final String WEIGHT_INIT_TAG = EFProgressiveDiff.MODID + ":weight_initialized";
	
	@SuppressWarnings("null")
	@SubscribeEvent
	public static void onSpawn(FinalizeSpawnEvent event) {
		Mob entity = event.getEntity();
		if(event.getLevel().isClientSide()) return;
		if(!(entity.level() instanceof ServerLevel)) return;
		
		double averageDifficulty = getDifficultyAround(entity);
		if (averageDifficulty < 0) return;
		applyDifficultyScaling(entity, averageDifficulty);
	}

	public static double getDifficultyAround(Entity entity) {
		return getDifficultyAround(entity, RADIUS);
	}

	@SuppressWarnings("null")
	@SubscribeEvent
	public static void onMobFirstTick(EntityTickEvent.Post event) {
		if (!(event.getEntity() instanceof Mob mob)) return;
		if (mob.level().isClientSide()) return;
		if (!(mob.level() instanceof ServerLevel)) return;
		if (mob.getPersistentData().getBoolean(WEIGHT_INIT_TAG)) return;

		AttributeInstance weight = mob.getAttribute(attributeHolder(EpicFightAttributes.WEIGHT));
		if (weight == null) return;

		if (weight.getBaseValue() == 0.0D) {
			double averageDifficulty = Math.max(0.0D, getDifficultyAround(mob));
			applyDifficultyScaling(mob, averageDifficulty);
		}

		mob.getPersistentData().putBoolean(WEIGHT_INIT_TAG, true);
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
		double WEIGHT_BASE_VALUE = PDConfig.weightBaseValue;
		double IMPACT_BASE_VALUE = PDConfig.impactBaseValue;
		double STUN_ARMOR_BASE_VALUE = PDConfig.stunArmorBaseValue;
		double MAX_STRIKES_BASE_VALUE = PDConfig.maxStrikesBaseValue;
		double ARMOR_NEGATION_BASE_VALUE = PDConfig.armorNegationBaseValue;

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
			double newWeight = dims.width() * dims.height() * LivingEntityPatch.WEIGHT_CORRECTION;
			weight.setBaseValue(newWeight);
		}
		amount = weight.getBaseValue() + WEIGHT_BASE_VALUE*(1+PDConfig.weightMultiply*averageDifficulty);
		weight.setBaseValue(amount);
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
		averageDifficulty += averageDifficulty * (PDConfig.groupBonus * Math.max(0, nearbyPlayers.size()-1));
		
		ResourceKey<Level> dimension = entity.level().dimension();
		double dimensionBonus = 0.0d;
		if (dimension == Level.OVERWORLD) dimensionBonus = PDConfig.overworldBonus;
		else if (dimension == Level.NETHER) dimensionBonus = PDConfig.netherBonus;
		else if (dimension == Level.END) dimensionBonus = PDConfig.theendBonus;
		else dimensionBonus = PDConfig.otherBonus;
		averageDifficulty += averageDifficulty * dimensionBonus;
		return Math.min(averageDifficulty, PDConfig.maxDifficultyCap);
	}

	private static Holder<net.minecraft.world.entity.ai.attributes.Attribute> attributeHolder(Holder<net.minecraft.world.entity.ai.attributes.Attribute> attribute) {
		return attribute;
	}
}