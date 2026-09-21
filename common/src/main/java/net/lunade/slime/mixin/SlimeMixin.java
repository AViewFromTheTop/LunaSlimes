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

package net.lunade.slime.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lunade.slime.config.frozenlib.LSVisualsAudioConfig;
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
		return LSVisualsAudioConfig.SLIME_BLOCK_PARTICLES.get() ? LUNASLIMES$NEW_SLIME_PARTICLES : original;
	}
}
