package net.lunade.slime.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.slime.client.renderer.entity.layers.MagmaCubeGlowingLayer;
import net.lunade.slime.config.frozenlib.LunaSlimesVisualsAudioConfig;
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

@Environment(EnvType.CLIENT)
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
		if (!LunaSlimesVisualsAudioConfig.GLOWING_MAGMA.get() || entity.isOnFire()) return original;
		return entity.level().getBrightness(LightLayer.BLOCK, blockPos);
	}
}
