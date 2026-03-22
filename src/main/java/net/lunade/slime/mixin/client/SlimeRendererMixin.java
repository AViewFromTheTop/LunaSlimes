package net.lunade.slime.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.slime.LunaSlimesUtil;
import net.lunade.slime.client.LunaSlimesRenderStateDataKeys;
import net.lunade.slime.config.frozenlib.LunaSlimesVisualsAudioConfig;
import net.lunade.slime.impl.SlimeInterface;
import net.minecraft.client.model.monster.slime.SlimeModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;
import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(SlimeRenderer.class)
public abstract class SlimeRendererMixin extends MobRenderer<Slime, SlimeRenderState, SlimeModel> {

	@Unique
	private static final Pair<Float, Float> LUNASLIMES$FALLBACK_WOBBLE = Pair.of(1F, 1F);

	public SlimeRendererMixin(EntityRendererProvider.Context context, SlimeModel model, float shadow) {
		super(context, model, shadow);
	}

	@Inject(
		method = "extractRenderState(Lnet/minecraft/world/entity/monster/Slime;Lnet/minecraft/client/renderer/entity/state/SlimeRenderState;F)V",
		at = @At("TAIL")
	)
	public void lunaSlimes$extractRenderState(Slime slime, SlimeRenderState renderState, float partialTicks, CallbackInfo info) {
		renderState.setData(LunaSlimesRenderStateDataKeys.IN_WORLD, ((SlimeInterface)slime).lunaSlimes$isInWorld());
		renderState.setData(LunaSlimesRenderStateDataKeys.WOBBLE, LunaSlimesUtil.wobbleAnim(slime, partialTicks));
		renderState.setData(LunaSlimesRenderStateDataKeys.SIZE, LunaSlimesUtil.getSlimeScale(slime, partialTicks));
	}

	@WrapOperation(
		method = "scale(Lnet/minecraft/client/renderer/entity/state/SlimeRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;)V",
		at = @At(
			value = "INVOKE",
			target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V",
			ordinal = 1
		)
	)
	public void lunaSlimes$newScaling(PoseStack poseStack, float a, float b, float c, Operation<Void> operation, SlimeRenderState renderState) {
		if (!renderState.getDataOrDefault(LunaSlimesRenderStateDataKeys.IN_WORLD, false)) {
			operation.call(poseStack, a, b, c);
			return;
		}

		final float slimeSize = renderState.getDataOrDefault(LunaSlimesRenderStateDataKeys.SIZE, 1F);
		final Pair<Float, Float> wobble = renderState.getDataOrDefault(LunaSlimesRenderStateDataKeys.WOBBLE, LUNASLIMES$FALLBACK_WOBBLE);
		final float wobbleXZ = wobble.getFirst();
		final float wobbleY = wobble.getSecond();
		poseStack.scale(wobbleXZ, wobbleY, wobbleXZ);
		poseStack.translate(0F, -(2.05F - (wobbleY * 2.05F)), 0F);
		final float i = (renderState.squish * (LunaSlimesVisualsAudioConfig.SQUISH_MULTIPLIER.get() * 0.1F)) / ((slimeSize) * 0.5F + 1F);

		final float j = 1F / (i + 1F);
		operation.call(poseStack, j * slimeSize, 1F / j * slimeSize, j * slimeSize);
	}

	@ModifyReturnValue(
		method = "getShadowRadius(Lnet/minecraft/client/renderer/entity/state/SlimeRenderState;)F",
		at = @At("RETURN")
	)
	public float lunaSlimes$newShadows(float original, SlimeRenderState renderState) {
		if (!LunaSlimesVisualsAudioConfig.NEW_SHADOWS.get()) return original;
		final float slimeSize = renderState.getDataOrDefault(LunaSlimesRenderStateDataKeys.SIZE, 1F);
		final Pair<Float, Float> wobble = renderState.getDataOrDefault(LunaSlimesRenderStateDataKeys.WOBBLE, LUNASLIMES$FALLBACK_WOBBLE);
		final float wobbleXZ = wobble.getFirst() * 2F;
		final float size = ((slimeSize * 0.999F) * 0.75F) * wobbleXZ;
		final float squish = (renderState.squish * (LunaSlimesVisualsAudioConfig.SQUISH_MULTIPLIER.get() * 0.1F)) / (size * 0.5F + 1F);
		final float j = (1F / (squish + 1F));
		return 0.25F * (j * size);
	}

}
