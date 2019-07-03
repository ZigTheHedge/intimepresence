package com.cwelth.intimepresence.renderers;

import com.cwelth.intimepresence.blocks.AllBlocks;
import com.cwelth.intimepresence.blocks.Pump;
import com.cwelth.intimepresence.tileentities.PumpTE;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class PumpTESR extends TileEntitySpecialRenderer<PumpTE> {

    @Override
    public void render(PumpTE tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        // Translate to the location of our tile entity
        GlStateManager.translate(x, y, z);
        GlStateManager.disableRescaleNormal();

        renderScrew(tileEntity, partialTicks);
        //GlStateManager.translate(x, y, z);
        renderGlass(tileEntity);

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    public void renderScrew(PumpTE te, float partialTicks)
    {
            GlStateManager.pushMatrix();

            RenderHelper.disableStandardItemLighting();
            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            if (Minecraft.isAmbientOcclusionEnabled()) {
                GlStateManager.shadeModel(GL11.GL_SMOOTH);
            } else {
                GlStateManager.shadeModel(GL11.GL_FLAT);
            }

            GlStateManager.translate(.5F, 0, .5F);
            long angle = (System.currentTimeMillis() / 10) % 360;
            if (te.isPowered) GlStateManager.rotate(-angle, 0, 1, 0);

            World world = te.getWorld();
            GlStateManager.translate(-te.getPos().getX() - .5F, -te.getPos().getY(), -te.getPos().getZ() - .5F);

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();
            bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

            IBlockState state = AllBlocks.pump.getDefaultState().withProperty(Pump.IS_SCREW, true);
            BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
            IBakedModel model = dispatcher.getModelForState(state);


            dispatcher.getBlockModelRenderer().renderModel(world, model, state, te.getPos(), bufferBuilder, true);
            tessellator.draw();

            RenderHelper.enableStandardItemLighting();
            GlStateManager.popMatrix();
    }

    public void renderGlass(PumpTE te)
    {
        GlStateManager.pushMatrix();

        RenderHelper.disableStandardItemLighting();
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        } else {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }
        //GlStateManager.translate(-te.getPos().getX() - .5F, -te.getPos().getY(), -te.getPos().getZ() - .5F);

        GlStateManager.translate(.5F, .5F, .5F);
        GlStateManager.scale(.9F, .9F, .9F);
        Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(Blocks.GLASS), ItemCameraTransforms.TransformType.NONE);

        GlStateManager.popMatrix();

        if(te.waterTank.getFluid() != null) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            Tessellator tess = Tessellator.getInstance();
            BufferBuilder buffer = tess.getBuffer();

            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

            TextureAtlasSprite still = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(te.waterTank.getFluid().getFluid().getStill().toString());
            TextureAtlasSprite flow = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(te.waterTank.getFluid().getFluid().getFlowing().toString());

            double posY = .1 + (.9 * ((float) te.waterTank.getFluidAmount() / (float) te.waterTank.getCapacity()));
            int color = te.waterTank.getFluid().getFluid().getColor();
            final int alpha = ClientUtils.getRGBAComponents(color).alpha;
            final int red = ClientUtils.getRGBAComponents(color).red;
            final int green = ClientUtils.getRGBAComponents(color).green;
            final int blue = ClientUtils.getRGBAComponents(color).blue;

            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            buffer.pos(1F / 16F, posY, 15F / 16F).tex(still.getInterpolatedU(1), still.getInterpolatedV(15)).color(red, green, blue, alpha).endVertex();
            buffer.pos(15F / 16F, posY, 15F / 16F).tex(still.getInterpolatedU(15), still.getInterpolatedV(15)).color(red, green, blue, alpha).endVertex();
            buffer.pos(15F / 16F, posY, 1F / 16F).tex(still.getInterpolatedU(15), still.getInterpolatedV(1)).color(red, green, blue, alpha).endVertex();
            buffer.pos(1F / 16F, posY, 1F / 16F).tex(still.getInterpolatedU(1), still.getInterpolatedV(1)).color(red, green, blue, alpha).endVertex();
            tess.draw();

            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            buffer.pos(15F / 16F, 1F / 16F, 15F / 16F).tex(flow.getInterpolatedU(15), flow.getInterpolatedV(15)).color(red, green, blue, alpha).endVertex();
            buffer.pos(15F / 16F, posY, 15F / 16F).tex(flow.getInterpolatedU(15), flow.getInterpolatedV(1)).color(red, green, blue, alpha).endVertex();
            buffer.pos(1F / 16F, posY, 15F / 16F).tex(flow.getInterpolatedU(1), flow.getInterpolatedV(1)).color(red, green, blue, alpha).endVertex();
            buffer.pos(1F / 16F, 1F / 16F, 15F / 16F).tex(flow.getInterpolatedU(1), flow.getInterpolatedV(15)).color(red, green, blue, alpha).endVertex();
            tess.draw();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            buffer.pos(1F / 16F, 1F / 16F, 1F / 16F).tex(flow.getInterpolatedU(1), flow.getInterpolatedV(15)).color(red, green, blue, alpha).endVertex();
            buffer.pos(1F / 16F, posY, 1F / 16F).tex(flow.getInterpolatedU(1), flow.getInterpolatedV(1)).color(red, green, blue, alpha).endVertex();
            buffer.pos(15F / 16F, posY, 1F / 16F).tex(flow.getInterpolatedU(15), flow.getInterpolatedV(1)).color(red, green, blue, alpha).endVertex();
            buffer.pos(15F / 16F, 1F / 16F, 1F / 16F).tex(flow.getInterpolatedU(15), flow.getInterpolatedV(15)).color(red, green, blue, alpha).endVertex();
            tess.draw();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            buffer.pos(15F / 16F, 1F / 16F, 1F / 16F).tex(flow.getInterpolatedU(1), flow.getInterpolatedV(15)).color(red, green, blue, alpha).endVertex();
            buffer.pos(15F / 16F, posY, 1F / 16F).tex(flow.getInterpolatedU(1), flow.getInterpolatedV(1)).color(red, green, blue, alpha).endVertex();
            buffer.pos(15F / 16F, posY, 15F / 16F).tex(flow.getInterpolatedU(15), flow.getInterpolatedV(1)).color(red, green, blue, alpha).endVertex();
            buffer.pos(15F / 16F, 1F / 16F, 15F / 16F).tex(flow.getInterpolatedU(15), flow.getInterpolatedV(15)).color(red, green, blue, alpha).endVertex();
            tess.draw();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            buffer.pos(1F / 16F, 1F / 16F, 15F / 16F).tex(flow.getInterpolatedU(15), flow.getInterpolatedV(15)).color(red, green, blue, alpha).endVertex();
            buffer.pos(1F / 16F, posY, 15F / 16F).tex(flow.getInterpolatedU(15), flow.getInterpolatedV(1)).color(red, green, blue, alpha).endVertex();
            buffer.pos(1F / 16F, posY, 1F / 16F).tex(flow.getInterpolatedU(1), flow.getInterpolatedV(1)).color(red, green, blue, alpha).endVertex();
            buffer.pos(1F / 16F, 1F / 16F, 1F / 16F).tex(flow.getInterpolatedU(1), flow.getInterpolatedV(15)).color(red, green, blue, alpha).endVertex();
            tess.draw();

            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

}
