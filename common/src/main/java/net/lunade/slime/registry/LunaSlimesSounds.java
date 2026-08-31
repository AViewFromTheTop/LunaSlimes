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

import net.frozenblock.lib.platform.api.registry.DeferredHolder;
import net.frozenblock.lib.platform.api.registry.DeferredRegister;
import net.lunade.slime.LunaSlimesConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public final class LunaSlimesSounds {
	private static final DeferredRegister<SoundEvent> REGISTER = DeferredRegister.create(
		Registries.SOUND_EVENT,
		LunaSlimesConstants.MOD_ID
	);

	public static final DeferredHolder<SoundEvent, SoundEvent> SLIME_MERGE = register("entity.slime.merge");
	public static final DeferredHolder<SoundEvent, SoundEvent> SLIME_SPLIT = register("entity.slime.split");
	public static final DeferredHolder<SoundEvent, SoundEvent> MAGMACUBE_MERGE = register("entity.magmacube.merge");
	public static final DeferredHolder<SoundEvent, SoundEvent> MAGMACUBE_SPLIT = register("entity.magmacube.split");

	static {
		REGISTER.register();
	}

	private LunaSlimesSounds() {
		throw new UnsupportedOperationException("LunaSlimesSounds contains only static declarations.");
	}

	public static void init() {}

	public static DeferredHolder<SoundEvent, SoundEvent> register(String path) {
		final Identifier id = LunaSlimesConstants.id(path);
		return REGISTER.register(path, () -> SoundEvent.createVariableRangeEvent(id));
	}

}
