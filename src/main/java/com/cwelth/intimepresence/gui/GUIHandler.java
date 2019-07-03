package com.cwelth.intimepresence.gui;

import com.cwelth.intimepresence.blocks.ObsidianCauldron;
import com.cwelth.intimepresence.gui.obsidiancauldron.ObsidianCauldronGUIContainer;
import com.cwelth.intimepresence.gui.obsidiancauldron.ObsidianCauldronServerContainer;
import com.cwelth.intimepresence.gui.shardprocessor.ShardProcessorGUIContainer;
import com.cwelth.intimepresence.gui.shardprocessor.ShardProcessorServerContainer;
import com.cwelth.intimepresence.gui.steamhammer.SteamHammerGUIContainer;
import com.cwelth.intimepresence.gui.steamhammer.SteamHammerServerContainer;
import com.cwelth.intimepresence.tileentities.ObsidianCauldronTE;
import com.cwelth.intimepresence.tileentities.ShardProcessorTE;
import com.cwelth.intimepresence.tileentities.SteamHammerTE;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class GUIHandler implements IGuiHandler {
    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        if(ID == AllGUIs.SteamHammerGUI) {
            SteamHammerTE te = (SteamHammerTE) world.getTileEntity(pos);
            return new SteamHammerServerContainer(player.inventory, te);
        } else if(ID == AllGUIs.ObsidianCauldronGUI) {
            ObsidianCauldronTE te = (ObsidianCauldronTE) world.getTileEntity(pos);
            return new ObsidianCauldronServerContainer(player.inventory, te);
        } else if(ID == AllGUIs.ShardProcessorGUI) {
            ShardProcessorTE te = (ShardProcessorTE) world.getTileEntity(pos);
            return new ShardProcessorServerContainer(player.inventory, te);
        } else
            return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        if(ID == AllGUIs.SteamHammerGUI) {
            SteamHammerTE te = (SteamHammerTE) world.getTileEntity(pos);
            return new SteamHammerGUIContainer(te, new SteamHammerServerContainer(player.inventory, te), "textures/gui/steamhammer.png", player);
        } else if(ID == AllGUIs.ObsidianCauldronGUI) {
            ObsidianCauldronTE te = (ObsidianCauldronTE) world.getTileEntity(pos);
            return new ObsidianCauldronGUIContainer(te, new ObsidianCauldronServerContainer(player.inventory, te), "textures/gui/obsidiancauldron.png", player);
        } else if(ID == AllGUIs.ShardProcessorGUI) {
            ShardProcessorTE te = (ShardProcessorTE) world.getTileEntity(pos);
            return new ShardProcessorGUIContainer(te, new ShardProcessorServerContainer(player.inventory, te), "textures/gui/shardprocessor.png", player);
        } else
            return null;
    }
}
