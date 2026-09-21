/*
 * Copyright 2026 Lunade Music/AViewFromTheTop
 * This file is part of Luna Slimes.
 *
 * This program is free software; you can modify it under
 * the terms of version 1 of the FrozenBlock Modding Oasis License
 * as published by FrozenBlock Modding Oasis.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * FrozenBlock Modding Oasis License for more details.
 *
 * You should have received a copy of the FrozenBlock Modding Oasis License
 * along with this program; if not, see <https://github.com/FrozenBlock/Licenses>.
 */

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
