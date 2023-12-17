package net.lunade.slime.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lunade.slime.impl.SlimeInterface;
import net.minecraft.client.model.LavaSlimeModel;
import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LavaSlimeModel.class)
public class LavaSlimeModelMixin<T extends Slime> {

    @WrapOperation(method = "prepareMobModel*", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;lerp(FFF)F", ordinal = 0), require = 0)
    public float lunaSlimes$prepareMobModel(float delta, float prev, float current, Operation<Float> operation, T slime) {
        return Math.max(operation.call(delta, ((SlimeInterface) slime).lunaSlimes$prevSquish(), current), 0F);
    }

}