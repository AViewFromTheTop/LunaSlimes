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
import net.lunade.slime.client.renderer.entity.layers.MagmaCubeGlowingLayer;
import net.lunade.slime.config.frozenlib.LSVisualsAudioConfig;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.model.monster.slime.MagmaCubeModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MagmaCubeRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.monster.cubemob.MagmaCube;
import net.minecraft.world.level.LightLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@ClientOnly
@Mixin(MagmaCubeRenderer.class)
public abstract class MagmaCubeRendererMixin extends MobRenderer<MagmaCube, SlimeRenderState, MagmaCubeModel> {

	public MagmaCubeRendererMixin(EntityRendererProvider.Context context, MagmaCubeModel model, float shadow) {
		super(context, model, shadow);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	public void lunaSlimes$init(EntityRendererProvider.Context context, CallbackInfo info) {
		final MagmaCubeRenderer renderer = MagmaCubeRenderer.class.cast(this);
		renderer.addLayer(new MagmaCubeGlowingLayer(renderer));
	}

	@ModifyReturnValue(
		method = "getBlockLightLevel(Lnet/minecraft/world/entity/monster/cubemob/MagmaCube;Lnet/minecraft/core/BlockPos;)I",
		at = @At("RETURN")
	)
	public int lunaSlimes$getBlockLightLevel(int original, MagmaCube entity, BlockPos blockPos) {
		if (!LSVisualsAudioConfig.GLOWING_MAGMA.get() || entity.isOnFire()) return original;
		return entity.level().getBrightness(LightLayer.BLOCK, blockPos);
	}
}
