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

package net.lunade.slime.client;

import com.mojang.datafixers.util.Pair;
import net.frozenblock.lib.renderer.RenderStateDataKey;
import net.lunade.slime.LSConstants;
import net.mehvahdjukaar.candlelight.api.ClientOnly;

@ClientOnly
public final class LSRenderStateDataKeys {
	public static final RenderStateDataKey<Pair<Float, Float>> WOBBLE = RenderStateDataKey.create(LSConstants.id("wobble"));
	public static final RenderStateDataKey<Float> SIZE = RenderStateDataKey.create(LSConstants.id("size"));
	public static final RenderStateDataKey<Boolean> IN_WORLD = RenderStateDataKey.create(LSConstants.id("in_world"));

	private LSRenderStateDataKeys() {}
}
