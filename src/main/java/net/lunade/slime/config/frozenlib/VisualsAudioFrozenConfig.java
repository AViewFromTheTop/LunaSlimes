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
			LunaSlimesMain.configPath("visuals_audio", true),
			JsonType.JSON
		) {
			@Override
			public void onSave() throws Exception {
				super.onSave();
				this.onSync(null);
			}

			@Override
			public void onSync(VisualsAudioFrozenConfig syncInstance) {
				var config = this.config();
				GROW_ANIM = config.growAnim;
				WOBBLE_ANIM = config.wobbleAnim;
				SQUISH_MULTIPLIER = config.squishMultiplier;
				DEATH_ANIM = config.deathAnim;
				NEW_SHADOWS = config.newShadows;
				SCALE_TEXTURES = config.scaleTextures;
				GLOWING_MAGMA_CUBE = config.glowingMagma;
			}
		}
	);

	public static boolean GROW_ANIM;
	public static boolean WOBBLE_ANIM;
	public static int SQUISH_MULTIPLIER;
	public static boolean DEATH_ANIM;
	public static boolean NEW_SHADOWS;
	public static boolean SCALE_TEXTURES;
	public static boolean GLOWING_MAGMA_CUBE;

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
