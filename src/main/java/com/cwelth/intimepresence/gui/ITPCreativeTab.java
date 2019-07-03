package com.cwelth.intimepresence.gui;

import com.cwelth.intimepresence.ModMain;
import com.cwelth.intimepresence.blocks.AllBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ITPCreativeTab extends CreativeTabs {


    public ITPCreativeTab() {
        super(ModMain.MODID);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(AllBlocks.steamHammer);
    }
}
