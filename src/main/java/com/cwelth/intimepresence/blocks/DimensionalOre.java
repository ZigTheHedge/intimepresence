package com.cwelth.intimepresence.blocks;

import com.cwelth.intimepresence.ModMain;
import com.cwelth.intimepresence.renderers.SteamHammerTESR;
import com.cwelth.intimepresence.tileentities.SteamHammerTE;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DimensionalOre extends SimpleBlock {

    public static final IProperty<EnumBlockType> TYPE = PropertyEnum.create("type", EnumBlockType.class);

    public DimensionalOre() {
        super(Material.ROCK, "dimensionalore");
        setCreativeTab(ModMain.itpCreativeTab);
        setHarvestLevel("pickaxe", 3);
        setHardness(10F);
        setDefaultState(blockState.getBaseState().withProperty(TYPE, EnumBlockType.GRAVEL));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        for(EnumBlockType type: EnumBlockType.values())
        {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), type.ordinal(), new ModelResourceLocation(getRegistryName(), "inventory."+type.ordinal()));
        }
    }

    @SideOnly(Side.CLIENT)
    public void initItemModel() {

    }


    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        int meta = 0;
        for(EnumBlockType type: EnumBlockType.values())
        {
            items.add(new ItemStack(this, 1, meta++));
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TYPE).ordinal();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(TYPE, EnumBlockType.values()[meta]);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {TYPE});
    }

    public enum EnumBlockType implements IStringSerializable {
        GRAVEL,
        NETHERRACK,
        ENDSTONE;

        public String toString()
        {
            return this.getName();
        }

        @Override
        public String getName() {
            if(this == GRAVEL)return "gravel";
            if(this == NETHERRACK)return "netherrack";
            if(this == ENDSTONE)return "endstone";
            return "";
        }
    }
}
