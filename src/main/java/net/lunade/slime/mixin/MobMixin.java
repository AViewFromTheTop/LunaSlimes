package net.lunade.slime.mixin;

import net.lunade.slime.SlimeMethods;
import net.lunade.slime.config.getter.ConfigValueGetter;
import net.lunade.slime.impl.SlimeInterface;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public class MobMixin {

    @Inject(at = @At("HEAD"), method = "handleEntityEvent")
    public void lunaSlimes$handleEntityEvent(byte b, CallbackInfo info) {
        if (Mob.class.cast(this) instanceof Slime slime && b == (byte) 61 && ConfigValueGetter.jumpAntic()) {
            SlimeMethods.setSquish(slime, -0.05F);
            ((SlimeInterface) slime).lunaSlimes$setJumpAnticTicks(3);
        }
    }

}
