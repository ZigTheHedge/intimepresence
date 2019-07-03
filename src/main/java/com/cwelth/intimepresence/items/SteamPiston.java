package com.cwelth.intimepresence.items;

import com.cwelth.intimepresence.ModMain;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SteamPiston extends Item {
    public SteamPiston()
    {
        setRegistryName("piston");
        setUnlocalizedName(ModMain.MODID + ".piston");
        setCreativeTab(ModMain.itpCreativeTab);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

}
