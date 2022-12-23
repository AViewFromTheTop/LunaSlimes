package net.lunade.slime.config.getter;

import net.lunade.slime.config.GameplayConfig;
import net.lunade.slime.config.VisualConfig;
import net.lunade.slime.config.LunaSlimesConfig;

public class ConfigValues {

    private static final VisualConfig VISUALS = LunaSlimesConfig.get().visuals;
    private static final GameplayConfig GAMEPLAY = LunaSlimesConfig.get().gameplay;

    public static boolean growAnim() {
        return VISUALS.growAnim;
    }

    public static boolean wobbleAnim() {
        return VISUALS.wobbleAnim;
    }

    public static float squishMultiplier() {
        return (float) VISUALS.squishMultiplier * 0.1F;
    }

    public static boolean newShadows() {
        return VISUALS.newShadows;
    }

    public static boolean particles() {
        return VISUALS.particles;
    }

    public static boolean scaleTextures() {
        return VISUALS.scaleTextures;
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
