package net.lunade.slime.mixin;

import net.lunade.slime.config.getter.LunaSlimesConfigValueGetter;
import net.lunade.slime.impl.SlimeInterface;
import net.minecraft.world.entity.monster.MagmaCube;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MagmaCube.class)
public class MagmaCubeMixin {

	@Inject(method = "decreaseSquish", at = @At("HEAD"), cancellable = true)
	public void lunaSlimes$decreaseSquish(CallbackInfo info) {
		if (!(MagmaCube.class.cast(this) instanceof SlimeInterface slimeInterface)) return;
		if ((slimeInterface.lunaSlimes$getJumpAntic() && LunaSlimesConfigValueGetter.jumpAntic()) || !slimeInterface.lunaSlimes$canSquish()) info.cancel();
	}

}
