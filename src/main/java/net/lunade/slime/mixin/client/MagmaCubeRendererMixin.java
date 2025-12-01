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
import net.lunade.slime.config.getter.LunaSlimesConfigValueGetter;
import net.lunade.slime.impl.SlimeInterface;
import net.lunade.slime.impl.client.SlimeRenderStateInterface;
import net.lunade.slime.render.MagmaCubeLayer;
import net.minecraft.client.model.monster.slime.MagmaCubeModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MagmaCubeRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.level.LightLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(MagmaCubeRenderer.class)
public abstract class MagmaCubeRendererMixin extends MobRenderer<MagmaCube, SlimeRenderState, MagmaCubeModel> {

	public MagmaCubeRendererMixin(EntityRendererProvider.Context context, MagmaCubeModel entityModel, float f) {
		super(context, entityModel, f);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	public void lunaSlimes$init(EntityRendererProvider.Context context, CallbackInfo info) {
		final MagmaCubeRenderer renderer = MagmaCubeRenderer.class.cast(this);
		renderer.addLayer(new MagmaCubeLayer(renderer));
	}

	@Inject(
		method = "extractRenderState(Lnet/minecraft/world/entity/monster/MagmaCube;Lnet/minecraft/client/renderer/entity/state/SlimeRenderState;F)V",
		at = @At("TAIL")
	)
	public void lunaSlimes$extractRenderState(MagmaCube magmaCube, SlimeRenderState renderState, float f, CallbackInfo info) {
		if (!(renderState instanceof SlimeRenderStateInterface renderStateInterface)) return;
		renderStateInterface.lunaSlimes$setInWorld(((SlimeInterface) magmaCube).lunaSlimes$isInWorld());
		renderStateInterface.lunaSlimes$setWobble(LunaSlimesUtil.wobbleAnim(magmaCube, f));
		renderStateInterface.lunaSlimes$setSize(LunaSlimesUtil.getSlimeScale(magmaCube, f));
	}

	@Inject(
		method = "scale(Lnet/minecraft/client/renderer/entity/state/SlimeRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;)V",
		at = @At("HEAD")
	)
	public void lunaSlimes$anims(
		SlimeRenderState renderState, PoseStack poseStack, CallbackInfo info,
		@Share("lunaSlimes$squash") LocalFloatRef squash,
		@Share("lunaSlimes$stretch") LocalFloatRef stretch
	) {
		if (!(renderState instanceof SlimeRenderStateInterface renderStateInterface)) return;
		final Pair<Float, Float> wobble = renderStateInterface.lunaSlimes$getWobble();
		final float wobbleXZ = wobble.getFirst();
		final float wobbleY = wobble.getSecond();
		poseStack.scale(wobbleXZ, wobbleY, wobbleXZ);
		poseStack.translate(0.0F, -(2.05F - (wobbleY * 2.05F)), 0F);
		final float size = renderStateInterface.lunaSlimes$getSize();
		final float squishValue = renderState.squish * LunaSlimesConfigValueGetter.squishMultiplier();

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
		if (renderState instanceof SlimeRenderStateInterface renderStateInterface && renderStateInterface.lunaSlimes$isInWorld()) {
			final float x = squash.get() * renderStateInterface.lunaSlimes$getSize();
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
		if (!(LunaSlimesConfigValueGetter.newShadows() && renderState instanceof SlimeRenderStateInterface renderStateInterface)) return original;
		final float slimeSize = renderStateInterface.lunaSlimes$getSize();
		final Pair<Float, Float> wobble = renderStateInterface.lunaSlimes$getWobble();
		final float wobbleXZ = wobble.getFirst() * 2F;
		final float size = ((slimeSize * 0.999F) * 0.75F) * wobbleXZ;
		final float squish = (renderState.squish * LunaSlimesConfigValueGetter.squishMultiplier()) / (size * 0.5F + 1F);
		final float j = (1F / (squish + 1F));
		return 0.25F * (j * size);
	}

	@ModifyReturnValue(
		method = "getBlockLightLevel(Lnet/minecraft/world/entity/monster/MagmaCube;Lnet/minecraft/core/BlockPos;)I",
		at = @At("RETURN")
	)
	public int lunaSlimes$getBlockLightLevel(int original, MagmaCube magmaCube, BlockPos pos) {
		if (!LunaSlimesConfigValueGetter.glowingMagma()) return original;
		return magmaCube.isOnFire() ? original : magmaCube.level().getBrightness(LightLayer.BLOCK, pos);
	}

}
