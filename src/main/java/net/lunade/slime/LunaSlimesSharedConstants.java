package net.lunade.slime;

import net.fabricmc.loader.api.FabricLoader;

public class LunaSlimesSharedConstants {
    public static final boolean HAS_CLOTH_CONFIG = FabricLoader.getInstance().isModLoaded("cloth-config");
    public static final boolean HAS_FROZENLIB = FabricLoader.getInstance().isModLoaded("frozenlib");
}
