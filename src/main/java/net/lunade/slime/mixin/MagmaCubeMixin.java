package net.lunade.slime.mixin;

import net.lunade.slime.config.frozenlib.LunaSlimesVisualsAudioConfig;
import net.lunade.slime.registry.LunaSlimesAttachmentTypes;
import net.minecraft.world.entity.monster.MagmaCube;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MagmaCube.class)
public class MagmaCubeMixin {

	@Inject(method = "decreaseSquish", at = @At("HEAD"), cancellable = true)
	public void lunaSlimes$decreaseSquish(CallbackInfo info) {
		final MagmaCube magmaCube = MagmaCube.class.cast(this);
		final boolean jumpAntic = magmaCube.getAttachedOrCreate(LunaSlimesAttachmentTypes.JUMP_ANTIC) && LunaSlimesVisualsAudioConfig.JUMP_ANTIC.get();
		if (jumpAntic|| !magmaCube.getAttachedOrCreate(LunaSlimesAttachmentTypes.CAN_SQUISH)) info.cancel();
	}

}
