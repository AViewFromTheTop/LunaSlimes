package net.lunade.slime.mixin;

import net.lunade.slime.config.frozenlib.LunaSlimesVisualsAudioConfig;
import net.lunade.slime.registry.LunaSlimesAttachmentTypes;
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

	@Inject(at = @At("TAIL"), method = "<init>")
	public void lunaSlimes$init(T cubeMob, CallbackInfo info) {
		this.jumpDelay = cubeMob.frozenLib$getAttachedOrCreate(LunaSlimesAttachmentTypes.JUMP_DELAY);
		this.lunaSlimes$cube = cubeMob;
	}

	@Inject(at = @At("HEAD"), method = "tick")
	public void lunaSlimes$startTick(CallbackInfo info) {
		this.lunaSlimes$cube.frozenLib$setAttached(LunaSlimesAttachmentTypes.JUMP_DELAY, this.jumpDelay);
		if (!LunaSlimesVisualsAudioConfig.JUMP_ANTIC.get()) {
			this.lunaSlimes$cube.frozenLib$setAttached(LunaSlimesAttachmentTypes.JUMP_ANTIC, false);
			return;
		}

		final boolean antic = this.lunaSlimes$cube.onGround() && !this.lunaSlimes$cube.isInWater();
		if (this.jumpDelay == 3 && antic) {
			this.lunaSlimes$cube.level().broadcastEntityEvent(lunaSlimes$cube, EntityEvent.TENDRILS_SHIVER);
			this.lunaSlimes$cube.targetSquish = -0.05F;
			this.lunaSlimes$cube.frozenLib$setAttached(LunaSlimesAttachmentTypes.JUMP_ANTIC, true);
		} else if (this.jumpDelay == 2 && antic) {
			this.lunaSlimes$cube.targetSquish = -0.15F;
			this.lunaSlimes$cube.frozenLib$setAttached(LunaSlimesAttachmentTypes.JUMP_ANTIC, true);
		} else if (this.jumpDelay == 1 && antic) {
			this.lunaSlimes$cube.targetSquish = -0.3F;
			this.lunaSlimes$cube.frozenLib$setAttached(LunaSlimesAttachmentTypes.JUMP_ANTIC, true);
		} else {
			this.lunaSlimes$cube.frozenLib$setAttached(LunaSlimesAttachmentTypes.JUMP_ANTIC, false);
		}
	}

	@Inject(at = @At("TAIL"), method = "tick")
	public void lunaSlimes$endTick(CallbackInfo info) {
		this.lunaSlimes$cube.frozenLib$setAttached(LunaSlimesAttachmentTypes.JUMP_DELAY, this.jumpDelay);
	}
}
