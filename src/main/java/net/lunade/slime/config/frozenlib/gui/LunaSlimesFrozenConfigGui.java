package net.lunade.slime.config.frozenlib.gui;

import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.lunade.slime.config.LunaSlimesConfig;
import net.lunade.slime.config.frozenlib.LunaSlimesGameplayFrozenConfig;
import net.lunade.slime.config.frozenlib.LunaSlimesVisualsAudioFrozenConfig;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.NotNull;

public class LunaSlimesFrozenConfigGui extends PartitioningSerializer.GlobalData {

    public static Screen buildScreen(@NotNull Screen parent) {
        var configBuilder = ConfigBuilder.create().setParentScreen(parent).setTitle(LunaSlimesConfig.text("component.title"));
        configBuilder.setSavingRunnable(() -> {
            LunaSlimesGameplayFrozenConfig.INSTANCE.save();
            LunaSlimesVisualsAudioFrozenConfig.INSTANCE.save();
        });

        ConfigEntryBuilder entryBuilder = configBuilder.entryBuilder();

        var gameplay = configBuilder.getOrCreateCategory(LunaSlimesConfig.text("gameplay"));
        LunaSlimesGameplayFrozenConfigGui.setupEntries(gameplay, entryBuilder);

        var visualsAudio = configBuilder.getOrCreateCategory(LunaSlimesConfig.text("visuals_audio"));
        LunaSlimesVisualsAudioFrozenConfigGui.setupEntries(visualsAudio, entryBuilder);

        return configBuilder.build();
    }

}
