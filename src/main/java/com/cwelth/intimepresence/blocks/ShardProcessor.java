package com.cwelth.intimepresence.blocks;

import com.cwelth.intimepresence.ModMain;
import com.cwelth.intimepresence.gui.AllGUIs;
import com.cwelth.intimepresence.renderers.PumpTESR;
import com.cwelth.intimepresence.renderers.ShardProcessorTESR;
import com.cwelth.intimepresence.tileentities.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ShardProcessor extends CommonTEBlock<ShardProcessorTE> {

    public ShardProcessor() {
        super(Material.IRON, "shardprocessor");
        setCreativeTab(ModMain.itpCreativeTab);
        setHardness(.5F);
        setHarvestLevel("pickaxe", 2);

        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public Class<ShardProcessorTE> getTileEntityClass() {
        return ShardProcessorTE.class;
    }

    @Nullable
    @Override
    public ShardProcessorTE createTileEntity(World worldIn, IBlockState meta) {
        return new ShardProcessorTE();
    }

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
        ClientRegistry.bindTileEntitySpecialRenderer(ShardProcessorTE.class, new ShardProcessorTESR());
    }

    @SideOnly(Side.CLIENT)
    public void initItemModel() {

        Item itemBlock = Item.REGISTRY.getObject(new ResourceLocation(ModMain.MODID, "shardprocessor"));
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
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote) {
            if (!playerIn.isSneaking()) {
                playerIn.openGui(ModMain.instance, AllGUIs.ShardProcessorGUI, worldIn, pos.getX(), pos.getY(), pos.getZ());
                return true;
            }
        }
        return true;
    }

    private void attachTE(IBlockAccess worldIn, BlockPos pos)
    {
        ShardProcessorTE te = (ShardProcessorTE)worldIn.getTileEntity(pos);
        if(te != null)
        {
            TileEntity north = worldIn.getTileEntity(pos.north());
            TileEntity east = worldIn.getTileEntity(pos.east());
            TileEntity south = worldIn.getTileEntity(pos.south());
            TileEntity west = worldIn.getTileEntity(pos.west());
            if(north != null && north instanceof TimeMachineTE)
            {
                te.attachedTE = pos.north();
            } else if(east != null && east instanceof TimeMachineTE)
            {
                te.attachedTE = pos.east();
            } else if(south != null && south instanceof TimeMachineTE)
            {
                te.attachedTE = pos.south();
            } else if(west != null && west instanceof TimeMachineTE)
            {
                te.attachedTE = pos.west();
            } else
                te.attachedTE = null;
            te.markDirty();
            te.getWorld().notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 3);

        }

    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        if(!world.isRemote)
        {
            attachTE(world, pos);
        }
    }


    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if(!worldIn.isRemote)
        {
            attachTE(worldIn, pos);
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if(!worldIn.isRemote)
        {
            ShardProcessorTE te = (ShardProcessorTE)worldIn.getTileEntity(pos);

            InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), te.itemStackHandler.getStackInSlot(0));
            InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), te.enderPearlSlot.getStackInSlot(0));
            InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), te.dimshardsSlot.getStackInSlot(0));
        }

    }

}
