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

}
