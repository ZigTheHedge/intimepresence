package com.cwelth.intimepresence.items;

import com.cwelth.intimepresence.ModMain;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CrudeIronPlate extends Item {
    public CrudeIronPlate()
    {
        setRegistryName("crudeironplate");
        setUnlocalizedName(ModMain.MODID + ".crudeironplate");
        setCreativeTab(ModMain.itpCreativeTab);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
