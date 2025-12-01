package net.lunade.slime.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import static net.lunade.slime.config.LunaSlimesConfig.text;
import static net.lunade.slime.config.LunaSlimesConfig.tooltip;
import net.minecraft.resources.Identifier;

@Config(name = "gameplay")
public final class LunaSlimesGameplayConfig implements ConfigData {
	public int maxSize = 4;
	public int mergeCooldown = 0;
	public int onSplitCooldown = 100;
	public int splitCooldown = 0;
	public int spawnedMergeCooldown = 0;
	public boolean useSplitting = true;

	@Environment(EnvType.CLIENT)
	static void setupEntries(ConfigCategory category, ConfigEntryBuilder builder) {
		final var config = LunaSlimesConfig.get().gameplay;
		category.setBackground(Identifier.fromNamespaceAndPath("lunaslimes", "textures/config/gameplay.png"));

		var maxSize = category.addEntry(builder.startIntSlider(text("max_size"), config.maxSize, 1, 127)
			.setDefaultValue(4)
			.setSaveConsumer(newValue -> config.maxSize = newValue)
			.setTooltip(tooltip("max_size"))
			.setMin(1)
			.setMax(127)
			.build()
		);

		var mergeCooldown = category.addEntry(builder.startIntSlider(text("merge_cooldown"), config.mergeCooldown, 0, 500)
			.setDefaultValue(0)
			.setSaveConsumer(newValue -> config.mergeCooldown = newValue)
			.setTooltip(tooltip("merge_cooldown"))
			.setMin(0)
			.setMax(500)
			.build()
		);

		var onSplitCooldown = category.addEntry(builder.startIntSlider(text("on_split_cooldown"), config.onSplitCooldown, 0, 500)
			.setDefaultValue(100)
			.setSaveConsumer(newValue -> config.onSplitCooldown = newValue)
			.setTooltip(tooltip("on_split_cooldown"))
			.setMin(0)
			.setMax(500)
			.build()
		);

		var splitCooldown = category.addEntry(builder.startIntSlider(text("split_cooldown"), config.splitCooldown, 0, 500)
			.setDefaultValue(0)
			.setSaveConsumer(newValue -> config.splitCooldown = newValue)
			.setTooltip(tooltip("split_cooldown"))
			.setMin(0)
			.setMax(500)
			.build()
		);

		var spawnedMergeCooldown = category.addEntry(builder.startIntSlider(text("spawned_merge_cooldown"), config.spawnedMergeCooldown, 0, 500)
			.setDefaultValue(0)
			.setSaveConsumer(newValue -> config.spawnedMergeCooldown = newValue)
			.setTooltip(tooltip("spawned_merge_cooldown"))
			.setMin(0)
			.setMax(500)
			.build()
		);

		var useSplitting = category.addEntry(builder.startBooleanToggle(text("use_splitting"), config.useSplitting)
			.setDefaultValue(true)
			.setSaveConsumer(newValue -> config.useSplitting = newValue)
			.setYesNoTextSupplier(bool -> text("use_splitting." + bool))
			.setTooltip(tooltip("use_splitting"))
			.build()
		);
	}
}
