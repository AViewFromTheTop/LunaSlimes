package net.lunade.slime.client;

import com.mojang.datafixers.util.Pair;
import net.frozenblock.lib.renderer.RenderStateDataKey;
import net.lunade.slime.LunaSlimesConstants;
import net.mehvahdjukaar.candlelight.api.ClientOnly;

@ClientOnly
public final class LunaSlimesRenderStateDataKeys {
	public static final RenderStateDataKey<Pair<Float, Float>> WOBBLE = RenderStateDataKey.create(
		LunaSlimesConstants.id("wobble")
	);
	public static final RenderStateDataKey<Float> SIZE = RenderStateDataKey.create(
		LunaSlimesConstants.id("size")
	);
	public static final RenderStateDataKey<Boolean> IN_WORLD = RenderStateDataKey.create(
		LunaSlimesConstants.id("in_world")
	);
}
