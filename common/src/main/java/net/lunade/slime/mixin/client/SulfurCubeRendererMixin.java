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

package net.lunade.slime.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.lunade.slime.client.LSRenderUtils;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.renderer.entity.SulfurCubeRenderer;
import net.minecraft.client.renderer.entity.state.SulfurCubeRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@ClientOnly
@Mixin(SulfurCubeRenderer.class)
public class SulfurCubeRendererMixin {

	@WrapOperation(
		method = "applySizeAndSquish(Lnet/minecraft/client/renderer/entity/state/SulfurCubeRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;)V",
		at = @At(
			value = "INVOKE",
			target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V",
			ordinal = 0
		)
	)
	public void lunaSlimes$newScaling(
		PoseStack poseStack, float xScale, float yScale, float zScale, Operation<Void> operation,
		@Local(argsOnly = true) SulfurCubeRenderState state
	) {
		final boolean hasBlock = !state.containedBlock.isEmpty();
		LSRenderUtils.applyWobbleAndSquish(poseStack, xScale, yScale, zScale, operation, state, hasBlock);
	}
}
