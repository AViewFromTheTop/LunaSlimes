package net.lunade.slime.config.gui;

import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.slime.config.frozenlib.LunaSlimesVisualsAudioConfig;

@Environment(EnvType.CLIENT)
public final class LunaSlimesVisualsAudioConfigGui {

	private LunaSlimesVisualsAudioConfigGui() {
		throw new UnsupportedOperationException("VisualsAudioFrozenConfigGui contains only static declarations.");
	}

	public static void setupEntries(ConfigCategory category, ConfigEntryBuilder builder) {
		category.addEntry(LunaSlimesConfigGuiHelper.booleanEntry(builder, "grow_anim", LunaSlimesVisualsAudioConfig.GROW_ANIM));
		category.addEntry(LunaSlimesConfigGuiHelper.booleanEntry(builder, "wobble_anim", LunaSlimesVisualsAudioConfig.WOBBLE_ANIM));
		category.addEntry(LunaSlimesConfigGuiHelper.intSliderEntry(builder, "squish_multiplier", LunaSlimesVisualsAudioConfig.SQUISH_MULTIPLIER, 0, 50));
		category.addEntry(LunaSlimesConfigGuiHelper.booleanEntry(builder, "jump_antic", LunaSlimesVisualsAudioConfig.JUMP_ANTIC));
		category.addEntry(LunaSlimesConfigGuiHelper.booleanEntry(builder, "death_anim", LunaSlimesVisualsAudioConfig.DEATH_ANIM));
		category.addEntry(LunaSlimesConfigGuiHelper.booleanEntry(builder, "new_shadows", LunaSlimesVisualsAudioConfig.NEW_SHADOWS));
		category.addEntry(LunaSlimesConfigGuiHelper.booleanEntry(builder, "particles", LunaSlimesVisualsAudioConfig.PARTICLES));
		category.addEntry(LunaSlimesConfigGuiHelper.booleanEntry(builder, "glowing_magma", LunaSlimesVisualsAudioConfig.GLOWING_MAGMA));
		category.addEntry(LunaSlimesConfigGuiHelper.booleanEntry(builder, "slime_block_particles", LunaSlimesVisualsAudioConfig.SLIME_BLOCK_PARTICLES));
		category.addEntry(LunaSlimesConfigGuiHelper.booleanEntry(builder, "merge_sounds", LunaSlimesVisualsAudioConfig.MERGE_SOUNDS));
		category.addEntry(LunaSlimesConfigGuiHelper.booleanEntry(builder, "split_sounds", LunaSlimesVisualsAudioConfig.SPLIT_SOUNDS));
	}

}
