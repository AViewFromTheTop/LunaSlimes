package net.lunade.slime.mixin;

import net.lunade.slime.SlimeMethods;
import net.lunade.slime.config.getter.ConfigValueGetter;
import net.lunade.slime.impl.SlimeInterface;
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
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slime.class)
public class SlimeMixin implements SlimeInterface {
    @Unique private static final EntityDataAccessor<Integer> PREV_WOBBLE_ANIM_PROGRESS = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.INT);
    @Unique private static final EntityDataAccessor<Integer> WOBBLE_ANIM_PROGRESS = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.INT);
    @Unique private static final EntityDataAccessor<Float> PREV_SIZE = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.FLOAT);
    @Unique private static final EntityDataAccessor<Float> CURRENT_SIZE = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.FLOAT);
    @Unique private static final EntityDataAccessor<Float> TARGET_SQUISH = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.FLOAT);

    @Unique private static final int WOBBLE_ANIM_LENGTH = 10;

    @Unique public int mergeCooldown;

    @Unique public float previousSquish;
    @Unique public int prevWobbleAnim;
    @Unique public int wobbleAnim;
    @Unique public float prevSize = 0F;
    @Unique public float currentSize = 0F;

    @Inject(at = @At("TAIL"), method = "defineSynchedData")
    protected void defineSynchedData(CallbackInfo info) {
        Slime slime = Slime.class.cast(this);
        slime.getEntityData().define(PREV_WOBBLE_ANIM_PROGRESS, 0);
        slime.getEntityData().define(WOBBLE_ANIM_PROGRESS, 0);
        slime.getEntityData().define(PREV_SIZE, 0F);
        slime.getEntityData().define(CURRENT_SIZE, 0F);
        slime.getEntityData().define(TARGET_SQUISH, 0F);
    }

    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
    public void addAdditionalSaveData(CompoundTag compoundTag, CallbackInfo info) {
        Slime slime = Slime.class.cast(this);
        compoundTag.putInt("PrevWobbleAnimProgress", slime.getEntityData().get(PREV_WOBBLE_ANIM_PROGRESS));
        compoundTag.putInt("WobbleAnimProgress", slime.getEntityData().get(WOBBLE_ANIM_PROGRESS));
        compoundTag.putFloat("PrevSize", slime.getEntityData().get(PREV_SIZE));
        compoundTag.putFloat("CurrentSize", slime.getEntityData().get(CURRENT_SIZE));
        compoundTag.putInt("MergeCooldown", this.getMergeCooldown());
        compoundTag.putFloat("TargetSquish", slime.getEntityData().get(TARGET_SQUISH));
    }

    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    public void readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo info) {
        Slime slime = Slime.class.cast(this);
        slime.getEntityData().set(PREV_WOBBLE_ANIM_PROGRESS, compoundTag.getInt("PrevWobbleAnimProgress"));
        slime.getEntityData().set(WOBBLE_ANIM_PROGRESS, compoundTag.getInt("WobbleAnimProgress"));
        slime.getEntityData().set(PREV_SIZE, compoundTag.getFloat("PrevSize"));
        slime.getEntityData().set(CURRENT_SIZE, compoundTag.getFloat("CurrentSize"));
        this.setMergeCooldown(compoundTag.getInt("MergeCooldown"));
        slime.getEntityData().set(TARGET_SQUISH, compoundTag.getFloat("TargetSquish"));
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
    }

    @Inject(at = @At("TAIL"), method = "tick")
    public void tickTail(CallbackInfo info) {
        Slime slime = Slime.class.cast(this);
        if (!slime.level.isClientSide) {
            slime.getEntityData().set(TARGET_SQUISH, slime.targetSquish);
            Slime.class.cast(this).decreaseSquish();
        }
        slime.targetSquish = Slime.class.cast(this).getEntityData().get(TARGET_SQUISH);
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
        if (Slime.class.cast(this).level.isClientSide) {
            info.cancel();
        }
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Slime;decreaseSquish()V"), method = "tick")
    public void stopDecreaseSquish(Slime slime) { }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"), method = "remove")
    public boolean beforeSpawnNewSlime(Level par1, Entity par2) {
        if (par2 instanceof Slime) {
            ((SlimeInterface) par2).setMergeCooldown(Math.max(ConfigValueGetter.onSplitCooldown(), ConfigValueGetter.splitCooldown()) * 2);
        }
        Slime slime = Slime.class.cast(this);
        par2.setSilent(slime.isSilent());
        return par1.addFreshEntity(par2);
    }

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

    @Override
    public float wobbleAnimProgress(float tickDelta) {
        return 1F - (Mth.lerp(tickDelta, this.prevWobbleAnim, this.wobbleAnim) / WOBBLE_ANIM_LENGTH);
    }

    @Override
    public void playWobbleAnim() {
        Slime slime = Slime.class.cast(this);
        if (slime.getEntityData().get(WOBBLE_ANIM_PROGRESS) == 0) {
            slime.getEntityData().set(WOBBLE_ANIM_PROGRESS, WOBBLE_ANIM_LENGTH);
        }
    }

    @Override
    public float getSizeScale(float tickDelta) {
        return Mth.lerp(tickDelta, this.prevSize, this.currentSize);
    }

    @Override
    public void cheatSize(float f) {
        Slime slime = Slime.class.cast(this);
        slime.getEntityData().set(PREV_SIZE, f);
        slime.getEntityData().set(CURRENT_SIZE, f);
        this.prevSize = f;
        this.currentSize = f;
    }

}