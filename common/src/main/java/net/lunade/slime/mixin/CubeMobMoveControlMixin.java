package net.lunade.slime.mixin;

import net.lunade.slime.config.frozenlib.LSVisualsAudioConfig;
import net.lunade.slime.registry.LSAttachmentTypes;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.monster.cubemob.AbstractCubeMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractCubeMob.CubeMobMoveControl.class)
public class CubeMobMoveControlMixin<T extends AbstractCubeMob> {
	@Shadow
	private int jumpDelay;

	@Unique
	private AbstractCubeMob lunaSlimes$cube;

	@Inject(at = @At("RETURN"), method = "<init>")
	public void lunaSlimes$init(T cubeMob, CallbackInfo info) {
		this.jumpDelay = LSAttachmentTypes.JUMP_DELAY.getAttachedOrCreate(cubeMob);
		this.lunaSlimes$cube = cubeMob;
	}

	@Inject(at = @At("HEAD"), method = "tick")
	public void lunaSlimes$startTick(CallbackInfo info) {
		if (this.lunaSlimes$cube.level().isClientSide()) return;

		LSAttachmentTypes.JUMP_DELAY.set(this.lunaSlimes$cube, this.jumpDelay);

		if (!LSVisualsAudioConfig.JUMP_ANTIC.get()) {
			LSAttachmentTypes.JUMP_ANTIC.set(this.lunaSlimes$cube, false);
			return;
		}

		final boolean antic = this.lunaSlimes$cube.onGround() && !this.lunaSlimes$cube.isInWater();
		if (this.jumpDelay == 3 && antic) {
			this.lunaSlimes$cube.level().broadcastEntityEvent(this.lunaSlimes$cube, EntityEvent.TENDRILS_SHIVER);
			this.lunaSlimes$cube.targetSquish = -0.05F;
			LSAttachmentTypes.JUMP_ANTIC.set(this.lunaSlimes$cube, true);
		} else if (this.jumpDelay == 2 && antic) {
			this.lunaSlimes$cube.targetSquish = -0.15F;
			LSAttachmentTypes.JUMP_ANTIC.set(this.lunaSlimes$cube, true);
		} else if (this.jumpDelay == 1 && antic) {
			this.lunaSlimes$cube.targetSquish = -0.3F;
			LSAttachmentTypes.JUMP_ANTIC.set(this.lunaSlimes$cube, true);
		} else {
			LSAttachmentTypes.JUMP_ANTIC.set(this.lunaSlimes$cube, false);
		}
	}

	@Inject(at = @At("RETURN"), method = "tick")
	public void lunaSlimes$endTick(CallbackInfo info) {
		if (this.lunaSlimes$cube.level().isClientSide()) return;

		LSAttachmentTypes.JUMP_DELAY.set(this.lunaSlimes$cube, this.jumpDelay);
	}
}
