package net.lunade.slime.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.lunade.slime.LunaSlimesUtil;
import net.lunade.slime.config.getter.LunaSlimesConfigValueGetter;
import net.lunade.slime.impl.SlimeInterface;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slime.class)
public class SlimeMixin implements SlimeInterface {
	@Unique
	private static final EntityDataAccessor<Integer> LUNASLIMES$PREV_WOBBLE_ANIM_PROGRESS = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.INT);
	@Unique
	private static final EntityDataAccessor<Integer> LUNASLIMES$WOBBLE_ANIM_PROGRESS = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.INT);
	@Unique
	private static final EntityDataAccessor<Float> LUNASLIMES$PREV_SIZE = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.FLOAT);
	@Unique
	private static final EntityDataAccessor<Float> LUNASLIMES$CURRENT_SIZE = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.FLOAT);
	@Unique
	private static final EntityDataAccessor<Boolean> LUNASLIMES$JUMP_ANTIC = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.BOOLEAN);
	@Unique
	private static final int LUNASLIMES$WOBBLE_ANIM_LENGTH = 10;
	@Unique
	private static final BlockParticleOption LUNASLIMES$NEW_SLIME_PARTICLES = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SLIME_BLOCK.defaultBlockState());

	@Unique
	public int lunaSlimes$mergeCooldown;
	@Unique
	public int lunaSlimes$jumpDelay;
	@Unique
	public int lunaSlimes$jumpSquishes;
	@Unique
	public IntArrayList lunaSlimes$landDelays = new IntArrayList();
	@Unique
	public int lunaSlimes$prevWobbleAnim;
	@Unique
	public int lunaSlimes$wobbleAnim;
	@Unique
	public float lunaSlimes$prevSize = 0F;
	@Unique
	public float lunaSlimes$currentSize = 0F;
	@Unique
	public boolean lunaSlimes$jumpAntic;
	@Unique
	public float lunaSlimes$prevTargetSquish;
	@Unique
	public int lunaSlimes$prevDeathTime;
	@Unique
	private boolean lunaSlimes$canSquish;
	@Unique
	private boolean lunaSlimes$inWorld;

	@Shadow
	public float targetSquish;

	@Inject(at = @At("TAIL"), method = "defineSynchedData")
	protected void lunaSlimes$defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo info) {
		builder.define(LUNASLIMES$PREV_WOBBLE_ANIM_PROGRESS, 0);
		builder.define(LUNASLIMES$WOBBLE_ANIM_PROGRESS, 0);
		builder.define(LUNASLIMES$PREV_SIZE, 0F);
		builder.define(LUNASLIMES$CURRENT_SIZE, 0F);
		builder.define(LUNASLIMES$JUMP_ANTIC, false);
	}

	@Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
	public void lunaSlimes$addAdditionalSaveData(ValueOutput output, CallbackInfo info) {
		SynchedEntityData entityData = Slime.class.cast(this).getEntityData();
		output.putInt("PrevWobbleAnimProgress", entityData.get(LUNASLIMES$PREV_WOBBLE_ANIM_PROGRESS));
		output.putInt("WobbleAnimProgress", entityData.get(LUNASLIMES$WOBBLE_ANIM_PROGRESS));
		output.putFloat("PrevSize", entityData.get(LUNASLIMES$PREV_SIZE));
		output.putFloat("CurrentSize", entityData.get(LUNASLIMES$CURRENT_SIZE));
		output.putInt("MergeCooldown", this.lunaSlimes$getMergeCooldown());
		output.putBoolean("JumpAntic", this.lunaSlimes$jumpAntic);
		output.putInt("SlimeJumpDelay", this.lunaSlimes$jumpDelay);
		output.putIntArray("LandDelays", this.lunaSlimes$landDelays.toIntArray());
	}

	@Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
	public void lunaSlimes$readAdditionalSaveData(ValueInput input, CallbackInfo info) {
		Slime slime = Slime.class.cast(this);
		input.getInt("PrevWobbleAnimProgress").ifPresent(i -> slime.getEntityData().set(LUNASLIMES$PREV_WOBBLE_ANIM_PROGRESS, i));
		input.getInt("WobbleAnimProgress").ifPresent(i -> slime.getEntityData().set(LUNASLIMES$WOBBLE_ANIM_PROGRESS, i));
		slime.getEntityData().set(LUNASLIMES$PREV_SIZE, input.getFloatOr("PrevSize", 0F));
		slime.getEntityData().set(LUNASLIMES$CURRENT_SIZE, input.getFloatOr("CurrentSize", 0F));
		this.lunaSlimes$setMergeCooldown(input.getIntOr("MergeCooldown", 0));
		this.lunaSlimes$jumpAntic = input.getBooleanOr("JumpAntic", false);
		this.lunaSlimes$jumpDelay = input.getIntOr("SlimeJumpDelay", 0);
		input.getIntArray("LandDelays").ifPresent(intArray -> this.lunaSlimes$landDelays = IntArrayList.wrap(intArray));
	}

	@Inject(at = @At("HEAD"), method = "push")
	public void lunaSlimes$push(Entity entity, CallbackInfo info) {
		if (entity instanceof Slime slime2) LunaSlimesUtil.mergeSlimes(Slime.class.cast(this), slime2);
	}

	@Inject(at = @At("HEAD"), method = "tick")
	public void lunaSlimes$tick(CallbackInfo info) {
		final Slime slime = Slime.class.cast(this);
		final SynchedEntityData entityData = slime.getEntityData();
		this.lunaSlimes$setMergeCooldown(this.lunaSlimes$getMergeCooldown() - 1);
		entityData.set(LUNASLIMES$PREV_WOBBLE_ANIM_PROGRESS, entityData.get(LUNASLIMES$WOBBLE_ANIM_PROGRESS));
		entityData.set(LUNASLIMES$PREV_SIZE, entityData.get(LUNASLIMES$CURRENT_SIZE));
		this.lunaSlimes$prevWobbleAnim = entityData.get(LUNASLIMES$PREV_WOBBLE_ANIM_PROGRESS);
		this.lunaSlimes$prevSize = entityData.get(LUNASLIMES$PREV_SIZE);

		entityData.set(LUNASLIMES$WOBBLE_ANIM_PROGRESS, Math.max(0, entityData.get(LUNASLIMES$WOBBLE_ANIM_PROGRESS) - 1));
		final float currentSize = entityData.get(LUNASLIMES$CURRENT_SIZE);
		final float sizeDiff = slime.getSize() - currentSize;
		entityData.set(LUNASLIMES$CURRENT_SIZE, currentSize + sizeDiff * 0.25F);
		this.lunaSlimes$wobbleAnim = entityData.get(LUNASLIMES$WOBBLE_ANIM_PROGRESS);
		this.lunaSlimes$currentSize = entityData.get(LUNASLIMES$CURRENT_SIZE);
		this.lunaSlimes$prevDeathTime = slime.deathTime;

		for (int index = 0; index < this.lunaSlimes$landDelays.size(); index++) {
			int array = this.lunaSlimes$landDelays.getInt(index);
			array -= 1;
			this.lunaSlimes$landDelays.set(index, array);
			if (array > 0) continue;
			if (array <= -1) {
				LunaSlimesUtil.spawnSlimeLandParticles(slime);
				slime.playSound(slime.getSquishSound(), slime.getSoundVolume(), ((slime.getRandom().nextFloat() - slime.getRandom().nextFloat()) * 0.2F + 1.0F) / 0.8F);
			} else {
				slime.targetSquish = -0.5F;
			}
		}
		this.lunaSlimes$landDelays.removeIf((integer -> integer <= -1));

		if (!slime.level().isClientSide()) entityData.set(LUNASLIMES$JUMP_ANTIC, this.lunaSlimes$jumpAntic);

		this.lunaSlimes$jumpAntic = Slime.class.cast(this).getEntityData().get(LUNASLIMES$JUMP_ANTIC);

		if (LunaSlimesConfigValueGetter.jumpAntic()) {
			if (this.lunaSlimes$jumpSquishes > 0) {
				if (this.lunaSlimes$jumpSquishes == 3 && this.lunaSlimes$jumpAntic) {
					LunaSlimesUtil.setSquish(slime, -0.05F);
				} else if (this.lunaSlimes$jumpSquishes == 2 && this.lunaSlimes$jumpAntic) {
					LunaSlimesUtil.setSquish(slime, -0.15F);
				} else if (this.lunaSlimes$jumpSquishes == 1 && this.lunaSlimes$jumpAntic) {
					LunaSlimesUtil.setSquish(slime, -0.3F);
				}
				--this.lunaSlimes$jumpSquishes;
			}
		}
	}

	@Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/monster/Slime;targetSquish:F", ordinal = 1, shift = At.Shift.BEFORE), method = "tick")
	public void lunaSlimes$captureSquish(CallbackInfo info) {
		this.lunaSlimes$prevTargetSquish = this.targetSquish;
		this.lunaSlimes$landDelays.add(1);
	}

	@Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/monster/Slime;targetSquish:F", ordinal = 1, shift = At.Shift.AFTER), method = "tick")
	public void lunaSlimes$undoSquish(CallbackInfo info) {
		this.targetSquish = this.lunaSlimes$prevTargetSquish;
	}

	@Inject(at = @At("HEAD"), method = "finalizeSpawn")
	public void lunaSlimes$finalizeSpawn(
		ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, EntitySpawnReason entitySpawnReason, SpawnGroupData spawnGroupData, CallbackInfoReturnable<SpawnGroupData> cir
	) {
		this.lunaSlimes$playWobbleAnim();
		if (entitySpawnReason != EntitySpawnReason.SPAWN_ITEM_USE
			&& entitySpawnReason != EntitySpawnReason.MOB_SUMMONED
			&& entitySpawnReason != EntitySpawnReason.BUCKET
			&& entitySpawnReason != EntitySpawnReason.DISPENSER
		) {
			this.lunaSlimes$setMergeCooldown(LunaSlimesConfigValueGetter.spawnedMergeCooldown());
		}
	}

	@WrapOperation(
		method = "setSize",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;setBaseValue(D)V", ordinal = 0),
		slice = @Slice(
			from = @At(
				value = "FIELD",
				target = "Lnet/minecraft/world/entity/ai/attributes/Attributes;MAX_HEALTH:Lnet/minecraft/core/Holder;",
				opcode = Opcodes.GETSTATIC
			)
		)
	)
	public void lunaSlimes$oddHealth(AttributeInstance attributeInstance, double value, Operation<Void> operation) {
		final int sqrt = (int) Math.sqrt(value);
		operation.call(attributeInstance, sqrt % 2 == 0 ? value : sqrt);
	}

	@Inject(at = @At("HEAD"), method = "decreaseSquish", cancellable = true)
	public void lunaSlimes$decreaseSquish(CallbackInfo info) {
		if ((this.lunaSlimes$jumpAntic && LunaSlimesConfigValueGetter.jumpAntic()) || !this.lunaSlimes$canSquish()) {
			info.cancel();
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;tick()V", shift = At.Shift.BEFORE), method = "tick")
	public void lunaSlimes$moveDecreaseSquish(CallbackInfo info) {
		this.lunaSlimes$canSquish = true;
		this.decreaseSquish();
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Slime;decreaseSquish()V", shift = At.Shift.BEFORE), method = "tick")
	public void lunaSlimes$stopDecreaseSquish(CallbackInfo info) {
		this.lunaSlimes$canSquish = false;
	}

	@WrapWithCondition(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"), method = "tick")
	public boolean lunaSlimes$stopParticles(Level level, ParticleOptions particleOptions, double a, double b, double c, double e, double f, double g) {
		return false;
	}

	@WrapWithCondition(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Slime;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"), method = "tick")
	public boolean lunaSlimes$stopSound(Slime slime, SoundEvent soundEvent, float a, float b) {
		return false;
	}

	@Inject(method = "method_63653", at = @At("HEAD"))
	public void lunaSlimes$beforeSpawnNewSlime(int i, float f, float g, Slime slime, CallbackInfo info) {
		((SlimeInterface) slime).lunaSlimes$setMergeCooldown(Math.max(LunaSlimesConfigValueGetter.onSplitCooldown(), LunaSlimesConfigValueGetter.splitCooldown()) * 2);
		slime.setSilent(slime.isSilent());
	}

	@ModifyReturnValue(at = @At("RETURN"), method = "getParticleType")
	public ParticleOptions lunaSlimes$getParticleType(ParticleOptions original) {
		return LunaSlimesConfigValueGetter.slimeBlockParticles() ? LUNASLIMES$NEW_SLIME_PARTICLES : original;
	}

	@Unique
	@Override
	public int lunaSlimes$getMergeCooldown() {
		return this.lunaSlimes$mergeCooldown;
	}

	@Unique
	@Override
	public void lunaSlimes$setMergeCooldown(int i) {
		this.lunaSlimes$mergeCooldown = i;
	}

	@Unique
	@Override
	public float lunaSlimes$wobbleAnimProgress(float tickDelta) {
		return 1F - (Mth.lerp(tickDelta, this.lunaSlimes$prevWobbleAnim, this.lunaSlimes$wobbleAnim) / LUNASLIMES$WOBBLE_ANIM_LENGTH);
	}

	@Unique
	@Override
	public void lunaSlimes$playWobbleAnim() {
		final SynchedEntityData entityData = Slime.class.cast(this).getEntityData();
		if (entityData.get(LUNASLIMES$WOBBLE_ANIM_PROGRESS) == 0) entityData.set(LUNASLIMES$WOBBLE_ANIM_PROGRESS, LUNASLIMES$WOBBLE_ANIM_LENGTH);
	}

	@Unique
	@Override
	public float lunaSlimes$getSizeScale(float tickDelta) {
		return Mth.lerp(tickDelta, this.lunaSlimes$prevSize, this.lunaSlimes$currentSize);
	}

	@Unique
	@Override
	public void lunaSlimes$cheatSize(float f) {
		final SynchedEntityData entityData = Slime.class.cast(this).getEntityData();
		entityData.set(LUNASLIMES$PREV_SIZE, f);
		entityData.set(LUNASLIMES$CURRENT_SIZE, f);
		this.lunaSlimes$prevSize = f;
		this.lunaSlimes$currentSize = f;
	}

	@Unique
	@Override
	public void lunaSlimes$setJumpAntic(boolean bl) {
		this.lunaSlimes$jumpAntic = bl;
	}

	@Unique
	@Override
	public boolean lunaSlimes$getJumpAntic() {
		return this.lunaSlimes$jumpAntic;
	}

	@Unique
	@Override
	public void lunaSlimes$setJumpAnticTicks(int i) {
		this.lunaSlimes$jumpSquishes = i;
	}

	@Unique
	@Override
	public int lunaSlimes$getSavedJumpDelay() {
		return this.lunaSlimes$jumpDelay;
	}

	@Unique
	@Override
	public void lunaSlimes$setJumpDelay(int i) {
		this.lunaSlimes$jumpDelay = i;
	}

	@Unique
	@Override
	public float lunaSlimes$getDeathProgress(float partialTick) {
		return LunaSlimesConfigValueGetter.deathAnim() && Slime.class.cast(this).isDeadOrDying()
			? ((20F - Mth.lerp(partialTick, this.lunaSlimes$prevDeathTime, (Slime.class.cast(this).deathTime))) / 20F)
			: 1F;
	}

	@Unique
	@Override
	public boolean lunaSlimes$canSquish() {
		return this.lunaSlimes$canSquish;
	}

	@Unique
	@Override
	public void lunaSlimes$setInWorld(boolean bl) {
		this.lunaSlimes$inWorld = bl;
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
