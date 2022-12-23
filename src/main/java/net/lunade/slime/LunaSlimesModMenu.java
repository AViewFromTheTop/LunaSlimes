package net.lunade.slime;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.slime.config.ClientClothScreenBuilder;
import net.minecraft.client.gui.screens.Screen;

@Environment(EnvType.CLIENT)
public final class LunaSlimesModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<Screen> getModConfigScreenFactory() {
        if (LunaSlimesMain.HAS_CLOTH_CONFIG) {
            return ClientClothScreenBuilder.buildScreen();
        }
        return (screen -> null);
    }

}
