package net.lunade.slime;

import net.fabricmc.api.ClientModInitializer;
import net.lunade.slime.render.SlimeTextures;

public class LunaSlimesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SlimeTextures.setup(4);
    }

}
