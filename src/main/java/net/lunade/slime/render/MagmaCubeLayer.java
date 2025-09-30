package net.lunade.slime.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.LavaSlimeModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class MagmaCubeLayer extends RenderLayer<SlimeRenderState, LavaSlimeModel> {
	private static final RenderType OVERLAY = RenderType.eyes(ResourceLocation.tryBuild("lunaslimes", "textures/entity/slime/magmacube_overlay.png"));

    public MagmaCubeLayer(RenderLayerParent<SlimeRenderState, LavaSlimeModel> renderLayerParent) {
		super(renderLayerParent);
	}

	@Override
	public void submit(PoseStack poseStack, @NotNull SubmitNodeCollector submitNodeCollector, int i, SlimeRenderState slimeRenderState, float f, float g) {
		submitNodeCollector.order(1)
			.submitModel(
				this.getParentModel(),
				slimeRenderState,
				poseStack,
				OVERLAY,
				i,
				LivingEntityRenderer.getOverlayCoords(slimeRenderState, 0F),
				-1,
				null,
				slimeRenderState.outlineColor,
				null
			);
	}

}
