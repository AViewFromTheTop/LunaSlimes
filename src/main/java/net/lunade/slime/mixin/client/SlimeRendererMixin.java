package net.lunade.slime.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lunade.slime.SlimeMethods;
import net.lunade.slime.config.getter.ConfigValueGetter;
import net.lunade.slime.impl.RendererShadowInterface;
import net.lunade.slime.impl.SlimeInterface;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SlimeRenderer.class)
public class SlimeRendererMixin {

    @Unique float partialTick;

    @ModifyVariable(at = @At("STORE"), method = "scale*", ordinal = 2)
    public float modifySize(float original, Slime slime, PoseStack poseStack, float f) {
        this.partialTick = f;
        return SlimeMethods.getSlimeScale(slime, f);
    }

    @ModifyVariable(at = @At("STORE"), method = "scale*", ordinal = 3)
    public float anims(float original, Slime slime, PoseStack poseStack, float f) {
        float wobbleAnimProgress = SlimeMethods.getSlimeWobbleAnimProgress(slime, f);
        float wobbleValue = (float) (((wobbleAnimProgress + (0.0955F * Math.PI)) * Math.PI) * 5F);
        float wobbleXZ = (float) ((Math.cos(wobbleValue) * 0.1F) + 1F);
        float wobbleY = (float) (-(Math.cos(wobbleValue) * 0.025F) + 1F);
        poseStack.scale(wobbleXZ, wobbleY, wobbleXZ);
        poseStack.translate(0.0F, -(2.05F - (wobbleY * 2.05F)), 0.0F);
        float slimeSize = SlimeMethods.getSlimeScale(slime, f);
        return (Mth.lerp(f, ((SlimeInterface)slime).prevSquish(), slime.squish) * ConfigValueGetter.squishMultiplier()) / ((slimeSize) * 0.5f + 1.0f);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/MobRenderer;render(Lnet/minecraft/world/entity/Mob;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", shift = At.Shift.BEFORE), method = "render*")
    public void newShadow(Slime slime, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo info) {
        if (ConfigValueGetter.newShadows()) {
            float slimeSize = SlimeMethods.getSlimeScale(slime, this.partialTick);
            float wobbleAnimProgress = SlimeMethods.getSlimeWobbleAnimProgress(slime, this.partialTick);
            float wobbleXZ = (float) ((Math.cos((float) (((wobbleAnimProgress + (0.0955F * Math.PI)) * Math.PI) * 5F)) * 0.1F) + 1F) * 2F;
            float size = ((slimeSize * 0.999F) * 0.75F) * wobbleXZ;
            float squish = (Mth.lerp(this.partialTick, ((SlimeInterface) slime).prevSquish(), slime.squish) * ConfigValueGetter.squishMultiplier()) / (size * 0.5f + 1.0f);
            float j = (1.0F / (squish + 1.0F));
            ((RendererShadowInterface) this).setShadowRadius(0.25F * (j * size));
        }
    }

    @Inject(at = @At("HEAD"), method = "getTextureLocation*", cancellable = true)
    public void getTextureLocation(Slime slime, CallbackInfoReturnable<ResourceLocation> info) {
        if (ConfigValueGetter.scaleTextures()) {
            int size = Math.min(slime.getSize(),4);
            info.setReturnValue(new ResourceLocation("lunaslimes","textures/entity/slime/slime_" + size + ".png"));
        }
    }

}