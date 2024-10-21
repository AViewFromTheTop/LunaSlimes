package net.lunade.slime.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.slime.config.getter.LunaSlimesConfigValueGetter;
import net.minecraft.client.model.LavaSlimeModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class MagmaCubeLayer extends EyesLayer<SlimeRenderState, LavaSlimeModel> {
    private static final RenderType UNSCALED_OVERLAY = RenderType.eyes(ResourceLocation.tryBuild("lunaslimes", "textures/entity/slime/magmacube_overlay_" + 1 + ".png"));

    public MagmaCubeLayer(RenderLayerParent<SlimeRenderState, LavaSlimeModel> renderLayerParent) {
        super(renderLayerParent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, SlimeRenderState slimeRenderState, float f, float g) {
        if (LunaSlimesConfigValueGetter.glowingMagma()) {
            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(this.newRenderType(slimeRenderState));
            this.getParentModel().renderToBuffer(poseStack, vertexConsumer, 15728640, OverlayTexture.NO_OVERLAY);
        }
    }

    @Override
    public @NotNull RenderType renderType() {
        return UNSCALED_OVERLAY;
    }

    public RenderType newRenderType(@NotNull SlimeRenderState slimeRenderState) {
        return SlimeTextures.getMagmaCubeOverlayRenderType(slimeRenderState.size, this.renderType());
    }
}
