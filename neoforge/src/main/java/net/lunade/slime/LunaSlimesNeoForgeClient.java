package net.lunade.slime;

import net.frozenblock.lib.platform.ModLoader;
import net.lunade.slime.config.gui.LSConfigGui;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = LSPreLoadConstants.MOD_ID, dist = Dist.CLIENT)
public final class LunaSlimesNeoForgeClient {

	public LunaSlimesNeoForgeClient(IEventBus modBus) {
		LunaSlimesClient.init();

		// AFTER register event
		modBus.addListener(FMLClientSetupEvent.class, event -> LunaSlimesClient.setup());

		if (ModLoader.isModLoaded("cloth-config") || ModLoader.isModLoaded("cloth_config")) {
			ModLoadingContext.get().registerExtensionPoint(
				IConfigScreenFactory.class,
				() -> (container, parent) -> LSConfigGui.buildScreen(parent)
			);
		}
	}
}
