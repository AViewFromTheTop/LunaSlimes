package net.lunade.slime.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.slime.config.getter.ConfigValueGetter;
import net.minecraft.client.model.LavaSlimeModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.MagmaCube;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class MagmaCubeLayer<T extends MagmaCube> extends EyesLayer<T, LavaSlimeModel<T>> {
    private static final RenderType UNSCALED_OVERLAY = RenderType.eyes(new ResourceLocation("lunaslimes", "textures/entity/slime/magmacube_overlay_" + 1 + ".png"));

    public MagmaCubeLayer(RenderLayerParent<T, LavaSlimeModel<T>> renderLayerParent) {
        super(renderLayerParent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T entity, float f, float g, float h, float j, float k, float l) {
        if (ConfigValueGetter.glowingMagma()) {
            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(this.newRenderType(entity));
            this.getParentModel().renderToBuffer(poseStack, vertexConsumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public RenderType renderType() {
        return UNSCALED_OVERLAY;
    }

    public RenderType newRenderType(@NotNull MagmaCube cube) {
        return SlimeTextures.getMagmaCubeOverlayRenderType(cube.getSize(), this.renderType());
    }
}
