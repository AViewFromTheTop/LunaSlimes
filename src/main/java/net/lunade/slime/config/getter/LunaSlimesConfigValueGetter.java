package net.lunade.slime.config.getter;

import net.lunade.slime.LunaSlimesConstants;
import net.lunade.slime.config.frozenlib.LunaSlimesGameplayFrozenConfig;
import net.lunade.slime.config.frozenlib.LunaSlimesVisualsAudioFrozenConfig;

public class LunaSlimesConfigValueGetter {

	public static boolean growAnim() {
		if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesVisualsAudioFrozenConfig.growAnim.get();
		return !LunaSlimesConstants.HAS_CLOTH_CONFIG || LunaSlimesConfigValues.growAnim();
	}

	public static boolean wobbleAnim() {
		if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesVisualsAudioFrozenConfig.wobbleAnim.get();
		return !LunaSlimesConstants.HAS_CLOTH_CONFIG || LunaSlimesConfigValues.wobbleAnim();
	}

	public static float squishMultiplier() {
		if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesVisualsAudioFrozenConfig.squishMultiplier.get() * 0.1F;
		return LunaSlimesConstants.HAS_CLOTH_CONFIG ? LunaSlimesConfigValues.squishMultiplier() : 2F;
	}

	public static boolean jumpAntic() {
		if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesVisualsAudioFrozenConfig.jumpAntic.get();
		return !LunaSlimesConstants.HAS_CLOTH_CONFIG || LunaSlimesConfigValues.jumpAntic();
	}

	public static boolean deathAnim() {
		if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesVisualsAudioFrozenConfig.deathAnim.get();
		return !LunaSlimesConstants.HAS_CLOTH_CONFIG || LunaSlimesConfigValues.deathAnim();
	}

	public static boolean newShadows() {
		if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesVisualsAudioFrozenConfig.newShadows.get();
		return !LunaSlimesConstants.HAS_CLOTH_CONFIG || LunaSlimesConfigValues.newShadows();
	}

	public static boolean particles() {
		if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesVisualsAudioFrozenConfig.particles.get();
		return !LunaSlimesConstants.HAS_CLOTH_CONFIG || LunaSlimesConfigValues.particles();
	}

	public static boolean glowingMagma() {
		if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesVisualsAudioFrozenConfig.glowingMagma.get();
		return !LunaSlimesConstants.HAS_CLOTH_CONFIG || LunaSlimesConfigValues.glowingMagma();
	}

	public static boolean slimeBlockParticles() {
		if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesVisualsAudioFrozenConfig.slimeBlockParticles.get();
		return !LunaSlimesConstants.HAS_CLOTH_CONFIG || LunaSlimesConfigValues.slimeBlockParticles();
	}

	public static boolean mergeSounds() {
		if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesVisualsAudioFrozenConfig.mergeSounds.get();
		return !LunaSlimesConstants.HAS_CLOTH_CONFIG || LunaSlimesConfigValues.mergeSounds();
	}

	public static boolean splitSounds() {
		if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesVisualsAudioFrozenConfig.splitSounds.get();
		return !LunaSlimesConstants.HAS_CLOTH_CONFIG || LunaSlimesConfigValues.splitSounds();
	}

	public static int mergeCooldown() {
		if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesGameplayFrozenConfig.mergeCooldown.get();
		return LunaSlimesConstants.HAS_CLOTH_CONFIG ? LunaSlimesConfigValues.mergeCooldown() : 0;
	}

	public static int maxSize() {
		if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesGameplayFrozenConfig.maxSize.get();
		return LunaSlimesConstants.HAS_CLOTH_CONFIG ? LunaSlimesConfigValues.maxSize() : 4;
	}

	public static int onSplitCooldown() {
		if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesGameplayFrozenConfig.onSplitCooldown.get();
		return LunaSlimesConstants.HAS_CLOTH_CONFIG ? LunaSlimesConfigValues.onSplitCooldown() : 100;
	}

	public static int splitCooldown() {
		if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesGameplayFrozenConfig.splitCooldown.get();
		return LunaSlimesConstants.HAS_CLOTH_CONFIG ? LunaSlimesConfigValues.splitCooldown() : 0;
	}

	public static int spawnedMergeCooldown() {
		if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesGameplayFrozenConfig.spawnedMergeCooldown.get();
		return LunaSlimesConstants.HAS_CLOTH_CONFIG ? LunaSlimesConfigValues.spawnedMergeCooldown() : 0;
	}

	public static boolean useSplitting() {
		if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesGameplayFrozenConfig.useSplitting.get();
		return !LunaSlimesConstants.HAS_CLOTH_CONFIG || LunaSlimesConfigValues.useSplitting();
	}

}
