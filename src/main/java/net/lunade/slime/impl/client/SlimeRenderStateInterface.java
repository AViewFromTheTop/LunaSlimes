package net.lunade.slime.impl.client;

import com.mojang.datafixers.util.Pair;

public interface SlimeRenderStateInterface {
	void lunaSlimes$setWobble(Pair<Float, Float> wobble);
	Pair<Float, Float> lunaSlimes$getWobble();
	void lunaSlimes$setSize(float size);
	float lunaSlimes$getSize();
	void lunaSlimes$setInWorld(boolean inWorld);
	boolean lunaSlimes$isInWorld();
}
