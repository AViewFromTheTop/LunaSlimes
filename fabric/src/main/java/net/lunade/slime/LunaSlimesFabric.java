package net.lunade.slime;

import net.fabricmc.loader.api.ModContainer;
import net.frozenblock.lib.entrypoint.api.FrozenModInitializer;

public final class LunaSlimesFabric extends FrozenModInitializer {

	public LunaSlimesFabric() {
		super(LSConstants.MOD_ID);
	}

	@Override
	public void onInitialize(String modId, ModContainer container) {
		LunaSlimes.init();
		LunaSlimes.setup();
	}
}
