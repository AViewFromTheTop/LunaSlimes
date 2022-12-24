package net.lunade.slime.mixin;

import net.lunade.slime.config.getter.ConfigValueGetter;
import net.lunade.slime.impl.SlimeInterface;
import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Slime.SlimeMoveControl.class)
public class SlimeMoveControlMixin {

    @Shadow private int jumpDelay;
    @Shadow @Final private Slime slime;

    @Inject(at = @At("TAIL"), method = "<init>")
    public void init(Slime slime, CallbackInfo info) {
        this.jumpDelay = ((SlimeInterface)slime).getJumpDelay();
    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void tick(CallbackInfo info) {
        ((SlimeInterface)slime).setJumpDelay(this.jumpDelay);
        if (ConfigValueGetter.jumpAntic()) {
            boolean antic = slime.isOnGround() && !slime.isInWater();
            if (this.jumpDelay == 3 && antic) {
                this.slime.targetSquish = -0.3F;
                ((SlimeInterface)this.slime).setJumpAntic(true);
            } else if (this.jumpDelay == 2 && antic) {
                this.slime.targetSquish = -0.4F;
                ((SlimeInterface)this.slime).setJumpAntic(true);
            } else if (this.jumpDelay == 1 && antic) {
                this.slime.targetSquish = -0.575F;
                ((SlimeInterface)this.slime).setJumpAntic(true);
            } else {
                ((SlimeInterface)this.slime).setJumpAntic(false);
            }
        } else {
            ((SlimeInterface)this.slime).setJumpAntic(false);
        }
    }

    @Inject(at = @At("TAIL"), method = "tick")
    public void tickTail(CallbackInfo info) {
        ((SlimeInterface)slime).setJumpDelay(this.jumpDelay);
    }

}
