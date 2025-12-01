package net.lunade.slime.config.frozenlib;

import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.instance.json.JsonConfig;
import net.frozenblock.lib.config.api.instance.json.JsonType;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;
import net.frozenblock.lib.config.api.sync.SyncBehavior;
import net.frozenblock.lib.config.api.sync.annotation.EntrySyncData;
import net.lunade.slime.LunaSlimes;

public final class LunaSlimesVisualsAudioFrozenConfig {
	public static final Config<LunaSlimesVisualsAudioFrozenConfig> INSTANCE = ConfigRegistry.register(
		new JsonConfig<>(
			"lunaslimes",
			LunaSlimesVisualsAudioFrozenConfig.class,
			LunaSlimes.configPath("visuals_audio", true),
			JsonType.JSON
		) {
			@Override
			public void onSave() throws Exception {
				super.onSave();
				this.onSync(null);
			}

			@Override
			public void onSync(LunaSlimesVisualsAudioFrozenConfig syncInstance) {
				var config = this.config();
				GROW_ANIM = config.growAnim;
				WOBBLE_ANIM = config.wobbleAnim;
				SQUISH_MULTIPLIER = config.squishMultiplier;
				DEATH_ANIM = config.deathAnim;
				NEW_SHADOWS = config.newShadows;
				GLOWING_MAGMA_CUBE = config.glowingMagma;
			}
		}
	);

	public static volatile boolean GROW_ANIM;
	public static volatile boolean WOBBLE_ANIM;
	public static volatile int SQUISH_MULTIPLIER;
	public static volatile boolean DEATH_ANIM;
	public static volatile boolean NEW_SHADOWS;
	public static volatile boolean GLOWING_MAGMA_CUBE;

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

	@EntrySyncData(value = "glowingMagma", behavior = SyncBehavior.UNSYNCABLE)
	public boolean glowingMagma = true;

	@EntrySyncData("slimeBlockParticles")
	public boolean slimeBlockParticles = true;

	@EntrySyncData("mergeSounds")
	public boolean mergeSounds = true;

	@EntrySyncData("splitSounds")
	public boolean splitSounds = true;

	public static LunaSlimesVisualsAudioFrozenConfig get() {
		return get(false);
	}

	public static LunaSlimesVisualsAudioFrozenConfig get(boolean real) {
		if (real) return INSTANCE.instance();
		return INSTANCE.config();
	}

	public static LunaSlimesVisualsAudioFrozenConfig getWithSync() {
		return INSTANCE.configWithSync();
	}
}
