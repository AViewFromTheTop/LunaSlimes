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

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.lunade.slime.config.LSVisualsAudioConfig;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;

@ClientOnly
public final class LSRenderUtils {
	public static final Pair<Float, Float> FALLBACK_WOBBLE = Pair.of(1F, 1F);

	public static void applyWobbleAndSquish(
		PoseStack poseStack, float xScale, float yScale, float zScale, Operation<Void> operation,
		SlimeRenderState state,
		boolean skipSquish
	) {
		if (!state.frozenLib$getDataOrDefault(LSRenderStateDataKeys.IN_WORLD, false)) {
			operation.call(poseStack, xScale, yScale, zScale);
			return;
		}

		final float cubeSize = state.frozenLib$getDataOrDefault(LSRenderStateDataKeys.SIZE, 1F);
		final Pair<Float, Float> wobble = state.frozenLib$getDataOrDefault(LSRenderStateDataKeys.WOBBLE, LSRenderUtils.FALLBACK_WOBBLE);
		final float wobbleXZ = wobble.getFirst();
		final float wobbleY = wobble.getSecond();
		poseStack.scale(wobbleXZ, wobbleY, wobbleXZ);
		poseStack.translate(0F, -(2.05F - (wobbleY * 2.05F)), 0F);

		final float squish = !skipSquish
			? (state.squish * (LSVisualsAudioConfig.SQUISH_MULTIPLIER.get() * 0.1F)) / ((cubeSize) * 0.5F + 1F)
			: 0F;

		final float relativeSquish = 1F / (squish + 1F);
		operation.call(poseStack, relativeSquish * cubeSize, 1F / relativeSquish * cubeSize, relativeSquish * cubeSize);
	}

	private LSRenderUtils() {}
}
