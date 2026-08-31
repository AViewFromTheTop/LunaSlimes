package net.lunade.slime;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(LunaSlimesPreLoadConstants.MOD_ID)
public final class LunaSlimesNeoForge {

	public LunaSlimesNeoForge(IEventBus modBus) {
		LunaSlimes.init();

		// AFTER register event
		modBus.addListener(FMLCommonSetupEvent.class, event -> LunaSlimes.setup());
	}
}
