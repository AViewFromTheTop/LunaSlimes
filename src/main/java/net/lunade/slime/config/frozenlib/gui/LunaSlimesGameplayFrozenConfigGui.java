package net.lunade.slime.config.frozenlib.gui;

import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.clothconfig.FrozenClothConfig;
import net.lunade.slime.config.LunaSlimesConfig;
import net.lunade.slime.config.frozenlib.LunaSlimesGameplayFrozenConfig;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public final class LunaSlimesGameplayFrozenConfigGui {

	private LunaSlimesGameplayFrozenConfigGui() {
		throw new UnsupportedOperationException("GameplayFrozenConfigGui contains only static declarations.");
	}

	public static void setupEntries(ConfigCategory category, ConfigEntryBuilder builder) {
		category.setBackground(Identifier.fromNamespaceAndPath("lunaslimes", "textures/config/gameplay.png"));

		var maxSize = category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startIntSlider(LunaSlimesConfig.text("max_size"), LunaSlimesGameplayFrozenConfig.maxSize.getWithSync(), 1, 127)
					.setTooltip(LunaSlimesConfig.tooltip("max_size"))
					.setMin(1)
					.setMax(127),
				LunaSlimesGameplayFrozenConfig.maxSize
			)
		);

		var mergeCooldown = category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startIntSlider(LunaSlimesConfig.text("merge_cooldown"), LunaSlimesGameplayFrozenConfig.mergeCooldown.getWithSync(), 0, 500)
					.setTooltip(LunaSlimesConfig.tooltip("merge_cooldown"))
					.setMin(0)
					.setMax(500),
				LunaSlimesGameplayFrozenConfig.mergeCooldown
			)
		);

		var onSplitCooldown = category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startIntSlider(LunaSlimesConfig.text("on_split_cooldown"), LunaSlimesGameplayFrozenConfig.onSplitCooldown.getWithSync(), 0, 500)
					.setTooltip(LunaSlimesConfig.tooltip("on_split_cooldown"))
					.setMin(0)
					.setMax(500),
				LunaSlimesGameplayFrozenConfig.onSplitCooldown
			)
		);

		var splitCooldown = category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startIntSlider(LunaSlimesConfig.text("split_cooldown"), LunaSlimesGameplayFrozenConfig.splitCooldown.getWithSync(), 0, 500)
					.setTooltip(LunaSlimesConfig.tooltip("split_cooldown"))
					.setMin(0)
					.setMax(500),
				LunaSlimesGameplayFrozenConfig.splitCooldown
			)
		);

		var spawnedMergeCooldown = category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startIntSlider(LunaSlimesConfig.text("spawned_merge_cooldown"), LunaSlimesGameplayFrozenConfig.spawnedMergeCooldown.getWithSync(), 0, 500)
					.setTooltip(LunaSlimesConfig.tooltip("spawned_merge_cooldown"))
					.setMin(0)
					.setMax(500),
				LunaSlimesGameplayFrozenConfig.spawnedMergeCooldown
			)
		);

		var useSplitting = category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(LunaSlimesConfig.text("use_splitting"), LunaSlimesGameplayFrozenConfig.useSplitting.getWithSync())
					.setYesNoTextSupplier(bool -> LunaSlimesConfig.text("use_splitting." + bool))
					.setTooltip(LunaSlimesConfig.tooltip("use_splitting")),
				LunaSlimesGameplayFrozenConfig.useSplitting
			)
		);
	}

}
