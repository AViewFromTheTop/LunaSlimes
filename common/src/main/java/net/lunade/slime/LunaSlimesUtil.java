/*
 * Copyright 2026 Lunade Music/AViewFromTheTop
 * This file is part of Luna Slimes.
 *
 * This program is free software; you can modify it under
 * the terms of version 1 of the FrozenBlock Modding Oasis License
 * as published by FrozenBlock Modding Oasis.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * FrozenBlock Modding Oasis License for more details.
 *
 * You should have received a copy of the FrozenBlock Modding Oasis License
 * along with this program; if not, see <https://github.com/FrozenBlock/Licenses>.
 */

package net.lunade.slime;

import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.lunade.slime.config.LSGameplayConfig;
import net.lunade.slime.config.LSVisualsAudioConfig;
import net.lunade.slime.impl.AbstractCubeMobInterface;
import net.lunade.slime.registry.LSAttachmentTypes;
import net.lunade.slime.registry.LSSoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.cubemob.AbstractCubeMob;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class LunaSlimesUtil {

	public static void mergeCubes(AbstractCubeMob cube1, AbstractCubeMob cube2) {
		final EntityType<? extends AbstractCubeMob> entityType = cube1.getType();
		if (!cube2.is(entityType) || !cube1.isAlive() || !cube2.isAlive() || entityType == EntityTypes.SULFUR_CUBE) return;
		if (!(cube1 instanceof AbstractCubeMobInterface abstractCubeMobInterface1) || !(cube2 instanceof AbstractCubeMobInterface abstractCubeMobInterface2)) return;

		final int thisSize = cube1.getSize();
		final int otherSize = cube2.getSize();
		if (thisSize < otherSize && thisSize != otherSize) return;
		if (thisSize > LSGameplayConfig.MAX_SIZE.get() - 1 || otherSize > LSGameplayConfig.MAX_SIZE.get() - 1) return;
		if (cube1.frozenLib$getAttachedOrCreate(LSAttachmentTypes.MERGE_COOLDOWN) > 0 || cube2.frozenLib$getAttachedOrCreate(LSAttachmentTypes.MERGE_COOLDOWN) > 0) return;

		final EntityDimensions oldDimensions = getDimensionsForSize(cube1, thisSize);
		final EntityDimensions inflated = getDimensionsForSize(cube1, thisSize + 1);
		final Vec3 newPos = cube1.position().add(0F, (inflated.height() - oldDimensions.height()) * 0.5F, 0F);
		final Vec3 deltaMovement = cube1.getDeltaMovement();
		final Vec3 vec32 = collideWithBox(cube1, deltaMovement, inflated.makeBoundingBox(newPos));
		final boolean horizontalCollision = !Mth.equal(deltaMovement.x, vec32.x) || !Mth.equal(deltaMovement.z, vec32.z);
		final boolean verticalCollision = deltaMovement.y != vec32.y;
		if (horizontalCollision || verticalCollision) return;

		cube1.setSize(thisSize + 1, true);
		cube1.frozenLib$setAttached(LSAttachmentTypes.MERGE_COOLDOWN, LSGameplayConfig.MERGE_COOLDOWN.get());
		abstractCubeMobInterface1.lunaSlimes$playWobbleAnim();
		if (LSVisualsAudioConfig.MERGE_SOUNDS.get()) {
			final RandomSource random = cube1.getRandom();
			cube1.playSound(
				entityType == EntityTypes.MAGMA_CUBE ? LSSoundEvents.MAGMACUBE_MERGE.get() : LSSoundEvents.SLIME_MERGE.get(),
				cube1.getSoundVolume(),
				1F + (random.nextFloat() - random.nextFloat()) * 0.4F
			);
		}
		abstractCubeMobInterface2.lunaSlimes$playWobbleAnim();

		if (cube2.isPersistenceRequired()) cube1.setPersistenceRequired();
		if (cube2.hasCustomName() && !cube1.hasCustomName()) cube1.setCustomName(cube2.getCustomName());
		cube1.setInvulnerable(cube2.isInvulnerable());
		cube1.setSilent(cube2.isSilent());
		cube1.setRemainingFireTicks((int) Math.max(cube1.getRemainingFireTicks(), cube2.getRemainingFireTicks() * 0.5));
		cube1.setTicksFrozen((int) Math.max(cube1.getTicksFrozen(), cube2.getTicksFrozen() * 0.5));
		cube1.setPos(newPos);
		if (otherSize - 1 <= 0) {
			cube2.discard();
		} else {
			cube2.setSize(otherSize - 1, true);
		}
	}

	public static int spawnSingleCube(AbstractCubeMob origin) {
		final int originalSize = origin.getSize();
		if (origin.level().isClientSide() || originalSize <= 0) return 0;
		if (!(origin instanceof AbstractCubeMobInterface originInterface)) return 0;

		int splitOff = 0;
		final RandomSource random = origin.getRandom();
		final Component customName = origin.getCustomName();
		final EntityType<? extends AbstractCubeMob> entityType = origin.getType();
		final AbstractCubeMob cube = entityType.create(origin.level(), EntitySpawnReason.TRIGGERED);
		if (!(cube instanceof AbstractCubeMobInterface abstractCubeMobInterface)) return splitOff;

		final float quarterSize = (float) originalSize / 4F;
		int posRandom = (int) ((2 + random.nextInt(3)) * random.nextDouble());
		float xRandom = ((float) (posRandom % 2) - 0.5F) * quarterSize;
		float zRandom = ((float) (posRandom / 2) - 0.5F) * quarterSize;
		cube.snapTo(origin.getX() + (double) xRandom, origin.getY() + 0.5D, origin.getZ() + (double) zRandom, random.nextFloat() * 360F, 0F);

		if (origin.isPersistenceRequired()) cube.setPersistenceRequired();
		cube.setCustomName(customName);
		cube.setNoAi(origin.isNoAi());
		cube.setInvulnerable(origin.isInvulnerable());
		cube.setSilent(origin.isSilent());
		cube.setSize(splitOff = originalSize % 2 == 0 ? (int) (originalSize * 0.5) : 1, true);
		cube.setRemainingFireTicks(origin.getRemainingFireTicks());
		cube.setTicksFrozen(origin.getTicksFrozen());
		cube.setDeltaMovement(origin.getDeltaMovement());

		origin.frozenLib$setAttached(LSAttachmentTypes.MERGE_COOLDOWN, LSGameplayConfig.ON_SPLIT_COOLDOWN.get());
		cube.frozenLib$setAttached(LSAttachmentTypes.MERGE_COOLDOWN, LSGameplayConfig.SPLIT_COOLDOWN.get());
		originInterface.lunaSlimes$playWobbleAnim();
		abstractCubeMobInterface.lunaSlimes$playWobbleAnim();
		LunaSlimesUtil.spawnCubeParticles(origin);

		origin.level().addFreshEntity(cube);
		if (LSVisualsAudioConfig.SPLIT_SOUNDS.get()) {
			cube.playSound(
				entityType == EntityTypes.MAGMA_CUBE
					? LSSoundEvents.MAGMACUBE_SPLIT.get()
					: entityType == EntityTypes.SULFUR_CUBE
					  ? SoundEvents.SULFUR_CUBE_DEATH
					  : LSSoundEvents.SLIME_SPLIT.get(),
				cube.getSoundVolume(),
				1F + (random.nextFloat() - random.nextFloat()) * 0.4F
			);
		}

		return splitOff;
	}

	public static void spawnCubeParticles(AbstractCubeMob cube) {
		if (!(cube.level() instanceof ServerLevel level) || !LSVisualsAudioConfig.PARTICLES.get()) return;
		final int size = cube.getSize();
		final double horizontalSpread = cube.getBbWidth() / 4F;
		final double verticalSpread = cube.getBbHeight() / 4F;
		level.sendParticles(cube.getParticleType(), cube.getX(), cube.getY(0.6666666666666666D), cube.getZ(), level.getRandom().nextInt(size * 6, size * 12), horizontalSpread, verticalSpread, horizontalSpread, 0.05D);
	}

	public static void spawnCubeLandParticles(AbstractCubeMob cube) {
		if (!(cube.level() instanceof ServerLevel level)) return;
		final int size = cube.getSize();
		final double horizontalSpread = cube.getBbWidth() / 3.5F;
		level.sendParticles(cube.getParticleType(), cube.getX(), cube.getY(), cube.getZ(), level.getRandom().nextInt(size * 6, size * 8), horizontalSpread, 0F, horizontalSpread, 0.05D);
	}

	public static float getCubeScale(AbstractCubeMob cube, float partialTicks) {
		if (!(cube instanceof AbstractCubeMobInterface abstractCubeMobInterface)) return cube.getSize();
		return (LSVisualsAudioConfig.GROW_ANIM.get() ? abstractCubeMobInterface.lunaSlimes$getSizeScale(partialTicks) : cube.getSize())
			* abstractCubeMobInterface.lunaSlimes$getDeathProgress(partialTicks);
	}

	public static float getCubeWobbleAnimProgress(AbstractCubeMob cube, float partialTicks) {
		if (!(cube instanceof AbstractCubeMobInterface abstractCubeMobInterface) || !LSVisualsAudioConfig.WOBBLE_ANIM.get()) return 0F;
		return abstractCubeMobInterface.lunaSlimes$wobbleAnimProgress(partialTicks);
	}

	public static Pair<Float, Float> wobbleAnim(AbstractCubeMob cube, float partialTicks) {
		final float cosWobble = (float) Math.cos((((getCubeWobbleAnimProgress(cube, partialTicks) + (0.0955F * Math.PI)) * Math.PI) * 5F));
		return Pair.of((cosWobble * 0.1F) + 1F, -(cosWobble * 0.025F) + 1F);
	}

	public static void setSquish(AbstractCubeMob cube, float squish) {
		if (squish < cube.targetSquish) cube.targetSquish = squish;
	}

	public static void setStretch(AbstractCubeMob cube, float stretch) {
		if (stretch > cube.targetSquish) cube.targetSquish = stretch;
	}

	private static EntityDimensions getDimensionsForSize(AbstractCubeMob cube, int size) {
		return cube.getType().getDimensions().scale(0.255F * (float) size);
	}

	private static Vec3 collideWithBox(AbstractCubeMob cube, Vec3 deltaMovement, AABB aABB) {
		final List<VoxelShape> list = cube.level().getEntityCollisions(cube, aABB.expandTowards(deltaMovement));
		final Vec3 collide = deltaMovement.lengthSqr() == 0D ? deltaMovement : Entity.collideBoundingBox(cube, deltaMovement, aABB, cube.level(), list);
		final boolean onGroundOrFalling = cube.onGround() || deltaMovement.y != collide.y && deltaMovement.y < 0F;
		if (!(cube.maxUpStep() > 0F && onGroundOrFalling && (deltaMovement.x != collide.x || deltaMovement.z != collide.z))) return collide;

		Vec3 vec35;
		Vec3 vec33 = Entity.collideBoundingBox(cube, new Vec3(deltaMovement.x, cube.maxUpStep(), deltaMovement.z), aABB, cube.level(), list);
		Vec3 vec34 = Entity.collideBoundingBox(cube, new Vec3(0F, cube.maxUpStep(), 0F), aABB.expandTowards(deltaMovement.x, 0F, deltaMovement.z), cube.level(), list);
		if (vec34.y < (double) cube.maxUpStep() && (vec35 = Entity.collideBoundingBox(cube, new Vec3(deltaMovement.x, 0F, deltaMovement.z), aABB.move(vec34), cube.level(), list).add(vec34)).horizontalDistanceSqr() > vec33.horizontalDistanceSqr()) {
			vec33 = vec35;
		}
		if (vec33.horizontalDistanceSqr() > collide.horizontalDistanceSqr()) {
			return vec33.add(Entity.collideBoundingBox(cube, new Vec3(0F, -vec33.y + deltaMovement.y, 0F), aABB.move(vec33), cube.level(), list));
		}
		return collide;
	}

	private LunaSlimesUtil() {}
}
