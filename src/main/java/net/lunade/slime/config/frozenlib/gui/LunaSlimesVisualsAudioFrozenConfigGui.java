package net.lunade.slime.config.frozenlib.gui;

import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.lib.config.clothconfig.FrozenClothConfig;
import static net.lunade.slime.config.LunaSlimesConfig.text;
import static net.lunade.slime.config.LunaSlimesConfig.tooltip;
import net.lunade.slime.config.frozenlib.LunaSlimesVisualsAudioFrozenConfig;

@Environment(EnvType.CLIENT)
public final class LunaSlimesVisualsAudioFrozenConfigGui {

	private LunaSlimesVisualsAudioFrozenConfigGui() {
		throw new UnsupportedOperationException("VisualsAudioFrozenConfigGui contains only static declarations.");
	}

	public static void setupEntries(ConfigCategory category, ConfigEntryBuilder builder) {
		category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("grow_anim"), LunaSlimesVisualsAudioFrozenConfig.GROW_ANIM.getWithSync())
					.setYesNoTextSupplier(bool -> text("grow_anim." + bool))
					.setTooltip(tooltip("grow_anim")),
				LunaSlimesVisualsAudioFrozenConfig.GROW_ANIM
			)
		);

		category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("wobble_anim"), LunaSlimesVisualsAudioFrozenConfig.WOBBLE_ANIM.getWithSync())
					.setYesNoTextSupplier(bool -> text("wobble_anim." + bool))
					.setTooltip(tooltip("wobble_anim")),
				LunaSlimesVisualsAudioFrozenConfig.WOBBLE_ANIM
			)
		);

		category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startIntSlider(text("squish_multiplier"), LunaSlimesVisualsAudioFrozenConfig.SQUISH_MULTIPLIER.getWithSync(), 0, 50)
					.setTooltip(tooltip("squish_multiplier"))
					.setMin(0)
					.setMax(50),
				LunaSlimesVisualsAudioFrozenConfig.SQUISH_MULTIPLIER
			)
		);

		category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("jump_antic"), LunaSlimesVisualsAudioFrozenConfig.JUMP_ANTIC.getWithSync())
					.setYesNoTextSupplier(bool -> text("jump_antic." + bool))
					.setTooltip(tooltip("jump_antic")),
				LunaSlimesVisualsAudioFrozenConfig.JUMP_ANTIC
			)
		);

		category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("death_anim"), LunaSlimesVisualsAudioFrozenConfig.DEATH_ANIM.getWithSync())
					.setYesNoTextSupplier(bool -> text("death_anim." + bool))
					.setTooltip(tooltip("death_anim")),
				LunaSlimesVisualsAudioFrozenConfig.DEATH_ANIM
			)
		);

		category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("new_shadows"), LunaSlimesVisualsAudioFrozenConfig.NEW_SHADOWS.getWithSync())
					.setYesNoTextSupplier(bool -> text("new_shadows." + bool))
					.setTooltip(tooltip("new_shadows")),
				LunaSlimesVisualsAudioFrozenConfig.NEW_SHADOWS
			)
		);

		category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("particles"), LunaSlimesVisualsAudioFrozenConfig.PARTICLES.getWithSync())
					.setYesNoTextSupplier(bool -> text("particles." + bool))
					.setTooltip(tooltip("particles")),
				LunaSlimesVisualsAudioFrozenConfig.PARTICLES
			)
		);

		category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("glowing_magma"), LunaSlimesVisualsAudioFrozenConfig.GLOWING_MAGMA.getWithSync())
					.setYesNoTextSupplier(bool -> text("glowing_magma." + bool))
					.setTooltip(tooltip("glowing_magma")),
				LunaSlimesVisualsAudioFrozenConfig.GLOWING_MAGMA
			)
		);

		category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("slime_block_particles"), LunaSlimesVisualsAudioFrozenConfig.SLIME_BLOCK_PARTICLES.getWithSync())
					.setYesNoTextSupplier(bool -> text("slime_block_particles." + bool))
					.setTooltip(tooltip("slime_block_particles")),
				LunaSlimesVisualsAudioFrozenConfig.SLIME_BLOCK_PARTICLES
			)
		);

		category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("merge_sounds"), LunaSlimesVisualsAudioFrozenConfig.MERGE_SOUNDS.getWithSync())
					.setYesNoTextSupplier(bool -> text("merge_sounds." + bool))
					.setTooltip(tooltip("merge_sounds")),
				LunaSlimesVisualsAudioFrozenConfig.MERGE_SOUNDS
			)
		);

		category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("split_sounds"), LunaSlimesVisualsAudioFrozenConfig.SPLIT_SOUNDS.getWithSync())
					.setYesNoTextSupplier(bool -> text("split_sounds." + bool))
					.setTooltip(tooltip("split_sounds")),
				LunaSlimesVisualsAudioFrozenConfig.SPLIT_SOUNDS
			)
		);

	}

}
