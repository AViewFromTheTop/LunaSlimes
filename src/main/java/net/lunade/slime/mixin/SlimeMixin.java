package net.lunade.slime.mixin;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.lunade.slime.SlimeMethods;
import net.lunade.slime.config.getter.ConfigValueGetter;
import net.lunade.slime.impl.SlimeInterface;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

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
    public float lunaSlimes$previousSquish;
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
    private boolean canSquish;

    @Inject(at = @At("TAIL"), method = "defineSynchedData")
    protected void lunaSlimes$defineSynchedData(CallbackInfo info) {
        Slime slime = Slime.class.cast(this);
        slime.getEntityData().define(LUNASLIMES$PREV_WOBBLE_ANIM_PROGRESS, 0);
        slime.getEntityData().define(LUNASLIMES$WOBBLE_ANIM_PROGRESS, 0);
        slime.getEntityData().define(LUNASLIMES$PREV_SIZE, 0F);
        slime.getEntityData().define(LUNASLIMES$CURRENT_SIZE, 0F);
        slime.getEntityData().define(LUNASLIMES$JUMP_ANTIC, false);
    }

    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
    public void lunaSlimes$addAdditionalSaveData(CompoundTag compoundTag, CallbackInfo info) {
        Slime slime = Slime.class.cast(this);
        compoundTag.putInt("PrevWobbleAnimProgress", slime.getEntityData().get(LUNASLIMES$PREV_WOBBLE_ANIM_PROGRESS));
        compoundTag.putInt("WobbleAnimProgress", slime.getEntityData().get(LUNASLIMES$WOBBLE_ANIM_PROGRESS));
        compoundTag.putFloat("PrevSize", slime.getEntityData().get(LUNASLIMES$PREV_SIZE));
        compoundTag.putFloat("CurrentSize", slime.getEntityData().get(LUNASLIMES$CURRENT_SIZE));
        compoundTag.putInt("MergeCooldown", this.lunaSlimes$getMergeCooldown());
        compoundTag.putBoolean("JumpAntic", this.lunaSlimes$jumpAntic);
        compoundTag.putInt("SlimeJumpDelay", this.lunaSlimes$jumpDelay);
        compoundTag.putIntArray("LandDelays", this.lunaSlimes$landDelays);
    }

    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    public void lunaSlimes$readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo info) {
        Slime slime = Slime.class.cast(this);
        if (compoundTag.contains("PrevWobbleAnimProgress")) {
            slime.getEntityData().set(LUNASLIMES$PREV_WOBBLE_ANIM_PROGRESS, compoundTag.getInt("PrevWobbleAnimProgress"));
        }
        if (compoundTag.contains("WobbleAnimProgress")) {
            slime.getEntityData().set(LUNASLIMES$WOBBLE_ANIM_PROGRESS, compoundTag.getInt("WobbleAnimProgress"));
        }
        if (compoundTag.contains("PrevSize")) {
            slime.getEntityData().set(LUNASLIMES$PREV_SIZE, compoundTag.getFloat("PrevSize"));
        }
        if (compoundTag.contains("CurrentSize")) {
            slime.getEntityData().set(LUNASLIMES$CURRENT_SIZE, compoundTag.getFloat("CurrentSize"));
        }
        if (compoundTag.contains("MergeCooldown")) {
            this.lunaSlimes$setMergeCooldown(compoundTag.getInt("MergeCooldown"));
        }
        if (compoundTag.contains("JumpAntic")) {
            this.lunaSlimes$jumpAntic = compoundTag.getBoolean("JumpAntic");
        }
        if (compoundTag.contains("SlimeJumpDelay")) {
            this.lunaSlimes$jumpDelay = compoundTag.getInt("SlimeJumpDelay");
        }
        if (compoundTag.contains("LandDelays")) {
            this.lunaSlimes$landDelays = IntArrayList.wrap(compoundTag.getIntArray("LandDelays"));
        }
    }

    @Inject(at = @At("HEAD"), method = "push")
    public void lunaSlimes$push(Entity entity, CallbackInfo info) {
        if (entity instanceof Slime slime2) {
            SlimeMethods.mergeSlimes(Slime.class.cast(this), slime2);
        }
    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void lunaSlimes$tick(CallbackInfo info) {
        Slime slime = Slime.class.cast(this);
        SynchedEntityData entityData = slime.getEntityData();
        this.lunaSlimes$setMergeCooldown(this.lunaSlimes$getMergeCooldown() - 1);
        this.lunaSlimes$previousSquish = Slime.class.cast(this).squish;
        entityData.set(LUNASLIMES$PREV_WOBBLE_ANIM_PROGRESS, entityData.get(LUNASLIMES$WOBBLE_ANIM_PROGRESS));
        entityData.set(LUNASLIMES$PREV_SIZE, entityData.get(LUNASLIMES$CURRENT_SIZE));
        this.lunaSlimes$prevWobbleAnim = entityData.get(LUNASLIMES$PREV_WOBBLE_ANIM_PROGRESS);
        this.lunaSlimes$prevSize = entityData.get(LUNASLIMES$PREV_SIZE);

        entityData.set(LUNASLIMES$WOBBLE_ANIM_PROGRESS, Math.max(0, entityData.get(LUNASLIMES$WOBBLE_ANIM_PROGRESS) - 1));
        float currentSize = entityData.get(LUNASLIMES$CURRENT_SIZE);
        float sizeDiff = slime.getSize() - currentSize;
        entityData.set(LUNASLIMES$CURRENT_SIZE, currentSize + sizeDiff * 0.25F);
        this.lunaSlimes$wobbleAnim = entityData.get(LUNASLIMES$WOBBLE_ANIM_PROGRESS);
        this.lunaSlimes$currentSize = entityData.get(LUNASLIMES$CURRENT_SIZE);
        this.lunaSlimes$prevDeathTime = slime.deathTime;

        for (int index = 0; index < this.lunaSlimes$landDelays.size(); index++) {
            int array = this.lunaSlimes$landDelays.getInt(index);
            array -= 1;
            this.lunaSlimes$landDelays.set(index, array);
            if (array <= 0) {
                if (array <= -1) {
                    SlimeMethods.spawnSlimeLandParticles(slime);
                    slime.playSound(slime.getSquishSound(), slime.getSoundVolume(), ((slime.getRandom().nextFloat() - slime.getRandom().nextFloat()) * 0.2F + 1.0F) / 0.8F);
                } else {
                    slime.targetSquish = -0.5F;
                }
            }
        }
        this.lunaSlimes$landDelays.removeIf((integer -> integer <= -1));

        if (!slime.level().isClientSide) {
            entityData.set(LUNASLIMES$JUMP_ANTIC, this.lunaSlimes$jumpAntic);
        }
        this.lunaSlimes$jumpAntic = Slime.class.cast(this).getEntityData().get(LUNASLIMES$JUMP_ANTIC);

        if (ConfigValueGetter.jumpAntic()) {
            if (this.lunaSlimes$jumpSquishes > 0) {
                if (this.lunaSlimes$jumpSquishes == 3 && this.lunaSlimes$jumpAntic) {
                    SlimeMethods.setSquish(slime, -0.05F);
                } else if (this.lunaSlimes$jumpSquishes == 2 && this.lunaSlimes$jumpAntic) {
                    SlimeMethods.setSquish(slime, -0.15F);
                } else if (this.lunaSlimes$jumpSquishes == 1 && this.lunaSlimes$jumpAntic) {
                    SlimeMethods.setSquish(slime, -0.3F);
                }
                --this.lunaSlimes$jumpSquishes;
            }
        }
    }

    @Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/monster/Slime;targetSquish:F", ordinal = 1, shift = At.Shift.BEFORE), method = "tick")
    public void lunaSlimes$captureSquish(CallbackInfo info) {
        Slime slime = Slime.class.cast(this);
        this.lunaSlimes$prevTargetSquish = slime.targetSquish;
        this.lunaSlimes$landDelays.add(1);
    }

    @Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/monster/Slime;targetSquish:F", ordinal = 1, shift = At.Shift.AFTER), method = "tick")
    public void lunaSlimes$undoSquish(CallbackInfo info) {
        Slime slime = Slime.class.cast(this);
        slime.targetSquish = this.lunaSlimes$prevTargetSquish;
    }

    @Inject(at = @At("HEAD"), method = "finalizeSpawn")
    public void lunaSlimes$finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag, CallbackInfoReturnable<SpawnGroupData> info) {
        Slime slime = Slime.class.cast(this);
        ((SlimeInterface) slime).lunaSlimes$playWobbleAnim();
        if (mobSpawnType != MobSpawnType.SPAWN_EGG && mobSpawnType != MobSpawnType.MOB_SUMMONED && mobSpawnType != MobSpawnType.BUCKET && mobSpawnType != MobSpawnType.DISPENSER) {
            ((SlimeInterface) slime).lunaSlimes$setMergeCooldown(ConfigValueGetter.spawnedMergeCooldown());
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;setBaseValue(D)V", ordinal = 0, shift = At.Shift.AFTER), method = "setSize")
    public void lunaSlimes$oddHealth(int i, boolean bl, CallbackInfo info) {
        int clampedSize = Mth.clamp(i, 1, 127);
        Slime.class.cast(this).getAttribute(Attributes.MAX_HEALTH).setBaseValue(clampedSize % 2 == 0 ? clampedSize * clampedSize : clampedSize);
    }

    @Inject(at = @At("HEAD"), method = "decreaseSquish", cancellable = true)
    public void lunaSlimes$decreaseSquish(CallbackInfo info) {
        if ((this.lunaSlimes$jumpAntic && ConfigValueGetter.jumpAntic()) || !this.lunaSlimes$canSquish()) {
            info.cancel();
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;tick()V", shift = At.Shift.BEFORE), method = "tick")
    public void lunaSlimes$moveDecreaseSquish(CallbackInfo info) {
        this.canSquish = true;
        Slime.class.cast(this).decreaseSquish();
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Slime;decreaseSquish()V", shift = At.Shift.BEFORE), method = "tick")
    public void lunaSlimes$stopDecreaseSquish(CallbackInfo info) {
        this.canSquish = false;
    }

    @ModifyArgs(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"), method = "tick")
    public void lunaSlimes$stopParticles(Args args) {
        int index = 0;
        if (!(args.get(index) instanceof Double)) {
            index += 1;
            if (!(args.get(index) instanceof Double)) {
                index += 1;
            }
            if (!(args.get(index) instanceof Double)) {
                index += 1;
            }
            if (!(args.get(index) instanceof Double)) {
                index += 1;
            }
        }
        args.set(index, (double) 0);
        args.set(index + 1, (double) -512);
        args.set(index + 2, (double) 0);
    }

    @ModifyArgs(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Slime;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"), method = "tick")
    public void lunaSlimes$stopSound(Args args) {
        int index = 0;
        if (!(args.get(index) instanceof Float)) {
            index += 1;
            if (!(args.get(index) instanceof Float)) {
                index += 1;
            }
            if (!(args.get(index) instanceof Float)) {
                index += 1;
            }
            if (!(args.get(index) instanceof Float)) {
                index += 1;
            }
        }
        args.set(index, (float) 0);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"), method = "remove")
    public boolean lunaSlimes$beforeSpawnNewSlime(Level par1, Entity par2) {
        if (par2 instanceof Slime) {
            ((SlimeInterface) par2).lunaSlimes$setMergeCooldown(Math.max(ConfigValueGetter.onSplitCooldown(), ConfigValueGetter.splitCooldown()) * 2);
        }
        Slime slime = Slime.class.cast(this);
        par2.setSilent(slime.isSilent());
        return par1.addFreshEntity(par2);
    }

    @Inject(at = @At("HEAD"), method = "getParticleType", cancellable = true)
    public void lunaSlimes$getParticleType(CallbackInfoReturnable<ParticleOptions> info) {
        if (ConfigValueGetter.slimeBlockParticles()) {
            info.setReturnValue(LUNASLIMES$NEW_SLIME_PARTICLES);
        }
    }

    @Unique
    @Override
    public float lunaSlimes$prevSquish() {
        return this.lunaSlimes$previousSquish;
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
        Slime slime = Slime.class.cast(this);
        if (slime.getEntityData().get(LUNASLIMES$WOBBLE_ANIM_PROGRESS) == 0) {
            slime.getEntityData().set(LUNASLIMES$WOBBLE_ANIM_PROGRESS, LUNASLIMES$WOBBLE_ANIM_LENGTH);
        }
    }

    @Unique
    @Override
    public float lunaSlimes$getSizeScale(float tickDelta) {
        return Mth.lerp(tickDelta, this.lunaSlimes$prevSize, this.lunaSlimes$currentSize);
    }

    @Unique
    @Override
    public void lunaSlimes$cheatSize(float f) {
        Slime slime = Slime.class.cast(this);
        slime.getEntityData().set(LUNASLIMES$PREV_SIZE, f);
        slime.getEntityData().set(LUNASLIMES$CURRENT_SIZE, f);
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
        return ConfigValueGetter.deathAnim() && Slime.class.cast(this).isDeadOrDying() ? ((20F - Mth.lerp(partialTick, this.lunaSlimes$prevDeathTime, (Slime.class.cast(this).deathTime))) / 20F) : 1F;
    }

    @Unique
    @Override
    public boolean lunaSlimes$canSquish() {
        return this.canSquish;
    }

}