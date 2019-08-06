package com.cwelth.intimepresence.gui.obsidiancauldron;

import com.cwelth.intimepresence.ModMain;
import com.cwelth.intimepresence.gui.CommonContainer;
import com.cwelth.intimepresence.network.SyncGUIOpened;
import com.cwelth.intimepresence.tileentities.CommonTE;
import com.cwelth.intimepresence.tileentities.ObsidianCauldronTE;
import com.cwelth.intimepresence.tileentities.SteamHammerTE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ObsidianCauldronGUIContainer<TE extends CommonTE, CNT extends CommonContainer> extends GuiContainer {

    public static final int WIDTH = 174;
    public static final int HEIGHT = 186;
    private ObsidianCauldronTE te;
    private EntityPlayer player;

    private static ResourceLocation background;
    private static ResourceLocation tempBar;


    public ObsidianCauldronGUIContainer(TE tileEntity, CNT container, String bg, EntityPlayer player) {
        super(container);
        this.player = player;
        te = (ObsidianCauldronTE) tileEntity;
        background = new ResourceLocation(ModMain.MODID, bg);
        tempBar = new ResourceLocation(ModMain.MODID, "textures/gui/steamhammertemperature.png");
        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        drawProgress();
        if(!te.isLavaUnderneath())drawCenteredString(Minecraft.getMinecraft().fontRenderer, I18n.format("obsidiancauldron.nolava"),guiLeft + WIDTH/2, guiTop + 92, 0xFFFFFFFF);
    }

    public void drawProgress()
    {
        if(te.workCycle == 0 || te.workCycleTotal == 0)return;
        mc.getTextureManager().bindTexture(tempBar);
        int posX = te.workCycle * 16 / te.workCycleTotal;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        drawTexturedModalRect(guiLeft + 115, guiTop + 60, 0, 96, 16 - posX, 13);

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }


    @Override
    public void onGuiClosed() {
        ModMain.network.sendToServer(new SyncGUIOpened(te));
    }
}
