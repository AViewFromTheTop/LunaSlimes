package net.lunade.slime.mixin;

import net.lunade.slime.config.getter.ConfigValueGetter;
import net.lunade.slime.impl.SlimeInterface;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MagmaCube.class)
public class MagmaCubeMixin {

    @Inject(at = @At("HEAD"), method = "decreaseSquish", cancellable = true)
    public void lunaSlimes$decreaseSquish(CallbackInfo info) {
        Slime slime = Slime.class.cast(this);
        if ((((SlimeInterface) slime).lunaSlimes$getJumpAntic() && ConfigValueGetter.jumpAntic()) || !((SlimeInterface) slime).lunaSlimes$canSquish()) {
            info.cancel();
        }
    }

}