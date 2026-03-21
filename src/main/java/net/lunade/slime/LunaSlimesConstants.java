package net.lunade.slime;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;

public class LunaSlimesConstants {
	public static final boolean HAS_CLOTH_CONFIG = FabricLoader.getInstance().isModLoaded("cloth-config");
	public static final boolean HAS_FROZENLIB = FabricLoader.getInstance().isModLoaded("frozenlib");

	public static final String MOD_ID = "lunaslimes";

	public static Component text(String key, final Object... args) {
		return Component.translatable("option." + MOD_ID + "." + key, args);
	}

	/**
	 * @return A tooltip component for use in a Config GUI
	 */
	public static Component tooltip(String key, final Object... args) {
		return Component.translatable("tooltip." + MOD_ID + "." + key, args);
	}
}
