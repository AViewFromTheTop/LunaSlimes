package net.lunade.slime.config.frozenlib;

import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.instance.json.JsonConfig;
import net.frozenblock.lib.config.api.instance.json.JsonType;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;
import net.frozenblock.lib.config.api.sync.SyncBehavior;
import net.frozenblock.lib.config.v2.config.ConfigData;
import net.frozenblock.lib.config.v2.config.ConfigSettings;
import net.frozenblock.lib.config.v2.entry.ConfigEntry;
import net.frozenblock.lib.config.v2.entry.EntryType;
import net.frozenblock.lib.config.v2.registry.ID;
import net.frozenblock.lib.shadow.blue.endless.jankson.JsonElement;
import net.lunade.slime.LunaSlimes;
import net.lunade.slime.LunaSlimesConstants;

public final class LunaSlimesVisualsAudioFrozenConfig {
	public static final ConfigData<JsonElement> CONFIG = ConfigData.createAndRegister(
		ID.of(LunaSlimesConstants.MOD_ID, "visuals_audio"),
		ConfigSettings.JSON
	);

	public static final ConfigEntry<Boolean> growAnim = CONFIG.unsyncableEntry("growAnim", EntryType.BOOL, true);

	public static final ConfigEntry<Boolean> wobbleAnim = CONFIG.unsyncableEntry("wobbleAnim", EntryType.BOOL, true);

	public static final ConfigEntry<Integer> squishMultiplier = CONFIG.unsyncableEntry("squishMultiplier", EntryType.INT, 20);

	public static final ConfigEntry<Boolean> jumpAntic = CONFIG.unsyncableEntry("jumpAntic", EntryType.BOOL, true);

	public static final ConfigEntry<Boolean> deathAnim = CONFIG.unsyncableEntry("deathAnim", EntryType.BOOL, true);

	public static final ConfigEntry<Boolean> newShadows = CONFIG.unsyncableEntry("newShadows", EntryType.BOOL, true);

	public static final ConfigEntry<Boolean> particles = CONFIG.entry("particles", EntryType.BOOL, true);

	public static final ConfigEntry<Boolean> glowingMagma = CONFIG.unsyncableEntry("glowingMagma", EntryType.BOOL, true);

	public static final ConfigEntry<Boolean> slimeBlockParticles = CONFIG.entry("slimeBlockParticles", EntryType.BOOL, true);

	public static final ConfigEntry<Boolean> mergeSounds = CONFIG.entry("mergeSounds", EntryType.BOOL, true);

	public static final ConfigEntry<Boolean> splitSounds = CONFIG.entry("splitSounds", EntryType.BOOL, true);
}
