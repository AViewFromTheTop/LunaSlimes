package net.lunade.slime.config.gui;

import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.lunade.slime.config.frozenlib.LSVisualsAudioConfig;
import net.mehvahdjukaar.candlelight.api.ClientOnly;

@ClientOnly
public final class LSVisualsAudioConfigGui {

	public static void setupEntries(ConfigCategory category, ConfigEntryBuilder builder) {
		category.addEntry(LSConfigGuiHelper.booleanEntry(builder, "grow_anim", LSVisualsAudioConfig.GROW_ANIM));
		category.addEntry(LSConfigGuiHelper.booleanEntry(builder, "wobble_anim", LSVisualsAudioConfig.WOBBLE_ANIM));
		category.addEntry(LSConfigGuiHelper.intSliderEntry(builder, "squish_multiplier", LSVisualsAudioConfig.SQUISH_MULTIPLIER, 0, 50));
		category.addEntry(LSConfigGuiHelper.booleanEntry(builder, "jump_antic", LSVisualsAudioConfig.JUMP_ANTIC));
		category.addEntry(LSConfigGuiHelper.booleanEntry(builder, "death_anim", LSVisualsAudioConfig.DEATH_ANIM));
		category.addEntry(LSConfigGuiHelper.booleanEntry(builder, "new_shadows", LSVisualsAudioConfig.NEW_SHADOWS));
		category.addEntry(LSConfigGuiHelper.booleanEntry(builder, "particles", LSVisualsAudioConfig.PARTICLES));
		category.addEntry(LSConfigGuiHelper.booleanEntry(builder, "glowing_magma", LSVisualsAudioConfig.GLOWING_MAGMA));
		category.addEntry(LSConfigGuiHelper.booleanEntry(builder, "slime_block_particles", LSVisualsAudioConfig.SLIME_BLOCK_PARTICLES));
		category.addEntry(LSConfigGuiHelper.booleanEntry(builder, "merge_sounds", LSVisualsAudioConfig.MERGE_SOUNDS));
		category.addEntry(LSConfigGuiHelper.booleanEntry(builder, "split_sounds", LSVisualsAudioConfig.SPLIT_SOUNDS));
	}

	private LSVisualsAudioConfigGui() {}
}
