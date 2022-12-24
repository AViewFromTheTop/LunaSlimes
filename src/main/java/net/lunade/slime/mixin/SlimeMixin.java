package net.lunade.slime.mixin;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.lunade.slime.SlimeMethods;
import net.lunade.slime.config.getter.ConfigValueGetter;
import net.lunade.slime.impl.SlimeInterface;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slime.class)
public class SlimeMixin implements SlimeInterface {
    @Unique private static final EntityDataAccessor<Integer> PREV_WOBBLE_ANIM_PROGRESS = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.INT);
    @Unique private static final EntityDataAccessor<Integer> WOBBLE_ANIM_PROGRESS = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.INT);
    @Unique private static final EntityDataAccessor<Float> PREV_SIZE = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.FLOAT);
    @Unique private static final EntityDataAccessor<Float> CURRENT_SIZE = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.FLOAT);
    @Unique private static final EntityDataAccessor<Boolean> JUMP_ANTIC = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.BOOLEAN);

    @Unique private static final int WOBBLE_ANIM_LENGTH = 10;

    @Unique public int mergeCooldown;
    @Unique public int jumpDelay;
    @Unique public int jumpSquishes;
    @Unique public IntArrayList landDelays = new IntArrayList();

    @Unique public float previousSquish;
    @Unique public int prevWobbleAnim;
    @Unique public int wobbleAnim;
    @Unique public float prevSize = 0F;
    @Unique public float currentSize = 0F;
    @Unique public boolean jumpAntic;
    @Unique public float prevTargetSquish;

    @Inject(at = @At("TAIL"), method = "defineSynchedData")
    protected void defineSynchedData(CallbackInfo info) {
        Slime slime = Slime.class.cast(this);
        slime.getEntityData().define(PREV_WOBBLE_ANIM_PROGRESS, 0);
        slime.getEntityData().define(WOBBLE_ANIM_PROGRESS, 0);
        slime.getEntityData().define(PREV_SIZE, 0F);
        slime.getEntityData().define(CURRENT_SIZE, 0F);
        slime.getEntityData().define(JUMP_ANTIC, false);
    }

    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
    public void addAdditionalSaveData(CompoundTag compoundTag, CallbackInfo info) {
        Slime slime = Slime.class.cast(this);
        compoundTag.putInt("PrevWobbleAnimProgress", slime.getEntityData().get(PREV_WOBBLE_ANIM_PROGRESS));
        compoundTag.putInt("WobbleAnimProgress", slime.getEntityData().get(WOBBLE_ANIM_PROGRESS));
        compoundTag.putFloat("PrevSize", slime.getEntityData().get(PREV_SIZE));
        compoundTag.putFloat("CurrentSize", slime.getEntityData().get(CURRENT_SIZE));
        compoundTag.putInt("MergeCooldown", this.getMergeCooldown());
        compoundTag.putBoolean("JumpAntic", this.jumpAntic);
        compoundTag.putInt("SlimeJumpDelay", this.jumpDelay);
        compoundTag.putIntArray("LandDelays", this.landDelays);
    }

    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    public void readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo info) {
        Slime slime = Slime.class.cast(this);
        slime.getEntityData().set(PREV_WOBBLE_ANIM_PROGRESS, compoundTag.getInt("PrevWobbleAnimProgress"));
        slime.getEntityData().set(WOBBLE_ANIM_PROGRESS, compoundTag.getInt("WobbleAnimProgress"));
        slime.getEntityData().set(PREV_SIZE, compoundTag.getFloat("PrevSize"));
        slime.getEntityData().set(CURRENT_SIZE, compoundTag.getFloat("CurrentSize"));
        this.setMergeCooldown(compoundTag.getInt("MergeCooldown"));
        this.jumpAntic = compoundTag.getBoolean("JumpAntic");
        this.jumpDelay = compoundTag.getInt("SlimeJumpDelay");
        this.landDelays = IntArrayList.wrap(compoundTag.getIntArray("LandDelays"));
    }

    @Inject(at = @At("HEAD"), method = "push")
    public void push(Entity entity, CallbackInfo info) {
        if (entity instanceof Slime slime2) {
            SlimeMethods.mergeSlimes(Slime.class.cast(this), slime2);
        }
    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void tick(CallbackInfo info) {
        Slime slime = Slime.class.cast(this);
        this.previousSquish = Slime.class.cast(this).squish;
        slime.getEntityData().set(PREV_WOBBLE_ANIM_PROGRESS, slime.getEntityData().get(WOBBLE_ANIM_PROGRESS));
        slime.getEntityData().set(PREV_SIZE, slime.getEntityData().get(CURRENT_SIZE));
        this.prevWobbleAnim = slime.getEntityData().get(PREV_WOBBLE_ANIM_PROGRESS);
        this.prevSize = slime.getEntityData().get(PREV_SIZE);

        slime.getEntityData().set(WOBBLE_ANIM_PROGRESS, Math.max(0, slime.getEntityData().get(WOBBLE_ANIM_PROGRESS) - 1));
        float sizeDiff = slime.getSize() - slime.getEntityData().get(CURRENT_SIZE);
        slime.getEntityData().set(CURRENT_SIZE, slime.getEntityData().get(CURRENT_SIZE) + sizeDiff * 0.25F);
        this.wobbleAnim = slime.getEntityData().get(WOBBLE_ANIM_PROGRESS);
        this.currentSize = slime.getEntityData().get(CURRENT_SIZE);

        for (int index = 0; index < this.landDelays.size(); index++) {
            int array = this.landDelays.getInt(index);
            array -= 1;
            this.landDelays.set(index, array);
            if (array <= 0) {
                SlimeMethods.spawnSlimeLandParticles(slime);
                slime.playSound(slime.getSquishSound(), slime.getSoundVolume(), ((slime.getRandom().nextFloat() - slime.getRandom().nextFloat()) * 0.2F + 1.0F) / 0.8F);
                slime.targetSquish = -0.5F;
            }
        }
        this.landDelays.removeIf((integer -> integer <= 0));

        if (!slime.level.isClientSide) {
            slime.getEntityData().set(JUMP_ANTIC, this.jumpAntic);
        }
        this.jumpAntic = Slime.class.cast(this).getEntityData().get(JUMP_ANTIC);

        if (this.jumpSquishes > 0) {
            if (this.jumpSquishes == 3 && this.jumpAntic) {
                slime.targetSquish = -0.05F;
            } else if (this.jumpSquishes == 2 && this.jumpAntic) {
                slime.targetSquish = -0.15F;
            } else if (this.jumpSquishes == 1 && this.jumpAntic) {
                slime.targetSquish = -0.3F;
            }
            --this.jumpSquishes;
        }
    }

    @Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/monster/Slime;targetSquish:F", ordinal = 1, shift = At.Shift.BEFORE), method = "tick")
    public void captureSquish(CallbackInfo info) {
        Slime slime = Slime.class.cast(this);
        this.prevTargetSquish = slime.targetSquish;
        this.landDelays.add(2);
    }

    @Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/monster/Slime;targetSquish:F", ordinal = 1, shift = At.Shift.AFTER), method = "tick")
    public void afterField(CallbackInfo info) {
        Slime slime = Slime.class.cast(this);
        slime.targetSquish = this.prevTargetSquish;
    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void mergeCooldownTick(CallbackInfo info) {
        this.setMergeCooldown(this.getMergeCooldown() - 1);
    }

    @Inject(at = @At("HEAD"), method = "finalizeSpawn")
    public void finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag, CallbackInfoReturnable<SpawnGroupData> info) {
        Slime slime = Slime.class.cast(this);
        ((SlimeInterface)slime).playWobbleAnim();
        if (mobSpawnType == MobSpawnType.NATURAL || mobSpawnType == MobSpawnType.SPAWNER || mobSpawnType == MobSpawnType.CHUNK_GENERATION) {
            ((SlimeInterface)slime).setMergeCooldown(ConfigValueGetter.spawnedMergeCooldown());
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;setBaseValue(D)V", ordinal = 0, shift = At.Shift.AFTER), method = "setSize")
    public void oddHealth(int i, boolean bl, CallbackInfo info) {
        int clampedSize = Mth.clamp(i, 1, 127);
        Slime.class.cast(this).getAttribute(Attributes.MAX_HEALTH).setBaseValue(clampedSize % 2 == 0 ? clampedSize * clampedSize : clampedSize);
    }

    @Inject(at = @At("HEAD"), method = "decreaseSquish", cancellable = true)
    public void decreaseSquish(CallbackInfo info) {
        if (this.jumpAntic) {
            info.cancel();
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;tick()V", shift = At.Shift.BEFORE), method = "tick")
    public void moveDecreaseSquish(CallbackInfo info) {
        Slime.class.cast(this).decreaseSquish();
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Slime;decreaseSquish()V"), method = "tick")
    public void stopDecreaseSquish(Slime slime) { }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"), method = "tick")
    public void stopParticles(Level level, ParticleOptions options, double d, double e, double f, double g, double h, double i) { }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Slime;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"), method = "tick")
    public void stopSound(Slime slime, SoundEvent event, float f, float g) { }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"), method = "remove")
    public boolean beforeSpawnNewSlime(Level par1, Entity par2) {
        if (par2 instanceof Slime) {
            ((SlimeInterface) par2).setMergeCooldown(Math.max(ConfigValueGetter.onSplitCooldown(), ConfigValueGetter.splitCooldown()) * 2);
        }
        Slime slime = Slime.class.cast(this);
        par2.setSilent(slime.isSilent());
        return par1.addFreshEntity(par2);
    }

    @Unique
    @Override
    public float prevSquish() {
        return this.previousSquish;
    }

    @Unique
    @Override
    public int getMergeCooldown() {
        return this.mergeCooldown;
    }

    @Unique
    @Override
    public void setMergeCooldown(int i) {
        this.mergeCooldown = i;
    }

    @Unique
    @Override
    public float wobbleAnimProgress(float tickDelta) {
        return 1F - (Mth.lerp(tickDelta, this.prevWobbleAnim, this.wobbleAnim) / WOBBLE_ANIM_LENGTH);
    }

    @Unique
    @Override
    public void playWobbleAnim() {
        Slime slime = Slime.class.cast(this);
        if (slime.getEntityData().get(WOBBLE_ANIM_PROGRESS) == 0) {
            slime.getEntityData().set(WOBBLE_ANIM_PROGRESS, WOBBLE_ANIM_LENGTH);
        }
    }

    @Unique
    @Override
    public float getSizeScale(float tickDelta) {
        return Mth.lerp(tickDelta, this.prevSize, this.currentSize);
    }

    @Unique
    @Override
    public void cheatSize(float f) {
        Slime slime = Slime.class.cast(this);
        slime.getEntityData().set(PREV_SIZE, f);
        slime.getEntityData().set(CURRENT_SIZE, f);
        this.prevSize = f;
        this.currentSize = f;
    }

    @Unique
    @Override
    public void setJumpAntic(boolean bl) {
        this.jumpAntic = bl;
    }

    @Override
    public void setJumpAnticTicks(int i) {
        this.jumpSquishes = i;
    }

    @Unique
    @Override
    public int getJumpDelay() {
        return this.jumpDelay;
    }

    @Unique
    @Override
    public void setJumpDelay(int i) {
        this.jumpDelay = i;
    }

}