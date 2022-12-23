package net.lunade.slime.mixin;

import net.lunade.slime.SlimeMethods;
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
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slime.class)
public class SlimeMixin implements SlimeInterface {
    @Unique private static final EntityDataAccessor<Integer> PREV_SPLIT_PROGRESS = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.INT);
    @Unique private static final EntityDataAccessor<Integer> SPLIT_ANIM_PROGRESS = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.INT);
    @Unique private static final EntityDataAccessor<Float> PREV_SIZE = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.FLOAT);
    @Unique private static final EntityDataAccessor<Float> CURRENT_SIZE = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.FLOAT);

    @Unique private static final int SPLIT_ANIM_LENGTH = 10;

    @Unique public int mergeCooldown;

    @Unique public float previousSquish;
    @Unique public int prevSplitAnim;
    @Unique public int splitAnim;
    @Unique public float prevSize;
    @Unique public float currentSize;

    @Inject(at = @At("TAIL"), method = "defineSynchedData")
    protected void defineSynchedData(CallbackInfo info) {
        Slime slime = Slime.class.cast(this);
        slime.getEntityData().define(PREV_SPLIT_PROGRESS, 0);
        slime.getEntityData().define(SPLIT_ANIM_PROGRESS, 0);
        slime.getEntityData().define(PREV_SIZE, 0F);
        slime.getEntityData().define(CURRENT_SIZE, 0F);
    }

    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
    public void addAdditionalSaveData(CompoundTag compoundTag, CallbackInfo info) {
        Slime slime = Slime.class.cast(this);
        compoundTag.putInt("PrevSplitAnimProgress", slime.getEntityData().get(PREV_SPLIT_PROGRESS));
        compoundTag.putInt("SplitAnimProgress", slime.getEntityData().get(SPLIT_ANIM_PROGRESS));
        compoundTag.putFloat("PrevSize", slime.getEntityData().get(PREV_SIZE));
        compoundTag.putFloat("CurrentSize", slime.getEntityData().get(CURRENT_SIZE));
        compoundTag.putInt("MergeCooldown", this.getMergeCooldown());
    }

    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    public void readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo info) {
        Slime slime = Slime.class.cast(this);
        slime.getEntityData().set(PREV_SPLIT_PROGRESS, compoundTag.getInt("PrevSplitAnimProgress"));
        slime.getEntityData().set(SPLIT_ANIM_PROGRESS, compoundTag.getInt("SplitAnimProgress"));
        slime.getEntityData().set(PREV_SIZE, compoundTag.getFloat("PrevSize"));
        slime.getEntityData().set(CURRENT_SIZE, compoundTag.getFloat("CurrentSize"));
        this.setMergeCooldown(compoundTag.getInt("MergeCooldown"));
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
        slime.getEntityData().set(PREV_SPLIT_PROGRESS, slime.getEntityData().get(SPLIT_ANIM_PROGRESS));
        slime.getEntityData().set(PREV_SIZE, slime.getEntityData().get(CURRENT_SIZE));
        this.prevSplitAnim = slime.getEntityData().get(PREV_SPLIT_PROGRESS);
        this.prevSize = slime.getEntityData().get(PREV_SIZE);

        slime.getEntityData().set(SPLIT_ANIM_PROGRESS, Math.max(0, slime.getEntityData().get(SPLIT_ANIM_PROGRESS) - 1));
        float sizeDiff = slime.getSize() - slime.getEntityData().get(CURRENT_SIZE);
        slime.getEntityData().set(CURRENT_SIZE, slime.getEntityData().get(CURRENT_SIZE) + sizeDiff * 0.25F);
        this.splitAnim = slime.getEntityData().get(SPLIT_ANIM_PROGRESS);
        this.currentSize = slime.getEntityData().get(CURRENT_SIZE);
    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void mergeCooldownTick(CallbackInfo info) {
        this.setMergeCooldown(this.getMergeCooldown() - 1);
    }

    @Inject(at = @At("HEAD"), method = "finalizeSpawn")
    public void finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag, CallbackInfoReturnable<SpawnGroupData> info) {
        ((SlimeInterface)Slime.class.cast(this)).playSplitAnim();
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
    public float splitAnimProgress(float tickDelta) {
        return 1F - (Mth.lerp(tickDelta, this.prevSplitAnim, this.splitAnim) / SPLIT_ANIM_LENGTH);
    }

    @Override
    public void playSplitAnim() {
        Slime slime = Slime.class.cast(this);
        slime.getEntityData().set(SPLIT_ANIM_PROGRESS, SPLIT_ANIM_LENGTH);
    }

    @Override
    public float getSizeScale(float tickDelta) {
        return Mth.lerp(tickDelta, this.prevSize, this.currentSize);
    }

}