package net.lunade.slime.config.frozenlib.gui;

import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.clothconfig.FrozenClothConfig;
import static net.lunade.slime.config.LunaSlimesConfig.text;
import static net.lunade.slime.config.LunaSlimesConfig.tooltip;
import net.lunade.slime.config.frozenlib.LunaSlimesVisualsAudioFrozenConfig;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public final class LunaSlimesVisualsAudioFrozenConfigGui {

	private LunaSlimesVisualsAudioFrozenConfigGui() {
		throw new UnsupportedOperationException("VisualsAudioFrozenConfigGui contains only static declarations.");
	}

	public static void setupEntries(ConfigCategory category, ConfigEntryBuilder builder) {
		category.setBackground(Identifier.fromNamespaceAndPath("lunaslimes", "textures/config/visuals_audio.png"));

		var growAnim = category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("grow_anim"), LunaSlimesVisualsAudioFrozenConfig.growAnim.getWithSync())
					.setYesNoTextSupplier(bool -> text("grow_anim." + bool))
					.setTooltip(tooltip("grow_anim")),
				LunaSlimesVisualsAudioFrozenConfig.growAnim
			)
		);

		var wobbleAnim = category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("wobble_anim"), LunaSlimesVisualsAudioFrozenConfig.wobbleAnim.getWithSync())
					.setYesNoTextSupplier(bool -> text("wobble_anim." + bool))
					.setTooltip(tooltip("wobble_anim")),
				LunaSlimesVisualsAudioFrozenConfig.wobbleAnim
			)
		);

		var squishMultiplier = category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startIntSlider(text("squish_multiplier"), LunaSlimesVisualsAudioFrozenConfig.squishMultiplier.getWithSync(), 0, 50)
					.setTooltip(tooltip("squish_multiplier"))
					.setMin(0)
					.setMax(50),
				LunaSlimesVisualsAudioFrozenConfig.squishMultiplier
			)
		);

		var jumpAntic = category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("jump_antic"), LunaSlimesVisualsAudioFrozenConfig.jumpAntic.getWithSync())
					.setYesNoTextSupplier(bool -> text("jump_antic." + bool))
					.setTooltip(tooltip("jump_antic")),
				LunaSlimesVisualsAudioFrozenConfig.jumpAntic
			)
		);

		var deathAnim = category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("death_anim"), LunaSlimesVisualsAudioFrozenConfig.deathAnim.getWithSync())
					.setYesNoTextSupplier(bool -> text("death_anim." + bool))
					.setTooltip(tooltip("death_anim")),
				LunaSlimesVisualsAudioFrozenConfig.deathAnim
			)
		);

		var newShadows = category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("new_shadows"), LunaSlimesVisualsAudioFrozenConfig.newShadows.getWithSync())
					.setYesNoTextSupplier(bool -> text("new_shadows." + bool))
					.setTooltip(tooltip("new_shadows")),
				LunaSlimesVisualsAudioFrozenConfig.newShadows
			)
		);

		var particles = category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("particles"), LunaSlimesVisualsAudioFrozenConfig.particles.getWithSync())
					.setYesNoTextSupplier(bool -> text("particles." + bool))
					.setTooltip(tooltip("particles")),
				LunaSlimesVisualsAudioFrozenConfig.particles
			)
		);

		var glowingMagma = category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("glowing_magma"), LunaSlimesVisualsAudioFrozenConfig.glowingMagma.getWithSync())
					.setYesNoTextSupplier(bool -> text("glowing_magma." + bool))
					.setTooltip(tooltip("glowing_magma")),
				LunaSlimesVisualsAudioFrozenConfig.glowingMagma
			)
		);

		var slimeBlockParticles = category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("slime_block_particles"), LunaSlimesVisualsAudioFrozenConfig.slimeBlockParticles.getWithSync())
					.setYesNoTextSupplier(bool -> text("slime_block_particles." + bool))
					.setTooltip(tooltip("slime_block_particles")),
				LunaSlimesVisualsAudioFrozenConfig.slimeBlockParticles
			)
		);

		var mergeSounds = category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("merge_sounds"), LunaSlimesVisualsAudioFrozenConfig.mergeSounds.getWithSync())
					.setYesNoTextSupplier(bool -> text("merge_sounds." + bool))
					.setTooltip(tooltip("merge_sounds")),
				LunaSlimesVisualsAudioFrozenConfig.mergeSounds
			)
		);

		var splitSounds = category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("split_sounds"), LunaSlimesVisualsAudioFrozenConfig.splitSounds.getWithSync())
					.setYesNoTextSupplier(bool -> text("split_sounds." + bool))
					.setTooltip(tooltip("split_sounds")),
				LunaSlimesVisualsAudioFrozenConfig.splitSounds
			)
		);

	}

}
