package net.lunade.slime;

import java.nio.file.Path;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.lunade.slime.config.LunaSlimesConfig;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class LunaSlimes implements ModInitializer {
	public static final SoundEvent SLIME_MERGE = SoundEvent.createVariableRangeEvent(ResourceLocation.tryBuild("lunaslimes", "entity.slime.merge"));
	public static final SoundEvent SLIME_SPLIT = SoundEvent.createVariableRangeEvent(ResourceLocation.tryBuild("lunaslimes", "entity.slime.split"));
	public static final SoundEvent MAGMA_MERGE = SoundEvent.createVariableRangeEvent(ResourceLocation.tryBuild("lunaslimes", "entity.magmacube.merge"));
	public static final SoundEvent MAGMA_SPLIT = SoundEvent.createVariableRangeEvent(ResourceLocation.tryBuild("lunaslimes", "entity.magmacube.split"));
	public static boolean areConfigsInit;

	@Override
	public void onInitialize() {
		if (FabricLoader.getInstance().isModLoaded("cloth-config") || FabricLoader.getInstance().isModLoaded("cloth_config")) LunaSlimesConfig.register();
		Registry.register(BuiltInRegistries.SOUND_EVENT, ResourceLocation.tryBuild("lunaslimes", "entity.slime.merge"), SLIME_MERGE);
		Registry.register(BuiltInRegistries.SOUND_EVENT, ResourceLocation.tryBuild("lunaslimes", "entity.slime.split"), SLIME_SPLIT);
		Registry.register(BuiltInRegistries.SOUND_EVENT, ResourceLocation.tryBuild("lunaslimes", "entity.magmacube.merge"), MAGMA_MERGE);
		Registry.register(BuiltInRegistries.SOUND_EVENT, ResourceLocation.tryBuild("lunaslimes", "entity.magmacube.split"), MAGMA_SPLIT);
	}

	@Contract(pure = true)
	public static @NotNull Path configPath(String name, boolean json5) {
		return Path.of("./config/" + "lunaslimes" + "/" + name + "." + (json5 ? "json5" : "json"));
	}

}
