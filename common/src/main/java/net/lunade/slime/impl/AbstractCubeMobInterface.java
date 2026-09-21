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

package net.lunade.slime.impl;

public interface AbstractCubeMobInterface {
	float lunaSlimes$wobbleAnimProgress(float partialTicks);
	void lunaSlimes$playWobbleAnim();
	float lunaSlimes$getSizeScale(float partialTicks);
	void lunaSlimes$cheatSize(float size);
	void lunaSlimes$setJumpAnticTicks(int jumpAnticTicks);
	float lunaSlimes$getDeathProgress(float partialTicks);
	void lunaSlimes$setInLevel(boolean inWorld);
	boolean lunaSlimes$isInLevel();
}
