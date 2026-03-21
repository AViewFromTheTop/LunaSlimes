package net.lunade.slime.config.getter;

import net.lunade.slime.LunaSlimesConstants;
import net.lunade.slime.config.frozenlib.LunaSlimesGameplayFrozenConfig;
import net.lunade.slime.config.frozenlib.LunaSlimesVisualsAudioFrozenConfig;

public class LunaSlimesConfigValueGetter {

	public static boolean growAnim() {
		if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesVisualsAudioFrozenConfig.GROW_ANIM.get();
		return !LunaSlimesConstants.HAS_CLOTH_CONFIG || LunaSlimesConfigValues.growAnim();
	}

	public static boolean wobbleAnim() {
		if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesVisualsAudioFrozenConfig.WOBBLE_ANIM.get();
		return !LunaSlimesConstants.HAS_CLOTH_CONFIG || LunaSlimesConfigValues.wobbleAnim();
	}

	public static float squishMultiplier() {
		if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesVisualsAudioFrozenConfig.SQUISH_MULTIPLIER.get() * 0.1F;
		return LunaSlimesConstants.HAS_CLOTH_CONFIG ? LunaSlimesConfigValues.squishMultiplier() : 2F;
	}

	public static boolean jumpAntic() {
		if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesVisualsAudioFrozenConfig.JUMP_ANTIC.get();
		return !LunaSlimesConstants.HAS_CLOTH_CONFIG || LunaSlimesConfigValues.jumpAntic();
	}

	public static boolean deathAnim() {
		if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesVisualsAudioFrozenConfig.DEATH_ANIM.get();
		return !LunaSlimesConstants.HAS_CLOTH_CONFIG || LunaSlimesConfigValues.deathAnim();
	}

	public static boolean newShadows() {
		if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesVisualsAudioFrozenConfig.NEW_SHADOWS.get();
		return !LunaSlimesConstants.HAS_CLOTH_CONFIG || LunaSlimesConfigValues.newShadows();
	}

	public static boolean particles() {
		if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesVisualsAudioFrozenConfig.PARTICLES.get();
		return !LunaSlimesConstants.HAS_CLOTH_CONFIG || LunaSlimesConfigValues.particles();
	}

	public static boolean glowingMagma() {
		if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesVisualsAudioFrozenConfig.GLOWING_MAGMA.get();
		return !LunaSlimesConstants.HAS_CLOTH_CONFIG || LunaSlimesConfigValues.glowingMagma();
	}

	public static boolean slimeBlockParticles() {
		if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesVisualsAudioFrozenConfig.SLIME_BLOCK_PARTICLES.get();
		return !LunaSlimesConstants.HAS_CLOTH_CONFIG || LunaSlimesConfigValues.slimeBlockParticles();
	}

	public static boolean mergeSounds() {
		if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesVisualsAudioFrozenConfig.MERGE_SOUNDS.get();
		return !LunaSlimesConstants.HAS_CLOTH_CONFIG || LunaSlimesConfigValues.mergeSounds();
	}

	public static boolean splitSounds() {
		if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesVisualsAudioFrozenConfig.SPLIT_SOUNDS.get();
		return !LunaSlimesConstants.HAS_CLOTH_CONFIG || LunaSlimesConfigValues.splitSounds();
	}

	public static int mergeCooldown() {
		if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesGameplayFrozenConfig.MERGE_COOLDOWN.get();
		return LunaSlimesConstants.HAS_CLOTH_CONFIG ? LunaSlimesConfigValues.mergeCooldown() : 0;
	}

	public static int maxSize() {
		if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesGameplayFrozenConfig.MAX_SIZE.get();
		return LunaSlimesConstants.HAS_CLOTH_CONFIG ? LunaSlimesConfigValues.maxSize() : 4;
	}

	public static int onSplitCooldown() {
		if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesGameplayFrozenConfig.ON_SPLIT_COOLDOWN.get();
		return LunaSlimesConstants.HAS_CLOTH_CONFIG ? LunaSlimesConfigValues.onSplitCooldown() : 100;
	}

	public static int splitCooldown() {
		if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesGameplayFrozenConfig.SPLIT_COOLDOWN.get();
		return LunaSlimesConstants.HAS_CLOTH_CONFIG ? LunaSlimesConfigValues.splitCooldown() : 0;
	}

	public static int spawnedMergeCooldown() {
		if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesGameplayFrozenConfig.SPAWNED_MERGE_COOLDOWN.get();
		return LunaSlimesConstants.HAS_CLOTH_CONFIG ? LunaSlimesConfigValues.spawnedMergeCooldown() : 0;
	}

	public static boolean useSplitting() {
		if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesGameplayFrozenConfig.USE_SPLITTING.get();
		return !LunaSlimesConstants.HAS_CLOTH_CONFIG || LunaSlimesConfigValues.useSplitting();
	}

}
