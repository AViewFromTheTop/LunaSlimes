package net.lunade.slime;

import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.lunade.slime.config.frozenlib.LunaSlimesGameplayConfig;
import net.lunade.slime.config.frozenlib.LunaSlimesVisualsAudioConfig;
import net.lunade.slime.impl.SlimeInterface;
import net.lunade.slime.registry.LunaSlimesAttachmentTypes;
import net.lunade.slime.registry.LunaSlimesSounds;
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

public class LunaSlimesUtil {

	public static void mergeCubes(AbstractCubeMob cube1, AbstractCubeMob cube2) {
		final EntityType<? extends AbstractCubeMob> entityType = cube1.getType();
		if (!cube2.is(entityType) || !cube1.isAlive() || !cube2.isAlive() || entityType == EntityTypes.SULFUR_CUBE) return;
		if (!(cube1 instanceof SlimeInterface slimeInterface1) || !(cube2 instanceof SlimeInterface slimeInterface2)) return;

		final int thisSize = cube1.getSize();
		final int otherSize = cube2.getSize();
		if (thisSize < otherSize && thisSize != otherSize) return;
		if (thisSize > LunaSlimesGameplayConfig.MAX_SIZE.get() - 1 || otherSize > LunaSlimesGameplayConfig.MAX_SIZE.get() - 1) return;
		if (cube1.getAttachedOrCreate(LunaSlimesAttachmentTypes.MERGE_COOLDOWN) > 0 || cube2.getAttachedOrCreate(LunaSlimesAttachmentTypes.MERGE_COOLDOWN) > 0) return;

		final EntityDimensions oldDimensions = getDimensionsForSize(cube1, thisSize);
		final EntityDimensions inflated = getDimensionsForSize(cube1, thisSize + 1);
		final Vec3 newPos = cube1.position().add(0F, (inflated.height() - oldDimensions.height()) * 0.5F, 0F);
		final Vec3 deltaMovement = cube1.getDeltaMovement();
		final Vec3 vec32 = collideWithBox(cube1, deltaMovement, inflated.makeBoundingBox(newPos));
		final boolean horizontalCollision = !Mth.equal(deltaMovement.x, vec32.x) || !Mth.equal(deltaMovement.z, vec32.z);
		final boolean verticalCollision = deltaMovement.y != vec32.y;
		if (horizontalCollision || verticalCollision) return;

		cube1.setSize(thisSize + 1, true);
		cube1.setAttached(LunaSlimesAttachmentTypes.MERGE_COOLDOWN, LunaSlimesGameplayConfig.MERGE_COOLDOWN.get());
		slimeInterface1.lunaSlimes$playWobbleAnim();
		if (LunaSlimesVisualsAudioConfig.MERGE_SOUNDS.get()) {
			final RandomSource random = cube1.getRandom();
			cube1.playSound(
				entityType == EntityTypes.MAGMA_CUBE ? LunaSlimesSounds.MAGMACUBE_MERGE : LunaSlimesSounds.SLIME_MERGE,
				cube1.getSoundVolume(),
				1F + (random.nextFloat() - random.nextFloat()) * 0.4F
			);
		}
		slimeInterface2.lunaSlimes$playWobbleAnim();

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
		if (!(origin instanceof SlimeInterface originInterface)) return 0;

		int splitOff = 0;
		final RandomSource random = origin.getRandom();
		final Component customName = origin.getCustomName();
		final EntityType<? extends AbstractCubeMob> entityType = origin.getType();
		final AbstractCubeMob cube = entityType.create(origin.level(), EntitySpawnReason.TRIGGERED);
		if (!(cube instanceof SlimeInterface slimeInterface)) return splitOff;

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

		origin.setAttached(LunaSlimesAttachmentTypes.MERGE_COOLDOWN, LunaSlimesGameplayConfig.ON_SPLIT_COOLDOWN.get());
		cube.setAttached(LunaSlimesAttachmentTypes.MERGE_COOLDOWN, LunaSlimesGameplayConfig.SPLIT_COOLDOWN.get());
		originInterface.lunaSlimes$playWobbleAnim();
		slimeInterface.lunaSlimes$playWobbleAnim();
		LunaSlimesUtil.spawnCubeParticles(origin);

		origin.level().addFreshEntity(cube);
		if (LunaSlimesVisualsAudioConfig.SPLIT_SOUNDS.get()) {
			cube.playSound(
				entityType == EntityTypes.MAGMA_CUBE
					? LunaSlimesSounds.MAGMACUBE_SPLIT
					: entityType == EntityTypes.SULFUR_CUBE
					  ? SoundEvents.SULFUR_CUBE_DEATH
					  : LunaSlimesSounds.SLIME_SPLIT,
				cube.getSoundVolume(),
				1F + (random.nextFloat() - random.nextFloat()) * 0.4F
			);
		}

		return splitOff;
	}

	public static void spawnCubeParticles(AbstractCubeMob cube) {
		if (!(cube.level() instanceof ServerLevel level) || !LunaSlimesVisualsAudioConfig.PARTICLES.get()) return;
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
		if (!(cube instanceof SlimeInterface slimeInterface)) return cube.getSize();
		return (LunaSlimesVisualsAudioConfig.GROW_ANIM.get() ? slimeInterface.lunaSlimes$getSizeScale(partialTicks) : cube.getSize())
			* slimeInterface.lunaSlimes$getDeathProgress(partialTicks);
	}

	public static float getCubeWobbleAnimProgress(AbstractCubeMob cube, float partialTick) {
		if (!(cube instanceof SlimeInterface slimeInterface) || !LunaSlimesVisualsAudioConfig.WOBBLE_ANIM.get()) return 0F;
		return slimeInterface.lunaSlimes$wobbleAnimProgress(partialTick);
	}

	public static Pair<Float, Float> wobbleAnim(AbstractCubeMob cube, float partialTick) {
		final float cosWobble = (float) Math.cos((((LunaSlimesUtil.getCubeWobbleAnimProgress(cube, partialTick) + (0.0955F * Math.PI)) * Math.PI) * 5F));
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

}
