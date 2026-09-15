package net.lunade.slime.config.gui;

import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.lunade.slime.LSConstants;
import net.lunade.slime.config.frozenlib.LSGameplayConfig;
import net.lunade.slime.config.frozenlib.LSVisualsAudioConfig;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.gui.screens.Screen;

@ClientOnly
public final class LSConfigGui extends PartitioningSerializer.GlobalData {

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
