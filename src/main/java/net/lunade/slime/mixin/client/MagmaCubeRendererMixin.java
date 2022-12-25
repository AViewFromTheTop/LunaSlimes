package net.lunade.slime.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.lunade.slime.SlimeMethods;
import net.lunade.slime.config.getter.ConfigValueGetter;
import net.lunade.slime.impl.RendererShadowInterface;
import net.lunade.slime.impl.SlimeInterface;
import net.minecraft.client.renderer.entity.MagmaCubeRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.MagmaCube;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(MagmaCubeRenderer.class)
public class MagmaCubeRendererMixin {

    @Unique float h;
    @Unique float i;
    @Unique float yStretch;

    @Inject(at = @At("HEAD"), method = "scale*")
    public void anims(MagmaCube slime, PoseStack poseStack, float f, CallbackInfo info) {
        Pair<Float, Float> wobble = SlimeMethods.wobbleAnim(slime, f);
        float wobbleXZ = wobble.getFirst();
        float wobbleY = wobble.getSecond();
        poseStack.scale(wobbleXZ, wobbleY, wobbleXZ);
        poseStack.translate(0.0F, -(2.05F - (wobbleY * 2.05F)), 0F);
        float size = SlimeMethods.getSlimeScale(slime, f);
        float squishValue = Mth.lerp(f, ((SlimeInterface)slime).prevSquish(), slime.squish) * ConfigValueGetter.squishMultiplier();

        if (ConfigValueGetter.newShadows()) {
            float wobbleAnimXZShadow = wobbleXZ * 2F;
            float shadowSize = ((size * 0.999F) * 0.75F) * wobbleAnimXZShadow;
            float squish = squishValue / (shadowSize * 0.5F + 1F);
            float j = (1F / (squish + 1F));
            ((RendererShadowInterface) this).setShadowRadius(0.25F * (j * shadowSize));
        } else {
            ((RendererShadowInterface) this).setShadowRadius(0.25F);
        }

        float g = squishValue / ((size) * 0.5F + 1F);
        this.h = (1F / (g + 1F));
        this.i = size;
        this.yStretch = 1F / this.h * size;
    }

    @ModifyArgs(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V"), method = "scale*")
    public void setScaleArgs(Args args) {
        float x = this.h * this.i;
        args.set(0, x);
        args.set(1, this.yStretch);
        args.set(2, x);
    }
    
    @Inject(at = @At("HEAD"), method = "getTextureLocation*", cancellable = true)
    public void getTextureLocation(MagmaCube slime, CallbackInfoReturnable<ResourceLocation> info) {
        if (ConfigValueGetter.scaleTextures()) {
            int size = Math.min(slime.getSize(), 4);
            info.setReturnValue(new ResourceLocation("lunaslimes","textures/entity/slime/magmacube_" + size + ".png"));
        }
    }

}