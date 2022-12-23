package net.lunade.slime.config.getter;

import net.lunade.slime.config.VisualConfig;
import net.lunade.slime.config.LunaSlimesConfig;

public class ConfigValues {

    private static final VisualConfig VISUALS = LunaSlimesConfig.get().visuals;

    public static boolean growAnim() {
        return VISUALS.growAnim;
    }

    public static boolean wobbleAnim() {
        return VISUALS.wobbleAnim;
    }

    public static float squishMultiplier() {
        return VISUALS.squishMultiplier;
    }

    public static boolean newShadows() {
        return VISUALS.newShadows;
    }

}
