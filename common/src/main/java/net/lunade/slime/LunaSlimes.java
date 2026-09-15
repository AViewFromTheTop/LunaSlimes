package net.lunade.slime;

import net.frozenblock.lib.entity.api.cubemob.sulfurcube.SulfurCubeEvents;
import net.lunade.slime.config.frozenlib.LSGameplayConfig;
import net.lunade.slime.config.frozenlib.LSVisualsAudioConfig;
import net.lunade.slime.impl.AbstractCubeMobInterface;
import net.lunade.slime.registry.LSAttachmentTypes;
import net.lunade.slime.registry.LSSoundEvents;

public final class LunaSlimes {

	public static void init() {
		LSSoundEvents.init();
		LSAttachmentTypes.init();
		LSGameplayConfig.CONFIG.load(true);
		LSVisualsAudioConfig.CONFIG.load(true);

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

	private LunaSlimes() {}
}
