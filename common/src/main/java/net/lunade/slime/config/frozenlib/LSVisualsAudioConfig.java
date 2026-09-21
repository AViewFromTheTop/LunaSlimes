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

package net.lunade.slime.config.frozenlib;

import blue.endless.jankson.JsonElement;
import net.frozenblock.lib.config.v2.config.ConfigData;
import net.frozenblock.lib.config.v2.config.ConfigSettings;
import net.frozenblock.lib.config.v2.entry.ConfigEntry;
import net.frozenblock.lib.config.v2.entry.EntryType;
import net.frozenblock.lib.config.v2.registry.ID;
import net.lunade.slime.LSConstants;

public final class LSVisualsAudioConfig {
	public static final ConfigData<JsonElement> CONFIG = ConfigData.createAndRegister(
		ID.of(LSConstants.id("visuals_audio")),
		ConfigSettings.JSON
	);

	public static final ConfigEntry<Boolean> GROW_ANIM = CONFIG.unsyncableEntry("growAnim", EntryType.BOOL, true);
	public static final ConfigEntry<Boolean> WOBBLE_ANIM = CONFIG.unsyncableEntry("wobbleAnim", EntryType.BOOL, true);
	public static final ConfigEntry<Integer> SQUISH_MULTIPLIER = CONFIG.unsyncableEntry("squishMultiplier", EntryType.INT, 20);
	public static final ConfigEntry<Boolean> JUMP_ANTIC = CONFIG.unsyncableEntry("jumpAntic", EntryType.BOOL, true);
	public static final ConfigEntry<Boolean> DEATH_ANIM = CONFIG.unsyncableEntry("deathAnim", EntryType.BOOL, true);
	public static final ConfigEntry<Boolean> NEW_SHADOWS = CONFIG.unsyncableEntry("newShadows", EntryType.BOOL, true);
	public static final ConfigEntry<Boolean> PARTICLES = CONFIG.entry("particles", EntryType.BOOL, true);
	public static final ConfigEntry<Boolean> GLOWING_MAGMA = CONFIG.unsyncableEntry("glowingMagma", EntryType.BOOL, true);
	public static final ConfigEntry<Boolean> SLIME_BLOCK_PARTICLES = CONFIG.entry("slimeBlockParticles", EntryType.BOOL, true);
	public static final ConfigEntry<Boolean> MERGE_SOUNDS = CONFIG.entry("mergeSounds", EntryType.BOOL, true);
	public static final ConfigEntry<Boolean> SPLIT_SOUNDS = CONFIG.entry("splitSounds", EntryType.BOOL, true);

	private LSVisualsAudioConfig() {}
}
