package net.lunade.slime.mixin;


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

    @Inject(at = @At("HEAD"), method = "tick")
    public void tick(CallbackInfo info) {
        if (this.jumpDelay == 10) {
            this.slime.targetSquish = -0.25F;
        }
    }

}
