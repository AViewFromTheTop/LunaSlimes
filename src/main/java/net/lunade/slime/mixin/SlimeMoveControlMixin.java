package net.lunade.slime.mixin;

import net.lunade.slime.config.frozenlib.LunaSlimesVisualsAudioConfig;
import net.lunade.slime.registry.LunaSlimesAttachmentTypes;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Slime.SlimeMoveControl.class)
public class SlimeMoveControlMixin {

	@Shadow
	private int jumpDelay;
	@Shadow
	@Final
	private Slime slime;

	@Inject(at = @At("TAIL"), method = "<init>")
	public void lunaSlimes$init(Slime slime, CallbackInfo info) {
		this.jumpDelay = slime.getAttachedOrCreate(LunaSlimesAttachmentTypes.JUMP_DELAY);
	}

	@Inject(at = @At("HEAD"), method = "tick")
	public void lunaSlimes$tick(CallbackInfo info) {
		this.slime.setAttached(LunaSlimesAttachmentTypes.JUMP_DELAY, this.jumpDelay);
		if (!LunaSlimesVisualsAudioConfig.JUMP_ANTIC.get()) {
			this.slime.setAttached(LunaSlimesAttachmentTypes.JUMP_ANTIC, false);
			return;
		}

		final boolean antic = this.slime.onGround() && !this.slime.isInWater();
		if (this.jumpDelay == 3 && antic) {
			this.slime.level().broadcastEntityEvent(slime, EntityEvent.TENDRILS_SHIVER);
			this.slime.targetSquish = -0.05F;
			this.slime.setAttached(LunaSlimesAttachmentTypes.JUMP_ANTIC, true);
		} else if (this.jumpDelay == 2 && antic) {
			this.slime.targetSquish = -0.15F;
			this.slime.setAttached(LunaSlimesAttachmentTypes.JUMP_ANTIC, true);
		} else if (this.jumpDelay == 1 && antic) {
			this.slime.targetSquish = -0.3F;
			this.slime.setAttached(LunaSlimesAttachmentTypes.JUMP_ANTIC, true);
		} else {
			this.slime.setAttached(LunaSlimesAttachmentTypes.JUMP_ANTIC, false);
		}
	}

	@Inject(at = @At("TAIL"), method = "tick")
	public void lunaSlimes$tickTail(CallbackInfo info) {
		this.slime.setAttached(LunaSlimesAttachmentTypes.JUMP_DELAY, this.jumpDelay);
	}

}
