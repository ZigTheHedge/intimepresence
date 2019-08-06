package com.cwelth.intimepresence.gui.steamhammer;

import com.cwelth.intimepresence.ModMain;
import com.cwelth.intimepresence.gui.CommonContainer;
import com.cwelth.intimepresence.network.SyncGUIOpened;
import com.cwelth.intimepresence.renderers.ClientUtils;
import com.cwelth.intimepresence.tileentities.CommonTE;
import com.cwelth.intimepresence.tileentities.SteamHammerTE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;


public class SteamHammerGUIContainer<TE extends CommonTE, CNT extends CommonContainer> extends GuiContainer {
    public static final int WIDTH = 174;
    public static final int HEIGHT = 186;
    private SteamHammerTE te;
    private EntityPlayer player;

    private static ResourceLocation background;
    private static ResourceLocation tempBar;

    public SteamHammerGUIContainer(TE tileEntity, CNT container, String bg, EntityPlayer player) {
        super(container);
        this.player = player;
        te = (SteamHammerTE) tileEntity;
        background = new ResourceLocation(ModMain.MODID, bg);
        tempBar = new ResourceLocation(ModMain.MODID, "textures/gui/steamhammertemperature.png");
        xSize = WIDTH;
        ySize = HEIGHT;

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawTooltip(25, 25, te.waterTank, mouseX, mouseY);
        drawTooltip(133, 25, te.steamTank, mouseX, mouseY);
        drawTooltip(153, 25, te.temperature, mouseX, mouseY);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        drawFluidContainer(25, 25, te.waterTank, mouseX, mouseY);
        drawFluidContainer(133, 25, te.steamTank, mouseX, mouseY);

        drawBurnTime(te.burnTime, te.initialBurnTime);
        drawTemperature(te.temperature);
    }

    protected void drawBurnTime(int burnTime, int maxTime)
    {
        if(maxTime == 0 || burnTime == 0)return;
        mc.getTextureManager().bindTexture(tempBar);
        int posY = burnTime * 16 / maxTime;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        drawTexturedModalRect(guiLeft + 133, guiTop + 83 + 16 - posY, 0, 93 - posY, 16, posY);

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    protected void drawTemperature(int temperature)
    {
        mc.getTextureManager().bindTexture(tempBar);
        int posY = temperature * 75 / 400;
        drawTexturedModalRect(guiLeft + 153, guiTop + 24 + 75 - posY, 0, 75 - posY, 7, posY);
    }

    protected void drawTooltip(int x, int y, FluidTank tank, int mouseX, int mouseY)
    {
        if (this.isPointInRegion(x, y, 16, 52, mouseX, mouseY)) {
            List<String> tooltip = new ArrayList<String>();
            if (tank.getFluid() != null) tooltip.add(tank.getFluid().getLocalizedName());
            else tooltip.add(I18n.format("tank.empty"));
            tooltip.add(tank.getFluidAmount() + " / " + tank.getCapacity() + "  mB");
            GuiUtils.drawHoveringText(tooltip, mouseX, mouseY, mc.displayWidth, mc.displayHeight, -1, mc.fontRenderer);
        }

    }

    protected void drawTooltip(int x, int y, int temperature, int mouseX, int mouseY)
    {
        if (this.isPointInRegion(x, y, 7, 75, mouseX, mouseY)) {
            List<String> tooltip = new ArrayList<String>();
            tooltip.add(I18n.format("temperature.name"));
            tooltip.add(temperature + " C");
            GuiUtils.drawHoveringText(tooltip, mouseX, mouseY, mc.displayWidth, mc.displayHeight, -1, mc.fontRenderer);
        }

    }

    protected void drawFluidContainer(int x, int y, FluidTank tank, int mouseX, int mouseY)
    {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        final Minecraft mc = Minecraft.getMinecraft();
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.pushMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        if (tank != null && tank.getFluidAmount() > 0) {
            final int color = tank.getFluid().getFluid().getColor();
            int i = tank.getFluidAmount() * 52 / tank.getCapacity();
            final TextureAtlasSprite still = mc.getTextureMapBlocks().getTextureExtry(tank.getFluid().getFluid().getStill().toString());

            ClientUtils.addTexturedQuad(buffer, still, guiLeft + x, guiTop + y + 52 - i, 16, i, color);
        }

        tessellator.draw();

        GlStateManager.disableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    @Override
    public void onGuiClosed() {
        ModMain.network.sendToServer(new SyncGUIOpened(te));
    }
}
