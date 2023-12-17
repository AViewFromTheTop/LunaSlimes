package net.lunade.slime.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.slime.SlimeMethods;
import net.lunade.slime.config.getter.ConfigValueGetter;
import net.lunade.slime.impl.SlimeInterface;
import net.lunade.slime.render.MagmaCubeLayer;
import net.lunade.slime.render.SlimeTextures;
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
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(MagmaCubeRenderer.class)
public abstract class MagmaCubeRendererMixin extends MobRenderer<MagmaCube, LavaSlimeModel<MagmaCube>> {

    @Unique
    float lunaSlimes$h;
    @Unique
    float lunaSlimes$i;
    @Unique
    float lunaSlimes$yStretch;

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
        this.lunaSlimes$h = (1F / (g + 1F));
        this.lunaSlimes$i = size;
        this.lunaSlimes$yStretch = 1F / this.lunaSlimes$h * size;
    }

    @Inject(method = "render*", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/MagmaCubeRenderer;shadowRadius:F", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
    public void lunaSlimes$newShadows(MagmaCube magmaCube, float f, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo info) {
        if (ConfigValueGetter.newShadows()) {
            float slimeSize = SlimeMethods.getSlimeScale(magmaCube, partialTick);
            Pair<Float, Float> wobble = SlimeMethods.wobbleAnim(magmaCube, partialTick);
            float wobbleXZ = wobble.getFirst() * 2F;
            float size = ((slimeSize * 0.999F) * 0.75F) * wobbleXZ;
            float squish = (Mth.lerp(partialTick, ((SlimeInterface) magmaCube).lunaSlimes$prevSquish(), magmaCube.squish) * ConfigValueGetter.squishMultiplier()) / (size * 0.5F + 1F);
            float j = (1.0F / (squish + 1F));
            this.shadowRadius = 0.25F * (j * size);
        }
    }

    @WrapOperation(method = "scale", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V"))
    public void lunaSlimes$setScaleArgs(PoseStack poseStack, float a, float b, float c, Operation<Void> operation, MagmaCube magmaCube) {
        if (((SlimeInterface) magmaCube).lunaSlimes$isInWorld()) {
            float x = this.lunaSlimes$h * this.lunaSlimes$i;
            operation.call(poseStack, x, this.lunaSlimes$yStretch, x);
        } else {
            operation.call(poseStack, a, b, c);
        }
    }

    @ModifyReturnValue(at = @At("RETURN"), method = "getTextureLocation")
    public ResourceLocation lunaSlimes$getTextureLocation(ResourceLocation original, MagmaCube magmaCube) {
        return SlimeTextures.getMamaCubeTexture(magmaCube.getSize(), original);
    }

    @ModifyReturnValue(at = @At("RETURN"), method = "getBlockLightLevel")
    public int lunaSlimes$getBlockLightLevel(int original, MagmaCube magmaCube, BlockPos pos) {
        if (ConfigValueGetter.glowingMagma()) {
            return magmaCube.isOnFire() ? original : magmaCube.level().getBrightness(LightLayer.BLOCK, pos);
        }
        return original;
    }

}