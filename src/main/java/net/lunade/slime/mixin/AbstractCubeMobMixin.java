package net.lunade.slime.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.lunade.slime.LunaSlimesUtil;
import net.lunade.slime.config.frozenlib.LunaSlimesGameplayConfig;
import net.lunade.slime.config.frozenlib.LunaSlimesVisualsAudioConfig;
import net.lunade.slime.impl.AbstractCubeMobInterface;
import net.lunade.slime.registry.LunaSlimesAttachmentTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.SulfurCubeArchetypes;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.monster.cubemob.AbstractCubeMob;
import net.minecraft.world.entity.monster.cubemob.SulfurCube;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractCubeMob.class)
public class AbstractCubeMobMixin implements AbstractCubeMobInterface {
	@Unique
	private static final int LUNASLIMES$WOBBLE_ANIM_LENGTH = 10;

	@Unique
	public int lunaSlimes$jumpSquishes;
	@Unique
	public IntArrayList lunaSlimes$landDelays = new IntArrayList();
	@Unique
	public int lunaSlimes$prevWobbleAnim;
	@Unique
	public float lunaSlimes$prevSize = 0F;
	@Unique
	public float lunaSlimes$prevTargetSquish;
	@Unique
	public int lunaSlimes$prevDeathTime;
	@Unique
	private boolean lunaSlimes$inWorld;

	@Shadow
	public float targetSquish;

	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	public void lunaSlimes$addAdditionalSaveData(ValueOutput output, CallbackInfo info) {
		output.putIntArray("LandDelays", this.lunaSlimes$landDelays.toIntArray());
	}

	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	public void lunaSlimes$readAdditionalSaveData(ValueInput input, CallbackInfo info) {
		input.getIntArray("LandDelays").ifPresent(intArray -> this.lunaSlimes$landDelays = IntArrayList.wrap(intArray));
	}

	@Inject(method = "push", at = @At("HEAD"))
	public void lunaSlimes$push(Entity entity, CallbackInfo info) {
		if (entity instanceof AbstractCubeMob cube) LunaSlimesUtil.mergeCubes(AbstractCubeMob.class.cast(this), cube);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void lunaSlimes$tick(CallbackInfo info) {
		final AbstractCubeMob cube = AbstractCubeMob.class.cast(this);

		cube.setAttached(LunaSlimesAttachmentTypes.MERGE_COOLDOWN, Math.max(0, cube.getAttachedOrCreate(LunaSlimesAttachmentTypes.MERGE_COOLDOWN) - 1));

		final float initialSize = cube.getAttachedOrCreate(LunaSlimesAttachmentTypes.SIZE);
		this.lunaSlimes$prevSize = initialSize;
		final float sizeDiff = cube.getSize() - initialSize;
		final float newSize = initialSize + (sizeDiff * 0.25F);
		cube.setAttached(LunaSlimesAttachmentTypes.SIZE, newSize);

		this.lunaSlimes$prevWobbleAnim = cube.getAttachedOrCreate(LunaSlimesAttachmentTypes.WOBBLE_ANIM_PROGRESS);
		cube.setAttached(LunaSlimesAttachmentTypes.WOBBLE_ANIM_PROGRESS, Math.max(0, this.lunaSlimes$prevWobbleAnim - 1));

		this.lunaSlimes$prevDeathTime = cube.deathTime;

		for (int index = 0; index < this.lunaSlimes$landDelays.size(); index++) {
			int array = this.lunaSlimes$landDelays.getInt(index);
			array -= 1;
			this.lunaSlimes$landDelays.set(index, array);
			if (array > 0) continue;
			if (array <= -1) {
				LunaSlimesUtil.spawnCubeLandParticles(cube);
				cube.playSound(cube.getSquishSound(), cube.getSoundVolume(), ((cube.getRandom().nextFloat() - cube.getRandom().nextFloat()) * 0.2F + 1.0F) / 0.8F);
			} else {
				cube.targetSquish = -0.5F;
			}
		}
		this.lunaSlimes$landDelays.removeIf(integer -> integer <= -1);

		jumpAntic: {
			if (!LunaSlimesVisualsAudioConfig.JUMP_ANTIC.get()) break jumpAntic;
			if (this.lunaSlimes$jumpSquishes <= 0) break jumpAntic;

			final boolean jumpAntic = cube.getAttachedOrCreate(LunaSlimesAttachmentTypes.JUMP_ANTIC);
			if (this.lunaSlimes$jumpSquishes == 3 && jumpAntic) {
				LunaSlimesUtil.setSquish(cube, -0.05F);
			} else if (this.lunaSlimes$jumpSquishes == 2 && jumpAntic) {
				LunaSlimesUtil.setSquish(cube, -0.15F);
			} else if (this.lunaSlimes$jumpSquishes == 1 && jumpAntic) {
				LunaSlimesUtil.setSquish(cube, -0.3F);
			}
			--this.lunaSlimes$jumpSquishes;
		}
	}

	@Inject(
		method = "tick",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/world/entity/monster/cubemob/AbstractCubeMob;targetSquish:F",
			ordinal = 1,
			shift = At.Shift.BEFORE
		)
	)
	public void lunaSlimes$captureSquish(CallbackInfo info) {
		this.lunaSlimes$prevTargetSquish = this.targetSquish;
		this.lunaSlimes$landDelays.add(1);
	}

	@Inject(
		method = "tick",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/world/entity/monster/cubemob/AbstractCubeMob;targetSquish:F",
			ordinal = 1,
			shift = At.Shift.AFTER
		)
	)
	public void lunaSlimes$undoSquish(CallbackInfo info) {
		this.targetSquish = this.lunaSlimes$prevTargetSquish;
	}

	@Inject(method = "finalizeSpawn", at = @At("HEAD"))
	public void lunaSlimes$finalizeSpawn(
		ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData, CallbackInfoReturnable<SpawnGroupData> info
	) {
		this.lunaSlimes$playWobbleAnim();
		if (spawnReason == EntitySpawnReason.SPAWN_ITEM_USE || spawnReason == EntitySpawnReason.MOB_SUMMONED || spawnReason == EntitySpawnReason.BUCKET || spawnReason == EntitySpawnReason.DISPENSER) return;
		AbstractCubeMob.class.cast(this).setAttached(LunaSlimesAttachmentTypes.MERGE_COOLDOWN, LunaSlimesGameplayConfig.SPAWNED_MERGE_COOLDOWN.get());
	}

	@WrapOperation(
		method = "setcubeMobHealth",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;setBaseValue(D)V",
			ordinal = 0
		)
	)
	public void lunaSlimes$oddHealth(AttributeInstance attribute, double baseValue, Operation<Void> operation) {
		final int sqrt = (int) Math.sqrt(baseValue);
		operation.call(attribute, sqrt % 2 == 0 ? baseValue : sqrt);
	}

	@Inject(method = "decreaseSquish", at = @At("HEAD"), cancellable = true)
	public void lunaSlimes$decreaseSquish(CallbackInfo info) {
		final AbstractCubeMob cubeMob = AbstractCubeMob.class.cast(this);
		final boolean jumpAntic = cubeMob.getAttachedOrCreate(LunaSlimesAttachmentTypes.JUMP_ANTIC) && LunaSlimesVisualsAudioConfig.JUMP_ANTIC.get();
		if (jumpAntic || !cubeMob.getAttachedOrCreate(LunaSlimesAttachmentTypes.CAN_SQUISH)) info.cancel();
	}

	@Inject(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/AgeableMob;tick()V",
			shift = At.Shift.BEFORE
		)
	)
	public void lunaSlimes$moveDecreaseSquish(CallbackInfo info) {
		AbstractCubeMob.class.cast(this).setAttached(LunaSlimesAttachmentTypes.CAN_SQUISH, true);
		this.decreaseSquish();
	}

	@Inject(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/monster/cubemob/AbstractCubeMob;decreaseSquish()V",
			shift = At.Shift.BEFORE
		)
	)
	public void lunaSlimes$stopDecreaseSquish(CallbackInfo info) {
		AbstractCubeMob.class.cast(this).setAttached(LunaSlimesAttachmentTypes.CAN_SQUISH, false);
	}

	@WrapWithCondition(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"
		)
	)
	public boolean lunaSlimes$stopParticles(Level level, ParticleOptions options, double x, double y, double z, double xd, double yd, double zd) {
		return false;
	}

	@WrapWithCondition(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/monster/cubemob/AbstractCubeMob;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"
		)
	)
	public boolean lunaSlimes$stopSound(AbstractCubeMob cube, SoundEvent sound, float volume, float pitch) {
		return false;
	}

	@Inject(method = "lambda$remove$0", at = @At("HEAD"))
	public void lunaSlimes$beforeSpawnNewSlime(int i, float f, float g, AbstractCubeMob cube, CallbackInfo info) {
		cube.setAttached(LunaSlimesAttachmentTypes.MERGE_COOLDOWN, Math.max(LunaSlimesGameplayConfig.ON_SPLIT_COOLDOWN.get(), LunaSlimesGameplayConfig.SPLIT_COOLDOWN.get()) * 2);
		cube.setSilent(cube.isSilent());
	}

	@Unique
	@Override
	public float lunaSlimes$wobbleAnimProgress(float partialTicks) {
		return 1F - (
			Mth.lerp(
				partialTicks,
				this.lunaSlimes$prevWobbleAnim,
				AbstractCubeMob.class.cast(this).getAttachedOrCreate(LunaSlimesAttachmentTypes.WOBBLE_ANIM_PROGRESS)
			) / LUNASLIMES$WOBBLE_ANIM_LENGTH
		);
	}

	@Unique
	@Override
	public void lunaSlimes$playWobbleAnim() {
		final AbstractCubeMob cubeMob = AbstractCubeMob.class.cast(this);
		if (cubeMob.getAttachedOrCreate(LunaSlimesAttachmentTypes.WOBBLE_ANIM_PROGRESS) != 0) return;
		cubeMob.setAttached(LunaSlimesAttachmentTypes.WOBBLE_ANIM_PROGRESS, LUNASLIMES$WOBBLE_ANIM_LENGTH);
	}

	@Unique
	@Override
	public float lunaSlimes$getSizeScale(float partialTicks) {
		return Mth.lerp(partialTicks, this.lunaSlimes$prevSize, AbstractCubeMob.class.cast(this).getAttachedOrCreate(LunaSlimesAttachmentTypes.SIZE));
	}

	@Unique
	@Override
	public void lunaSlimes$cheatSize(float size) {
		AbstractCubeMob.class.cast(this).setAttached(LunaSlimesAttachmentTypes.SIZE, size);
		this.lunaSlimes$prevSize = size;
	}

	@Unique
	@Override
	public void lunaSlimes$setJumpAnticTicks(int jumpAnticTicks) {
		this.lunaSlimes$jumpSquishes = jumpAnticTicks;
	}

	@Unique
	@Override
	public float lunaSlimes$getDeathProgress(float partialTicks) {
		return LunaSlimesVisualsAudioConfig.DEATH_ANIM.get() && AbstractCubeMob.class.cast(this).isDeadOrDying()
			? ((20F - Mth.lerp(partialTicks, this.lunaSlimes$prevDeathTime, (AbstractCubeMob.class.cast(this).deathTime))) / 20F)
			: 1F;
	}

	@Unique
	@Override
	public void lunaSlimes$setInWorld(boolean inWorld) {
		this.lunaSlimes$inWorld = inWorld;
	}

	@Unique
	@Override
	public boolean lunaSlimes$isInWorld() {
		return this.lunaSlimes$inWorld;
	}

	@Shadow
	public void decreaseSquish() {
	}

}
