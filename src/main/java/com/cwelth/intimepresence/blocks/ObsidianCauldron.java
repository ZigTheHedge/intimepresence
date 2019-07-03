package com.cwelth.intimepresence.blocks;

import com.cwelth.intimepresence.ModMain;
import com.cwelth.intimepresence.gui.AllGUIs;
import com.cwelth.intimepresence.renderers.ObsidianCauldronTESR;
import com.cwelth.intimepresence.renderers.PumpTESR;
import com.cwelth.intimepresence.tileentities.CommonTEBlock;
import com.cwelth.intimepresence.tileentities.ObsidianCauldronTE;
import com.cwelth.intimepresence.tileentities.PumpTE;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ObsidianCauldron extends CommonTEBlock<ObsidianCauldronTE> {

    AxisAlignedBB aabb = new AxisAlignedBB(0, 0, 0, 1, 0.8, 1);

    public ObsidianCauldron() {
        super(Material.IRON, "obsidiancauldron");
        setCreativeTab(ModMain.itpCreativeTab);
        setHardness(.5F);
        setHarvestLevel("pickaxe", 2);
        setLightLevel(0.5F);
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public Class<ObsidianCauldronTE> getTileEntityClass() {
        return ObsidianCauldronTE.class;
    }

    @Nullable
    @Override
    public ObsidianCauldronTE createTileEntity(World worldIn, IBlockState meta) {
        return new ObsidianCauldronTE();
    }

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
        ClientRegistry.bindTileEntitySpecialRenderer(ObsidianCauldronTE.class, new ObsidianCauldronTESR());
    }

    @SideOnly(Side.CLIENT)
    public void initItemModel() {
        Item itemBlock = Item.REGISTRY.getObject(new ResourceLocation(ModMain.MODID, "obsidiancauldron"));
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
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, aabb);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return aabb;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote) {
            if (!playerIn.isSneaking()) {
                playerIn.openGui(ModMain.instance, AllGUIs.ObsidianCauldronGUI, worldIn, pos.getX(), pos.getY(), pos.getZ());
                return true;
            }
        }
        return true;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        //TODO: Fix lighting
        if(!worldIn.isRemote) {
            ObsidianCauldronTE te = (ObsidianCauldronTE) worldIn.getTileEntity(pos);
            if (te != null) {
                if(te.getLiquidAmount() > 0)setLightLevel(.8F);
                else setLightLevel(0);
            }
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if(!worldIn.isRemote)
        {
            ObsidianCauldronTE te = (ObsidianCauldronTE)worldIn.getTileEntity(pos);

            InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), te.itemStackHandler.getStackInSlot(0));
            InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), te.itemStackHandler.getStackInSlot(1));
            InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), te.itemStackHandler.getStackInSlot(2));

            InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), te.outputHandler.getStackInSlot(0));
        }

    }
}
