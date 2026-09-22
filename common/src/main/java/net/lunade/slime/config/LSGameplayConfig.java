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

package net.lunade.slime.config;

import net.frozenblock.lib.config.v2.config.ConfigData;
import net.frozenblock.lib.config.v2.config.ConfigSettings;
import net.frozenblock.lib.config.v2.entry.ConfigEntry;
import net.frozenblock.lib.config.v2.entry.EntryType;
import net.frozenblock.lib.config.v2.registry.ID;
import net.lunade.slime.LSConstants;

public final class LSGameplayConfig {
	public static final ConfigData<?> CONFIG = ConfigData.createAndRegister(
		ID.of(LSConstants.id("gameplay")),
		ConfigSettings.JSON
	);

	public static final ConfigEntry<Integer> MAX_SIZE = CONFIG.entry("maxSize", EntryType.INT, 4);
	public static final ConfigEntry<Integer> MERGE_COOLDOWN = CONFIG.entry("mergeCooldown", EntryType.INT, 0);
	public static final ConfigEntry<Integer> ON_SPLIT_COOLDOWN = CONFIG.entry("onSplitCooldown", EntryType.INT, 100);
	public static final ConfigEntry<Integer> SPLIT_COOLDOWN = CONFIG.entry("splitCooldown", EntryType.INT, 0);
	public static final ConfigEntry<Integer> SPAWNED_MERGE_COOLDOWN = CONFIG.entry("spawnedMergeCooldown", EntryType.INT, 0);
	public static final ConfigEntry<Boolean> USE_SPLITTING = CONFIG.entry("useSplitting", EntryType.BOOL, true);

	private LSGameplayConfig() {}
}
