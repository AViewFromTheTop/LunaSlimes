package net.lunade.slime.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lunade.slime.impl.RendererShadowInterface;
import net.lunade.slime.impl.SlimeInterface;
import net.minecraft.client.renderer.entity.MagmaCubeRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.MagmaCube;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MagmaCubeRenderer.class)
public class MagmaCubeRendererMixin {

    @Inject(at = @At("HEAD"), method = "scale", cancellable = true)
    public void anims(MagmaCube slime, PoseStack poseStack, float f, CallbackInfo info) {
        info.cancel();
        float splitAnimProgress = ((SlimeInterface)slime).splitAnimProgress(f);
        float splitValue = (float) (((splitAnimProgress + (0.0955F * Math.PI)) * Math.PI) * 5F);
        float splitAnimXZ = (float) ((Math.cos(splitValue) * 0.1F) + 1F);
        float splitAnimY = (float) (-(Math.cos(splitValue) * 0.025F) + 1F);
        poseStack.scale(splitAnimXZ, splitAnimY, splitAnimXZ);
        poseStack.translate(0.0F, -(2.05F - (splitAnimY * 2.05F)), 0.0F);

        float splitAnimXZShadow = splitAnimXZ * 2F;
        float size = ((((SlimeInterface)slime).getSizeScale(f) * 0.999F) * 0.75F) * splitAnimXZShadow;
        float squish = Mth.lerp(f, ((SlimeInterface)slime).prevSquish(), slime.squish) / (size * 0.5f + 1.0f);
        float j = (1.0F / (squish + 1.0F));
        ((RendererShadowInterface)this).setShadowRadius(0.25F * (j * size));

        float i = ((SlimeInterface)slime).getSizeScale(f);
        float g = Mth.lerp(f, ((SlimeInterface)slime).prevSquish(), slime.squish) / ((i) * 0.5f + 1.0f);
        float h = (1.0F / (g + 1.0F));
        poseStack.scale(h, 1.0F / h * i, h);
    }

}