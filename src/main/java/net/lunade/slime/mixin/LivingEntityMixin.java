package net.lunade.slime.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import java.util.Optional;
import net.lunade.slime.LunaSlimesUtil;
import net.lunade.slime.config.frozenlib.LunaSlimesGameplayConfig;
import net.lunade.slime.config.frozenlib.LunaSlimesVisualsAudioConfig;
import net.lunade.slime.impl.AbstractCubeMobInterface;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.cubemob.AbstractCubeMob;
import net.minecraft.world.entity.monster.cubemob.Slime;
import net.minecraft.world.entity.monster.cubemob.SulfurCube;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

	@Inject(method = "recreateFromPacket", at = @At("TAIL"))
	public void luneSlimes$recreateFromPacket(ClientboundAddEntityPacket packet, CallbackInfo info) {
		if (LivingEntity.class.cast(this) instanceof AbstractCubeMobInterface abstractCubeMobInterface) abstractCubeMobInterface.lunaSlimes$setInWorld(true);
	}

	@ModifyReturnValue(method = "hurtServer", at = @At("RETURN"))
	public boolean lunaSlimes$hurtServer(boolean original, ServerLevel level, DamageSource source) {
		if (original && LivingEntity.class.cast(this) instanceof AbstractCubeMob cube && !cube.isTiny() && cube.isDeadOrDying() && LunaSlimesGameplayConfig.USE_SPLITTING.get()) {
			final int split = LunaSlimesUtil.spawnSingleCube(cube);
			cube.setSize(cube.getSize() - split, true);
			cube.deathTime = 0;

			if (cube instanceof SulfurCube sulfur) {
				sulfur.dropPreservedEquipment(level);
			}
		}
		return original;
	}

	@Inject(method = "die", at = @At("HEAD"), cancellable = true)
	public void lunaSlimes$die(DamageSource source, CallbackInfo info) {
		if (!(LivingEntity.class.cast(this) instanceof AbstractCubeMob cube)) return;
		final Optional<ResourceKey<DamageType>> type = source.typeHolder().unwrapKey();
		if (type.isPresent() && type.get() != DamageTypes.GENERIC_KILL && !cube.isTiny() && LunaSlimesGameplayConfig.USE_SPLITTING.get()) info.cancel();
	}

	@Inject(method = "doPush", at = @At("HEAD"))
	public void lunaSlimes$doPush(Entity entity, CallbackInfo info) {
		if (!(LivingEntity.class.cast(this) instanceof AbstractCubeMob cube) || !(entity instanceof Slime cube2)) return;
		LunaSlimesUtil.mergeCubes(cube, cube2);
	}

	@Inject(method = "knockback(DDDLnet/minecraft/world/damagesource/DamageSource;FZ)V", at = @At("HEAD"), cancellable = true)
	public void lunaSlimes$knockback(double power, double xd, double zd, DamageSource source, float damage, boolean comesFromEffect, CallbackInfo info) {
		if (!(LivingEntity.class.cast(this) instanceof AbstractCubeMob cube)) return;
		if (cube.isTiny() && cube.isDeadOrDying() && LunaSlimesVisualsAudioConfig.DEATH_ANIM.get()) info.cancel();
	}

}
