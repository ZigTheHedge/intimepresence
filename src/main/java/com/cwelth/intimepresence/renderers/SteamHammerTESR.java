package com.cwelth.intimepresence.renderers;

import com.cwelth.intimepresence.blocks.AllBlocks;
import com.cwelth.intimepresence.blocks.SteamHammer;
import com.cwelth.intimepresence.tileentities.SteamHammerTE;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class SteamHammerTESR extends TileEntitySpecialRenderer {
    @Override
    public void render(TileEntity tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        SteamHammerTE te = (SteamHammerTE)tileEntity;

        if(te.getWorld().getBlockState(te.getPos()).getValue(SteamHammer.HALF) == SteamHammer.BlockPart.UPPER)return;

        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        // Translate to the location of our tile entity
        GlStateManager.translate(x, y, z);
        GlStateManager.disableRescaleNormal();

        renderPiston(te, partialTicks);


        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    private void renderPiston(SteamHammerTE te, float partialTicks) {
        GlStateManager.pushMatrix();

        RenderHelper.disableStandardItemLighting();
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        } else {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        World world = te.getWorld();
        GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY() , -te.getPos().getZ());

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

        IBlockState state = AllBlocks.steamHammer.getDefaultState().withProperty(SteamHammer.IS_PISTON, true);
        BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        IBakedModel model = dispatcher.getModelForState(state);

        double delta = (((te.currentPistonPosition + (partialTicks * te.delta)) - 30) * 16F / 100F) / 16F;
        if(te.delta == -1 || te.delta == 1)
            delta = (((te.currentPistonPosition) - 30) * 16F / 100F) / 16F;
        GlStateManager.translate(0, delta,0);
        dispatcher.getBlockModelRenderer().renderModel(world, model, state, te.getPos(), bufferBuilder, true);
        tessellator.draw();

        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }
}
