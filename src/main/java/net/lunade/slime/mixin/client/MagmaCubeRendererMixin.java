package net.lunade.slime.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.lunade.slime.SlimeMethods;
import net.lunade.slime.config.getter.ConfigValueGetter;
import net.lunade.slime.impl.SlimeInterface;
import net.lunade.slime.render.MagmaCubeLayer;
import net.minecraft.client.model.LavaSlimeModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MagmaCubeRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.level.LightLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(MagmaCubeRenderer.class)
public abstract class MagmaCubeRendererMixin extends MobRenderer<MagmaCube, LavaSlimeModel<MagmaCube>> {

    @Unique
    float h;
    @Unique
    float i;
    @Unique
    float yStretch;

    public MagmaCubeRendererMixin(EntityRendererProvider.Context context, LavaSlimeModel<MagmaCube> entityModel, float f) {
        super(context, entityModel, f);
    }

    @Inject(at = @At("TAIL"), method = "<init>")
    public void lunaSlimes$init(EntityRendererProvider.Context context, CallbackInfo info) {
        MagmaCubeRenderer renderer = MagmaCubeRenderer.class.cast(this);
        renderer.addLayer(new MagmaCubeLayer<>(renderer));
    }

    @Inject(at = @At("HEAD"), method = "scale*")
    public void lunaSlimes$anims(MagmaCube slime, PoseStack poseStack, float f, CallbackInfo info) {
        Pair<Float, Float> wobble = SlimeMethods.wobbleAnim(slime, f);
        float wobbleXZ = wobble.getFirst();
        float wobbleY = wobble.getSecond();
        poseStack.scale(wobbleXZ, wobbleY, wobbleXZ);
        poseStack.translate(0.0F, -(2.05F - (wobbleY * 2.05F)), 0F);
        float size = SlimeMethods.getSlimeScale(slime, f);
        float squishValue = Mth.lerp(f, ((SlimeInterface) slime).lunaSlimes$prevSquish(), slime.squish) * ConfigValueGetter.squishMultiplier();

        float g = squishValue / ((size) * 0.5F + 1F);
        this.h = (1F / (g + 1F));
        this.i = size;
        this.yStretch = 1F / this.h * size;
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/MobRenderer;render(Lnet/minecraft/world/entity/Mob;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", shift = At.Shift.BEFORE), method = "render*")
    public void lunaSlimes$newShadows(MagmaCube magmaCube, float f, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        if (ConfigValueGetter.newShadows()) {
            float slimeSize = SlimeMethods.getSlimeScale(magmaCube, partialTick);
            Pair<Float, Float> wobble = SlimeMethods.wobbleAnim(magmaCube, partialTick);
            float wobbleXZ = wobble.getFirst() * 2F;
            float size = ((slimeSize * 0.999F) * 0.75F) * wobbleXZ;
            float squish = (Mth.lerp(partialTick, ((SlimeInterface) magmaCube).lunaSlimes$prevSquish(), magmaCube.squish) * ConfigValueGetter.squishMultiplier()) / (size * 0.5F + 1F);
            float j = (1.0F / (squish + 1F));
            this.shadowRadius = 0.25F * (j * size);
        } else {
            this.shadowRadius = 0.25F * (float)magmaCube.getSize();
        }
    }

    @ModifyArgs(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V"), method = "scale*")
    public void lunaSlimes$setScaleArgs(Args args) {
        float x = this.h * this.i;
        args.set(0, x);
        args.set(1, this.yStretch);
        args.set(2, x);
    }

    @Inject(at = @At("HEAD"), method = "getTextureLocation*", cancellable = true)
    public void lunaSlimes$getTextureLocation(MagmaCube slime, CallbackInfoReturnable<ResourceLocation> info) {
        if (ConfigValueGetter.scaleTextures()) {
            int size = Math.min(slime.getSize(), 4);
            info.setReturnValue(new ResourceLocation("lunaslimes", "textures/entity/slime/magmacube_" + size + ".png"));
        }
    }

    @Inject(at = @At("HEAD"), method = "getBlockLightLevel*", cancellable = true)
    public void lunaSlimes$getBlockLightLevel(MagmaCube entity, BlockPos pos, CallbackInfoReturnable<Integer> info) {
        if (ConfigValueGetter.glowingMagma()) {
            info.setReturnValue(entity.isOnFire() ? 15 : entity.level().getBrightness(LightLayer.BLOCK, pos));
        }
    }

}