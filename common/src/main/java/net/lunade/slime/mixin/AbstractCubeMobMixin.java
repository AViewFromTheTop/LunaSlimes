package net.lunade.slime.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.lunade.slime.LunaSlimesUtil;
import net.lunade.slime.config.frozenlib.LSGameplayConfig;
import net.lunade.slime.config.frozenlib.LSVisualsAudioConfig;
import net.lunade.slime.impl.AbstractCubeMobInterface;
import net.lunade.slime.registry.LSAttachmentTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.monster.cubemob.AbstractCubeMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.objectweb.asm.Opcodes;
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
	private boolean lunaSlimes$inLevel;

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

		LSAttachmentTypes.MERGE_COOLDOWN.set(cube, Math.max(0, LSAttachmentTypes.MERGE_COOLDOWN.getAttachedOrCreate(cube) - 1));

		final float initialSize = LSAttachmentTypes.SIZE.getAttachedOrCreate(cube);
		this.lunaSlimes$prevSize = initialSize;
		final float sizeDiff = cube.getSize() - initialSize;
		final float newSize = initialSize + (sizeDiff * 0.25F);
		LSAttachmentTypes.SIZE.set(cube, newSize);

		this.lunaSlimes$prevWobbleAnim = LSAttachmentTypes.WOBBLE_ANIM_PROGRESS.getAttachedOrCreate(cube);
		LSAttachmentTypes.WOBBLE_ANIM_PROGRESS.set(cube, Math.max(0, this.lunaSlimes$prevWobbleAnim - 1));

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
			if (!LSVisualsAudioConfig.JUMP_ANTIC.get()) break jumpAntic;
			if (this.lunaSlimes$jumpSquishes <= 0) break jumpAntic;

			final boolean jumpAntic = LSAttachmentTypes.JUMP_ANTIC.getAttachedOrCreate(cube);
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
			ordinal = 0,
			shift = At.Shift.BEFORE,
			opcode = Opcodes.PUTFIELD
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
			ordinal = 0,
			shift = At.Shift.AFTER,
			opcode = Opcodes.PUTFIELD
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

		if (spawnReason == EntitySpawnReason.SPAWN_ITEM_USE
			|| spawnReason == EntitySpawnReason.MOB_SUMMONED
			|| spawnReason == EntitySpawnReason.BUCKET
			|| spawnReason == EntitySpawnReason.DISPENSER
		) return;

		LSAttachmentTypes.MERGE_COOLDOWN.set(AbstractCubeMob.class.cast(this), LSGameplayConfig.SPAWNED_MERGE_COOLDOWN.get());
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
		final boolean jumpAntic = LSAttachmentTypes.JUMP_ANTIC.getAttachedOrCreate(cubeMob) && LSVisualsAudioConfig.JUMP_ANTIC.get();
		if (jumpAntic || !LSAttachmentTypes.CAN_SQUISH.getAttachedOrCreate(cubeMob)) info.cancel();
	}

	@Inject(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/AgeableMob;tick()V"
		)
	)
	public void lunaSlimes$moveDecreaseSquish(CallbackInfo info) {
		LSAttachmentTypes.CAN_SQUISH.set(AbstractCubeMob.class.cast(this), true);
		this.decreaseSquish();
		LSAttachmentTypes.CAN_SQUISH.set(AbstractCubeMob.class.cast(this), false);
	}

	@WrapWithCondition(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"
		)
	)
	public boolean lunaSlimes$stopParticles(Level level, ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd) {
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
	public void lunaSlimes$beforeSpawnNewCubeMob(int halfSize, float xd, float zd, AbstractCubeMob cubeMob, CallbackInfo info) {
		LSAttachmentTypes.MERGE_COOLDOWN.set(
			cubeMob,
			Math.max(LSGameplayConfig.ON_SPLIT_COOLDOWN.get(), LSGameplayConfig.SPLIT_COOLDOWN.get()) * 2
		);
		cubeMob.setSilent(cubeMob.isSilent());
	}

	@Unique
	@Override
	public float lunaSlimes$wobbleAnimProgress(float partialTicks) {
		return 1F - (
			Mth.lerp(
				partialTicks,
				this.lunaSlimes$prevWobbleAnim,
				LSAttachmentTypes.WOBBLE_ANIM_PROGRESS.getAttachedOrCreate(AbstractCubeMob.class.cast(this))
			) / LUNASLIMES$WOBBLE_ANIM_LENGTH
		);
	}

	@Unique
	@Override
	public void lunaSlimes$playWobbleAnim() {
		final AbstractCubeMob cubeMob = AbstractCubeMob.class.cast(this);
		if (LSAttachmentTypes.WOBBLE_ANIM_PROGRESS.getAttachedOrCreate(cubeMob) != 0) return;
		LSAttachmentTypes.WOBBLE_ANIM_PROGRESS.set(cubeMob, LUNASLIMES$WOBBLE_ANIM_LENGTH);
	}

	@Unique
	@Override
	public float lunaSlimes$getSizeScale(float partialTicks) {
		return Mth.lerp(partialTicks, this.lunaSlimes$prevSize, LSAttachmentTypes.SIZE.getAttachedOrCreate(AbstractCubeMob.class.cast(this)));
	}

	@Unique
	@Override
	public void lunaSlimes$cheatSize(float size) {
		LSAttachmentTypes.SIZE.set(AbstractCubeMob.class.cast(this), size);
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
		return LSVisualsAudioConfig.DEATH_ANIM.get() && AbstractCubeMob.class.cast(this).isDeadOrDying()
			? ((20F - Mth.lerp(partialTicks, this.lunaSlimes$prevDeathTime, (AbstractCubeMob.class.cast(this).deathTime))) / 20F)
			: 1F;
	}

	@Unique
	@Override
	public void lunaSlimes$setInLevel(boolean inLevel) {
		this.lunaSlimes$inLevel = inLevel;
	}

	@Unique
	@Override
	public boolean lunaSlimes$isInLevel() {
		return this.lunaSlimes$inLevel;
	}

	@Shadow
	public void decreaseSquish() {}
}
