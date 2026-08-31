package net.lunade.slime.impl;

public interface AbstractCubeMobInterface {
	float lunaSlimes$wobbleAnimProgress(float partialTicks);
	void lunaSlimes$playWobbleAnim();
	float lunaSlimes$getSizeScale(float partialTicks);
	void lunaSlimes$cheatSize(float size);
	void lunaSlimes$setJumpAnticTicks(int jumpAnticTicks);
	float lunaSlimes$getDeathProgress(float partialTicks);
	void lunaSlimes$setInWorld(boolean inWorld);
	boolean lunaSlimes$isInWorld();
}
