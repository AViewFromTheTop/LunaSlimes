package net.lunade.slime.mixin.client;

import net.lunade.slime.impl.SlimeInterface;
import net.minecraft.client.model.LavaSlimeModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LavaSlimeModel.class)
public class LavaSlimeModelMixin<T extends Slime> {

    @ModifyVariable(at = @At("STORE"), method = "prepareMobModel*", ordinal = 3)
    public float prepareMobModel(float original, T slime, float f, float g, float h) {
        return Math.max(Mth.lerp(h, ((SlimeInterface)slime).prevSquish(), slime.squish), 0F);
    }

}