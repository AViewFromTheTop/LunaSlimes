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

import net.lunade.slime.LunaSlimesConstants;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public final class LunaSlimesSounds {
	public static final SoundEvent SLIME_MERGE = register("entity.slime.merge");
	public static final SoundEvent SLIME_SPLIT = register("entity.slime.split");
	public static final SoundEvent MAGMACUBE_MERGE = register("entity.magmacube.merge");
	public static final SoundEvent MAGMACUBE_SPLIT = register("entity.magmacube.split");

	private LunaSlimesSounds() {
		throw new UnsupportedOperationException("LunaSlimesSounds contains only static declarations.");
	}

	private static Holder.Reference<SoundEvent> registerForHolder(String path) {
		return registerForHolder(LunaSlimesConstants.id(path));
	}

	private static Holder.Reference<SoundEvent> registerForHolder(Identifier id) {
		return registerForHolder(id, id);
	}

	public static SoundEvent register(String path) {
		final Identifier id = LunaSlimesConstants.id(path);
		return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
	}

	private static Holder.Reference<SoundEvent> registerForHolder(Identifier id, Identifier id2) {
		return Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id2));
	}

	public static void init() {}

}
