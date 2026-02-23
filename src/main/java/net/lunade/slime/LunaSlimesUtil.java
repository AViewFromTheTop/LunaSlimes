package net.lunade.slime;

import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.lunade.slime.config.getter.LunaSlimesConfigValueGetter;
import net.lunade.slime.impl.SlimeInterface;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LunaSlimesUtil {

	public static void mergeSlimes(Slime slime1, Slime slime2) {
		final EntityType<? extends Slime> entityType = slime1.getType();
		if (slime2.getType() != entityType || !slime1.isAlive() || !slime2.isAlive()) return;
		if (!(slime1 instanceof SlimeInterface slimeInterface1) || !(slime2 instanceof SlimeInterface slimeInterface2)) return;

		final int thisSize = slime1.getSize();
		final int otherSize = slime2.getSize();

		if (thisSize < otherSize && thisSize != otherSize) return;
		if (thisSize > LunaSlimesConfigValueGetter.maxSize() - 1) return;
		if (thisSize > LunaSlimesConfigValueGetter.maxSize() - 1) return;
		if (slimeInterface1.lunaSlimes$getMergeCooldown() > 0) return;
		if (slimeInterface2.lunaSlimes$getMergeCooldown() > 0) return;

		final EntityDimensions oldDimensions = getDimensionsForSize(slime1, thisSize);
		final EntityDimensions inflated = getDimensionsForSize(slime1, thisSize + 1);
		final Vec3 newPos = slime1.position().add(0F, (inflated.height() - oldDimensions.height()) * 0.5F, 0F);
		final Vec3 deltaMovement = slime1.getDeltaMovement();
		final Vec3 vec32 = collideWithBox(slime1, deltaMovement, inflated.makeBoundingBox(newPos));
		final boolean horizontalCollision = !Mth.equal(deltaMovement.x, vec32.x) || !Mth.equal(deltaMovement.z, vec32.z);
		final boolean verticalCollision = deltaMovement.y != vec32.y;
		if (horizontalCollision || verticalCollision) return;

		slime1.setSize(thisSize + 1, true);
		slimeInterface1.lunaSlimes$setMergeCooldown(LunaSlimesConfigValueGetter.mergeCooldown());
		slimeInterface1.lunaSlimes$playWobbleAnim();
		if (LunaSlimesConfigValueGetter.mergeSounds()) {
			final RandomSource random = slime1.getRandom();
			slime1.playSound(
				entityType == EntityType.MAGMA_CUBE ? LunaSlimes.MAGMA_MERGE : LunaSlimes.SLIME_MERGE,
				slime1.getSoundVolume(),
				1F + (random.nextFloat() - random.nextFloat()) * 0.4F
			);
		}
		slimeInterface2.lunaSlimes$playWobbleAnim();

		if (slime2.isPersistenceRequired()) slime1.setPersistenceRequired();
		if (slime2.hasCustomName() && !slime1.hasCustomName()) slime1.setCustomName(slime2.getCustomName());
		slime1.setInvulnerable(slime2.isInvulnerable());
		slime1.setSilent(slime2.isSilent());
		slime1.setRemainingFireTicks((int) Math.max(slime1.getRemainingFireTicks(), slime2.getRemainingFireTicks() * 0.5));
		slime1.setTicksFrozen((int) Math.max(slime1.getTicksFrozen(), slime2.getTicksFrozen() * 0.5));
		slime1.setPos(newPos);
		if (otherSize - 1 <= 0) {
			slime2.discard();
		} else {
			slime2.setSize(otherSize - 1, true);
		}
	}

	public static int spawnSingleSlime(Slime origin) {
		final int originalSize = origin.getSize();
		if (origin.level().isClientSide() || originalSize <= 0) return 0;
		if (!(origin instanceof SlimeInterface originInterface)) return 0;

		int splitOff = 0;
		final RandomSource random = origin.getRandom();
		final Component customName = origin.getCustomName();
		final EntityType<? extends Slime> entityType = origin.getType();
		final Slime slime = entityType.create(origin.level(), EntitySpawnReason.TRIGGERED);
		if (!(slime instanceof SlimeInterface slimeInterface)) return splitOff;

		final float quarterSize = (float) originalSize / 4F;
		int posRandom = (int) ((2 + random.nextInt(3)) * random.nextDouble());
		float xRandom = ((float) (posRandom % 2) - 0.5F) * quarterSize;
		float zRandom = ((float) (posRandom / 2) - 0.5F) * quarterSize;
		slime.snapTo(origin.getX() + (double) xRandom, origin.getY() + 0.5D, origin.getZ() + (double) zRandom, random.nextFloat() * 360F, 0F);

		if (origin.isPersistenceRequired()) slime.setPersistenceRequired();
		slime.setCustomName(customName);
		slime.setNoAi(origin.isNoAi());
		slime.setInvulnerable(origin.isInvulnerable());
		slime.setSilent(origin.isSilent());
		slime.setSize(splitOff = originalSize % 2 == 0 ? (int) (originalSize * 0.5) : 1, true);
		slime.setRemainingFireTicks(origin.getRemainingFireTicks());
		slime.setTicksFrozen(origin.getTicksFrozen());
		slime.setDeltaMovement(origin.getDeltaMovement());

		originInterface.lunaSlimes$setMergeCooldown(LunaSlimesConfigValueGetter.onSplitCooldown());
		slimeInterface.lunaSlimes$setMergeCooldown(LunaSlimesConfigValueGetter.splitCooldown());
		originInterface.lunaSlimes$playWobbleAnim();
		slimeInterface.lunaSlimes$playWobbleAnim();
		LunaSlimesUtil.spawnSlimeParticles(origin);

		origin.level().addFreshEntity(slime);
		if (LunaSlimesConfigValueGetter.splitSounds()) {
			slime.playSound(
				entityType == EntityType.MAGMA_CUBE ? LunaSlimes.MAGMA_SPLIT : LunaSlimes.SLIME_SPLIT,
				slime.getSoundVolume(),
				1F + (random.nextFloat() - random.nextFloat()) * 0.4F
			);
		}

		return splitOff;
	}

	public static void spawnSlimeParticles(Slime slime) {
		if (!(slime.level() instanceof ServerLevel level) || !LunaSlimesConfigValueGetter.particles()) return;
		final int size = slime.getSize();
		final double horizontalSpread = slime.getBbWidth() / 4F;
		final double verticalSpread = slime.getBbHeight() / 4F;
		level.sendParticles(slime.getParticleType(), slime.getX(), slime.getY(0.6666666666666666D), slime.getZ(), level.getRandom().nextInt(size * 6, size * 12), horizontalSpread, verticalSpread, horizontalSpread, 0.05D);
	}

	public static void spawnSlimeLandParticles(Slime slime) {
		if (!(slime.level() instanceof ServerLevel level)) return;
		final int size = slime.getSize();
		final double horizontalSpread = slime.getBbWidth() / 3.5F;
		level.sendParticles(slime.getParticleType(), slime.getX(), slime.getY(), slime.getZ(), level.getRandom().nextInt(size * 6, size * 8), horizontalSpread, 0F, horizontalSpread, 0.05D);
	}

	public static float getSlimeScale(Slime slime, float partialTick) {
		if (!(slime instanceof SlimeInterface slimeInterface)) return slime.getSize();
		return (LunaSlimesConfigValueGetter.growAnim() ? slimeInterface.lunaSlimes$getSizeScale(partialTick) : slime.getSize())
			* slimeInterface.lunaSlimes$getDeathProgress(partialTick);
	}

	public static float getSlimeWobbleAnimProgress(Slime slime, float partialTick) {
		if (!(slime instanceof SlimeInterface slimeInterface) || !LunaSlimesConfigValueGetter.wobbleAnim()) return 0F;
		return slimeInterface.lunaSlimes$wobbleAnimProgress(partialTick);
	}

	public static Pair<Float, Float> wobbleAnim(Slime slime, float partialTick) {
		final float cosWobble = (float) Math.cos((((LunaSlimesUtil.getSlimeWobbleAnimProgress(slime, partialTick) + (0.0955F * Math.PI)) * Math.PI) * 5F));
		return Pair.of((cosWobble * 0.1F) + 1F, -(cosWobble * 0.025F) + 1F);
	}

	public static void setSquish(Slime slime, float squish) {
		if (squish < slime.targetSquish) slime.targetSquish = squish;
	}

	public static void setStretch(Slime slime, float stretch) {
		if (stretch > slime.targetSquish) slime.targetSquish = stretch;
	}

	private static EntityDimensions getDimensionsForSize(Slime slime, int size) {
		return slime.getType().getDimensions().scale(0.255F * (float) size);
	}

	private static Vec3 collideWithBox(Slime slime, Vec3 deltaMovement, AABB aABB) {
		final List<VoxelShape> list = slime.level().getEntityCollisions(slime, aABB.expandTowards(deltaMovement));
		final Vec3 collide = deltaMovement.lengthSqr() == 0D ? deltaMovement : Entity.collideBoundingBox(slime, deltaMovement, aABB, slime.level(), list);
		final boolean onGroundOrFalling = slime.onGround() || deltaMovement.y != collide.y && deltaMovement.y < 0F;
		if (!(slime.maxUpStep() > 0F && onGroundOrFalling && (deltaMovement.x != collide.x || deltaMovement.z != collide.z))) return collide;

		Vec3 vec35;
		Vec3 vec33 = Entity.collideBoundingBox(slime, new Vec3(deltaMovement.x, slime.maxUpStep(), deltaMovement.z), aABB, slime.level(), list);
		Vec3 vec34 = Entity.collideBoundingBox(slime, new Vec3(0F, slime.maxUpStep(), 0F), aABB.expandTowards(deltaMovement.x, 0F, deltaMovement.z), slime.level(), list);
		if (vec34.y < (double) slime.maxUpStep() && (vec35 = Entity.collideBoundingBox(slime, new Vec3(deltaMovement.x, 0F, deltaMovement.z), aABB.move(vec34), slime.level(), list).add(vec34)).horizontalDistanceSqr() > vec33.horizontalDistanceSqr()) {
			vec33 = vec35;
		}
		if (vec33.horizontalDistanceSqr() > collide.horizontalDistanceSqr()) {
			return vec33.add(Entity.collideBoundingBox(slime, new Vec3(0F, -vec33.y + deltaMovement.y, 0F), aABB.move(vec33), slime.level(), list));
		}
		return collide;
	}

}
