package net.lunade.slime.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.slime.SlimeMethods;
import net.lunade.slime.config.getter.ConfigValueGetter;
import net.lunade.slime.impl.SlimeInterface;
import net.lunade.slime.render.SlimeTextures;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(SlimeRenderer.class)
public abstract class SlimeRendererMixin extends MobRenderer<Slime, SlimeModel<Slime>> {

    public SlimeRendererMixin(EntityRendererProvider.Context context, SlimeModel<Slime> entityModel, float f) {
        super(context, entityModel, f);
    }

    @ModifyVariable(at = @At("STORE"), method = "scale*", ordinal = 2)
    public float lunaSlimes$modifySize(float original, Slime slime, PoseStack poseStack, float f) {
        return SlimeMethods.getSlimeScale(slime, f);
    }

    @ModifyVariable(at = @At("STORE"), method = "scale*", ordinal = 3)
    public float lunaSlimes$anims(float original, Slime slime, PoseStack poseStack, float f) {
        Pair<Float, Float> wobble = SlimeMethods.wobbleAnim(slime, f);
        float wobbleXZ = wobble.getFirst();
        float wobbleY = wobble.getSecond();
        poseStack.scale(wobbleXZ, wobbleY, wobbleXZ);
        poseStack.translate(0.0F, -(2.05F - (wobbleY * 2.05F)), 0.0F);
        float slimeSize = SlimeMethods.getSlimeScale(slime, f);
        return (Mth.lerp(f, ((SlimeInterface) slime).lunaSlimes$prevSquish(), slime.squish) * ConfigValueGetter.squishMultiplier()) / ((slimeSize) * 0.5f + 1.0f);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/MobRenderer;render(Lnet/minecraft/world/entity/Mob;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", shift = At.Shift.BEFORE), method = "render*")
    public void lunaSlimes$newShadow(Slime slime, float f, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo info) {
        if (ConfigValueGetter.newShadows()) {
            float slimeSize = SlimeMethods.getSlimeScale(slime, partialTick);
            Pair<Float, Float> wobble = SlimeMethods.wobbleAnim(slime, partialTick);
            float wobbleXZ = wobble.getFirst() * 2F;
            float size = ((slimeSize * 0.999F) * 0.75F) * wobbleXZ;
            float squish = (Mth.lerp(partialTick, ((SlimeInterface) slime).lunaSlimes$prevSquish(), slime.squish) * ConfigValueGetter.squishMultiplier()) / (size * 0.5F + 1F);
            float j = (1.0F / (squish + 1F));
            this.shadowRadius = 0.25F * (j * size);
        } else {
            this.shadowRadius = 0.25F * (float)slime.getSize();
        }
    }

    @ModifyReturnValue(at = @At("RETURN"), method = "getTextureLocation")
    public ResourceLocation lunaSlimes$getTextureLocation(ResourceLocation original, Slime slime) {
        return SlimeTextures.getSlimeTexture(slime.getSize(), original);
    }

}