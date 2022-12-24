package net.lunade.slime;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class LunaSlimesMain implements ModInitializer {

	public static final boolean HAS_CLOTH_CONFIG = FabricLoader.getInstance().isModLoaded("cloth-config");
	public static boolean areConfigsInit;

	public static final SoundEvent SLIME_MERGE = new SoundEvent(new ResourceLocation("lunaslimes", "entity.slime.merge"));
	public static final SoundEvent SLIME_SPLIT = new SoundEvent(new ResourceLocation("lunaslimes", "entity.slime.split"));

	@Override
	public void onInitialize() {
		Registry.register(Registry.SOUND_EVENT, new ResourceLocation("lunaslimes", "entity.slime.merge"), SLIME_MERGE);
		Registry.register(Registry.SOUND_EVENT, new ResourceLocation("lunaslimes", "entity.slime.split"), SLIME_SPLIT);
	}

}
