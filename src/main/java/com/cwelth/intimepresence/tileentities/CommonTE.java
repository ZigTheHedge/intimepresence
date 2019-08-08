package com.cwelth.intimepresence.tileentities;

import com.cwelth.intimepresence.ModMain;
import com.cwelth.intimepresence.blocks.ObsidianCauldron;
import com.cwelth.intimepresence.network.SyncObsidianCauldron;
import com.cwelth.intimepresence.network.SyncShardProcessor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class CommonTE extends TileEntity {
    public static int INVSIZE;

    public ItemStackHandler itemStackHandler;

    public CommonTE(int invsize) {
        INVSIZE = invsize;

        itemStackHandler = new ItemStackHandler(INVSIZE) {
            @Override
            protected void onContentsChanged(int slot) {
                if(!CommonTE.this.world.isRemote) {
                    CommonTE.this.markDirty();
                    if (CommonTE.this instanceof ObsidianCauldronTE) {
                        ModMain.network.sendToAllAround(new SyncObsidianCauldron((ObsidianCauldronTE) CommonTE.this), new NetworkRegistry.TargetPoint(CommonTE.this.world.provider.getDimension(),
                                CommonTE.this.pos.getX(), CommonTE.this.pos.getY(), CommonTE.this.pos.getZ(), 64D));
                    }
                    if (CommonTE.this instanceof ShardProcessorTE) {
                        ModMain.network.sendToAllAround(new SyncShardProcessor((ShardProcessorTE) CommonTE.this), new NetworkRegistry.TargetPoint(CommonTE.this.world.provider.getDimension(),
                                CommonTE.this.pos.getX(), CommonTE.this.pos.getY(), CommonTE.this.pos.getZ(), 64D));
                    }
                }
            }
        };

    }

    public void updateTEFromPacket(int[] params)
    {

    }

    public boolean prepareGUIToBeOpened(boolean shouldOpen)
    {
        return true;
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) itemStackHandler;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        this.writeToNBT(nbtTag);
        return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        super.onDataPacket(net, packet);
        if(this instanceof TimeMachineTE)
            if(((TimeMachineTE)this).isOffline != packet.getNbtCompound().getBoolean("isOffline"))
                world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);

        this.readFromNBT(packet.getNbtCompound());
    }

    public NBTTagCompound writeFluidTankToNBT(String string, FluidTank tank, NBTTagCompound tag)
    {
        if(tank.getFluid() != null)
        {
            tag.setString(string + "_FluidName", FluidRegistry.getFluidName(tank.getFluid()));
            tag.setInteger(string + "_Amount", tank.getFluidAmount());

            if (tank.getFluid().tag != null)
            {
                tag.setTag(string + "_Tag", tank.getFluid().tag);
            }
        } else
        {
            tag.setString(string + "_Empty", "");
        }
        return tag;
    }

    public void readFluidTankNBT(String string, FluidTank tank, NBTTagCompound tag)
    {
        if(tag.hasKey(string + "_Empty"))
            tank.setFluid(null);
        else
        {
            if(tag.hasKey(string + "_Tag"))
                tank.setFluid(new FluidStack(FluidRegistry.getFluid(tag.getString(string + "_FluidName")), tag.getInteger(string + "_Amount"), tag.getCompoundTag(string + "_Tag")));
            else
                tank.setFluid(new FluidStack(FluidRegistry.getFluid(tag.getString(string + "_FluidName")), tag.getInteger(string + "_Amount")));
        }
    }

}
