package net.lunade.slime;

import net.frozenblock.lib.entity.api.cubemob.sulfurcube.SulfurCubeEvents;
import net.lunade.slime.config.frozenlib.LunaSlimesGameplayConfig;
import net.lunade.slime.config.frozenlib.LunaSlimesVisualsAudioConfig;
import net.lunade.slime.impl.AbstractCubeMobInterface;
import net.lunade.slime.registry.LunaSlimesAttachmentTypes;
import net.lunade.slime.registry.LunaSlimesSounds;

// TODO FIX ANIMATIONS ON NEOFORGE
public final class LunaSlimes {

	public static void init() {
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

	public static void setup() {}
}
