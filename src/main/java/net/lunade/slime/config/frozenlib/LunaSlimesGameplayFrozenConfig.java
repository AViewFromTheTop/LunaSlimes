package net.lunade.slime.config.frozenlib;

import net.frozenblock.lib.config.v2.config.ConfigData;
import net.frozenblock.lib.config.v2.config.ConfigSettings;
import net.frozenblock.lib.config.v2.entry.ConfigEntry;
import net.frozenblock.lib.config.v2.entry.EntryType;
import net.frozenblock.lib.config.v2.registry.ID;
import net.frozenblock.lib.shadow.blue.endless.jankson.JsonElement;
import net.lunade.slime.LunaSlimesConstants;

public final class LunaSlimesGameplayFrozenConfig {
	public static final ConfigData<JsonElement> CONFIG = ConfigData.createAndRegister(
		ID.of(LunaSlimesConstants.MOD_ID, "gameplay"),
		ConfigSettings.JSON
	);

	public static final ConfigEntry<Integer> MAX_SIZE = CONFIG.entry("maxSize", EntryType.INT, 4);
	public static final ConfigEntry<Integer> MERGE_COOLDOWN = CONFIG.entry("mergeCooldown", EntryType.INT, 0);
	public static final ConfigEntry<Integer> ON_SPLIT_COOLDOWN = CONFIG.entry("onSplitCooldown", EntryType.INT, 100);
	public static final ConfigEntry<Integer> SPLIT_COOLDOWN = CONFIG.entry("splitCooldown", EntryType.INT, 0);
	public static final ConfigEntry<Integer> SPAWNED_MERGE_COOLDOWN = CONFIG.entry("spawnedMergeCooldown", EntryType.INT, 0);
	public static final ConfigEntry<Boolean> USE_SPLITTING = CONFIG.entry("useSplitting", EntryType.BOOL, true);
}
