package net.lunade.slime.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lunade.slime.impl.SlimeInterface;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SlimeRenderer.class)
public class SlimeRendererMixin {
    @Unique private static final ResourceLocation SLIME_1 = new ResourceLocation("lunaslimes", "textures/entity/slime/slime_1.png");
    @Unique private static final ResourceLocation SLIME_2 = new ResourceLocation("lunaslimes", "textures/entity/slime/slime_2.png");
    @Unique private static final ResourceLocation SLIME_4 = new ResourceLocation("lunaslimes", "textures/entity/slime/slime_4.png");

    @ModifyVariable(at = @At("STORE"), method = "scale", ordinal = 3)
    public float anims(float original, Slime slime, PoseStack poseStack, float f) {
        float spawnAnimProgress = ((SlimeInterface)slime).spawnAnimProgress(f);
        float splitAnimProgress = ((SlimeInterface)slime).splitAnimProgress(f);

        float splitValue = (float) (((splitAnimProgress + (0.0955F * Math.PI)) * Math.PI) * 5F);
        float splitAnimXZ = (float) ((Math.cos(splitValue) * 0.1F) + 1F);
        float splitAnimY = (float) (-(Math.cos(splitValue) * 0.025F) + 1F);
        poseStack.scale(splitAnimXZ, splitAnimY, splitAnimXZ);
        poseStack.translate(0.0F, -(2.05F - (splitAnimY * 2.05F)), 0.0F);

        return Mth.lerp(f, ((SlimeInterface)slime).prevSquish(), slime.squish) / (((float)slime.getSize()) * 0.5f + 1.0f);
    }

    @Inject(at = @At("HEAD"), method = "getTextureLocation", cancellable = true)
    public void getTextureLocation(Slime slime, CallbackInfoReturnable<ResourceLocation> info) {
        int size = slime.getSize();
        info.setReturnValue(size == 1 ? SLIME_1 : size == 2 ? SLIME_2 : SLIME_4);
    }

}