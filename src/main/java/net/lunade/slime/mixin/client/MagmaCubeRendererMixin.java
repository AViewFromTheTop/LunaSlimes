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
import net.lunade.slime.render.MagmaCubeLayer;
import net.lunade.slime.render.SlimeTextures;
import net.minecraft.client.model.LavaSlimeModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MagmaCubeRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.level.LightLayer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(MagmaCubeRenderer.class)
public abstract class MagmaCubeRendererMixin extends MobRenderer<MagmaCube, SlimeRenderState, LavaSlimeModel> {

    @Unique
    private float lunaSlimes$h;
    @Unique
    private float lunaSlimes$i;
    @Unique
    private float lunaSlimes$yStretch;

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
    public void lunaSlimes$anims(SlimeRenderState slimeRenderState, PoseStack poseStack, CallbackInfo info) {
        if (slimeRenderState instanceof SlimeRenderStateInterface renderStateInterface) {
            Pair<Float, Float> wobble = renderStateInterface.lunaSlimes$getWobble();
            float wobbleXZ = wobble.getFirst();
            float wobbleY = wobble.getSecond();
            poseStack.scale(wobbleXZ, wobbleY, wobbleXZ);
            poseStack.translate(0.0F, -(2.05F - (wobbleY * 2.05F)), 0F);
            float size = renderStateInterface.lunaSlimes$getSize();
            float squishValue = slimeRenderState.squish * LunaSlimesConfigValueGetter.squishMultiplier();

            float g = squishValue / ((size) * 0.5F + 1F);
            this.lunaSlimes$h = (1F / (g + 1F));
            this.lunaSlimes$i = size;
            this.lunaSlimes$yStretch = 1F / this.lunaSlimes$h * size;
        }
    }

    @WrapOperation(
            method = "scale(Lnet/minecraft/client/renderer/entity/state/SlimeRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V"
            )
    )
    public void lunaSlimes$setScaleArgs(PoseStack poseStack, float a, float b, float c, Operation<Void> operation, SlimeRenderState slimeRenderState) {
        if (slimeRenderState instanceof SlimeRenderStateInterface renderStateInterface && renderStateInterface.lunaSlimes$isInWorld()) {
            float x = this.lunaSlimes$h * this.lunaSlimes$i;
            operation.call(poseStack, x, this.lunaSlimes$yStretch, x);
        } else {
            operation.call(poseStack, a, b, c);
        }
    }

    @Inject(
		method = "render(Lnet/minecraft/client/renderer/entity/state/SlimeRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/renderer/entity/MagmaCubeRenderer;shadowRadius:F",
			opcode = Opcodes.PUTFIELD,
			shift = At.Shift.AFTER
		)
	)
    public void lunaSlimes$newShadows(SlimeRenderState slimeRenderState, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo info) {
        if (LunaSlimesConfigValueGetter.newShadows() && slimeRenderState instanceof SlimeRenderStateInterface renderStateInterface) {
            float slimeSize = renderStateInterface.lunaSlimes$getSize();
            Pair<Float, Float> wobble = renderStateInterface.lunaSlimes$getWobble();
            float wobbleXZ = wobble.getFirst() * 2F;
            float size = ((slimeSize * 0.999F) * 0.75F) * wobbleXZ;
            float squish = (slimeRenderState.squish * LunaSlimesConfigValueGetter.squishMultiplier()) / (size * 0.5F + 1F);
            float j = (1.0F / (squish + 1F));
            this.shadowRadius = 0.25F * (j * size);
        }
    }

    @ModifyReturnValue(
            at = @At("RETURN"),
            method = "getTextureLocation(Lnet/minecraft/client/renderer/entity/state/SlimeRenderState;)Lnet/minecraft/resources/ResourceLocation;"
    )
    public ResourceLocation lunaSlimes$getTextureLocation(ResourceLocation original, SlimeRenderState slimeRenderState) {
        return SlimeTextures.getMamaCubeTexture(slimeRenderState.size, original);
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
