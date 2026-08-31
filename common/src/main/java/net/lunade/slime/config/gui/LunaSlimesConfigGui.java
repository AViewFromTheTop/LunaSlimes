package net.lunade.slime.config.gui;

import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.lunade.slime.LunaSlimesConstants;
import net.lunade.slime.config.frozenlib.LunaSlimesGameplayConfig;
import net.lunade.slime.config.frozenlib.LunaSlimesVisualsAudioConfig;
import net.minecraft.client.gui.screens.Screen;

public class LunaSlimesConfigGui extends PartitioningSerializer.GlobalData {

	public static Screen buildScreen(Screen parent) {
		final ConfigBuilder configBuilder = ConfigBuilder.create().setParentScreen(parent).setTitle(LunaSlimesConstants.text("component.title"));
		configBuilder.setSavingRunnable(() -> {
			LunaSlimesGameplayConfig.CONFIG.save();
			LunaSlimesVisualsAudioConfig.CONFIG.save();
		});

		final ConfigEntryBuilder entryBuilder = configBuilder.entryBuilder();

		final ConfigCategory gameplayCategory = configBuilder.getOrCreateCategory(LunaSlimesConstants.text("gameplay"));
		LunaSlimesGameplayConfigGui.setupEntries(gameplayCategory, entryBuilder);

		final ConfigCategory visualsAndAudioCategory = configBuilder.getOrCreateCategory(LunaSlimesConstants.text("visuals_audio"));
		LunaSlimesVisualsAudioConfigGui.setupEntries(visualsAndAudioCategory, entryBuilder);

		return configBuilder.build();
	}

}
