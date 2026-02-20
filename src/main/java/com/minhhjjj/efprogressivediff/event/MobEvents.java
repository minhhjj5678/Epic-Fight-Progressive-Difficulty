package com.minhhjjj.efprogressivediff.event;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;

import yesman.epicfight.registry.entries.EpicFightAttributes;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.EventBusSubscriber;

import com.minhhjjj.efprogressivediff.EFProgressiveDiff;
import com.minhhjjj.efprogressivediff.config.PDConfig;

@EventBusSubscriber(modid = EFProgressiveDiff.MODID)
public class MobEvents {
	
	@SubscribeEvent
	public static void onSpawn(FinalizeSpawnEvent event) {
		Mob entity = event.getEntity();
		if(event.getLevel().isClientSide()) return;
		if(!(entity.level() instanceof ServerLevel level)) return;
		int dayCount = (int) (level.getGameTime() / Level.TICKS_PER_DAY);
		AttributeInstance weight = entity.getAttribute(EpicFightAttributes.WEIGHT);
		AttributeInstance impact = entity.getAttribute(EpicFightAttributes.IMPACT);
		AttributeInstance stunArmor = entity.getAttribute(EpicFightAttributes.STUN_ARMOR);
		AttributeInstance maxStrikes = entity.getAttribute(EpicFightAttributes.MAX_STRIKES);
		AttributeInstance armorNegation = entity.getAttribute(EpicFightAttributes.ARMOR_NEGATION);
		double amount;
		double BASE_VALUE = PDConfig.baseValue;

		if(weight != null && impact != null && stunArmor != null && maxStrikes != null && armorNegation != null) {
			amount = impact.getBaseValue() + Math.min(BASE_VALUE*(1+PDConfig.impactMultiply*dayCount), PDConfig.impactCap);
			impact.setBaseValue(amount);
			amount = stunArmor.getBaseValue() + Math.min(BASE_VALUE*(1+PDConfig.stunArmorMultiply*dayCount), PDConfig.stunArmorCap);
			stunArmor.setBaseValue(amount);
			amount = maxStrikes.getBaseValue() + Math.min(BASE_VALUE*(1+PDConfig.maxStrikesMultiply*dayCount), PDConfig.maxStrikesCap);
			maxStrikes.setBaseValue(amount);
			amount = armorNegation.getBaseValue() + Math.min(BASE_VALUE*(1+PDConfig.armorNegationMultiply*dayCount), PDConfig.armorNegationCap);
			armorNegation.setBaseValue(amount);

			if(weight.getBaseValue() == 0.0D) {
				EntityDimensions dims = entity.getDimensions(Pose.STANDING);
				double newWeight = dims.width() * dims.height() * LivingEntityPatch.WEIGHT_CORRECTION;
				weight.setBaseValue(newWeight);
			}
			amount = weight.getBaseValue() + Math.min(BASE_VALUE*(1+PDConfig.weightMultiply*dayCount), PDConfig.weightCap);
			weight.setBaseValue(amount);
		}
	}
}