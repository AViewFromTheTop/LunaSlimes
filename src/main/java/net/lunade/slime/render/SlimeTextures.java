package net.lunade.slime.render;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.slime.config.getter.ConfigValueGetter;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class SlimeTextures {
    private static final Map<Integer, ResourceLocation> SLIME_TEXTURES = new Int2ObjectArrayMap<>();
    private static final Map<Integer, ResourceLocation> MAGMA_CUBE_TEXTURES = new Int2ObjectArrayMap<>();
    private static final Map<Integer, ResourceLocation> MAGMA_CUBE_OVERLAY_TEXTURES = new Int2ObjectArrayMap<>();
    private static final Map<Integer, RenderType> MAGMA_CUBE_OVERLAY_RENDER_TYPES = new Int2ObjectArrayMap<>();

    public static void setup(int max) {
        for (int i = 1; i < max + 1; i++) {
            System.out.println(i);
            addEveryTexture(i);
        }
    }

    public static ResourceLocation getSlimeTexture(int size, ResourceLocation backUp) {
        if (ConfigValueGetter.scaleTextures()) return SLIME_TEXTURES.computeIfAbsent(size, (integer) -> backUp);
        return backUp;
    }

    public static ResourceLocation getMamaCubeTexture(int size, ResourceLocation backUp) {
        if (ConfigValueGetter.scaleTextures()) return MAGMA_CUBE_TEXTURES.computeIfAbsent(size, (integer) -> backUp);
        return backUp;
    }

    public static ResourceLocation getMagmaCubeOverlayTexture(int size, ResourceLocation backUp) {
        if (ConfigValueGetter.scaleTextures()) return MAGMA_CUBE_OVERLAY_TEXTURES.computeIfAbsent(size, (integer) -> backUp);
        return backUp;
    }

    public static RenderType getMagmaCubeOverlayRenderType(int size, RenderType backUp) {
        if (ConfigValueGetter.scaleTextures()) return MAGMA_CUBE_OVERLAY_RENDER_TYPES.computeIfAbsent(size, (integer) -> backUp);
        return backUp;
    }

    private static void addEveryTexture(int i) {
        addSlimeTexture(i);
        addMagmaCubeTexture(i);
        addMagmaCubeOverlayTexture(i);
        addMagmaCubeOverlayRenderType(i);
    }

    private static void addSlimeTexture(int i) {
        SLIME_TEXTURES.put(i, new ResourceLocation("lunaslimes", "textures/entity/slime/slime_" + i + ".png"));
    }

    private static void addMagmaCubeTexture(int i) {
        MAGMA_CUBE_TEXTURES.put(i, new ResourceLocation("lunaslimes", "textures/entity/slime/magmacube_" + i + ".png"));
    }

    private static void addMagmaCubeOverlayTexture(int i) {
        MAGMA_CUBE_OVERLAY_TEXTURES.put(i, new ResourceLocation("lunaslimes", "textures/entity/slime/magmacube_overlay_" + i + ".png"));
    }

    private static void addMagmaCubeOverlayRenderType(int i) {
        MAGMA_CUBE_OVERLAY_RENDER_TYPES.put(i, RenderType.eyes(new ResourceLocation("lunaslimes", "textures/entity/slime/magmacube_overlay_" + i + ".png")));
    }

}
