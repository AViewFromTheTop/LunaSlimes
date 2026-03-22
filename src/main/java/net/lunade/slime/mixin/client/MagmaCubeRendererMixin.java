package net.lunade.slime.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.slime.LunaSlimesUtil;
import net.lunade.slime.client.LunaSlimesRenderStateDataKeys;
import net.lunade.slime.client.renderer.entity.layers.MagmaCubeGlowingLayer;
import net.lunade.slime.config.frozenlib.LunaSlimesVisualsAudioConfig;
import net.lunade.slime.impl.SlimeInterface;
import net.minecraft.client.model.monster.slime.MagmaCubeModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MagmaCubeRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.level.LightLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(MagmaCubeRenderer.class)
public abstract class MagmaCubeRendererMixin extends MobRenderer<MagmaCube, SlimeRenderState, MagmaCubeModel> {

	@Unique
	private static final Pair<Float, Float> LUNASLIMES$FALLBACK_WOBBLE = Pair.of(1F, 1F);

	public MagmaCubeRendererMixin(EntityRendererProvider.Context context, MagmaCubeModel model, float shadow) {
		super(context, model, shadow);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	public void lunaSlimes$init(EntityRendererProvider.Context context, CallbackInfo info) {
		final MagmaCubeRenderer renderer = MagmaCubeRenderer.class.cast(this);
		renderer.addLayer(new MagmaCubeGlowingLayer(renderer));
	}

	@Inject(
		method = "extractRenderState(Lnet/minecraft/world/entity/monster/MagmaCube;Lnet/minecraft/client/renderer/entity/state/SlimeRenderState;F)V",
		at = @At("TAIL")
	)
	public void lunaSlimes$extractRenderState(MagmaCube magmaCube, SlimeRenderState renderState, float partialTicks, CallbackInfo info) {
		renderState.setData(LunaSlimesRenderStateDataKeys.IN_WORLD, ((SlimeInterface)magmaCube).lunaSlimes$isInWorld());
		renderState.setData(LunaSlimesRenderStateDataKeys.WOBBLE, LunaSlimesUtil.wobbleAnim(magmaCube, partialTicks));
		renderState.setData(LunaSlimesRenderStateDataKeys.SIZE, LunaSlimesUtil.getSlimeScale(magmaCube, partialTicks));
	}

	@Inject(
		method = "scale(Lnet/minecraft/client/renderer/entity/state/SlimeRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;)V",
		at = @At("HEAD")
	)
	public void lunaSlimes$scale(
		SlimeRenderState renderState, PoseStack poseStack, CallbackInfo info,
		@Share("lunaSlimes$squash") LocalFloatRef squash,
		@Share("lunaSlimes$stretch") LocalFloatRef stretch
	) {
		final Pair<Float, Float> wobble = renderState.getDataOrDefault(LunaSlimesRenderStateDataKeys.WOBBLE, LUNASLIMES$FALLBACK_WOBBLE);
		final float wobbleXZ = wobble.getFirst();
		final float wobbleY = wobble.getSecond();
		poseStack.scale(wobbleXZ, wobbleY, wobbleXZ);
		poseStack.translate(0.0F, -(2.05F - (wobbleY * 2.05F)), 0F);
		final float size = renderState.getDataOrDefault(LunaSlimesRenderStateDataKeys.SIZE, 0F);
		final float squishValue = renderState.squish * (LunaSlimesVisualsAudioConfig.SQUISH_MULTIPLIER.get() * 0.1F);

		final float g = squishValue / ((size) * 0.5F + 1F);
		final float sq = (1F / (g + 1F));

		squash.set(sq);
		stretch.set(1F / sq * size);
	}

	@WrapOperation(
		method = "scale(Lnet/minecraft/client/renderer/entity/state/SlimeRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;)V",
		at = @At(
			value = "INVOKE",
			target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V"
		)
	)
	public void lunaSlimes$setScaleArgs(
		PoseStack poseStack, float a, float b, float c, Operation<Void> operation, SlimeRenderState renderState,
		@Share("lunaSlimes$squash") LocalFloatRef squash,
		@Share("lunaSlimes$stretch") LocalFloatRef stretch
	) {
		if (renderState.getDataOrDefault(LunaSlimesRenderStateDataKeys.IN_WORLD, false)) {
			final float x = squash.get() * renderState.getDataOrDefault(LunaSlimesRenderStateDataKeys.SIZE, 1F);
			operation.call(poseStack, x, stretch.get(), x);
		} else {
			operation.call(poseStack, a, b, c);
		}
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

	@ModifyReturnValue(
		method = "getBlockLightLevel(Lnet/minecraft/world/entity/monster/MagmaCube;Lnet/minecraft/core/BlockPos;)I",
		at = @At("RETURN")
	)
	public int lunaSlimes$getBlockLightLevel(int original, MagmaCube magmaCube, BlockPos pos) {
		if (!LunaSlimesVisualsAudioConfig.GLOWING_MAGMA.get() || magmaCube.isOnFire()) return original;
		return magmaCube.level().getBrightness(LightLayer.BLOCK, pos);
	}

}
