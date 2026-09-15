/*
 * Copyright 2025-2026 FrozenBlock
 * This file is part of Wilder Wild.
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

package net.lunade.slime.registry;

import net.frozenblock.lib.platform.api.registry.DeferredRegister;
import net.frozenblock.lib.platform.api.registry.DeferredSoundEvent;
import net.lunade.slime.LSConstants;

public final class LSSoundEvents {
	private static final DeferredRegister.SoundEvents REGISTER = DeferredRegister.createSoundEvents(LSConstants.MOD_ID);

	public static final DeferredSoundEvent SLIME_MERGE = register("entity.slime.merge");
	public static final DeferredSoundEvent SLIME_SPLIT = register("entity.slime.split");
	public static final DeferredSoundEvent MAGMACUBE_MERGE = register("entity.magmacube.merge");
	public static final DeferredSoundEvent MAGMACUBE_SPLIT = register("entity.magmacube.split");

	static {
		REGISTER.register();
	}

	public static void init() {}

	public static DeferredSoundEvent register(String path) {
		return REGISTER.register(path);
	}

	private LSSoundEvents() {}
}
