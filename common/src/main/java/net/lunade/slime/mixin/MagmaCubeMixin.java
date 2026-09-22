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

import net.lunade.slime.config.LSVisualsAudioConfig;
import net.lunade.slime.registry.LSAttachmentTypes;
import net.minecraft.world.entity.monster.cubemob.MagmaCube;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MagmaCube.class)
public class MagmaCubeMixin {

	@Inject(method = "decreaseSquish", at = @At("HEAD"), cancellable = true)
	public void lunaSlimes$decreaseSquish(CallbackInfo info) {
		final MagmaCube magmaCube = MagmaCube.class.cast(this);
		final boolean jumpAntic = LSAttachmentTypes.JUMP_ANTIC.getAttachedOrCreate(magmaCube) && LSVisualsAudioConfig.JUMP_ANTIC.get();
		if (jumpAntic|| !LSAttachmentTypes.CAN_SQUISH.getAttachedOrCreate(magmaCube)) info.cancel();
	}
}
