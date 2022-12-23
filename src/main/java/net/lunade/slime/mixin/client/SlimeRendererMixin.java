package net.lunade.slime.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
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
    @Unique private static final ResourceLocation SLIME_1 = new ResourceLocation("lunaslimes", "textures/entity/slime/slime_1.png");
    @Unique private static final ResourceLocation SLIME_2 = new ResourceLocation("lunaslimes", "textures/entity/slime/slime_2.png");
    @Unique private static final ResourceLocation SLIME_4 = new ResourceLocation("lunaslimes", "textures/entity/slime/slime_4.png");

    @Unique float partialTick;

    @ModifyVariable(at = @At("STORE"), method = "scale", ordinal = 2)
    public float modifySize(float original, Slime slime, PoseStack poseStack, float f) {
        this.partialTick = f;
        return ((SlimeInterface)slime).getSizeScale(f);
    }

    @ModifyVariable(at = @At("STORE"), method = "scale", ordinal = 3)
    public float anims(float original, Slime slime, PoseStack poseStack, float f) {
        float splitAnimProgress = ((SlimeInterface)slime).splitAnimProgress(f);

        float splitValue = (float) (((splitAnimProgress + (0.0955F * Math.PI)) * Math.PI) * 5F);
        float splitAnimXZ = (float) ((Math.cos(splitValue) * 0.1F) + 1F);
        float splitAnimY = (float) (-(Math.cos(splitValue) * 0.025F) + 1F);
        poseStack.scale(splitAnimXZ, splitAnimY, splitAnimXZ);
        poseStack.translate(0.0F, -(2.05F - (splitAnimY * 2.05F)), 0.0F);

        return Mth.lerp(f, ((SlimeInterface)slime).prevSquish(), slime.squish) / ((((SlimeInterface)slime).getSizeScale(f)) * 0.5f + 1.0f);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/MobRenderer;render(Lnet/minecraft/world/entity/Mob;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", shift = At.Shift.BEFORE), method = "render")
    public void render(Slime slime, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo info) {
        float splitAnimProgress = ((SlimeInterface)slime).splitAnimProgress(this.partialTick);
        float splitAnimXZ = (float) ((Math.cos((float) (((splitAnimProgress + (0.0955F * Math.PI)) * Math.PI) * 5F)) * 0.1F) + 1F) * 2F;
        float size = ((((SlimeInterface)slime).getSizeScale(this.partialTick) * 0.999F) * 0.75F) * splitAnimXZ;
        float squish = Mth.lerp(this.partialTick, ((SlimeInterface)slime).prevSquish(), slime.squish) / (size * 0.5f + 1.0f);
        float j = (1.0F / (squish + 1.0F));
        ((RendererShadowInterface)this).setShadowRadius(0.25F * (j * size));
    }

    @Inject(at = @At("HEAD"), method = "getTextureLocation", cancellable = true)
    public void getTextureLocation(Slime slime, CallbackInfoReturnable<ResourceLocation> info) {
        int size = slime.getSize();
        info.setReturnValue(size == 1 ? SLIME_1 : size == 2 ? SLIME_2 : SLIME_4);
    }

}