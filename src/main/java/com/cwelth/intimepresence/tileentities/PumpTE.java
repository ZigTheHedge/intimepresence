package com.cwelth.intimepresence.tileentities;

import com.cwelth.intimepresence.fluids.AllFluids;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public class PumpTE extends CommonTE implements ITickable, ICapabilityProvider {

    public int workCycle = 20;
    public boolean isPowered = false;

    public FluidTank waterTank;


    public PumpTE() {
        super(0);
        waterTank = new FluidTank(10000) {
            @Override
            protected void onContentsChanged() {
                super.onContentsChanged();
                world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
                PumpTE.this.markDirty();
            }
        };
    }

    @Override
    public void update() {
        if(workCycle > 0 && isPowered) {
            workCycle--;
            if(workCycle == 0)
            {
                workCycle = 20;
                if(world.getBlockState(pos.down()).getBlock() == Blocks.WATER) {
                    if (waterTank.getFluidAmount() < waterTank.getCapacity() - 100) {
                        FluidStack water = new FluidStack(FluidRegistry.getFluid("water"), 100);

                        waterTank.fill(water, true);

                    }
                }
                TileEntity teNorth = world.getTileEntity(pos.north());
                TileEntity teEast = world.getTileEntity(pos.east());
                TileEntity teSouth = world.getTileEntity(pos.south());
                TileEntity teWest = world.getTileEntity(pos.west());
                TileEntity teUp = world.getTileEntity(pos.up());
                if(waterTank.getFluid() != null) {
                    FluidStack toFill = new FluidStack(waterTank.getFluid(), (waterTank.getFluidAmount() >= 100) ? 100 : waterTank.getFluidAmount());
                    if (teNorth != null && teNorth.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.SOUTH)) {
                        IFluidHandler fh = teNorth.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.SOUTH);
                        int amount = fh.fill(toFill, false);
                        if (amount == 100) {
                            fh.fill(toFill, true);
                            waterTank.drain(toFill.amount, true);
                        } else {
                            fh.fill(toFill, true);
                            waterTank.drain(amount, true);
                        }
                    } else if (teEast != null && teEast.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.WEST)) {
                        IFluidHandler fh = teEast.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.WEST);
                        int amount = fh.fill(toFill, false);
                        if (amount == 100) {
                            fh.fill(toFill, true);
                            waterTank.drain(toFill.amount, true);
                        } else {
                            fh.fill(toFill, true);
                            waterTank.drain(amount, true);
                        }
                    } else if (teSouth != null && teSouth.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.NORTH)) {
                        IFluidHandler fh = teSouth.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.NORTH);
                        int amount = fh.fill(toFill, false);
                        if (amount == 100) {
                            fh.fill(toFill, true);
                            waterTank.drain(toFill.amount, true);
                        } else {
                            fh.fill(toFill, true);
                            waterTank.drain(amount, true);
                        }
                    } else if (teWest != null && teWest.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.EAST)) {
                        IFluidHandler fh = teWest.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.EAST);
                        int amount = fh.fill(toFill, false);
                        if (amount == 100) {
                            fh.fill(toFill, true);
                            waterTank.drain(toFill.amount, true);
                        } else {
                            fh.fill(toFill, true);
                            waterTank.drain(amount, true);
                        }
                    } else if (teUp != null && teUp.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP)) {
                        IFluidHandler fh = teUp.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
                        int amount = fh.fill(toFill, false);
                        if (amount == 100) {
                            fh.fill(toFill, true);
                            waterTank.drain(toFill.amount, true);
                        } else {
                            fh.fill(toFill, true);
                            waterTank.drain(amount, true);
                        }
                    }
                }
            }
        }
    }

    public void setActive(boolean isActive)
    {
        isPowered = isActive;
        if(isPowered)
            workCycle = 20;
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
        markDirty();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return this.getCapability(capability, facing) != null;
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return (T) waterTank;

        } else
            return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if(compound.hasKey("workCycle"))
            workCycle = compound.getInteger("workCycle");
        if(compound.hasKey("isPowered"))
            isPowered = compound.getBoolean("isPowered");
        readFluidTankNBT("waterTank", waterTank, compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);
        compound.setInteger("workCycle", workCycle);
        compound.setBoolean("isPowered", isPowered);
        writeFluidTankToNBT("waterTank", waterTank, compound);
        return compound;
    }
}
