package net.lunade.slime.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.lunade.slime.LunaSlimesUtil;
import net.lunade.slime.client.LunaSlimesRenderStateDataKeys;
import net.lunade.slime.client.LunaSlimesRenderUtil;
import net.lunade.slime.config.frozenlib.LunaSlimesVisualsAudioConfig;
import net.lunade.slime.impl.AbstractCubeMobInterface;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.AbstractCubeMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;
import net.minecraft.client.renderer.entity.state.SulfurCubeRenderState;
import net.minecraft.world.entity.monster.cubemob.AbstractCubeMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@ClientOnly
@Mixin(AbstractCubeMobRenderer.class)
public abstract class AbstractCubeMobRendererMixin<T extends AbstractCubeMob, S extends SlimeRenderState, M extends EntityModel<? super S>> extends MobRenderer<T, S, M> {

	public AbstractCubeMobRendererMixin(EntityRendererProvider.Context context, M model, float shadow) {
		super(context, model, shadow);
	}

	@Inject(
		method = "extractRenderState(Lnet/minecraft/world/entity/monster/cubemob/AbstractCubeMob;Lnet/minecraft/client/renderer/entity/state/SlimeRenderState;F)V",
		at = @At("TAIL")
	)
	public void lunaSlimes$extractRenderState(T entity, S state, float partialTicks, CallbackInfo info) {
		state.frozenLib$setData(LunaSlimesRenderStateDataKeys.IN_WORLD, ((AbstractCubeMobInterface)entity).lunaSlimes$isInWorld());
		state.frozenLib$setData(LunaSlimesRenderStateDataKeys.WOBBLE, LunaSlimesUtil.wobbleAnim(entity, partialTicks));
		state.frozenLib$setData(LunaSlimesRenderStateDataKeys.SIZE, LunaSlimesUtil.getCubeScale(entity, partialTicks));
	}

	@WrapOperation(
		method = "applySizeAndSquish(Lnet/minecraft/client/renderer/entity/state/SlimeRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;)V",
		at = @At(
			value = "INVOKE",
			target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V",
			ordinal = 0
		)
	)
	public void lunaSlimes$newScaling(
		PoseStack poseStack, float xScale, float yScale, float zScale, Operation<Void> operation,
		@Local(argsOnly = true) SlimeRenderState state
	) {
		LunaSlimesRenderUtil.applyWobbleAndSquish(poseStack, xScale, yScale, zScale, operation, state, false);
	}

	@ModifyReturnValue(
		method = "getShadowRadius(Lnet/minecraft/client/renderer/entity/state/SlimeRenderState;)F",
		at = @At("RETURN")
	)
	public float lunaSlimes$newShadows(float original, SlimeRenderState state) {
		if (!LunaSlimesVisualsAudioConfig.NEW_SHADOWS.get()) return original;
		final boolean skipSquish = state instanceof SulfurCubeRenderState sulfurCubeRenderState && !sulfurCubeRenderState.containedBlock.isEmpty();
		final float slimeSize = state.frozenLib$getDataOrDefault(LunaSlimesRenderStateDataKeys.SIZE, 1F);
		final Pair<Float, Float> wobble = state.frozenLib$getDataOrDefault(LunaSlimesRenderStateDataKeys.WOBBLE, LunaSlimesRenderUtil.FALLBACK_WOBBLE);
		final float wobbleXZ = wobble.getFirst() * 2F;
		final float cubeSize = ((slimeSize * 0.999F) * 0.75F) * wobbleXZ;
		final float squish = !skipSquish
			? (state.squish * (LunaSlimesVisualsAudioConfig.SQUISH_MULTIPLIER.get() * 0.1F)) / (cubeSize * 0.5F + 1F)
			: 0F;
		final float relativeSquish = (1F / (squish + 1F));
		return 0.25F * (relativeSquish * cubeSize);
	}
}
