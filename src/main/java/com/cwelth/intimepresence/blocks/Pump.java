package com.cwelth.intimepresence.blocks;

import com.cwelth.intimepresence.ModMain;
import com.cwelth.intimepresence.renderers.PumpTESR;
import com.cwelth.intimepresence.renderers.SteamHammerTESR;
import com.cwelth.intimepresence.tileentities.CommonTEBlock;
import com.cwelth.intimepresence.tileentities.PumpTE;
import com.cwelth.intimepresence.tileentities.SteamHammerTE;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class Pump extends CommonTEBlock <PumpTE> {

    public static final IProperty<Boolean> IS_SCREW = PropertyBool.create("is_screw");

    public Pump() {
        super(Material.IRON, "pump");
        setCreativeTab(ModMain.itpCreativeTab);
        setHarvestLevel("pickaxe", 2);

        setHardness(.5F);
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(IS_SCREW, false));
    }

    @Override
    public Class<PumpTE> getTileEntityClass() {
        return PumpTE.class;
    }

    @Nullable
    @Override
    public PumpTE createTileEntity(World worldIn, IBlockState meta) {
        return new PumpTE();
    }

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
        ClientRegistry.bindTileEntitySpecialRenderer(PumpTE.class, new PumpTESR());
    }

    @SideOnly(Side.CLIENT)
    public void initItemModel() {

        Item itemBlock = Item.REGISTRY.getObject(new ResourceLocation(ModMain.MODID, "pump"));
        ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation(getRegistryName(), "inventory");
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemBlock, 0, itemModelResourceLocation);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, IS_SCREW);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.withProperty(IS_SCREW, false);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!worldIn.isRemote)
        {
            PumpTE te = (PumpTE)worldIn.getTileEntity(pos);
            if (worldIn.isBlockPowered(pos))
            {
                te.setActive(true);
            }
            else
            {
                te.setActive(false);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote) {
            PumpTE te = (PumpTE) worldIn.getTileEntity(pos);
            ItemStack heldItem = playerIn.getHeldItem(hand);
            if(!playerIn.isSneaking()) {
                if (heldItem.getItem() == Items.BUCKET) {
                    if (FluidUtil.interactWithFluidHandler(playerIn, hand, te.waterTank)) {
                        //playerIn.setHeldItem(hand, new ItemStack(Items.WATER_BUCKET));
                    }
                    return true;
                }
                return true;
            }
        }
        return true;

    }
}
