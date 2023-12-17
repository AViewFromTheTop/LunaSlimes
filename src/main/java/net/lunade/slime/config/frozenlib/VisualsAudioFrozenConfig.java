package net.lunade.slime.config.frozenlib;

import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.instance.json.JsonConfig;
import net.frozenblock.lib.config.api.instance.json.JsonType;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;
import net.frozenblock.lib.config.api.sync.SyncBehavior;
import net.frozenblock.lib.config.api.sync.annotation.EntrySyncData;
import net.lunade.slime.LunaSlimesMain;

public final class VisualsAudioFrozenConfig {

	public static final Config<VisualsAudioFrozenConfig> INSTANCE = ConfigRegistry.register(
		new JsonConfig<>(
			"lunaslimes",
			VisualsAudioFrozenConfig.class,
			LunaSlimesMain.configPath("visuals_audio", false),
			JsonType.JSON,
			null,
			null
		)
	);

	@EntrySyncData(value = "growAnim", behavior = SyncBehavior.UNSYNCABLE)
	public boolean growAnim = true;

	@EntrySyncData(value = "wobbleAnim", behavior = SyncBehavior.UNSYNCABLE)
	public boolean wobbleAnim = true;

	@EntrySyncData(value = "squishMultiplier", behavior = SyncBehavior.UNSYNCABLE)
	public int squishMultiplier = 20;

	@EntrySyncData(value = "jumpAntic", behavior = SyncBehavior.UNSYNCABLE)
	public boolean jumpAntic = true;

	@EntrySyncData(value = "deathAnim", behavior = SyncBehavior.UNSYNCABLE)
	public boolean deathAnim = true;

	@EntrySyncData(value = "newShadows", behavior = SyncBehavior.UNSYNCABLE)
	public boolean newShadows = true;

	@EntrySyncData("particles")
	public boolean particles = true;

	@EntrySyncData(value = "scaleTextures", behavior = SyncBehavior.UNSYNCABLE)
	public boolean scaleTextures = true;

	@EntrySyncData(value = "glowingMagma", behavior = SyncBehavior.UNSYNCABLE)
	public boolean glowingMagma = true;

	@EntrySyncData("slimeBlockParticles")
	public boolean slimeBlockParticles = true;

	@EntrySyncData("mergeSounds")
	public boolean mergeSounds = true;

	@EntrySyncData("splitSounds")
	public boolean splitSounds = true;

	public static VisualsAudioFrozenConfig get() {
		return get(false);
	}

	public static VisualsAudioFrozenConfig get(boolean real) {
		if (real)
			return INSTANCE.instance();
		return INSTANCE.config();
	}

	public static VisualsAudioFrozenConfig getWithSync() {
		return INSTANCE.configWithSync();
	}
}
