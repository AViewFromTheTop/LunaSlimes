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
import net.lunade.slime.LunaSlimesMain;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@Config(name = "lunaslimes")
public class LunaSlimesConfig extends PartitioningSerializer.GlobalData {

    @ConfigEntry.Category("visuals")
    @ConfigEntry.Gui.TransitiveObject
    public final VisualConfig visuals = new VisualConfig();

    @Environment(EnvType.CLIENT)
    public static Screen buildScreen(Screen parent) {
        var configBuilder = ConfigBuilder.create().setParentScreen(parent).setTitle(text("component.title"));
        configBuilder.setSavingRunnable(() -> AutoConfig.getConfigHolder(LunaSlimesConfig.class).save());
        var animations = configBuilder.getOrCreateCategory(text("visuals"));
        ConfigEntryBuilder entryBuilder = configBuilder.entryBuilder();
        VisualConfig.setupEntries(animations, entryBuilder);
        return configBuilder.build();
    }

    public static LunaSlimesConfig get() {
        if (!LunaSlimesMain.areConfigsInit) {
            AutoConfig.register(LunaSlimesConfig.class, PartitioningSerializer.wrap(GsonConfigSerializer::new));
            LunaSlimesMain.areConfigsInit = true;
        }
        return AutoConfig.getConfigHolder(LunaSlimesConfig.class).getConfig();
    }

    public static Component text(String key) {
        return Component.translatable("option." + "lunaslimes" + "." + key);
    }

    public static Component tooltip(String key) {
        return Component.translatable("tooltip." + "lunaslimes" + "." + key);
    }

}
