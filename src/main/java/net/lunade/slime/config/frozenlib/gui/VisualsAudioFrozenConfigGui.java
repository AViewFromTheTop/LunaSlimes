package net.lunade.slime.config.frozenlib.gui;

import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.clothconfig.FrozenClothConfig;
import net.lunade.slime.config.frozenlib.VisualsAudioFrozenConfig;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static net.lunade.slime.config.LunaSlimesConfig.text;
import static net.lunade.slime.config.LunaSlimesConfig.tooltip;

@Environment(EnvType.CLIENT)
public final class VisualsAudioFrozenConfigGui {
	private VisualsAudioFrozenConfigGui() {
		throw new UnsupportedOperationException("VisualsAudioFrozenConfigGui contains only static declarations.");
	}

	public static void setupEntries(@NotNull ConfigCategory category, @NotNull ConfigEntryBuilder entryBuilder) {
		var config = VisualsAudioFrozenConfig.get(true);
		var modifiedConfig = VisualsAudioFrozenConfig.getWithSync();
		Class<? extends VisualsAudioFrozenConfig> clazz = config.getClass();
		Config<?> configInstance = VisualsAudioFrozenConfig.INSTANCE;
		var defaultConfig = VisualsAudioFrozenConfig.INSTANCE.defaultInstance();

		category.setBackground(new ResourceLocation("lunaslimes", "textures/config/visuals_audio.png"));

		var growAnim = category.addEntry(
				FrozenClothConfig.syncedEntry(
						entryBuilder.startBooleanToggle(text("grow_anim"), modifiedConfig.growAnim)
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
						entryBuilder.startBooleanToggle(text("wobble_anim"), modifiedConfig.wobbleAnim)
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
						entryBuilder.startIntSlider(text("squish_multiplier"), modifiedConfig.squishMultiplier, 0, 50)
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
						entryBuilder.startBooleanToggle(text("jump_antic"), modifiedConfig.jumpAntic)
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
						entryBuilder.startBooleanToggle(text("death_anim"), modifiedConfig.deathAnim)
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
						entryBuilder.startBooleanToggle(text("new_shadows"), modifiedConfig.newShadows)
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
						entryBuilder.startBooleanToggle(text("particles"), modifiedConfig.particles)
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

		var scaleTextures = category.addEntry(
				FrozenClothConfig.syncedEntry(
						entryBuilder.startBooleanToggle(text("scale_textures"), modifiedConfig.scaleTextures)
								.setDefaultValue(defaultConfig.scaleTextures)
								.setSaveConsumer(newValue -> config.scaleTextures = newValue)
								.setYesNoTextSupplier(bool -> text("scale_textures." + bool))
								.setTooltip(tooltip("scale_textures"))
								.build(),
						clazz,
						"scaleTextures",
						configInstance
				)
		);

		var glowingMagma = category.addEntry(
				FrozenClothConfig.syncedEntry(
						entryBuilder.startBooleanToggle(text("glowing_magma"), modifiedConfig.glowingMagma)
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
						entryBuilder.startBooleanToggle(text("slime_block_particles"), modifiedConfig.slimeBlockParticles)
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
						entryBuilder.startBooleanToggle(text("merge_sounds"), modifiedConfig.mergeSounds)
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
						entryBuilder.startBooleanToggle(text("split_sounds"), modifiedConfig.splitSounds)
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
