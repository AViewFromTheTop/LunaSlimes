package net.lunade.slime.config.getter;

import net.lunade.slime.LunaSlimesMain;

public class ConfigValueGetter {

    public static boolean growAnim() {
        return !LunaSlimesMain.HAS_CLOTH_CONFIG || ConfigValues.growAnim();
    }

    public static boolean wobbleAnim() {
        return !LunaSlimesMain.HAS_CLOTH_CONFIG || ConfigValues.wobbleAnim();
    }

    public static float squishMultiplier() {
        return LunaSlimesMain.HAS_CLOTH_CONFIG ? ConfigValues.squishMultiplier() : 2F;
    }

    public static boolean jumpAntic() {
        return !LunaSlimesMain.HAS_CLOTH_CONFIG || ConfigValues.jumpAntic();
    }

    public static boolean deathAnim() {
        return !LunaSlimesMain.HAS_CLOTH_CONFIG || ConfigValues.deathAnim();
    }

    public static boolean newShadows() {
        return !LunaSlimesMain.HAS_CLOTH_CONFIG || ConfigValues.newShadows();
    }

    public static boolean particles() {
        return !LunaSlimesMain.HAS_CLOTH_CONFIG || ConfigValues.particles();
    }

    public static boolean glowingMagma() {
        return !LunaSlimesMain.HAS_CLOTH_CONFIG || ConfigValues.glowingMagma();
    }

    public static boolean slimeBlockParticles() {
        return !LunaSlimesMain.HAS_CLOTH_CONFIG || ConfigValues.slimeBlockParticles();
    }

    public static boolean scaleTextures() {
        return !LunaSlimesMain.HAS_CLOTH_CONFIG || ConfigValues.scaleTextures();
    }

    public static boolean mergeSounds() {
        return !LunaSlimesMain.HAS_CLOTH_CONFIG || ConfigValues.mergeSounds();
    }

    public static boolean splitSounds() {
        return !LunaSlimesMain.HAS_CLOTH_CONFIG || ConfigValues.splitSounds();
    }

    public static int mergeCooldown() {
        return LunaSlimesMain.HAS_CLOTH_CONFIG ? ConfigValues.mergeCooldown() : 0;
    }

    public static int maxSize() {
        return LunaSlimesMain.HAS_CLOTH_CONFIG ? ConfigValues.maxSize() : 4;
    }

    public static int onSplitCooldown() {
        return LunaSlimesMain.HAS_CLOTH_CONFIG ? ConfigValues.onSplitCooldown() : 100;
    }

    public static int splitCooldown() {
        return LunaSlimesMain.HAS_CLOTH_CONFIG ? ConfigValues.splitCooldown() : 0;
    }

    public static int spawnedMergeCooldown() {
        return LunaSlimesMain.HAS_CLOTH_CONFIG ? ConfigValues.spawnedMergeCooldown() : 0;
    }

    public static boolean useSplitting() {
        return !LunaSlimesMain.HAS_CLOTH_CONFIG || ConfigValues.useSplitting();
    }

}
