package net.lunade.slime.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.lunade.slime.config.frozenlib.LunaSlimesVisualsAudioConfig;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;

@ClientOnly
public final class LunaSlimesRenderUtil {
	private LunaSlimesRenderUtil() {}

	public static final Pair<Float, Float> FALLBACK_WOBBLE = Pair.of(1F, 1F);

	public static void applyWobbleAndSquish(
		PoseStack poseStack, float xScale, float yScale, float zScale, Operation<Void> operation,
		SlimeRenderState state,
		boolean skipSquish
	) {
		if (!state.frozenLib$getDataOrDefault(LunaSlimesRenderStateDataKeys.IN_WORLD, false)) {
			operation.call(poseStack, xScale, yScale, zScale);
			return;
		}

		final float cubeSize = state.frozenLib$getDataOrDefault(LunaSlimesRenderStateDataKeys.SIZE, 1F);
		final Pair<Float, Float> wobble = state.frozenLib$getDataOrDefault(LunaSlimesRenderStateDataKeys.WOBBLE, LunaSlimesRenderUtil.FALLBACK_WOBBLE);
		final float wobbleXZ = wobble.getFirst();
		final float wobbleY = wobble.getSecond();
		poseStack.scale(wobbleXZ, wobbleY, wobbleXZ);
		poseStack.translate(0F, -(2.05F - (wobbleY * 2.05F)), 0F);

		final float squish = !skipSquish
			? (state.squish * (LunaSlimesVisualsAudioConfig.SQUISH_MULTIPLIER.get() * 0.1F)) / ((cubeSize) * 0.5F + 1F)
			: 0F;

		final float relativeSquish = 1F / (squish + 1F);
		operation.call(poseStack, relativeSquish * cubeSize, 1F / relativeSquish * cubeSize, relativeSquish * cubeSize);
	}
}
