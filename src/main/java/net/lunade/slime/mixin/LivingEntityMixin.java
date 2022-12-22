package net.lunade.slime.mixin;

import net.lunade.slime.impl.SlimeInterface;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Unique float prevHealth;

    @Inject(at = @At("HEAD"), method = "hurt")
    public void savePrevHealth(DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> infoReturnable) {
        this.prevHealth = LivingEntity.class.cast(this).getHealth();
    }

    @Inject(at = @At("RETURN"), method = "hurt")
    public void hurt(DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> infoReturnable) {
        if (infoReturnable.getReturnValue() && LivingEntity.class.cast(this) instanceof Slime slime) {
            if (!slime.isTiny()) {
                slime.setHealth(this.prevHealth);
                int split = ((SlimeInterface)slime).spawnSingleSlime();
                slime.setSize(slime.getSize() - split, true);
                slime.deathTime = 0;
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "die", cancellable = true)
    public void die(DamageSource damageSource, CallbackInfo info) {
        LivingEntity entity = LivingEntity.class.cast(this);
        if (entity instanceof Slime slime) {
            if (damageSource != DamageSource.OUT_OF_WORLD && !slime.isTiny()) {
                info.cancel();
            }
        }
    }

}