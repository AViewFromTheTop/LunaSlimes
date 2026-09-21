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

package net.lunade.slime.config.gui;

import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.lunade.slime.config.frozenlib.LSGameplayConfig;
import net.mehvahdjukaar.candlelight.api.ClientOnly;

@ClientOnly
public final class LSGameplayConfigGui {

	public static void setupEntries(ConfigCategory category, ConfigEntryBuilder builder) {
		category.addEntry(LSConfigGuiHelper.intSliderEntry(builder, "max_size", LSGameplayConfig.MAX_SIZE, 1, 127));
		category.addEntry(LSConfigGuiHelper.zeroToFiveHundredEntry(builder, "merge_cooldown", LSGameplayConfig.MERGE_COOLDOWN));
		category.addEntry(LSConfigGuiHelper.zeroToFiveHundredEntry(builder, "on_split_cooldown", LSGameplayConfig.ON_SPLIT_COOLDOWN));
		category.addEntry(LSConfigGuiHelper.zeroToFiveHundredEntry(builder, "split_cooldown", LSGameplayConfig.SPLIT_COOLDOWN));
		category.addEntry(LSConfigGuiHelper.zeroToFiveHundredEntry(builder, "spawned_merge_cooldown", LSGameplayConfig.SPAWNED_MERGE_COOLDOWN));
		category.addEntry(LSConfigGuiHelper.booleanEntry(builder, "use_splitting", LSGameplayConfig.USE_SPLITTING));
	}

	private LSGameplayConfigGui() {}
}
