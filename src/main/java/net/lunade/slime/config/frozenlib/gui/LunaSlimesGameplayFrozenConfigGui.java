package net.lunade.slime.config.frozenlib.gui;

import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.lib.config.clothconfig.FrozenClothConfig;
import net.lunade.slime.config.LunaSlimesConfig;
import net.lunade.slime.config.frozenlib.LunaSlimesGameplayFrozenConfig;

@Environment(EnvType.CLIENT)
public final class LunaSlimesGameplayFrozenConfigGui {

	private LunaSlimesGameplayFrozenConfigGui() {
		throw new UnsupportedOperationException("GameplayFrozenConfigGui contains only static declarations.");
	}

	public static void setupEntries(ConfigCategory category, ConfigEntryBuilder builder) {
		category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startIntSlider(LunaSlimesConfig.text("max_size"), LunaSlimesGameplayFrozenConfig.MAX_SIZE.getWithSync(), 1, 127)
					.setTooltip(LunaSlimesConfig.tooltip("max_size"))
					.setMin(1)
					.setMax(127),
				LunaSlimesGameplayFrozenConfig.MAX_SIZE
			)
		);

		category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startIntSlider(LunaSlimesConfig.text("merge_cooldown"), LunaSlimesGameplayFrozenConfig.MERGE_COOLDOWN.getWithSync(), 0, 500)
					.setTooltip(LunaSlimesConfig.tooltip("merge_cooldown"))
					.setMin(0)
					.setMax(500),
				LunaSlimesGameplayFrozenConfig.MERGE_COOLDOWN
			)
		);

		category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startIntSlider(LunaSlimesConfig.text("on_split_cooldown"), LunaSlimesGameplayFrozenConfig.ON_SPLIT_COOLDOWN.getWithSync(), 0, 500)
					.setTooltip(LunaSlimesConfig.tooltip("on_split_cooldown"))
					.setMin(0)
					.setMax(500),
				LunaSlimesGameplayFrozenConfig.ON_SPLIT_COOLDOWN
			)
		);

		category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startIntSlider(LunaSlimesConfig.text("split_cooldown"), LunaSlimesGameplayFrozenConfig.SPLIT_COOLDOWN.getWithSync(), 0, 500)
					.setTooltip(LunaSlimesConfig.tooltip("split_cooldown"))
					.setMin(0)
					.setMax(500),
				LunaSlimesGameplayFrozenConfig.SPLIT_COOLDOWN
			)
		);

		category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startIntSlider(LunaSlimesConfig.text("spawned_merge_cooldown"), LunaSlimesGameplayFrozenConfig.SPAWNED_MERGE_COOLDOWN.getWithSync(), 0, 500)
					.setTooltip(LunaSlimesConfig.tooltip("spawned_merge_cooldown"))
					.setMin(0)
					.setMax(500),
				LunaSlimesGameplayFrozenConfig.SPAWNED_MERGE_COOLDOWN
			)
		);

		category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(LunaSlimesConfig.text("use_splitting"), LunaSlimesGameplayFrozenConfig.USE_SPLITTING.getWithSync())
					.setYesNoTextSupplier(bool -> LunaSlimesConfig.text("use_splitting." + bool))
					.setTooltip(LunaSlimesConfig.tooltip("use_splitting")),
				LunaSlimesGameplayFrozenConfig.USE_SPLITTING
			)
		);
	}

}
