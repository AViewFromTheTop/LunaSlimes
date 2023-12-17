package net.lunade.slime.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static net.lunade.slime.config.LunaSlimesConfig.text;
import static net.lunade.slime.config.LunaSlimesConfig.tooltip;

@Config(name = "gameplay")
public final class GameplayConfig implements ConfigData {

    public int maxSize = 4;
    public int mergeCooldown = 0;
    public int onSplitCooldown = 100;
    public int splitCooldown = 0;
    public int spawnedMergeCooldown = 0;
    public boolean useSplitting = true;

    @Environment(EnvType.CLIENT)
    static void setupEntries(@NotNull ConfigCategory category, @NotNull ConfigEntryBuilder entryBuilder) {
        var config = LunaSlimesConfig.get().gameplay;
        category.setBackground(new ResourceLocation("lunaslimes", "textures/config/gameplay.png"));

        var maxSize = category.addEntry(entryBuilder.startIntSlider(text("max_size"), config.maxSize, 1, 127)
                .setDefaultValue(4)
                .setSaveConsumer(newValue -> config.maxSize = newValue)
                .setTooltip(tooltip("max_size"))
                .setMin(1)
                .setMax(127)
                .build()
        );

        var mergeCooldown = category.addEntry(entryBuilder.startIntSlider(text("merge_cooldown"), config.mergeCooldown, 0, 500)
                .setDefaultValue(0)
                .setSaveConsumer(newValue -> config.mergeCooldown = newValue)
                .setTooltip(tooltip("merge_cooldown"))
                .setMin(0)
                .setMax(500)
                .build()
        );

        var onSplitCooldown = category.addEntry(entryBuilder.startIntSlider(text("on_split_cooldown"), config.onSplitCooldown, 0, 500)
                .setDefaultValue(100)
                .setSaveConsumer(newValue -> config.onSplitCooldown = newValue)
                .setTooltip(tooltip("on_split_cooldown"))
                .setMin(0)
                .setMax(500)
                .build()
        );

        var splitCooldown = category.addEntry(entryBuilder.startIntSlider(text("split_cooldown"), config.splitCooldown, 0, 500)
                .setDefaultValue(0)
                .setSaveConsumer(newValue -> config.splitCooldown = newValue)
                .setTooltip(tooltip("split_cooldown"))
                .setMin(0)
                .setMax(500)
                .build()
        );

        var spawnedMergeCooldown = category.addEntry(entryBuilder.startIntSlider(text("spawned_merge_cooldown"), config.spawnedMergeCooldown, 0, 500)
                .setDefaultValue(0)
                .setSaveConsumer(newValue -> config.spawnedMergeCooldown = newValue)
                .setTooltip(tooltip("spawned_merge_cooldown"))
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
