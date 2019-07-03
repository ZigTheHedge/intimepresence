package com.cwelth.intimepresence.blocks;

import com.cwelth.intimepresence.ModMain;
import com.cwelth.intimepresence.fluids.AllFluids;
import com.cwelth.intimepresence.fluids.FluidMaterials;
import com.cwelth.intimepresence.renderers.SteamHammerTESR;
import com.cwelth.intimepresence.tileentities.SteamHammerTE;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SteamFluidBlock extends BlockFluidFinite {
    public SteamFluidBlock() {
        super(AllFluids.fluidSteam, FluidMaterials.materialSteam);
        setRegistryName("steamfluidblock");
        setUnlocalizedName(ModMain.MODID + ".steamfluidblock");
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
        ClientRegistry.bindTileEntitySpecialRenderer(SteamHammerTE.class, new SteamHammerTESR());
    }

    @SideOnly(Side.CLIENT)
    public void initItemModel() {

        Item itemBlock = Item.REGISTRY.getObject(new ResourceLocation(ModMain.MODID, "steamfluidblock"));
        ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation(getRegistryName(), "inventory");
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemBlock, 0, itemModelResourceLocation);
    }



}
