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

import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.lunade.slime.LSConstants;
import net.lunade.slime.config.LSGameplayConfig;
import net.lunade.slime.config.LSVisualsAudioConfig;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.gui.screens.Screen;

@ClientOnly
public final class LSMainConfigGui extends PartitioningSerializer.GlobalData {

	public static Screen buildScreen(Screen parent) {
		final ConfigBuilder configBuilder = ConfigBuilder.create().setParentScreen(parent).setTitle(LSConstants.text("component.title"));
		configBuilder.setSavingRunnable(() -> {
			LSGameplayConfig.CONFIG.save();
			LSVisualsAudioConfig.CONFIG.save();
		});

		final ConfigEntryBuilder entryBuilder = configBuilder.entryBuilder();

		final ConfigCategory gameplayCategory = configBuilder.getOrCreateCategory(LSConstants.text("gameplay"));
		LSGameplayConfigGui.setupEntries(gameplayCategory, entryBuilder);

		final ConfigCategory visualsAndAudioCategory = configBuilder.getOrCreateCategory(LSConstants.text("visuals_audio"));
		LSVisualsAudioConfigGui.setupEntries(visualsAndAudioCategory, entryBuilder);

		return configBuilder.build();
	}
}
