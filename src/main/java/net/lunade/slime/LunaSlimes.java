package net.lunade.slime;

import net.fabricmc.loader.api.ModContainer;
import net.frozenblock.lib.entrypoint.api.FrozenModInitializer;
import net.lunade.slime.registry.LunaSlimesAttachmentTypes;
import net.lunade.slime.registry.LunaSlimesSounds;

public final class LunaSlimes extends FrozenModInitializer {

	public LunaSlimes() {
		super(LunaSlimesConstants.MOD_ID);
	}

	@Override
	public void onInitialize(String modId, ModContainer container) {
		LunaSlimesSounds.init();
		LunaSlimesAttachmentTypes.init();
	}

}
