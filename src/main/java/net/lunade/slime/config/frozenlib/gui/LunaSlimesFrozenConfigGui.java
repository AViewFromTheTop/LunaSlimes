package net.lunade.slime.config.frozenlib.gui;

import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.lunade.slime.config.LunaSlimesConfig;
import net.lunade.slime.config.frozenlib.LunaSlimesGameplayFrozenConfig;
import net.lunade.slime.config.frozenlib.LunaSlimesVisualsAudioFrozenConfig;
import net.minecraft.client.gui.screens.Screen;

public class LunaSlimesFrozenConfigGui extends PartitioningSerializer.GlobalData {

	public static Screen buildScreen(Screen parent) {
		var configBuilder = ConfigBuilder.create().setParentScreen(parent).setTitle(LunaSlimesConfig.text("component.title"));
		configBuilder.setSavingRunnable(() -> {
			LunaSlimesGameplayFrozenConfig.CONFIG.save();
			LunaSlimesVisualsAudioFrozenConfig.CONFIG.save();
		});

		ConfigEntryBuilder entryBuilder = configBuilder.entryBuilder();

		var gameplay = configBuilder.getOrCreateCategory(LunaSlimesConfig.text("gameplay"));
		LunaSlimesGameplayFrozenConfigGui.setupEntries(gameplay, entryBuilder);

		var visualsAudio = configBuilder.getOrCreateCategory(LunaSlimesConfig.text("visuals_audio"));
		LunaSlimesVisualsAudioFrozenConfigGui.setupEntries(visualsAudio, entryBuilder);

		return configBuilder.build();
	}

}
