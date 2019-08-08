package com.cwelth.intimepresence.renderers;

import com.cwelth.intimepresence.ModMain;
import com.cwelth.intimepresence.tileentities.ObsidianCauldronTE;
import com.cwelth.intimepresence.tileentities.PumpTE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.ItemStackHandler;
import org.lwjgl.opengl.GL11;

public class ObsidianCauldronTESR extends TileEntitySpecialRenderer {

    @Override
    public void render(TileEntity tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        ObsidianCauldronTE te = (ObsidianCauldronTE) tileEntity;
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        // Translate to the location of our tile entity
        GlStateManager.translate(x, y, z);
        GlStateManager.disableRescaleNormal();

        if(te.getLiquidAmount() > 0 && te.isLavaUnderneath()){
            renderLiquid(te);
        }
        renderRecipe(te.itemStackHandler);

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    public void renderLiquid(ObsidianCauldronTE te)
    {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderHelper.disableStandardItemLighting();

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buffer = tess.getBuffer();

        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        TextureAtlasSprite still = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/lava_still");
        //TextureAtlasSprite still = new ResourceLocation(ModMain.MODID, "textures/fluids/steam_still.png");

        double posY = .2 + (.7 * ((float) te.getLiquidAmount() / 100F));

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(1F / 16F, posY, 15F / 16F).tex(still.getInterpolatedU(1), still.getInterpolatedV(15)).endVertex();
        buffer.pos(15F / 16F, posY, 15F / 16F).tex(still.getInterpolatedU(15), still.getInterpolatedV(15)).endVertex();
        buffer.pos(15F / 16F, posY, 1F / 16F).tex(still.getInterpolatedU(15), still.getInterpolatedV(1)).endVertex();
        buffer.pos(1F / 16F, posY, 1F / 16F).tex(still.getInterpolatedU(1), still.getInterpolatedV(1)).endVertex();
        tess.draw();

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public void renderRecipe(ItemStackHandler in)
    {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        //RenderHelper.enableStandardItemLighting();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableLighting();

        for(int i = 0; i < 3; i++)
        {
            if(!in.getStackInSlot(i).isEmpty())
            {
                GlStateManager.pushMatrix();
                GlStateManager.translate(.5F, .2F, .3F + i*.2F);
                //GlStateManager.scale(.6f, .6f, .6f);

                Minecraft.getMinecraft().getRenderItem().renderItem(in.getStackInSlot(i), ItemCameraTransforms.TransformType.GROUND);
                GlStateManager.popMatrix();
            }
        }

        GlStateManager.disableLighting();
        //RenderHelper.disableStandardItemLighting();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}
