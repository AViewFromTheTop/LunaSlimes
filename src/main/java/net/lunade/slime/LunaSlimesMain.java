package net.lunade.slime;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class LunaSlimesMain implements ModInitializer {

	public static final boolean HAS_CLOTH_CONFIG = FabricLoader.getInstance().isModLoaded("cloth-config");
	public static boolean areConfigsInit;

	@Override
	public void onInitialize() {

	}

}
