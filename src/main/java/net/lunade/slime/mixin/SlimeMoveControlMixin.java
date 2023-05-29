package net.lunade.slime.mixin;

import net.lunade.slime.config.getter.ConfigValueGetter;
import net.lunade.slime.impl.SlimeInterface;
import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Slime.SlimeMoveControl.class)
public class SlimeMoveControlMixin {

    @Unique
    boolean lunaSlimes$wasInit;
    @Shadow
    private int jumpDelay;
    @Shadow
    @Final
    private Slime slime;

    @Inject(at = @At("TAIL"), method = "<init>")
    public void lunaSlimes$init(Slime slime, CallbackInfo info) {
        if (!this.lunaSlimes$wasInit) {
            this.jumpDelay = ((SlimeInterface) slime).lunaSlimes$getSavedJumpDelay();
            this.lunaSlimes$wasInit = true;
        }
    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void lunaSlimes$tick(CallbackInfo info) {
        SlimeInterface slimeInterface = (SlimeInterface) this.slime;
        slimeInterface.lunaSlimes$setJumpDelay(this.jumpDelay);
        if (ConfigValueGetter.jumpAntic()) {
            boolean antic = this.slime.onGround() && !this.slime.isInWater();
            if (this.jumpDelay == 3 && antic) {
                slime.level().broadcastEntityEvent(slime, (byte) 61);
                this.slime.targetSquish = -0.05F;
                slimeInterface.lunaSlimes$setJumpAntic(true);
            } else if (this.jumpDelay == 2 && antic) {
                this.slime.targetSquish = -0.15F;
                slimeInterface.lunaSlimes$setJumpAntic(true);
            } else if (this.jumpDelay == 1 && antic) {
                this.slime.targetSquish = -0.3F;
                slimeInterface.lunaSlimes$setJumpAntic(true);
            } else {
                slimeInterface.lunaSlimes$setJumpAntic(false);
            }
        } else {
            slimeInterface.lunaSlimes$setJumpAntic(false);
        }
    }

    @Inject(at = @At("TAIL"), method = "tick")
    public void lunaSlimes$tickTail(CallbackInfo info) {
        ((SlimeInterface) this.slime).lunaSlimes$setJumpDelay(this.jumpDelay);
    }

}
