package net.lunade.slime.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.slime.LunaSlimesConstants;
import net.lunade.slime.config.frozenlib.gui.LunaSlimesFrozenConfigGui;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class LunaSlimesClothScreenBuilder {

    @Contract(pure = true)
    public static @NotNull ConfigScreenFactory<Screen> buildScreen() {
        if (LunaSlimesConstants.HAS_FROZENLIB) return LunaSlimesFrozenConfigGui::buildScreen;
        return LunaSlimesConfig::buildScreen;
    }

}
