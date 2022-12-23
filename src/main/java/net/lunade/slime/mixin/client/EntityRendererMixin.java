package net.lunade.slime.mixin.client;

import net.lunade.slime.impl.RendererShadowInterface;
import net.minecraft.client.renderer.entity.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin implements RendererShadowInterface {

    @Shadow
    public float shadowRadius;

    @Override
    public void setShadowRadius(float radius) {
        this.shadowRadius = radius;
    }

}