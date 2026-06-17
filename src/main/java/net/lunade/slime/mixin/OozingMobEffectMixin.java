package net.lunade.slime.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lunade.slime.registry.LunaSlimesAttachmentTypes;
import net.minecraft.world.effect.OozingMobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.cubemob.Slime;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(OozingMobEffect.class)
public class OozingMobEffectMixin {

	@WrapOperation(
		method = "spawnSlimeOffspring",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
		)
	)
	public boolean lunaSlimes$spawnSlimeOffspring(Level instance, Entity entity, Operation<Boolean> original) {
		if (entity instanceof Slime slime) slime.setAttached(LunaSlimesAttachmentTypes.MERGE_COOLDOWN, 100);
		return original.call(instance, entity);
	}

}
