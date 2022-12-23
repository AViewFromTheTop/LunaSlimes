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
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(MagmaCubeRenderer.class)
public class MagmaCubeRendererMixin {

    @Unique float hI;

    @Inject(at = @At("HEAD"), method = "scale")
    public void anims(MagmaCube slime, PoseStack poseStack, float f, CallbackInfo info) {
        float splitAnimProgress = ((SlimeInterface)slime).splitAnimProgress(f);

        float splitValue = (float) (((splitAnimProgress + (0.0955F * Math.PI)) * Math.PI) * 5F);
        float splitAnimXZ = (float) ((Math.cos(splitValue) * 0.1F) + 1F);
        float splitAnimY = (float) (-(Math.cos(splitValue) * 0.025F) + 1F);
        poseStack.scale(splitAnimXZ, splitAnimY, splitAnimXZ);
        poseStack.translate(0.0F, -(2.05F - (splitAnimY * 2.05F)), 0.0F);

        float splitAnimXZShadow = (float) ((Math.cos((float) (((splitAnimProgress + (0.0955F * Math.PI)) * Math.PI) * 5F)) * 0.1F) + 1F) * 2F;
        float size = ((((SlimeInterface)slime).getSizeScale(f) * 0.999F) * 0.75F) * splitAnimXZShadow;
        float squish = Mth.lerp(f, ((SlimeInterface)slime).prevSquish(), slime.squish) / (size * 0.5f + 1.0f);
        float j = (1.0F / (squish + 1.0F));
        ((RendererShadowInterface)this).setShadowRadius(0.25F * (j * size));

        float i = ((SlimeInterface)slime).getSizeScale(f);
        float g = Mth.lerp(f, ((SlimeInterface)slime).prevSquish(), slime.squish) / ((i) * 0.5f + 1.0f);
        this.hI = (1.0F / (g + 1.0F)) * i;
    }

    @ModifyArgs(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V"), method = "scale")
    public void setScaleArgs(Args args) {
        args.set(0, hI);
        args.set(1, 1F / hI);
        args.set(2, hI);
    }

}