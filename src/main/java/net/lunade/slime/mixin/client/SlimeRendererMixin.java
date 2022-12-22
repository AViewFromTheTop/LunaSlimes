package net.lunade.slime.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lunade.slime.impl.SlimeInterface;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SlimeRenderer.class)
public class SlimeRendererMixin {

    @ModifyVariable(at = @At("STORE"), method = "scale", ordinal = 3)
    public float newLerp(float original, Slime slime, PoseStack poseStack, float f) {
        return Mth.lerp(f, ((SlimeInterface)slime).prevSquish(), slime.squish) / (((float)slime.getSize()) * 0.5f + 1.0f);
    }

}