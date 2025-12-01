package net.lunade.slime;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.slime.config.LunaSlimesClothScreenBuilder;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Contract;

@Environment(EnvType.CLIENT)
public final class LunaSlimesModMenu implements ModMenuApi {

	@Contract(pure = true)
	@Override
	public ConfigScreenFactory<Screen> getModConfigScreenFactory() {
		if (LunaSlimesConstants.HAS_CLOTH_CONFIG) return LunaSlimesClothScreenBuilder.buildScreen();
		return screen -> null;
	}

}
