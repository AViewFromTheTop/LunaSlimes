package net.lunade.slime.config.gui;

import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.lunade.slime.config.frozenlib.LunaSlimesGameplayConfig;
import net.mehvahdjukaar.candlelight.api.ClientOnly;

@ClientOnly
public final class LunaSlimesGameplayConfigGui {

	private LunaSlimesGameplayConfigGui() {
		throw new UnsupportedOperationException("GameplayFrozenConfigGui contains only static declarations.");
	}

	public static void setupEntries(ConfigCategory category, ConfigEntryBuilder builder) {
		category.addEntry(LunaSlimesConfigGuiHelper.intSliderEntry(builder, "max_size", LunaSlimesGameplayConfig.MAX_SIZE, 1, 127));
		category.addEntry(LunaSlimesConfigGuiHelper.zeroToFiveHundredEntry(builder, "merge_cooldown", LunaSlimesGameplayConfig.MERGE_COOLDOWN));
		category.addEntry(LunaSlimesConfigGuiHelper.zeroToFiveHundredEntry(builder, "on_split_cooldown", LunaSlimesGameplayConfig.ON_SPLIT_COOLDOWN));
		category.addEntry(LunaSlimesConfigGuiHelper.zeroToFiveHundredEntry(builder, "split_cooldown", LunaSlimesGameplayConfig.SPLIT_COOLDOWN));
		category.addEntry(LunaSlimesConfigGuiHelper.zeroToFiveHundredEntry(builder, "spawned_merge_cooldown", LunaSlimesGameplayConfig.SPAWNED_MERGE_COOLDOWN));
		category.addEntry(LunaSlimesConfigGuiHelper.booleanEntry(builder, "use_splitting", LunaSlimesGameplayConfig.USE_SPLITTING));
	}

}
