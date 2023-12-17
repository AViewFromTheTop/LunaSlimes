package net.lunade.slime;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class LunaSlimesMain implements ModInitializer {

    public static final SoundEvent SLIME_MERGE = SoundEvent.createVariableRangeEvent(new ResourceLocation("lunaslimes", "entity.slime.merge"));
    public static final SoundEvent SLIME_SPLIT = SoundEvent.createVariableRangeEvent(new ResourceLocation("lunaslimes", "entity.slime.split"));
    public static final SoundEvent MAGMA_MERGE = SoundEvent.createVariableRangeEvent(new ResourceLocation("lunaslimes", "entity.magmacube.merge"));
    public static final SoundEvent MAGMA_SPLIT = SoundEvent.createVariableRangeEvent(new ResourceLocation("lunaslimes", "entity.magmacube.split"));
    public static boolean areConfigsInit;

    @Override
    public void onInitialize() {
        Registry.register(BuiltInRegistries.SOUND_EVENT, new ResourceLocation("lunaslimes", "entity.slime.merge"), SLIME_MERGE);
        Registry.register(BuiltInRegistries.SOUND_EVENT, new ResourceLocation("lunaslimes", "entity.slime.split"), SLIME_SPLIT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, new ResourceLocation("lunaslimes", "entity.magmacube.merge"), MAGMA_MERGE);
        Registry.register(BuiltInRegistries.SOUND_EVENT, new ResourceLocation("lunaslimes", "entity.magmacube.split"), MAGMA_SPLIT);
    }

    @Contract(pure = true)
    public static @NotNull Path configPath(String name, boolean json5) {
        return Path.of("./config/" + "lunaslimes" + "/" + name + "." + (json5 ? "json5" : "json"));
    }

}
