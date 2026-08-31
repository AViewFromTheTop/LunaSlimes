package net.lunade.slime.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lunade.slime.LunaSlimesConstants;
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
	private static final RenderType OVERLAY_TEXTURE = RenderTypes.eyes(LunaSlimesConstants.id("textures/entity/slime/magmacube_overlay.png"));

    public MagmaCubeGlowingLayer(RenderLayerParent<SlimeRenderState, MagmaCubeModel> parent) {
		super(parent);
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, SlimeRenderState renderState, float yRot, float xRot) {
		if (renderState.isInvisible) return;
		collector.order(1).submitModel(
			this.getParentModel(),
			renderState,
			poseStack,
			OVERLAY_TEXTURE,
			LightCoordsUtil.FULL_BRIGHT,
			LivingEntityRenderer.getOverlayCoords(renderState, 0F),
			-1,
			null,
			renderState.outlineColor,
			null
		);
	}

}
