package net.lunade.slime.mixin;

import net.lunade.slime.config.frozenlib.LSVisualsAudioConfig;
import net.lunade.slime.registry.LSAttachmentTypes;
import net.minecraft.world.entity.monster.cubemob.MagmaCube;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MagmaCube.class)
public class MagmaCubeMixin {

	@Inject(method = "decreaseSquish", at = @At("HEAD"), cancellable = true)
	public void lunaSlimes$decreaseSquish(CallbackInfo info) {
		final MagmaCube magmaCube = MagmaCube.class.cast(this);
		final boolean jumpAntic = LSAttachmentTypes.JUMP_ANTIC.getAttachedOrCreate(magmaCube) && LSVisualsAudioConfig.JUMP_ANTIC.get();
		if (jumpAntic|| !LSAttachmentTypes.CAN_SQUISH.getAttachedOrCreate(magmaCube)) info.cancel();
	}
}
