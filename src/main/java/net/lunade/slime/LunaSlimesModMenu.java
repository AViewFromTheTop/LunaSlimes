package net.lunade.slime;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.slime.config.ClientClothScreenBuilder;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public final class LunaSlimesModMenu implements ModMenuApi {

    @Contract(pure = true)
    @Override
    public @NotNull ConfigScreenFactory<Screen> getModConfigScreenFactory() {
        if (LunaSlimesSharedConstants.HAS_CLOTH_CONFIG) {
            return ClientClothScreenBuilder.buildScreen();
        }
        return (screen -> null);
    }

}
