package net.lunade.slime.client;

import com.mojang.datafixers.util.Pair;
import net.frozenblock.lib.renderer.RenderStateDataKey;
import net.lunade.slime.LSConstants;
import net.mehvahdjukaar.candlelight.api.ClientOnly;

@ClientOnly
public final class LSRenderStateDataKeys {
	public static final RenderStateDataKey<Pair<Float, Float>> WOBBLE = RenderStateDataKey.create(LSConstants.id("wobble"));
	public static final RenderStateDataKey<Float> SIZE = RenderStateDataKey.create(LSConstants.id("size"));
	public static final RenderStateDataKey<Boolean> IN_WORLD = RenderStateDataKey.create(LSConstants.id("in_world"));

	private LSRenderStateDataKeys() {}
}
