package net.lunade.slime.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;

import static net.lunade.slime.config.LunaSlimesConfig.text;
import static net.lunade.slime.config.LunaSlimesConfig.tooltip;

@Config(name = "gameplay")
public final class GameplayConfig implements ConfigData {

    public boolean merging = true;
    public int maxSize = 7;
    public int mergeCooldown = 0;
    public int onSplitCooldown = 100;
    public int splitCooldown = 0;
    public boolean useSplitting = true;

    @Environment(EnvType.CLIENT)
    static void setupEntries(ConfigCategory category, ConfigEntryBuilder entryBuilder) {
        var config = LunaSlimesConfig.get().gameplay;
        category.setBackground(new ResourceLocation("lunaslimes", "textures/config/gameplay.png"));

        var merging = category.addEntry(entryBuilder.startBooleanToggle(text("merging"), config.merging)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.merging = newValue)
                .setYesNoTextSupplier(bool -> text("merging." + bool))
                .setTooltip(tooltip("merging"))
                .build()
        );

        var maxSize = category.addEntry(entryBuilder.startIntField(text("max_size"), config.maxSize)
                .setDefaultValue(7)
                .setSaveConsumer(newValue -> config.maxSize = newValue)
                .setTooltip(tooltip("max_size"))
                .setMin(1)
                .setMax(127)
                .build()
        );

        var mergeCooldown = category.addEntry(entryBuilder.startIntField(text("merge_cooldown"), config.mergeCooldown)
                .setDefaultValue(0)
                .setSaveConsumer(newValue -> config.mergeCooldown = newValue)
                .setTooltip(tooltip("merge_cooldown"))
                .setMin(0)
                .setMax(500)
                .build()
        );

        var onSplitCooldown = category.addEntry(entryBuilder.startIntField(text("on_split_cooldown"), config.onSplitCooldown)
                .setDefaultValue(100)
                .setSaveConsumer(newValue -> config.onSplitCooldown = newValue)
                .setTooltip(tooltip("on_split_cooldown"))
                .setMin(0)
                .setMax(500)
                .build()
        );

        var splitCooldown = category.addEntry(entryBuilder.startIntField(text("split_cooldown"), config.splitCooldown)
                .setDefaultValue(0)
                .setSaveConsumer(newValue -> config.splitCooldown = newValue)
                .setTooltip(tooltip("split_cooldown"))
                .setMin(0)
                .setMax(500)
                .build()
        );

        var useSplitting = category.addEntry(entryBuilder.startBooleanToggle(text("use_splitting"), config.useSplitting)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.useSplitting = newValue)
                .setYesNoTextSupplier(bool -> text("use_splitting." + bool))
                .setTooltip(tooltip("use_splitting"))
                .build()
        );
    }
}
