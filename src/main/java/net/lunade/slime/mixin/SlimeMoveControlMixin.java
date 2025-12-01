package net.lunade.slime.mixin;

import net.lunade.slime.config.getter.LunaSlimesConfigValueGetter;
import net.lunade.slime.impl.SlimeInterface;
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
		if (!(this.slime instanceof SlimeInterface slimeInterface)) return;
		this.jumpDelay = slimeInterface.lunaSlimes$getSavedJumpDelay();
	}

	@Inject(at = @At("HEAD"), method = "tick")
	public void lunaSlimes$tick(CallbackInfo info) {
		if (!(this.slime instanceof SlimeInterface slimeInterface)) return;

		slimeInterface.lunaSlimes$setJumpDelay(this.jumpDelay);
		if (!LunaSlimesConfigValueGetter.jumpAntic()) {
			slimeInterface.lunaSlimes$setJumpAntic(false);
			return;
		}

		final boolean antic = this.slime.onGround() && !this.slime.isInWater();
		if (this.jumpDelay == 3 && antic) {
			this.slime.level().broadcastEntityEvent(slime, (byte) 61);
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
	}

	@Inject(at = @At("TAIL"), method = "tick")
	public void lunaSlimes$tickTail(CallbackInfo info) {
		if (!(this.slime instanceof SlimeInterface slimeInterface)) return;
		slimeInterface.lunaSlimes$setJumpDelay(this.jumpDelay);
	}

}
