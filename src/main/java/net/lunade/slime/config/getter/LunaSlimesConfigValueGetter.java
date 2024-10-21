package net.lunade.slime.config.getter;

import net.lunade.slime.LunaSlimesConstants;
import net.lunade.slime.config.frozenlib.LunaSlimesGameplayFrozenConfig;
import net.lunade.slime.config.frozenlib.LunaSlimesVisualsAudioFrozenConfig;

public class LunaSlimesConfigValueGetter {

    public static boolean growAnim() {
		if (LunaSlimesConstants.HAS_FROZENLIB) {
			return LunaSlimesVisualsAudioFrozenConfig.GROW_ANIM;
		}
        return !LunaSlimesConstants.HAS_CLOTH_CONFIG || LunaSlimesConfigValues.growAnim();
    }

    public static boolean wobbleAnim() {
        if (LunaSlimesConstants.HAS_FROZENLIB) {
            return LunaSlimesVisualsAudioFrozenConfig.WOBBLE_ANIM;
        }
        return !LunaSlimesConstants.HAS_CLOTH_CONFIG || LunaSlimesConfigValues.wobbleAnim();
    }

    public static float squishMultiplier() {
        if (LunaSlimesConstants.HAS_FROZENLIB) {
            return LunaSlimesVisualsAudioFrozenConfig.SQUISH_MULTIPLIER * 0.1F;
        }
        return LunaSlimesConstants.HAS_CLOTH_CONFIG ? LunaSlimesConfigValues.squishMultiplier() : 2F;
    }

    public static boolean jumpAntic() {
        if (LunaSlimesConstants.HAS_FROZENLIB) {
            return LunaSlimesVisualsAudioFrozenConfig.get().jumpAntic;
        }
        return !LunaSlimesConstants.HAS_CLOTH_CONFIG || LunaSlimesConfigValues.jumpAntic();
    }

    public static boolean deathAnim() {
        if (LunaSlimesConstants.HAS_FROZENLIB) {
            return LunaSlimesVisualsAudioFrozenConfig.DEATH_ANIM;
        }
        return !LunaSlimesConstants.HAS_CLOTH_CONFIG || LunaSlimesConfigValues.deathAnim();
    }

    public static boolean newShadows() {
        if (LunaSlimesConstants.HAS_FROZENLIB) {
            return LunaSlimesVisualsAudioFrozenConfig.NEW_SHADOWS;
        }
        return !LunaSlimesConstants.HAS_CLOTH_CONFIG || LunaSlimesConfigValues.newShadows();
    }

    public static boolean particles() {
        if (LunaSlimesConstants.HAS_FROZENLIB) {
            return LunaSlimesVisualsAudioFrozenConfig.get().particles;
        }
        return !LunaSlimesConstants.HAS_CLOTH_CONFIG || LunaSlimesConfigValues.particles();
    }

    public static boolean glowingMagma() {
        if (LunaSlimesConstants.HAS_FROZENLIB) {
            return LunaSlimesVisualsAudioFrozenConfig.GLOWING_MAGMA_CUBE;
        }
        return !LunaSlimesConstants.HAS_CLOTH_CONFIG || LunaSlimesConfigValues.glowingMagma();
    }

    public static boolean slimeBlockParticles() {
        if (LunaSlimesConstants.HAS_FROZENLIB) {
            return LunaSlimesVisualsAudioFrozenConfig.get().slimeBlockParticles;
        }
        return !LunaSlimesConstants.HAS_CLOTH_CONFIG || LunaSlimesConfigValues.slimeBlockParticles();
    }

    public static boolean scaleTextures() {
        if (LunaSlimesConstants.HAS_FROZENLIB) {
            return LunaSlimesVisualsAudioFrozenConfig.SCALE_TEXTURES;
        }
        return !LunaSlimesConstants.HAS_CLOTH_CONFIG || LunaSlimesConfigValues.scaleTextures();
    }

    public static boolean mergeSounds() {
        if (LunaSlimesConstants.HAS_FROZENLIB) {
            return LunaSlimesVisualsAudioFrozenConfig.get().mergeSounds;
        }
        return !LunaSlimesConstants.HAS_CLOTH_CONFIG || LunaSlimesConfigValues.mergeSounds();
    }

    public static boolean splitSounds() {
        if (LunaSlimesConstants.HAS_FROZENLIB) {
            return LunaSlimesVisualsAudioFrozenConfig.get().splitSounds;
        }
        return !LunaSlimesConstants.HAS_CLOTH_CONFIG || LunaSlimesConfigValues.splitSounds();
    }

    public static int mergeCooldown() {
        if (LunaSlimesConstants.HAS_FROZENLIB) {
            return LunaSlimesGameplayFrozenConfig.get().mergeCooldown;
        }
        return LunaSlimesConstants.HAS_CLOTH_CONFIG ? LunaSlimesConfigValues.mergeCooldown() : 0;
    }

    public static int maxSize() {
        if (LunaSlimesConstants.HAS_FROZENLIB) {
            return LunaSlimesGameplayFrozenConfig.get().maxSize;
        }
        return LunaSlimesConstants.HAS_CLOTH_CONFIG ? LunaSlimesConfigValues.maxSize() : 4;
    }

    public static int onSplitCooldown() {
        if (LunaSlimesConstants.HAS_FROZENLIB) {
            return LunaSlimesGameplayFrozenConfig.get().onSplitCooldown;
        }
        return LunaSlimesConstants.HAS_CLOTH_CONFIG ? LunaSlimesConfigValues.onSplitCooldown() : 100;
    }

    public static int splitCooldown() {
        if (LunaSlimesConstants.HAS_FROZENLIB) {
            return LunaSlimesGameplayFrozenConfig.get().splitCooldown;
        }
        return LunaSlimesConstants.HAS_CLOTH_CONFIG ? LunaSlimesConfigValues.splitCooldown() : 0;
    }

    public static int spawnedMergeCooldown() {
        if (LunaSlimesConstants.HAS_FROZENLIB) {
            return LunaSlimesGameplayFrozenConfig.get().spawnedMergeCooldown;
        }
        return LunaSlimesConstants.HAS_CLOTH_CONFIG ? LunaSlimesConfigValues.spawnedMergeCooldown() : 0;
    }

    public static boolean useSplitting() {
        if (LunaSlimesConstants.HAS_FROZENLIB) {
            return LunaSlimesGameplayFrozenConfig.get().useSplitting;
        }
        return !LunaSlimesConstants.HAS_CLOTH_CONFIG || LunaSlimesConfigValues.useSplitting();
    }

}
