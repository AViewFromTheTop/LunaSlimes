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

package net.lunade.slime.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.frozenblock.lib.FrozenLibEarlyConstants;
import net.lunade.slime.LSConstants;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.model.monster.slime.MagmaCubeModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.LightCoordsUtil;

@ClientOnly
public class MagmaCubeGlowingLayer extends RenderLayer<SlimeRenderState, MagmaCubeModel> {
	private static final RenderType OVERLAY_TEXTURE = RenderTypes.eyes(LSConstants.id("textures/entity/slime/magmacube_overlay.png"));

    public MagmaCubeGlowingLayer(RenderLayerParent<SlimeRenderState, MagmaCubeModel> parent) {
		super(parent);
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, SlimeRenderState renderState, float yRot, float xRot) {
		if (renderState.isInvisible || FrozenLibEarlyConstants.HAS_GLOWTONE) return;

		collector.order(1).submitModel(
			this.getParentModel(),
			renderState,
			poseStack,
			OVERLAY_TEXTURE,
			LightCoordsUtil.FULL_BRIGHT,
			LivingEntityRenderer.getOverlayCoords(renderState, 0F),
			-1,
			null,
			renderState.outlineColor
		);
	}
}
