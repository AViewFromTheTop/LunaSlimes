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
		final var config = LunaSlimesVisualsAudioFrozenConfig.get(true);
		final var modifiedConfig = LunaSlimesVisualsAudioFrozenConfig.getWithSync();
		final Class<? extends LunaSlimesVisualsAudioFrozenConfig> clazz = config.getClass();
		final Config<?> configInstance = LunaSlimesVisualsAudioFrozenConfig.INSTANCE;
		final var defaultConfig = LunaSlimesVisualsAudioFrozenConfig.INSTANCE.defaultInstance();

		category.setBackground(Identifier.fromNamespaceAndPath("lunaslimes", "textures/config/visuals_audio.png"));

		var growAnim = category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("grow_anim"), modifiedConfig.growAnim)
					.setDefaultValue(defaultConfig.growAnim)
					.setSaveConsumer(newValue -> config.growAnim = newValue)
					.setYesNoTextSupplier(bool -> text("grow_anim." + bool))
					.setTooltip(tooltip("grow_anim"))
					.build(),
				clazz,
				"growAnim",
				configInstance
			)
		);

		var wobbleAnim = category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("wobble_anim"), modifiedConfig.wobbleAnim)
					.setDefaultValue(defaultConfig.wobbleAnim)
					.setSaveConsumer(newValue -> config.wobbleAnim = newValue)
					.setYesNoTextSupplier(bool -> text("wobble_anim." + bool))
					.setTooltip(tooltip("wobble_anim"))
					.build(),
				clazz,
				"wobbleAnim",
				configInstance
			)
		);

		var squishMultiplier = category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startIntSlider(text("squish_multiplier"), modifiedConfig.squishMultiplier, 0, 50)
					.setDefaultValue(defaultConfig.squishMultiplier)
					.setSaveConsumer(newValue -> config.squishMultiplier = newValue)
					.setTooltip(tooltip("squish_multiplier"))
					.setMin(0)
					.setMax(50)
					.build(),
				clazz,
				"squishMultiplier",
				configInstance
			)
		);

		var jumpAntic = category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("jump_antic"), modifiedConfig.jumpAntic)
					.setDefaultValue(defaultConfig.jumpAntic)
					.setSaveConsumer(newValue -> config.jumpAntic = newValue)
					.setYesNoTextSupplier(bool -> text("jump_antic." + bool))
					.setTooltip(tooltip("jump_antic"))
					.build(),
				clazz,
				"jumpAntic",
				configInstance
			)
		);

		var deathAnim = category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("death_anim"), modifiedConfig.deathAnim)
					.setDefaultValue(defaultConfig.deathAnim)
					.setSaveConsumer(newValue -> config.deathAnim = newValue)
					.setYesNoTextSupplier(bool -> text("death_anim." + bool))
					.setTooltip(tooltip("death_anim"))
					.build(),
				clazz,
				"deathAnim",
				configInstance
			)
		);

		var newShadows = category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("new_shadows"), modifiedConfig.newShadows)
					.setDefaultValue(defaultConfig.newShadows)
					.setSaveConsumer(newValue -> config.newShadows = newValue)
					.setYesNoTextSupplier(bool -> text("new_shadows." + bool))
					.setTooltip(tooltip("new_shadows"))
					.build(),
				clazz,
				"newShadows",
				configInstance
			)
		);

		var particles = category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("particles"), modifiedConfig.particles)
					.setDefaultValue(defaultConfig.particles)
					.setSaveConsumer(newValue -> config.particles = newValue)
					.setYesNoTextSupplier(bool -> text("particles." + bool))
					.setTooltip(tooltip("particles"))
					.build(),
				clazz,
				"particles",
				configInstance
			)
		);

		var glowingMagma = category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("glowing_magma"), modifiedConfig.glowingMagma)
					.setDefaultValue(defaultConfig.glowingMagma)
					.setSaveConsumer(newValue -> config.glowingMagma = newValue)
					.setYesNoTextSupplier(bool -> text("glowing_magma." + bool))
					.setTooltip(tooltip("glowing_magma"))
					.build(),
				clazz,
				"glowingMagma",
				configInstance
			)
		);

		var slimeBlockParticles = category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("slime_block_particles"), modifiedConfig.slimeBlockParticles)
					.setDefaultValue(defaultConfig.slimeBlockParticles)
					.setSaveConsumer(newValue -> config.slimeBlockParticles = newValue)
					.setYesNoTextSupplier(bool -> text("slime_block_particles." + bool))
					.setTooltip(tooltip("slime_block_particles"))
					.build(),
				clazz,
				"slimeBlockParticles",
				configInstance
			)
		);

		var mergeSounds = category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("merge_sounds"), modifiedConfig.mergeSounds)
					.setDefaultValue(defaultConfig.mergeSounds)
					.setSaveConsumer(newValue -> config.mergeSounds = newValue)
					.setYesNoTextSupplier(bool -> text("merge_sounds." + bool))
					.setTooltip(tooltip("merge_sounds"))
					.build(),
				clazz,
				"mergeSounds",
				configInstance
			)
		);

		var splitSounds = category.addEntry(
			FrozenClothConfig.syncedEntry(
				builder.startBooleanToggle(text("split_sounds"), modifiedConfig.splitSounds)
					.setDefaultValue(defaultConfig.splitSounds)
					.setSaveConsumer(newValue -> config.splitSounds = newValue)
					.setYesNoTextSupplier(bool -> text("split_sounds." + bool))
					.setTooltip(tooltip("split_sounds"))
					.build(),
				clazz,
				"splitSounds",
				configInstance
			)
		);

	}

}
