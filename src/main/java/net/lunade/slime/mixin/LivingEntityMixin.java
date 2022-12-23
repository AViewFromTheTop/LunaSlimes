package net.lunade.slime.mixin;

import net.lunade.slime.SlimeMethods;
import net.lunade.slime.config.getter.ConfigValueGetter;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(at = @At("RETURN"), method = "hurt")
    public void hurt(DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> infoReturnable) {
        if (infoReturnable.getReturnValue() && LivingEntity.class.cast(this) instanceof Slime slime) {
            if (!slime.isTiny() && slime.isDeadOrDying() && ConfigValueGetter.useSplitting()) {
                int split = SlimeMethods.spawnSingleSlime(slime);
                slime.setSize(slime.getSize() - split, true);
                slime.deathTime = 0;
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "die", cancellable = true)
    public void die(DamageSource damageSource, CallbackInfo info) {
        LivingEntity entity = LivingEntity.class.cast(this);
        if (entity instanceof Slime slime) {
            if (damageSource != DamageSource.OUT_OF_WORLD && !slime.isTiny()  && ConfigValueGetter.useSplitting()) {
                info.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "doPush")
    public void doPush(Entity entity, CallbackInfo info) {
        LivingEntity thisEntity = LivingEntity.class.cast(this);
        if (thisEntity instanceof Slime && entity instanceof Slime slime2) {
            SlimeMethods.mergeSlimes(Slime.class.cast(this), slime2);
        }
    }

}