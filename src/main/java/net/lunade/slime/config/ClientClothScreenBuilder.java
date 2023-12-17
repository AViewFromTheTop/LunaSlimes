package net.lunade.slime.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import net.lunade.slime.LunaSlimesSharedConstants;
import net.lunade.slime.config.frozenlib.gui.LunaSlimesFrozenConfigGui;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ClientClothScreenBuilder {

    @Contract(pure = true)
    public static @NotNull ConfigScreenFactory<Screen> buildScreen() {
        if (LunaSlimesSharedConstants.HAS_FROZENLIB) return LunaSlimesFrozenConfigGui::buildScreen;
        return LunaSlimesConfig::buildScreen;
    }

}
