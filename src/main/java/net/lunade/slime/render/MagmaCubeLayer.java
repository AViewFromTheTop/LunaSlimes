package net.lunade.slime.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.monster.slime.MagmaCubeModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class MagmaCubeLayer extends RenderLayer<SlimeRenderState, MagmaCubeModel> {
	private static final RenderType OVERLAY = RenderTypes.eyes(Identifier.fromNamespaceAndPath("lunaslimes", "textures/entity/slime/magmacube_overlay.png"));

    public MagmaCubeLayer(RenderLayerParent<SlimeRenderState, MagmaCubeModel> parent) {
		super(parent);
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light, SlimeRenderState renderState, float f, float g) {
		collector.order(1).submitModel(
			this.getParentModel(),
			renderState,
			poseStack,
			OVERLAY,
			light,
			LivingEntityRenderer.getOverlayCoords(renderState, 0F),
			-1,
			null,
			renderState.outlineColor,
			null
		);
	}

}
