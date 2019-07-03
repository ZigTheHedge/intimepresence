package com.cwelth.intimepresence.gui;

import com.cwelth.intimepresence.ModMain;
import com.cwelth.intimepresence.player.GhostPlayerProvider;
import com.cwelth.intimepresence.player.IGhostPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;

public class ClientPresenceTimeRenderer extends Gui {
    public static final ResourceLocation DIGITAL_TEXTURE = new ResourceLocation(ModMain.MODID,"textures/gui/digital.png");

    Minecraft thisMc;


    public ClientPresenceTimeRenderer(Minecraft mc)
    {
        thisMc = mc;
        ScaledResolution scaledResolution = new ScaledResolution(mc);

        IGhostPlayer player = mc.player.getCapability(GhostPlayerProvider.GHOST_PLAYER_CAPABILITY, null);

        if(player.getHudInstalled()) {
            int presenceTime = player.getPresenceTime();

            if (presenceTime < 36000 && presenceTime >= 12000) GlStateManager.color(1F, 1F, 0F, 1F);
            else if (presenceTime < 12000) GlStateManager.color(1F, 0F, 0F, 1F);
            else GlStateManager.color(0F, 1F, 0F, 1F);

            if(mc.player.getHeldItem(EnumHand.OFF_HAND).isEmpty())
                drawDigitalString(player.getPresenceString(true), scaledResolution);
            else
                drawDigitalString(player.getPresenceString(false), scaledResolution);
        }
    }

    public void drawDigitalString(String In, ScaledResolution scaledResolution)
    {
        GlStateManager.enableAlpha();
        thisMc.getTextureManager().bindTexture(DIGITAL_TEXTURE);

        int width = scaledResolution.getScaledWidth();
        int height = scaledResolution.getScaledHeight();

        int curX = width / 2 - 120 - 82;

        for(int i=0; i < In.length(); i++)
        {
            if(In.charAt(i) >= '0' && In.charAt(i) <= '9')
                this.drawTexturedModalRect(curX + 9*i, height - 20, (In.charAt(i) - '0')*8, 0, 8, 16);
            else
                this.drawTexturedModalRect(curX + 9*i, height - 20, 80, 0, 8, 16);
        }
        GlStateManager.disableAlpha();
    }

}
