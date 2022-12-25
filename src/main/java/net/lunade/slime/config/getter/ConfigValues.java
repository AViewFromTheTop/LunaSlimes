package net.lunade.slime.config.getter;

import net.lunade.slime.config.GameplayConfig;
import net.lunade.slime.config.LunaSlimesConfig;
import net.lunade.slime.config.VisualsAudioConfig;

public class ConfigValues {

    private static final VisualsAudioConfig VISUALS_AUDIO = LunaSlimesConfig.get().visuals_audio;
    private static final GameplayConfig GAMEPLAY = LunaSlimesConfig.get().gameplay;

    public static boolean growAnim() {
        return VISUALS_AUDIO.growAnim;
    }

    public static boolean wobbleAnim() {
        return VISUALS_AUDIO.wobbleAnim;
    }

    public static float squishMultiplier() {
        return (float) VISUALS_AUDIO.squishMultiplier * 0.1F;
    }

    public static boolean jumpAntic() {
        return VISUALS_AUDIO.jumpAntic;
    }

    public static boolean deathAnim() {
        return VISUALS_AUDIO.deathAnim;
    }

    public static boolean newShadows() {
        return VISUALS_AUDIO.newShadows;
    }

    public static boolean particles() {
        return VISUALS_AUDIO.particles;
    }

    public static boolean scaleTextures() {
        return VISUALS_AUDIO.scaleTextures;
    }

    public static boolean glowingMagma() {
        return VISUALS_AUDIO.glowingMagma;
    }

    public static boolean slimeBlockParticles() {
        return VISUALS_AUDIO.slimeBlockParticles;
    }

    public static boolean mergeSounds() {
        return VISUALS_AUDIO.mergeSounds;
    }

    public static boolean splitSounds() {
        return VISUALS_AUDIO.splitSounds;
    }

    public static int maxSize() {
        return GAMEPLAY.maxSize;
    }

    public static int mergeCooldown() {
        return GAMEPLAY.mergeCooldown;
    }

    public static int onSplitCooldown() {
        return GAMEPLAY.onSplitCooldown;
    }

    public static int splitCooldown() {
        return GAMEPLAY.splitCooldown;
    }

    public static int spawnedMergeCooldown() {
        return GAMEPLAY.spawnedMergeCooldown;
    }

    public static boolean useSplitting() {
        return GAMEPLAY.useSplitting;
    }

}
