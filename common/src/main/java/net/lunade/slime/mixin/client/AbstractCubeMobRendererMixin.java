/*
 * Copyright 2026 Lunade Music/AViewFromTheTop
 * This file is part of Luna Slimes.
 *
 * This program is free software; you can modify it under
 * the terms of version 1 of the FrozenBlock Modding Oasis License
 * as published by FrozenBlock Modding Oasis.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * FrozenBlock Modding Oasis License for more details.
 *
 * You should have received a copy of the FrozenBlock Modding Oasis License
 * along with this program; if not, see <https://github.com/FrozenBlock/Licenses>.
 */

package net.lunade.slime.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.lunade.slime.LunaSlimesUtil;
import net.lunade.slime.client.LSRenderStateDataKeys;
import net.lunade.slime.client.LSRenderUtils;
import net.lunade.slime.config.frozenlib.LSVisualsAudioConfig;
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
		state.frozenLib$setData(LSRenderStateDataKeys.IN_WORLD, ((AbstractCubeMobInterface)entity).lunaSlimes$isInLevel());
		state.frozenLib$setData(LSRenderStateDataKeys.WOBBLE, LunaSlimesUtil.wobbleAnim(entity, partialTicks));
		state.frozenLib$setData(LSRenderStateDataKeys.SIZE, LunaSlimesUtil.getCubeScale(entity, partialTicks));
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
		LSRenderUtils.applyWobbleAndSquish(poseStack, xScale, yScale, zScale, operation, state, false);
	}

	@ModifyReturnValue(
		method = "getShadowRadius(Lnet/minecraft/client/renderer/entity/state/SlimeRenderState;)F",
		at = @At("RETURN")
	)
	public float lunaSlimes$newShadows(float original, SlimeRenderState state) {
		if (!LSVisualsAudioConfig.NEW_SHADOWS.get()) return original;

		final boolean skipSquish = state instanceof SulfurCubeRenderState sulfurCubeRenderState && !sulfurCubeRenderState.containedBlock.isEmpty();
		final float slimeSize = state.frozenLib$getDataOrDefault(LSRenderStateDataKeys.SIZE, 1F);
		final Pair<Float, Float> wobble = state.frozenLib$getDataOrDefault(LSRenderStateDataKeys.WOBBLE, LSRenderUtils.FALLBACK_WOBBLE);

		final float wobbleXZ = wobble.getFirst() * 2F;
		final float cubeSize = ((slimeSize * 0.999F) * 0.75F) * wobbleXZ;
		final float squish = !skipSquish
			? (state.squish * (LSVisualsAudioConfig.SQUISH_MULTIPLIER.get() * 0.1F)) / (cubeSize * 0.5F + 1F)
			: 0F;
		final float relativeSquish = (1F / (squish + 1F));

		return 0.25F * (relativeSquish * cubeSize);
	}
}
