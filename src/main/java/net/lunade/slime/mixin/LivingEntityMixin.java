package net.lunade.slime.mixin;

import net.lunade.slime.SlimeMethods;
import net.lunade.slime.config.getter.ConfigValueGetter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(at = @At("RETURN"), method = "hurt")
    public void lunaSlimes$hurt(DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> infoReturnable) {
        if (infoReturnable.getReturnValue() && LivingEntity.class.cast(this) instanceof Slime slime) {
            if (!slime.isTiny() && slime.isDeadOrDying() && ConfigValueGetter.useSplitting()) {
                int split = SlimeMethods.spawnSingleSlime(slime);
                slime.setSize(slime.getSize() - split, true);
                slime.deathTime = 0;
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "die", cancellable = true)
    public void lunaSlimes$die(DamageSource damageSource, CallbackInfo info) {
        LivingEntity entity = LivingEntity.class.cast(this);
        if (entity instanceof Slime slime) {
            Optional<ResourceKey<DamageType>> damageType = damageSource.typeHolder().unwrapKey();
            if (damageType.isPresent() && damageType.get() != DamageTypes.GENERIC_KILL && !slime.isTiny() && ConfigValueGetter.useSplitting()) {
                info.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "doPush")
    public void lunaSlimes$doPush(Entity entity, CallbackInfo info) {
        LivingEntity thisEntity = LivingEntity.class.cast(this);
        if (thisEntity instanceof Slime && entity instanceof Slime slime2) {
            SlimeMethods.mergeSlimes(Slime.class.cast(this), slime2);
        }
    }

    @Inject(at = @At("HEAD"), method = "knockback", cancellable = true)
    public void lunaSlimes$knockback(double d, double e, double f, CallbackInfo info) {
        LivingEntity entity = LivingEntity.class.cast(this);
        if (entity instanceof Slime slime) {
            if (slime.isTiny() && slime.isDeadOrDying() && ConfigValueGetter.deathAnim()) {
                info.cancel();
            }
        }
    }

}