package net.lunade.slime;

import net.fabricmc.loader.api.ModContainer;
import net.frozenblock.lib.entity.api.cubemob.sulfurcube.SulfurCubeEvents;
import net.frozenblock.lib.entrypoint.api.FrozenModInitializer;
import net.lunade.slime.config.frozenlib.LunaSlimesGameplayConfig;
import net.lunade.slime.config.frozenlib.LunaSlimesVisualsAudioConfig;
import net.lunade.slime.impl.AbstractCubeMobInterface;
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
		LunaSlimesGameplayConfig.CONFIG.load(true);
		LunaSlimesVisualsAudioConfig.CONFIG.load(true);

		SulfurCubeEvents.ON_HIT.register((sulfurCube, pushVelocity, source, damage, comesFromEffect) -> {
			if (pushVelocity.length() <= 0.9D) return;
			if (sulfurCube instanceof AbstractCubeMobInterface cubeMobInterface) cubeMobInterface.lunaSlimes$playWobbleAnim();
		});

		SulfurCubeEvents.ON_PUSH_SOUND_PLAYED.register((sulfurCube, player, pushVelocity) -> {
			if (pushVelocity.length() <= 0.9D) return;
			if (sulfurCube instanceof AbstractCubeMobInterface cubeMobInterface) cubeMobInterface.lunaSlimes$playWobbleAnim();
		});
	}
}
