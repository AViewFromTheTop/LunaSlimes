package net.lunade.slime.config.getter;

import net.lunade.slime.LunaSlimesSharedConstants;
import net.lunade.slime.config.frozenlib.GameplayFrozenConfig;
import net.lunade.slime.config.frozenlib.VisualsAudioFrozenConfig;

public class ConfigValueGetter {

    public static boolean growAnim() {
        return !LunaSlimesSharedConstants.HAS_CLOTH_CONFIG || ConfigValues.growAnim();
    }

    public static boolean wobbleAnim() {
        if (LunaSlimesSharedConstants.HAS_FROZENLIB) {
            return VisualsAudioFrozenConfig.get().wobbleAnim;
        }
        return !LunaSlimesSharedConstants.HAS_CLOTH_CONFIG || ConfigValues.wobbleAnim();
    }

    public static float squishMultiplier() {
        if (LunaSlimesSharedConstants.HAS_FROZENLIB) {
            return VisualsAudioFrozenConfig.get().squishMultiplier * 0.1F;
        }
        return LunaSlimesSharedConstants.HAS_CLOTH_CONFIG ? ConfigValues.squishMultiplier() : 2F;
    }

    public static boolean jumpAntic() {
        if (LunaSlimesSharedConstants.HAS_FROZENLIB) {
            return VisualsAudioFrozenConfig.get().jumpAntic;
        }
        return !LunaSlimesSharedConstants.HAS_CLOTH_CONFIG || ConfigValues.jumpAntic();
    }

    public static boolean deathAnim() {
        if (LunaSlimesSharedConstants.HAS_FROZENLIB) {
            return VisualsAudioFrozenConfig.get().deathAnim;
        }
        return !LunaSlimesSharedConstants.HAS_CLOTH_CONFIG || ConfigValues.deathAnim();
    }

    public static boolean newShadows() {
        if (LunaSlimesSharedConstants.HAS_FROZENLIB) {
            return VisualsAudioFrozenConfig.get().newShadows;
        }
        return !LunaSlimesSharedConstants.HAS_CLOTH_CONFIG || ConfigValues.newShadows();
    }

    public static boolean particles() {
        if (LunaSlimesSharedConstants.HAS_FROZENLIB) {
            return VisualsAudioFrozenConfig.get().particles;
        }
        return !LunaSlimesSharedConstants.HAS_CLOTH_CONFIG || ConfigValues.particles();
    }

    public static boolean glowingMagma() {
        if (LunaSlimesSharedConstants.HAS_FROZENLIB) {
            return VisualsAudioFrozenConfig.get().glowingMagma;
        }
        return !LunaSlimesSharedConstants.HAS_CLOTH_CONFIG || ConfigValues.glowingMagma();
    }

    public static boolean slimeBlockParticles() {
        if (LunaSlimesSharedConstants.HAS_FROZENLIB) {
            return VisualsAudioFrozenConfig.get().slimeBlockParticles;
        }
        return !LunaSlimesSharedConstants.HAS_CLOTH_CONFIG || ConfigValues.slimeBlockParticles();
    }

    public static boolean scaleTextures() {
        if (LunaSlimesSharedConstants.HAS_FROZENLIB) {
            return VisualsAudioFrozenConfig.get().scaleTextures;
        }
        return !LunaSlimesSharedConstants.HAS_CLOTH_CONFIG || ConfigValues.scaleTextures();
    }

    public static boolean mergeSounds() {
        if (LunaSlimesSharedConstants.HAS_FROZENLIB) {
            return VisualsAudioFrozenConfig.get().mergeSounds;
        }
        return !LunaSlimesSharedConstants.HAS_CLOTH_CONFIG || ConfigValues.mergeSounds();
    }

    public static boolean splitSounds() {
        if (LunaSlimesSharedConstants.HAS_FROZENLIB) {
            return VisualsAudioFrozenConfig.get().splitSounds;
        }
        return !LunaSlimesSharedConstants.HAS_CLOTH_CONFIG || ConfigValues.splitSounds();
    }

    public static int mergeCooldown() {
        if (LunaSlimesSharedConstants.HAS_FROZENLIB) {
            return GameplayFrozenConfig.get().mergeCooldown;
        }
        return LunaSlimesSharedConstants.HAS_CLOTH_CONFIG ? ConfigValues.mergeCooldown() : 0;
    }

    public static int maxSize() {
        if (LunaSlimesSharedConstants.HAS_FROZENLIB) {
            return GameplayFrozenConfig.get().maxSize;
        }
        return LunaSlimesSharedConstants.HAS_CLOTH_CONFIG ? ConfigValues.maxSize() : 4;
    }

    public static int onSplitCooldown() {
        if (LunaSlimesSharedConstants.HAS_FROZENLIB) {
            return GameplayFrozenConfig.get().onSplitCooldown;
        }
        return LunaSlimesSharedConstants.HAS_CLOTH_CONFIG ? ConfigValues.onSplitCooldown() : 100;
    }

    public static int splitCooldown() {
        if (LunaSlimesSharedConstants.HAS_FROZENLIB) {
            return GameplayFrozenConfig.get().splitCooldown;
        }
        return LunaSlimesSharedConstants.HAS_CLOTH_CONFIG ? ConfigValues.splitCooldown() : 0;
    }

    public static int spawnedMergeCooldown() {
        if (LunaSlimesSharedConstants.HAS_FROZENLIB) {
            return GameplayFrozenConfig.get().spawnedMergeCooldown;
        }
        return LunaSlimesSharedConstants.HAS_CLOTH_CONFIG ? ConfigValues.spawnedMergeCooldown() : 0;
    }

    public static boolean useSplitting() {
        if (LunaSlimesSharedConstants.HAS_FROZENLIB) {
            return GameplayFrozenConfig.get().useSplitting;
        }
        return !LunaSlimesSharedConstants.HAS_CLOTH_CONFIG || ConfigValues.useSplitting();
    }

}
