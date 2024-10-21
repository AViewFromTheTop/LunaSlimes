package net.lunade.slime.config.frozenlib;

import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.instance.json.JsonConfig;
import net.frozenblock.lib.config.api.instance.json.JsonType;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;
import net.frozenblock.lib.config.api.sync.annotation.EntrySyncData;
import net.lunade.slime.LunaSlimes;

public final class LunaSlimesGameplayFrozenConfig {

	public static final Config<LunaSlimesGameplayFrozenConfig> INSTANCE = ConfigRegistry.register(
		new JsonConfig<>(
			"lunaslimes",
			LunaSlimesGameplayFrozenConfig.class,
			LunaSlimes.configPath("gameplay", true),
			JsonType.JSON,
			null,
			null
		)
	);

	@EntrySyncData("maxSize")
	public int maxSize = 4;

	@EntrySyncData("mergeCooldown")
	public int mergeCooldown = 0;

	@EntrySyncData("onSplitCooldown")
	public int onSplitCooldown = 100;

	@EntrySyncData("splitCooldown")
	public int splitCooldown = 0;

	@EntrySyncData("spawnedMergeCooldown")
	public int spawnedMergeCooldown = 0;

	@EntrySyncData("useSplitting")
	public boolean useSplitting = true;

	public static LunaSlimesGameplayFrozenConfig get() {
		return get(false);
	}

	public static LunaSlimesGameplayFrozenConfig get(boolean real) {
		if (real)
			return INSTANCE.instance();
		return INSTANCE.config();
	}

	public static LunaSlimesGameplayFrozenConfig getWithSync() {
		return INSTANCE.configWithSync();
	}
}
