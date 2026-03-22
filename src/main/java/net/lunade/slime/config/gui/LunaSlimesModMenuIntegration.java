package net.lunade.slime.config.gui;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.lib.FrozenBools;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Contract;

@Environment(EnvType.CLIENT)
public final class LunaSlimesModMenuIntegration implements ModMenuApi {

	@Contract(pure = true)
	@Override
	public ConfigScreenFactory<Screen> getModConfigScreenFactory() {
		if (FrozenBools.HAS_CLOTH_CONFIG) return LunaSlimesConfigGui::buildScreen;
		return screen -> null;
	}

}
