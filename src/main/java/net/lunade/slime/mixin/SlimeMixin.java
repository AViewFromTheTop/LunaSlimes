package net.lunade.slime.mixin;

import net.lunade.slime.SlimeMethods;
import net.lunade.slime.impl.SlimeInterface;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Slime.class)
public class SlimeMixin implements SlimeInterface {
    @Unique private static final EntityDataAccessor<Integer> PREV_SPAWN_PROGRESS = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.INT);
    @Unique private static final EntityDataAccessor<Integer> SPAWN_ANIM_PROGRESS = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.INT);
    @Unique private static final EntityDataAccessor<Integer> PREV_SPLIT_PROGRESS = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.INT);
    @Unique private static final EntityDataAccessor<Integer> SPLIT_ANIM_PROGRESS = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.INT);

    @Unique private static final int SPAWN_ANIM_LENGTH = 20;
    @Unique private static final int SPLIT_ANIM_LENGTH = 10;

    @Unique public int mergeCooldown;

    @Unique public float previousSquish;
    @Unique public int prevSpawnAnim;
    @Unique public int spawnAnim;
    @Unique public int prevSplitAnim;
    @Unique public int splitAnim;

    @Inject(at = @At("TAIL"), method = "defineSynchedData")
    protected void defineSynchedData(CallbackInfo info) {
        Slime slime = Slime.class.cast(this);
        slime.getEntityData().define(PREV_SPAWN_PROGRESS, 0);
        slime.getEntityData().define(SPAWN_ANIM_PROGRESS, 0);
        slime.getEntityData().define(PREV_SPLIT_PROGRESS, 0);
        slime.getEntityData().define(SPLIT_ANIM_PROGRESS, 0);
    }

    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
    public void addAdditionalSaveData(CompoundTag compoundTag, CallbackInfo info) {
        Slime slime = Slime.class.cast(this);
        compoundTag.putInt("PrevSpawnAnimProgress", slime.getEntityData().get(PREV_SPAWN_PROGRESS));
        compoundTag.putInt("SpawnAnimProgress", slime.getEntityData().get(SPAWN_ANIM_PROGRESS));
        compoundTag.putInt("PrevSplitAnimProgress", slime.getEntityData().get(PREV_SPLIT_PROGRESS));
        compoundTag.putInt("SplitAnimProgress", slime.getEntityData().get(SPLIT_ANIM_PROGRESS));
        compoundTag.putInt("MergeCooldown", this.getMergeCooldown());
    }

    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    public void readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo info) {
        Slime slime = Slime.class.cast(this);
        slime.getEntityData().set(PREV_SPAWN_PROGRESS, compoundTag.getInt("PrevSpawnAnimProgress"));
        slime.getEntityData().set(SPAWN_ANIM_PROGRESS, compoundTag.getInt("SpawnAnimProgress"));
        slime.getEntityData().set(PREV_SPLIT_PROGRESS, compoundTag.getInt("PrevSplitAnimProgress"));
        slime.getEntityData().set(SPLIT_ANIM_PROGRESS, compoundTag.getInt("SplitAnimProgress"));
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
        slime.getEntityData().set(PREV_SPAWN_PROGRESS, slime.getEntityData().get(SPAWN_ANIM_PROGRESS));
        slime.getEntityData().set(PREV_SPLIT_PROGRESS, slime.getEntityData().get(SPLIT_ANIM_PROGRESS));
        this.prevSpawnAnim = slime.getEntityData().get(PREV_SPAWN_PROGRESS);
        this.prevSplitAnim = slime.getEntityData().get(PREV_SPLIT_PROGRESS);

        slime.getEntityData().set(SPAWN_ANIM_PROGRESS, Math.max(0, slime.getEntityData().get(SPAWN_ANIM_PROGRESS) - 1));
        slime.getEntityData().set(SPLIT_ANIM_PROGRESS, Math.max(0, slime.getEntityData().get(SPLIT_ANIM_PROGRESS) - 1));
        this.spawnAnim = slime.getEntityData().get(SPAWN_ANIM_PROGRESS);
        this.splitAnim = slime.getEntityData().get(SPLIT_ANIM_PROGRESS);
    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void mergeCooldownTick(CallbackInfo info) {
        this.setMergeCooldown(this.getMergeCooldown() - 1);
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
    public float spawnAnimProgress(float tickDelta) {
        return 1F - (Mth.lerp(tickDelta, this.prevSpawnAnim, this.spawnAnim) / SPAWN_ANIM_LENGTH);
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

}