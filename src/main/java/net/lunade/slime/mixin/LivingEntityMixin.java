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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.cubemob.AbstractCubeMob;
import net.minecraft.world.entity.monster.cubemob.SulfurCube;
import net.minecraft.world.item.ItemStack;
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
	public boolean lunaSlimes$triggerSplitOnDeath(boolean original, ServerLevel level, DamageSource source) {
		if (original
			&& LivingEntity.class.cast(this) instanceof AbstractCubeMob cubeMob
			&& !cubeMob.isTiny()
			&& cubeMob.isDeadOrDying()
			&& LunaSlimesGameplayConfig.USE_SPLITTING.get()
		) {
			final int split = LunaSlimesUtil.spawnSingleCube(cubeMob);
			cubeMob.setSize(cubeMob.getSize() - split, true);
			cubeMob.deathTime = 0;

			if (cubeMob.shouldDropLoot(level)) {
				cubeMob.dropCustomDeathLoot(level, source, cubeMob.lastHurtByPlayerMemoryTime > 0);
			} else if (cubeMob instanceof SulfurCube) {
				for (EquipmentSlot slot : EquipmentSlot.VALUES) {
					cubeMob.setItemSlot(slot, ItemStack.EMPTY);
				}
			}

			cubeMob.dropEquipment(level);
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
		if (!(LivingEntity.class.cast(this) instanceof AbstractCubeMob cube) || !(entity instanceof AbstractCubeMob cube2)) return;
		LunaSlimesUtil.mergeCubes(cube, cube2);
	}

	@Inject(method = "knockback(DDDLnet/minecraft/world/damagesource/DamageSource;FZ)V", at = @At("HEAD"), cancellable = true)
	public void lunaSlimes$knockback(double power, double xd, double zd, DamageSource source, float damage, boolean comesFromEffect, CallbackInfo info) {
		if (!(LivingEntity.class.cast(this) instanceof AbstractCubeMob cube)) return;
		if (cube.isTiny() && cube.isDeadOrDying() && LunaSlimesVisualsAudioConfig.DEATH_ANIM.get()) info.cancel();
	}
}
