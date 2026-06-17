package net.lunade.slime.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.slime.LunaSlimesUtil;
import net.lunade.slime.client.LunaSlimesRenderStateDataKeys;
import net.lunade.slime.config.frozenlib.LunaSlimesVisualsAudioConfig;
import net.lunade.slime.impl.SlimeInterface;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.AbstractCubeMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;
import net.minecraft.world.entity.monster.cubemob.AbstractCubeMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(AbstractCubeMobRenderer.class)
public abstract class AbstractCubeMobRendererMixin<T extends AbstractCubeMob, S extends SlimeRenderState, M extends EntityModel<? super S>> extends MobRenderer<T, S, M> {

	@Unique
	private static final Pair<Float, Float> LUNASLIMES$FALLBACK_WOBBLE = Pair.of(1F, 1F);

	public AbstractCubeMobRendererMixin(EntityRendererProvider.Context context, M model, float shadow) {
		super(context, model, shadow);
	}

	@Inject(
		method = "extractRenderState(Lnet/minecraft/world/entity/monster/cubemob/AbstractCubeMob;Lnet/minecraft/client/renderer/entity/state/SlimeRenderState;F)V",
		at = @At("TAIL")
	)
	public void lunaSlimes$extractRenderState(T entity, S state, float partialTicks, CallbackInfo info) {
		state.setData(LunaSlimesRenderStateDataKeys.IN_WORLD, ((SlimeInterface)entity).lunaSlimes$isInWorld());
		state.setData(LunaSlimesRenderStateDataKeys.WOBBLE, LunaSlimesUtil.wobbleAnim(entity, partialTicks));
		state.setData(LunaSlimesRenderStateDataKeys.SIZE, LunaSlimesUtil.getCubeScale(entity, partialTicks));
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
		if (!state.getDataOrDefault(LunaSlimesRenderStateDataKeys.IN_WORLD, false)) {
			operation.call(poseStack, xScale, yScale, zScale);
			return;
		}

		final float slimeSize = state.getDataOrDefault(LunaSlimesRenderStateDataKeys.SIZE, 1F);
		final Pair<Float, Float> wobble = state.getDataOrDefault(LunaSlimesRenderStateDataKeys.WOBBLE, LUNASLIMES$FALLBACK_WOBBLE);
		final float wobbleXZ = wobble.getFirst();
		final float wobbleY = wobble.getSecond();
		poseStack.scale(wobbleXZ, wobbleY, wobbleXZ);
		poseStack.translate(0F, -(2.05F - (wobbleY * 2.05F)), 0F);
		final float i = (state.squish * (LunaSlimesVisualsAudioConfig.SQUISH_MULTIPLIER.get() * 0.1F)) / ((slimeSize) * 0.5F + 1F);

		final float j = 1F / (i + 1F);
		operation.call(poseStack, j * slimeSize, 1F / j * slimeSize, j * slimeSize);
	}

	@ModifyReturnValue(
		method = "getShadowRadius(Lnet/minecraft/client/renderer/entity/state/SlimeRenderState;)F",
		at = @At("RETURN")
	)
	public float lunaSlimes$newShadows(float original, SlimeRenderState state) {
		if (!LunaSlimesVisualsAudioConfig.NEW_SHADOWS.get()) return original;
		final float slimeSize = state.getDataOrDefault(LunaSlimesRenderStateDataKeys.SIZE, 1F);
		final Pair<Float, Float> wobble = state.getDataOrDefault(LunaSlimesRenderStateDataKeys.WOBBLE, LUNASLIMES$FALLBACK_WOBBLE);
		final float wobbleXZ = wobble.getFirst() * 2F;
		final float size = ((slimeSize * 0.999F) * 0.75F) * wobbleXZ;
		final float squish = (state.squish * (LunaSlimesVisualsAudioConfig.SQUISH_MULTIPLIER.get() * 0.1F)) / (size * 0.5F + 1F);
		final float j = (1F / (squish + 1F));
		return 0.25F * (j * size);
	}

}
