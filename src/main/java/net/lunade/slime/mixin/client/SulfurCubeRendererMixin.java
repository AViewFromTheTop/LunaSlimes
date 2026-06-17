package net.lunade.slime.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.lunade.slime.client.LunaSlimesRenderStateDataKeys;
import net.lunade.slime.client.LunaSlimesRenderUtil;
import net.lunade.slime.config.frozenlib.LunaSlimesVisualsAudioConfig;
import net.minecraft.client.renderer.entity.SulfurCubeRenderer;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;
import net.minecraft.client.renderer.entity.state.SulfurCubeRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SulfurCubeRenderer.class)
public class SulfurCubeRendererMixin {
	@WrapOperation(
		method = "applySizeAndSquish(Lnet/minecraft/client/renderer/entity/state/SulfurCubeRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;)V",
		at = @At(
			value = "INVOKE",
			target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V",
			ordinal = 0
		)
	)
	public void lunaSlimes$newScaling(
		PoseStack poseStack, float xScale, float yScale, float zScale, Operation<Void> operation,
		@Local(argsOnly = true) SulfurCubeRenderState state
	) {
		LunaSlimesRenderUtil.newScaling(poseStack, xScale, yScale, zScale, operation, state);
	}
}
