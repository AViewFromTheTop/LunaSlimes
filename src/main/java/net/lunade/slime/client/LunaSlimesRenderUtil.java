package net.lunade.slime.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.slime.config.frozenlib.LunaSlimesVisualsAudioConfig;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;
import net.minecraft.client.renderer.entity.state.SulfurCubeRenderState;

@Environment(EnvType.CLIENT)
public final class LunaSlimesRenderUtil {
	private LunaSlimesRenderUtil() {}

	public static final Pair<Float, Float> FALLBACK_WOBBLE = Pair.of(1F, 1F);

	public static void newScaling(
		PoseStack poseStack, float xScale, float yScale, float zScale, Operation<Void> operation,
		SlimeRenderState state
	) {
		if (!state.getDataOrDefault(LunaSlimesRenderStateDataKeys.IN_WORLD, false)) {
			operation.call(poseStack, xScale, yScale, zScale);
			return;
		}

		final float slimeSize = state.getDataOrDefault(LunaSlimesRenderStateDataKeys.SIZE, 1F);
		final Pair<Float, Float> wobble = state.getDataOrDefault(LunaSlimesRenderStateDataKeys.WOBBLE, LunaSlimesRenderUtil.FALLBACK_WOBBLE);
		final float wobbleXZ = wobble.getFirst();
		final float wobbleY = wobble.getSecond();
		poseStack.scale(wobbleXZ, wobbleY, wobbleXZ);
		poseStack.translate(0F, -(2.05F - (wobbleY * 2.05F)), 0F);
		final float i = (state.squish * (LunaSlimesVisualsAudioConfig.SQUISH_MULTIPLIER.get() * 0.1F)) / ((slimeSize) * 0.5F + 1F);

		final float j = 1F / (i + 1F);
		operation.call(poseStack, j * slimeSize, 1F / j * slimeSize, j * slimeSize);
	}
}
