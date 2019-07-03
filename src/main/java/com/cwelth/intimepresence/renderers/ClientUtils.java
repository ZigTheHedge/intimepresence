package com.cwelth.intimepresence.renderers;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class ClientUtils {

    public static class Color{
        public int red;
        public int green;
        public int blue;
        public int alpha;

        public Color(int RGBA)
        {
            alpha = RGBA >> 24 & 0xFF;
            red = RGBA >> 16 & 0xFF;
            green = RGBA >> 8 & 0xFF;
            blue = RGBA & 0xFF;
        }

        public static Color fromRGBA(int RGBA)
        {
            return new Color(RGBA);
        }
    }


    public static Color getRGBAComponents(int color)
    {
        return Color.fromRGBA(color);
    }

    public static void addTexturedQuad(BufferBuilder buffer, TextureAtlasSprite sprite, double x, double y, double width, double height, int color) {
        if (sprite == null) {
            return;
        }

        addTextureQuad(buffer, sprite, x, y, width, height, getRGBAComponents(color).red, getRGBAComponents(color).green, getRGBAComponents(color).blue, getRGBAComponents(color).alpha);
    }

    public static void addTextureQuad(BufferBuilder buffer, TextureAtlasSprite sprite, double x, double y, double width, double height, int red, int green, int blue, int alpha) {

        double minU;
        double maxU;
        double minV;
        double maxV;

        final double x2 = x + width;
        final double y2 = y + height;

        minU = sprite.getMinU();
        maxU = sprite.getMaxU();
        minV = sprite.getMinV();
        maxV = sprite.getMaxV();

        buffer.pos(x, y, 0).tex(minU, maxV).color(red, green, blue, alpha).endVertex();
        buffer.pos(x, y2, 0).tex(minU, minV).color(red, green, blue, alpha).endVertex();
        buffer.pos(x2, y2, 0).tex(maxU, minV).color(red, green, blue, alpha).endVertex();
        buffer.pos(x2, y, 0).tex(maxU, maxV).color(red, green, blue, alpha).endVertex();
    }
}
