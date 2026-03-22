package net.lunade.slime.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import java.util.Optional;
import net.lunade.slime.LunaSlimesUtil;
import net.lunade.slime.config.frozenlib.LunaSlimesGameplayConfig;
import net.lunade.slime.config.frozenlib.LunaSlimesVisualsAudioConfig;
import net.lunade.slime.impl.SlimeInterface;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

	@Inject(method = "recreateFromPacket", at = @At("TAIL"))
	public void luneSlimes$recreateFromPacket(ClientboundAddEntityPacket clientboundAddEntityPacket, CallbackInfo info) {
		if (LivingEntity.class.cast(this) instanceof SlimeInterface slimeInterface) slimeInterface.lunaSlimes$setInWorld(true);
	}

	@ModifyReturnValue(method = "hurtServer", at = @At("RETURN"))
	public boolean lunaSlimes$hurtServer(boolean original) {
		if (original && LivingEntity.class.cast(this) instanceof Slime slime && !slime.isTiny() && slime.isDeadOrDying() && LunaSlimesGameplayConfig.USE_SPLITTING.get()) {
			final int split = LunaSlimesUtil.spawnSingleSlime(slime);
			slime.setSize(slime.getSize() - split, true);
			slime.deathTime = 0;
		}
		return original;
	}

	@Inject(method = "die", at = @At("HEAD"), cancellable = true)
	public void lunaSlimes$die(DamageSource source, CallbackInfo info) {
		if (!(LivingEntity.class.cast(this) instanceof Slime slime)) return;
		final Optional<ResourceKey<DamageType>> type = source.typeHolder().unwrapKey();
		if (type.isPresent() && type.get() != DamageTypes.GENERIC_KILL && !slime.isTiny() && LunaSlimesGameplayConfig.USE_SPLITTING.get()) info.cancel();
	}

	@Inject(method = "doPush", at = @At("HEAD"))
	public void lunaSlimes$doPush(Entity entity, CallbackInfo info) {
		if (!(LivingEntity.class.cast(this) instanceof Slime) || !(entity instanceof Slime slime2)) return;
		LunaSlimesUtil.mergeSlimes(Slime.class.cast(this), slime2);
	}

	@Inject(method = "knockback", at = @At("HEAD"), cancellable = true)
	public void lunaSlimes$knockback(double power, double xd, double zd, CallbackInfo info) {
		if (!(LivingEntity.class.cast(this) instanceof Slime slime)) return;
		if (slime.isTiny() && slime.isDeadOrDying() && LunaSlimesVisualsAudioConfig.DEATH_ANIM.get()) info.cancel();
	}

}
