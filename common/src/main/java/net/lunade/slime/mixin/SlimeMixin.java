package net.lunade.slime.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lunade.slime.config.frozenlib.LunaSlimesVisualsAudioConfig;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.monster.cubemob.Slime;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Slime.class)
public class SlimeMixin {
	@Unique
	private static final BlockParticleOption LUNASLIMES$NEW_SLIME_PARTICLES = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SLIME_BLOCK.defaultBlockState());

	@ModifyReturnValue(method = "getParticleType", at = @At("RETURN"))
	public ParticleOptions lunaSlimes$getParticleType(ParticleOptions original) {
		return LunaSlimesVisualsAudioConfig.SLIME_BLOCK_PARTICLES.get() ? LUNASLIMES$NEW_SLIME_PARTICLES : original;
	}
}
