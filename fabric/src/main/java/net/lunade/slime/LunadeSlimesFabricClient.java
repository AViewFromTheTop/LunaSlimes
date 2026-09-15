package net.lunade.slime;

import net.fabricmc.api.ClientModInitializer;
import net.mehvahdjukaar.candlelight.api.ClientOnly;

@ClientOnly
public final class LunadeSlimesFabricClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		LunaSlimesClient.init();
		LunaSlimesClient.setup();
	}
}
