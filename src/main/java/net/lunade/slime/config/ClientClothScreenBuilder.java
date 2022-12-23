package net.lunade.slime.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import net.minecraft.client.gui.screens.Screen;

public class ClientClothScreenBuilder {

    public static ConfigScreenFactory<Screen> buildScreen() {
        return LunaSlimesConfig::buildScreen;
    }

}
