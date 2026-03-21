package net.lunade.slime.config.frozenlib;

import net.frozenblock.lib.config.v2.config.ConfigData;
import net.frozenblock.lib.config.v2.config.ConfigSettings;
import net.frozenblock.lib.config.v2.entry.ConfigEntry;
import net.frozenblock.lib.config.v2.entry.EntryType;
import net.frozenblock.lib.config.v2.registry.ID;
import net.frozenblock.lib.shadow.blue.endless.jankson.JsonElement;
import net.lunade.slime.LunaSlimesConstants;

public final class LunaSlimesVisualsAudioFrozenConfig {
	public static final ConfigData<JsonElement> CONFIG = ConfigData.createAndRegister(
		ID.of(LunaSlimesConstants.MOD_ID, "visuals_audio"),
		ConfigSettings.JSON
	);

	public static final ConfigEntry<Boolean> GROW_ANIM = CONFIG.unsyncableEntry("growAnim", EntryType.BOOL, true);
	public static final ConfigEntry<Boolean> WOBBLE_ANIM = CONFIG.unsyncableEntry("wobbleAnim", EntryType.BOOL, true);
	public static final ConfigEntry<Integer> SQUISH_MULTIPLIER = CONFIG.unsyncableEntry("squishMultiplier", EntryType.INT, 20);
	public static final ConfigEntry<Boolean> JUMP_ANTIC = CONFIG.unsyncableEntry("jumpAntic", EntryType.BOOL, true);
	public static final ConfigEntry<Boolean> DEATH_ANIM = CONFIG.unsyncableEntry("deathAnim", EntryType.BOOL, true);
	public static final ConfigEntry<Boolean> NEW_SHADOWS = CONFIG.unsyncableEntry("newShadows", EntryType.BOOL, true);
	public static final ConfigEntry<Boolean> PARTICLES = CONFIG.entry("particles", EntryType.BOOL, true);
	public static final ConfigEntry<Boolean> GLOWING_MAGMA = CONFIG.unsyncableEntry("glowingMagma", EntryType.BOOL, true);
	public static final ConfigEntry<Boolean> SLIME_BLOCK_PARTICLES = CONFIG.entry("slimeBlockParticles", EntryType.BOOL, true);
	public static final ConfigEntry<Boolean> MERGE_SOUNDS = CONFIG.entry("mergeSounds", EntryType.BOOL, true);
	public static final ConfigEntry<Boolean> SPLIT_SOUNDS = CONFIG.entry("splitSounds", EntryType.BOOL, true);
}
