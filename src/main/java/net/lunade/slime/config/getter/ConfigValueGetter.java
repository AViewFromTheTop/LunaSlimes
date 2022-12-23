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

    public static boolean newShadows() {
        return !LunaSlimesMain.HAS_CLOTH_CONFIG || ConfigValues.newShadows();
    }

    public static boolean particles() {
        return !LunaSlimesMain.HAS_CLOTH_CONFIG || ConfigValues.particles();
    }

    public static int mergeCooldown() {
        return LunaSlimesMain.HAS_CLOTH_CONFIG ? ConfigValues.mergeCooldown() : 0;
    }

    public static int maxSize() {
        return LunaSlimesMain.HAS_CLOTH_CONFIG ? ConfigValues.maxSize() : 7;
    }

    public static int onSplitCooldown() {
        return LunaSlimesMain.HAS_CLOTH_CONFIG ? ConfigValues.onSplitCooldown() : 100;
    }

    public static int splitCooldown() {
        return LunaSlimesMain.HAS_CLOTH_CONFIG ? ConfigValues.splitCooldown() : 0;
    }

    public static boolean useSplitting() {
        return !LunaSlimesMain.HAS_CLOTH_CONFIG || ConfigValues.useSplitting();
    }

}
