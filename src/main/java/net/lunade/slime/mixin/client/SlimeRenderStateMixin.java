package net.lunade.slime.mixin.client;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.slime.impl.client.SlimeRenderStateInterface;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
@Mixin(SlimeRenderState.class)
public class SlimeRenderStateMixin implements SlimeRenderStateInterface {

	@Unique
	private Pair<Float, Float> lunaSlimes$wobbleAnim;

	@Unique
	private float lunaSlimes$slimeSize;

	@Unique
	private boolean lunaSlimes$inWorld;

	@Unique
	@Override
	public void lunaSlimes$setWobble(Pair<Float, Float> wobble) {
		this.lunaSlimes$wobbleAnim = wobble;
	}

	@Unique
	@Override
	public Pair<Float, Float> lunaSlimes$getWobble() {
		return this.lunaSlimes$wobbleAnim;
	}

	@Unique
	@Override
	public void lunaSlimes$setSize(float size) {
		this.lunaSlimes$slimeSize = size;
	}

	@Unique
	@Override
	public float lunaSlimes$getSize() {
		return this.lunaSlimes$slimeSize;
	}

	@Unique
	@Override
	public void lunaSlimes$setInWorld(boolean inWorld) {
		this.lunaSlimes$inWorld = inWorld;
	}

	@Unique
	@Override
	public boolean lunaSlimes$isInWorld() {
		return this.lunaSlimes$inWorld;
	}
}
