package com.cwelth.intimepresence.renderers;

import com.cwelth.intimepresence.blocks.AllBlocks;
import com.cwelth.intimepresence.blocks.CommonBlock;
import com.cwelth.intimepresence.blocks.TimeMachine;
import com.cwelth.intimepresence.items.AllItems;
import com.cwelth.intimepresence.tileentities.TimeMachineTE;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class TimeMachineTESR extends TileEntitySpecialRenderer {

    @Override
    public void render(TileEntity tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        TimeMachineTE te = (TimeMachineTE) tileEntity;
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        // Translate to the location of our tile entity
        GlStateManager.translate(x, y, z);
        GlStateManager.disableRescaleNormal();

        renderCase(te, partialTicks);
        renderTimeBattery(te, partialTicks);
        //renderScrew(te, partialTicks);
        //GlStateManager.translate(x, y, z);


        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    public void renderCase(TimeMachineTE te, float partialTicks)
    {
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

        IBlockState state = AllBlocks.timeMachine.getDefaultState().withProperty(TimeMachine.IS_CASE_RAISED, true);
        BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        IBakedModel model = dispatcher.getModelForState(state);

        float delta = -0.85F + ((te.caseLevel + partialTicks * ((te.isPowered && te.caseLevel < 34)? 1 : -1)) * 34F / 100F) / 20F;

        if(te.caseLevel == 0)
            GlStateManager.translate(0, -0.85F, 0);
        else if(te.caseLevel == 34)
            GlStateManager.translate(0, -0.28F, 0);
        else
            GlStateManager.translate(0, delta, 0);
        dispatcher.getBlockModelRenderer().renderModel(world, model, state, te.getPos(), bufferBuilder, true);
        tessellator.draw();

        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    void renderTimeBattery(TimeMachineTE te, float partialTicks)
    {
        if(!te.isTimeBatteryPresent)return;

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableLighting();


        if(te.getWorld().getBlockState(te.getPos()).getValue(CommonBlock.FACING) == EnumFacing.EAST ||
                te.getWorld().getBlockState(te.getPos()).getValue(CommonBlock.FACING) == EnumFacing.WEST) {
            GlStateManager.translate(0, 0, 1F);
            GlStateManager.rotate(90F, 0, 1,0);
        }


        GlStateManager.pushMatrix();
        GlStateManager.translate(.5F, 1.1F, .5F);
        float delta = -0.57F + ((te.caseLevel + partialTicks * ((te.isPowered && te.caseLevel < 34)? 1 : -1)) * 34F / 100F) / 20F;

        if(te.caseLevel == 0)
            GlStateManager.translate(0, -0.57F, 0);
        else if(te.caseLevel == 34)
            GlStateManager.translate(0, 0, 0);
        else
            GlStateManager.translate(0, delta, 0);




        Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(AllItems.timeBattery), ItemCameraTransforms.TransformType.GROUND);
        GlStateManager.popMatrix();

        GlStateManager.disableLighting();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

    }
}
