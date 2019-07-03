package com.cwelth.intimepresence.renderers;

import com.cwelth.intimepresence.blocks.AllBlocks;
import com.cwelth.intimepresence.blocks.Pump;
import com.cwelth.intimepresence.blocks.ShardProcessor;
import com.cwelth.intimepresence.tileentities.PumpTE;
import com.cwelth.intimepresence.tileentities.ShardProcessorTE;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class ShardProcessorTESR extends TileEntitySpecialRenderer<ShardProcessorTE> {

    @Override
    public void render(ShardProcessorTE tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        // Translate to the location of our tile entity
        GlStateManager.translate(x, y, z);
        GlStateManager.scale(.6F, .6F, .6F);
        GlStateManager.disableRescaleNormal();

        renderFire(tileEntity);
        renderItems(tileEntity);

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    public void renderFire(ShardProcessorTE te)
    {
        if(te.burnTime == 0)return;
        GlStateManager.pushMatrix();

        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.enableBlend();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.color(.5F, 1F, 0F, 1F);

        World world = te.getWorld();
        GlStateManager.translate(-te.getPos().getX()+.32F, -te.getPos().getY()+.1F, -te.getPos().getZ()+ .32F);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

        IBlockState state = Blocks.FIRE.getDefaultState();
        BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        IBakedModel model = dispatcher.getModelForState(state);


        dispatcher.getBlockModelRenderer().renderModel(world, model, state, te.getPos(), bufferBuilder, true);
        tessellator.draw();
        GlStateManager.popMatrix();
    }

    void renderItems(ShardProcessorTE te)
    {
        if(te.enderPearlSlot.getStackInSlot(0).isEmpty())return;
        GlStateManager.pushMatrix();

        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.enableBlend();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.color(.5F, 1F, 0F, 1F);

        GlStateManager.translate(.82F, .7F, .82F);
        long angle = (System.currentTimeMillis() / 10) % 360;
        GlStateManager.rotate(-angle, 0, 1, 0);

        Minecraft.getMinecraft().getRenderItem().renderItem(te.enderPearlSlot.getStackInSlot(0), ItemCameraTransforms.TransformType.GROUND);

        GlStateManager.popMatrix();
    }
}
