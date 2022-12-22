package net.lunade.slime.mixin;

import net.lunade.slime.impl.SlimeInterface;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Slime.class)
public class SlimeMixin implements SlimeInterface {
    @Unique private static final EntityDataAccessor<Integer> PREV_SPAWN_PROGRESS = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.INT);
    @Unique private static final EntityDataAccessor<Integer> SPAWN_ANIM_PROGRESS = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.INT);
    @Unique private static final EntityDataAccessor<Integer> PREV_SPLIT_PROGRESS = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.INT);
    @Unique private static final EntityDataAccessor<Integer> SPLIT_ANIM_PROGRESS = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.INT);

    @Unique private static final int SPAWN_ANIM_LENGTH = 20;
    @Unique private static final int SPLIT_ANIM_LENGTH = 20;

    @Unique public float previousSquish;
    @Unique public int mergeCooldown;

    @Inject(at = @At("TAIL"), method = "defineSynchedData")
    protected void defineSynchedData(CallbackInfo info) {
        Slime slime = Slime.class.cast(this);
        slime.getEntityData().define(PREV_SPAWN_PROGRESS, SPAWN_ANIM_LENGTH);
        slime.getEntityData().define(SPAWN_ANIM_PROGRESS, SPAWN_ANIM_LENGTH);
        slime.getEntityData().define(PREV_SPLIT_PROGRESS, SPLIT_ANIM_LENGTH);
        slime.getEntityData().define(SPLIT_ANIM_PROGRESS, SPLIT_ANIM_LENGTH);
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
            this.mergeSlimes(slime2);
        }
    }

    @Unique
    public void mergeSlimes(Slime slime2) {
        Slime slime1 = Slime.class.cast(this);
        int thisSize = slime1.getSize();
        int otherSize = slime2.getSize();
        if ((thisSize > otherSize || thisSize == otherSize) && thisSize <= 6 && ((SlimeInterface)slime1).getMergeCooldown() <= 0 && ((SlimeInterface)slime2).getMergeCooldown() <= 0) {
            EntityDimensions oldDimensions = this.getDimensionsForSize(thisSize);
            EntityDimensions inflated = this.getDimensionsForSize(thisSize + 1);
            Vec3 newPos = slime1.position().add(0F, (inflated.height - oldDimensions.height) * 0.5F, 0F);
            Vec3 vec3 = slime1.getDeltaMovement();
            Vec3 vec32 = this.collideWithBox(vec3, inflated.makeBoundingBox(newPos));
            boolean horizontalCollision = !Mth.equal(vec3.x, vec32.x) || !Mth.equal(vec3.z, vec32.z);
            boolean verticalCollision = vec3.y != vec32.y;
            if (!horizontalCollision && !verticalCollision) {
                slime1.setSize(thisSize + 1, true);
                slime1.setPos(newPos);
                if (otherSize - 1 <= 0) {
                    slime2.discard();
                } else {
                    slime2.setSize(otherSize - 1, true);
                }
            }
        }
    }

    @Unique
    public final Vec3 collideWithBox(Vec3 vec3, AABB aABB) {
        Slime slime = Slime.class.cast(this);
        List<VoxelShape> list = slime.level.getEntityCollisions(slime, aABB.expandTowards(vec3));
        Vec3 vec32 = vec3.lengthSqr() == 0.0 ? vec3 : Entity.collideBoundingBox(slime, vec3, aABB, slime.level, list);
        boolean bool = slime.isOnGround() || vec3.y != vec32.y && vec3.y < 0.0;
        if (slime.maxUpStep > 0.0f && bool && (vec3.x != vec32.x || vec3.z != vec32.z)) {
            Vec3 vec35;
            Vec3 vec33 = Entity.collideBoundingBox(slime, new Vec3(vec3.x, slime.maxUpStep, vec3.z), aABB, slime.level, list);
            Vec3 vec34 = Entity.collideBoundingBox(slime, new Vec3(0.0, slime.maxUpStep, 0.0), aABB.expandTowards(vec3.x, 0.0, vec3.z), slime.level, list);
            if (vec34.y < (double)slime.maxUpStep && (vec35 = Entity.collideBoundingBox(slime, new Vec3(vec3.x, 0.0, vec3.z), aABB.move(vec34), slime.level, list).add(vec34)).horizontalDistanceSqr() > vec33.horizontalDistanceSqr()) {
                vec33 = vec35;
            }
            if (vec33.horizontalDistanceSqr() > vec32.horizontalDistanceSqr()) {
                return vec33.add(Entity.collideBoundingBox(slime, new Vec3(0.0, -vec33.y + vec3.y, 0.0), aABB.move(vec33), slime.level, list));
            }
        }
        return vec32;
    }

    @Unique
    public EntityDimensions getDimensionsForSize(int size) {
        Slime slime = Slime.class.cast(this);
        return slime.getType().getDimensions().scale(0.255f * (float)size);
    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void squishAnimFix(CallbackInfo info) {
        this.previousSquish = Slime.class.cast(this).squish;
    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void mergeCooldownTick(CallbackInfo info) {
        this.setMergeCooldown(this.getMergeCooldown() - 1);
    }

    @Override
    public float prevSquish() {
        return this.previousSquish;
    }

    @Override
    public int spawnSingleSlime() {
        Slime origin = Slime.class.cast(this);
        int i = origin.getSize();
        int splitOff = 0;
        if (!origin.level.isClientSide && i > 1) {
            Component component = origin.getCustomName();
            boolean bl = origin.isNoAi();
            float f = (float)i / 4.0f;
            int l = (int) ((2 + origin.getRandom().nextInt(3)) * origin.getRandom().nextDouble());
            float g = ((float)(l % 2) - 0.5f) * f;
            float h = ((float)(l / 2) - 0.5f) * f;
            Slime slime = origin.getType().create(origin.level);
            if (slime != null) {
                if (origin.isPersistenceRequired()) {
                    slime.setPersistenceRequired();
                }
                slime.setCustomName(component);
                slime.setNoAi(bl);
                slime.setInvulnerable(origin.isInvulnerable());
                slime.setSize(splitOff = i % 2 == 0 ? (int) (i * 0.5) : 1, true);
                slime.moveTo(origin.getX() + (double) g, origin.getY() + 0.5, origin.getZ() + (double) h, origin.getRandom().nextFloat() * 360.0f, 0.0f);
                ((SlimeInterface)slime).setMergeCooldown(100);
                origin.level.addFreshEntity(slime);
            }
        }
        return splitOff;
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

}