package com.minhhjjj.efprogressivediff.event;

import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import com.minhhjjj.efprogressivediff.EFProgressiveDiff;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import com.minhhjjj.efprogressivediff.config.PDConfig;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;

@Mod.EventBusSubscriber(modid = EFProgressiveDiff.MODID)
public class MobEvents {
	
	@SuppressWarnings("null")
	@SubscribeEvent
	public static void onSpawn(MobSpawnEvent.FinalizeSpawn event) {
		Mob entity = event.getEntity();
		if(event.getLevel().isClientSide()) return;
		if(!(entity.level() instanceof ServerLevel level)) return;
		int dayCount = (int) (level.getGameTime() / Level.TICKS_PER_DAY);
		AttributeInstance weight = entity.getAttribute(EpicFightAttributes.WEIGHT.get());
		AttributeInstance impact = entity.getAttribute(EpicFightAttributes.IMPACT.get());
		AttributeInstance stunArmor = entity.getAttribute(EpicFightAttributes.STUN_ARMOR.get());
		AttributeInstance maxStrikes = entity.getAttribute(EpicFightAttributes.MAX_STRIKES.get());
		AttributeInstance armorNegation = entity.getAttribute(EpicFightAttributes.ARMOR_NEGATION.get());
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
				double newWeight = dims.width * dims.height * LivingEntityPatch.WEIGHT_CORRECTION;
				weight.setBaseValue(newWeight);
			}
			amount = weight.getBaseValue() + Math.min(BASE_VALUE*(1+PDConfig.weightMultiply*dayCount), PDConfig.weightCap);
			weight.setBaseValue(amount);
		}
	}
}