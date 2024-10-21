package net.lunade.slime.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.slime.LunaSlimes;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Config(name = "lunaslimes")
public class LunaSlimesConfig extends PartitioningSerializer.GlobalData {

    @ConfigEntry.Category("gameplay")
    @ConfigEntry.Gui.TransitiveObject
    public final LunaSlimesGameplayConfig gameplay = new LunaSlimesGameplayConfig();

    @ConfigEntry.Category("visuals_audio")
    @ConfigEntry.Gui.TransitiveObject
    public final LunaSlimesVisualsAudioConfig visuals_audio = new LunaSlimesVisualsAudioConfig();

    @Environment(EnvType.CLIENT)
    public static Screen buildScreen(Screen parent) {
        var configBuilder = ConfigBuilder.create().setParentScreen(parent).setTitle(text("component.title"));
        configBuilder.setSavingRunnable(() -> AutoConfig.getConfigHolder(LunaSlimesConfig.class).save());
        ConfigEntryBuilder entryBuilder = configBuilder.entryBuilder();

        var gameplayTab = configBuilder.getOrCreateCategory(text("gameplay"));
        LunaSlimesGameplayConfig.setupEntries(gameplayTab, entryBuilder);

        var visualTab = configBuilder.getOrCreateCategory(text("visuals_audio"));
        LunaSlimesVisualsAudioConfig.setupEntries(visualTab, entryBuilder);

        return configBuilder.build();
    }

    public static LunaSlimesConfig get() {
        if (!LunaSlimes.areConfigsInit) {
            AutoConfig.register(LunaSlimesConfig.class, PartitioningSerializer.wrap(GsonConfigSerializer::new));
            LunaSlimes.areConfigsInit = true;
        }
        return AutoConfig.getConfigHolder(LunaSlimesConfig.class).getConfig();
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static Component text(String key) {
        return Component.translatable("option." + "lunaslimes" + "." + key);
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static Component tooltip(String key) {
        return Component.translatable("tooltip." + "lunaslimes" + "." + key);
    }

}
