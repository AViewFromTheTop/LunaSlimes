package net.lunade.slime.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.slime.LunaSlimesUtil;
import net.lunade.slime.config.getter.LunaSlimesConfigValueGetter;
import net.lunade.slime.impl.SlimeInterface;
import net.lunade.slime.impl.client.SlimeRenderStateInterface;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;
import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(SlimeRenderer.class)
public abstract class SlimeRendererMixin extends MobRenderer<Slime, SlimeRenderState, SlimeModel> {

	public SlimeRendererMixin(EntityRendererProvider.Context context, SlimeModel entityModel, float f) {
		super(context, entityModel, f);
	}

	@Inject(
		method = "extractRenderState(Lnet/minecraft/world/entity/monster/Slime;Lnet/minecraft/client/renderer/entity/state/SlimeRenderState;F)V",
		at = @At("TAIL")
	)
	public void lunaSlimes$extractRenderState(Slime slime, SlimeRenderState slimeRenderState, float f, CallbackInfo info) {
		if (!(slimeRenderState instanceof SlimeRenderStateInterface renderStateInterface)) return;
		renderStateInterface.lunaSlimes$setInWorld(((SlimeInterface) slime).lunaSlimes$isInWorld());
		renderStateInterface.lunaSlimes$setWobble(LunaSlimesUtil.wobbleAnim(slime, f));
		renderStateInterface.lunaSlimes$setSize(LunaSlimesUtil.getSlimeScale(slime, f));
	}

	@WrapOperation(
		method = "scale(Lnet/minecraft/client/renderer/entity/state/SlimeRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;)V",
		at = @At(
			value = "INVOKE",
			target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V",
			ordinal = 1
		)
	)
	public void lunaSlimes$newScaling(PoseStack poseStack, float a, float b, float c, Operation<Void> operation, SlimeRenderState slimeRenderState, PoseStack poseStack2) {
		if (!(slimeRenderState instanceof SlimeRenderStateInterface renderStateInterface && renderStateInterface.lunaSlimes$isInWorld())) {
			operation.call(poseStack, a, b, c);
			return;
		}

		final float slimeSize = renderStateInterface.lunaSlimes$getSize();
		final Pair<Float, Float> wobble = renderStateInterface.lunaSlimes$getWobble();
		final float wobbleXZ = wobble.getFirst();
		final float wobbleY = wobble.getSecond();
		poseStack.scale(wobbleXZ, wobbleY, wobbleXZ);
		poseStack.translate(0F, -(2.05F - (wobbleY * 2.05F)), 0F);
		final float i = (slimeRenderState.squish * LunaSlimesConfigValueGetter.squishMultiplier()) / ((slimeSize) * 0.5F + 1F);

		final float j = 1F / (i + 1F);
		operation.call(poseStack, j * slimeSize, 1F / j * slimeSize, j * slimeSize);
	}

	@ModifyReturnValue(
		method = "getShadowRadius(Lnet/minecraft/client/renderer/entity/state/SlimeRenderState;)F",
		at = @At("RETURN")
	)
	public float lunaSlimes$newShadows(float original, SlimeRenderState slimeRenderState) {
		if (!(LunaSlimesConfigValueGetter.newShadows() && slimeRenderState instanceof SlimeRenderStateInterface renderStateInterface)) return original;
		final float slimeSize = renderStateInterface.lunaSlimes$getSize();
		final Pair<Float, Float> wobble = renderStateInterface.lunaSlimes$getWobble();
		final float wobbleXZ = wobble.getFirst() * 2F;
		final float size = ((slimeSize * 0.999F) * 0.75F) * wobbleXZ;
		final float squish = (slimeRenderState.squish * LunaSlimesConfigValueGetter.squishMultiplier()) / (size * 0.5F + 1F);
		final float j = (1F / (squish + 1F));
		return 0.25F * (j * size);
	}

}
