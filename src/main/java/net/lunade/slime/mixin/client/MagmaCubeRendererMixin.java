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
import net.minecraft.client.model.LavaSlimeModel;
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
public abstract class MagmaCubeRendererMixin extends MobRenderer<MagmaCube, SlimeRenderState, LavaSlimeModel> {

	public MagmaCubeRendererMixin(EntityRendererProvider.Context context, LavaSlimeModel entityModel, float f) {
		super(context, entityModel, f);
	}

	@Inject(at = @At("TAIL"), method = "<init>")
	public void lunaSlimes$init(EntityRendererProvider.Context context, CallbackInfo info) {
		MagmaCubeRenderer renderer = MagmaCubeRenderer.class.cast(this);
		renderer.addLayer(new MagmaCubeLayer(renderer));
	}

	@Inject(
		method = "extractRenderState(Lnet/minecraft/world/entity/monster/MagmaCube;Lnet/minecraft/client/renderer/entity/state/SlimeRenderState;F)V",
		at = @At("TAIL")
	)
	public void lunaSlimes$extractRenderState(MagmaCube magmaCube, SlimeRenderState slimeRenderState, float f, CallbackInfo info) {
		if (slimeRenderState instanceof SlimeRenderStateInterface renderStateInterface) {
			renderStateInterface.lunaSlimes$setInWorld(((SlimeInterface) magmaCube).lunaSlimes$isInWorld());
			renderStateInterface.lunaSlimes$setWobble(LunaSlimesUtil.wobbleAnim(magmaCube, f));
			renderStateInterface.lunaSlimes$setSize(LunaSlimesUtil.getSlimeScale(magmaCube, f));
		}
	}

	@Inject(at = @At("HEAD"), method = "scale(Lnet/minecraft/client/renderer/entity/state/SlimeRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;)V")
	public void lunaSlimes$anims(
		SlimeRenderState slimeRenderState, PoseStack poseStack, CallbackInfo info,
		@Share("lunaSlimes$squash") LocalFloatRef squash,
		@Share("lunaSlimes$stretch") LocalFloatRef stretch
	) {
		if (slimeRenderState instanceof SlimeRenderStateInterface renderStateInterface) {
			Pair<Float, Float> wobble = renderStateInterface.lunaSlimes$getWobble();
			float wobbleXZ = wobble.getFirst();
			float wobbleY = wobble.getSecond();
			poseStack.scale(wobbleXZ, wobbleY, wobbleXZ);
			poseStack.translate(0.0F, -(2.05F - (wobbleY * 2.05F)), 0F);
			float size = renderStateInterface.lunaSlimes$getSize();
			float squishValue = slimeRenderState.squish * LunaSlimesConfigValueGetter.squishMultiplier();

			float g = squishValue / ((size) * 0.5F + 1F);
			float sq = (1F / (g + 1F));

			squash.set(sq);
			stretch.set(1F / sq * size);
		}
	}

	@WrapOperation(
		method = "scale(Lnet/minecraft/client/renderer/entity/state/SlimeRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;)V",
		at = @At(
			value = "INVOKE",
			target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V"
		)
	)
	public void lunaSlimes$setScaleArgs(
		PoseStack poseStack, float a, float b, float c, Operation<Void> operation, SlimeRenderState slimeRenderState,
		@Share("lunaSlimes$squash") LocalFloatRef squash,
		@Share("lunaSlimes$stretch") LocalFloatRef stretch
	) {
		if (slimeRenderState instanceof SlimeRenderStateInterface renderStateInterface && renderStateInterface.lunaSlimes$isInWorld()) {
			float x = squash.get() * renderStateInterface.lunaSlimes$getSize();
			operation.call(poseStack, x, stretch.get(), x);
		} else {
			operation.call(poseStack, a, b, c);
		}
	}

	@ModifyReturnValue(
		method = "getShadowRadius(Lnet/minecraft/client/renderer/entity/state/SlimeRenderState;)F",
		at = @At("RETURN")
	)
	public float lunaSlimes$newShadows(float original, SlimeRenderState slimeRenderState) {
		if (LunaSlimesConfigValueGetter.newShadows() && slimeRenderState instanceof SlimeRenderStateInterface renderStateInterface) {
			float slimeSize = renderStateInterface.lunaSlimes$getSize();
			Pair<Float, Float> wobble = renderStateInterface.lunaSlimes$getWobble();
			float wobbleXZ = wobble.getFirst() * 2F;
			float size = ((slimeSize * 0.999F) * 0.75F) * wobbleXZ;
			float squish = (slimeRenderState.squish * LunaSlimesConfigValueGetter.squishMultiplier()) / (size * 0.5F + 1F);
			float j = (1F / (squish + 1F));
			return 0.25F * (j * size);
		}
		return original;
	}

	@ModifyReturnValue(
		at = @At("RETURN"),
		method = "getBlockLightLevel(Lnet/minecraft/world/entity/monster/MagmaCube;Lnet/minecraft/core/BlockPos;)I"
	)
	public int lunaSlimes$getBlockLightLevel(int original, MagmaCube magmaCube, BlockPos pos) {
		if (LunaSlimesConfigValueGetter.glowingMagma()) {
			return magmaCube.isOnFire() ? original : magmaCube.level().getBrightness(LightLayer.BLOCK, pos);
		}
		return original;
	}

}
