package net.lunade.slime.config.frozenlib.gui;

import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.clothconfig.FrozenClothConfig;
import net.lunade.slime.config.LunaSlimesConfig;
import net.lunade.slime.config.frozenlib.GameplayFrozenConfig;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public final class GameplayFrozenConfigGui {
	private GameplayFrozenConfigGui() {
		throw new UnsupportedOperationException("GameplayFrozenConfigGui contains only static declarations.");
	}

	public static void setupEntries(@NotNull ConfigCategory category, @NotNull ConfigEntryBuilder entryBuilder) {
		var config = GameplayFrozenConfig.get(true);
		var modifiedConfig = GameplayFrozenConfig.getWithSync();
		Class<? extends GameplayFrozenConfig> clazz = config.getClass();
		Config<?> configInstance = GameplayFrozenConfig.INSTANCE;
		var defaultConfig = GameplayFrozenConfig.INSTANCE.defaultInstance();

		category.setBackground(new ResourceLocation("lunaslimes", "textures/config/gameplay.png"));

		var maxSize = category.addEntry(
				FrozenClothConfig.syncedEntry(
						entryBuilder.startIntSlider(LunaSlimesConfig.text("max_size"), modifiedConfig.maxSize, 1, 127)
								.setDefaultValue(defaultConfig.maxSize)
								.setSaveConsumer(newValue -> config.maxSize = newValue)
								.setTooltip(LunaSlimesConfig.tooltip("max_size"))
								.setMin(1)
								.setMax(127)
								.build(),
						clazz,
						"maxSize",
						configInstance
				)
		);

		var mergeCooldown = category.addEntry(
				FrozenClothConfig.syncedEntry(
						entryBuilder.startIntSlider(LunaSlimesConfig.text("merge_cooldown"), modifiedConfig.mergeCooldown, 0, 500)
								.setDefaultValue(defaultConfig.mergeCooldown)
								.setSaveConsumer(newValue -> config.mergeCooldown = newValue)
								.setTooltip(LunaSlimesConfig.tooltip("merge_cooldown"))
								.setMin(0)
								.setMax(500)
								.build(),
						clazz,
						"mergeCooldown",
						configInstance
				)
		);

		var onSplitCooldown = category.addEntry(
				FrozenClothConfig.syncedEntry(
						entryBuilder.startIntSlider(LunaSlimesConfig.text("on_split_cooldown"), modifiedConfig.onSplitCooldown, 0, 500)
								.setDefaultValue(defaultConfig.onSplitCooldown)
								.setSaveConsumer(newValue -> config.onSplitCooldown = newValue)
								.setTooltip(LunaSlimesConfig.tooltip("on_split_cooldown"))
								.setMin(0)
								.setMax(500)
								.build(),
						clazz,
						"onSplitCooldown",
						configInstance
				)
		);

		var splitCooldown = category.addEntry(
				FrozenClothConfig.syncedEntry(
						entryBuilder.startIntSlider(LunaSlimesConfig.text("split_cooldown"), modifiedConfig.splitCooldown, 0, 500)
								.setDefaultValue(defaultConfig.splitCooldown)
								.setSaveConsumer(newValue -> config.splitCooldown = newValue)
								.setTooltip(LunaSlimesConfig.tooltip("split_cooldown"))
								.setMin(0)
								.setMax(500)
								.build(),
						clazz,
						"splitCooldown",
						configInstance
				)
		);

		var spawnedMergeCooldown = category.addEntry(
				FrozenClothConfig.syncedEntry(
						entryBuilder.startIntSlider(LunaSlimesConfig.text("spawned_merge_cooldown"), modifiedConfig.spawnedMergeCooldown, 0, 500)
								.setDefaultValue(defaultConfig.spawnedMergeCooldown)
								.setSaveConsumer(newValue -> config.spawnedMergeCooldown = newValue)
								.setTooltip(LunaSlimesConfig.tooltip("spawned_merge_cooldown"))
								.setMin(0)
								.setMax(500)
								.build(),
						clazz,
						"spawnedMergeCooldown",
						configInstance
				)
		);

		var useSplitting = category.addEntry(
				FrozenClothConfig.syncedEntry(
						entryBuilder.startBooleanToggle(LunaSlimesConfig.text("use_splitting"), modifiedConfig.useSplitting)
								.setDefaultValue(defaultConfig.useSplitting)
								.setSaveConsumer(newValue -> config.useSplitting = newValue)
								.setYesNoTextSupplier(bool -> LunaSlimesConfig.text("use_splitting." + bool))
								.setTooltip(LunaSlimesConfig.tooltip("use_splitting"))
								.build(),
						clazz,
						"useSplitting",
						configInstance
				)
		);
	}

}
