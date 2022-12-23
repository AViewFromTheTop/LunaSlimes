package net.lunade.slime;

import net.lunade.slime.impl.SlimeInterface;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class SlimeMethods {

    public static void mergeSlimes(Slime slime1, Slime slime2) {
        int thisSize = slime1.getSize();
        int otherSize = slime2.getSize();
        if ((thisSize > otherSize || thisSize == otherSize) && thisSize <= 6 && ((SlimeInterface)slime1).getMergeCooldown() <= 0 && ((SlimeInterface)slime2).getMergeCooldown() <= 0) {
            EntityDimensions oldDimensions = getDimensionsForSize(slime1, thisSize);
            EntityDimensions inflated = getDimensionsForSize(slime1, thisSize + 1);
            Vec3 newPos = slime1.position().add(0F, (inflated.height - oldDimensions.height) * 0.5F, 0F);
            Vec3 vec3 = slime1.getDeltaMovement();
            Vec3 vec32 = collideWithBox(slime1, vec3, inflated.makeBoundingBox(newPos));
            boolean horizontalCollision = !Mth.equal(vec3.x, vec32.x) || !Mth.equal(vec3.z, vec32.z);
            boolean verticalCollision = vec3.y != vec32.y;
            if (!horizontalCollision && !verticalCollision) {
                slime1.setSize(thisSize + 1, true);
                ((SlimeInterface)slime1).playSplitAnim();
                slime1.setPos(newPos);
                if (otherSize - 1 <= 0) {
                    slime2.discard();
                } else {
                    slime2.setSize(otherSize - 1, true);
                }
            }
        }
    }

    public static Vec3 collideWithBox(Slime slime, Vec3 vec3, AABB aABB) {
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

    public static EntityDimensions getDimensionsForSize(Slime slime, int size) {
        return slime.getType().getDimensions().scale(0.255f * (float)size);
    }

    public static int spawnSingleSlime(Slime origin) {
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
                if (splitOff == 1) {
                    slime.setHealth(slime.getHealth() * 0.5F);
                }
                slime.moveTo(origin.getX() + (double) g, origin.getY() + 0.5, origin.getZ() + (double) h, origin.getRandom().nextFloat() * 360.0f, 0.0f);
                ((SlimeInterface)origin).setMergeCooldown(100);
                ((SlimeInterface)origin).playSplitAnim();
                ((SlimeInterface)slime).playSplitAnim();
                SlimeMethods.spawnSlimeParticles(origin);
                origin.level.addFreshEntity(slime);
            }
        }
        return splitOff;
    }

    public static void spawnSlimeParticles(Slime slime) {
        if (slime.level instanceof ServerLevel level) {
            level.sendParticles(slime.getParticleType(), slime.getX(), slime.getY(0.6666666666666666D), slime.getZ(), level.random.nextInt(slime.getSize() * 6, slime.getSize() * 12), slime.getBbWidth() / 4.0F, slime.getBbHeight() / 4.0F, slime.getBbWidth() / 4.0F, 0.05D);
        }
    }

}
