package net.lunade.slime.client;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;

@Environment(EnvType.CLIENT)
public final class LunaSlimesRenderStateDataKeys {
	public static final RenderStateDataKey<Pair<Float, Float>> WOBBLE = RenderStateDataKey.create();
	public static final RenderStateDataKey<Float> SIZE = RenderStateDataKey.create();
	public static final RenderStateDataKey<Boolean> IN_WORLD = RenderStateDataKey.create();
}
