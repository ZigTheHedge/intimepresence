package com.cwelth.intimepresence.tileentities;

import com.cwelth.intimepresence.fluids.AllFluids;
import com.cwelth.intimepresence.gui.SidedItemHandler;
import com.cwelth.intimepresence.recipies.SelfRecipies;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class SteamHammerTE extends CommonTE implements ITickable, ICapabilityProvider {

    public int delta = -1;
    public int currentPistonPosition = 50;
    public int workCountdown = 0;
    public int temperature = 0;
    public int burnTime = 0;
    public int initialBurnTime = 0;

    public FluidTank waterTank;
    public FluidTank steamTank;

    public ItemStackHandler outputHandler;
    public ItemStackHandler bucketHandler;
    public ItemStackHandler fuelHandler;

    public ItemStack recipeResult = ItemStack.EMPTY;

    public SidedItemHandler horizontalItemHandler;
    public SidedItemHandler verticaalItemHandler;

    public SteamHammerTE() {
        super(1);
        waterTank = new FluidTank(10000) {
            @Override
            protected void onContentsChanged() {
                super.onContentsChanged();
                world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
                SteamHammerTE.this.markDirty();
            }
        };
        steamTank = new FluidTank(10000) {
            @Override
            protected void onContentsChanged() {
                super.onContentsChanged();
                world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
                SteamHammerTE.this.markDirty();
            }
        };

        outputHandler = new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
                SteamHammerTE.this.markDirty();
            }
        };

        bucketHandler = new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
                SteamHammerTE.this.markDirty();
            }
        };

        fuelHandler = new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
                SteamHammerTE.this.markDirty();
            }
        };

        horizontalItemHandler = new SidedItemHandler(itemStackHandler, outputHandler, 1);
        verticaalItemHandler = new SidedItemHandler(fuelHandler, outputHandler, 1);

    }

    public boolean checkValidRecipe()
    {
        if(itemStackHandler.getStackInSlot(0).isEmpty())
            return false;
        if(SelfRecipies.steamHammerRecepies.isItemValid(itemStackHandler.getStackInSlot(0)) && steamTank.getFluidAmount() >= SelfRecipies.steamHammerRecepies.getWorkCycles(itemStackHandler.getStackInSlot(0)) * 100)
        {
            if(outputHandler.getStackInSlot(0).isEmpty() || (outputHandler.getStackInSlot(0).getItem() == SelfRecipies.steamHammerRecepies.getOutput(itemStackHandler.getStackInSlot(0)).getItem() && outputHandler.getStackInSlot(0).getCount() + SelfRecipies.steamHammerRecepies.getOutput(itemStackHandler.getStackInSlot(0)).getCount() <= outputHandler.getStackInSlot(0).getMaxStackSize())) {
                workCountdown = SelfRecipies.steamHammerRecepies.getWorkCycles(itemStackHandler.getStackInSlot(0));
                recipeResult = SelfRecipies.steamHammerRecepies.getOutput(itemStackHandler.getStackInSlot(0)).copy();
                itemStackHandler.getStackInSlot(0).shrink(1);
                return true;
            } else
                return false;
        } else
            return false;
    }

    @Override
    public void update() {
        if(!world.isRemote)
        {
            if(!bucketHandler.getStackInSlot(0).isEmpty())
            {
                if(bucketHandler.getStackInSlot(0).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null))
                {
                    if(waterTank.getFluidAmount() <= 9000)
                    {
                        if(FluidUtil.getFluidContained(bucketHandler.getStackInSlot(0)) != null && FluidUtil.getFluidContained(bucketHandler.getStackInSlot(0)).getFluid() == FluidRegistry.WATER) {
                            FluidActionResult far = FluidUtil.tryEmptyContainer(bucketHandler.getStackInSlot(0), waterTank, waterTank.getCapacity() - waterTank.getFluidAmount(), null, true);
                            if(far.success)
                                bucketHandler.setStackInSlot(0, far.result);
                        }
                    }
                }
            }

            if(temperature > 100)
            {
                if(waterTank.getFluid() != null && FluidRegistry.getFluidName(waterTank.getFluid()) == "water" && waterTank.getFluidAmount() > 9)
                {
                    if(steamTank.getFluidAmount() < 10000)
                    {
                        waterTank.drain(10, true);
                        FluidStack steam = new FluidStack(AllFluids.fluidSteam, 60);

                        int amount = steamTank.fill(steam, true);
                    }
                }
            }

            if(burnTime == 0 && !fuelHandler.getStackInSlot(0).isEmpty())
            {
                if((initialBurnTime = burnTime = TileEntityFurnace.getItemBurnTime(fuelHandler.getStackInSlot(0))) > 0)
                {
                    fuelHandler.getStackInSlot(0).shrink(1);

                }
            }

            if(burnTime > 0)
            {
                burnTime -= 10;
                if(burnTime < 0)burnTime = 0;
                temperature++;
                if(temperature > 400)temperature = 400;
                //world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
                markDirty();
            } else
            {
                if(temperature > world.getBiome(pos).getTemperature(pos)){
                    temperature--;
                    //world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
                    markDirty();
                }
            }
        }

        if((workCountdown > 0 || currentPistonPosition < 50) && steamTank.getFluidAmount() >= 100 * workCountdown) {
            if (delta != 0) {
                if (delta < 0) {
                    delta *= 2;
                }
                currentPistonPosition += delta;
                if (currentPistonPosition < 0) {
                    currentPistonPosition = 0;
                    steamTank.drain(100, true);
                    world.playSound((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
                    workCountdown--;
                    if(!world.isRemote) {
                        /*
                        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
                        markDirty();

                         */
                        double x = (double) pos.getX() + 0.5D;
                        double y = (double) pos.getY() + 1.0D;
                        double z = (double) pos.getZ() + 0.5D;
                        ((WorldServer)world).spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 8, 0.25D + world.rand.nextDouble() / 2, 0, 0.25D + world.rand.nextDouble() / 2, 0.1D, 1);
                        ((WorldServer)world).spawnParticle(EnumParticleTypes.FLAME, x, y, z, 4, 0.25D + world.rand.nextDouble() / 2, 0, 0.25D + world.rand.nextDouble() / 2, 0.1D, 1);
                        if (workCountdown == 0) {
                            if(outputHandler.getStackInSlot(0).isEmpty())
                                outputHandler.setStackInSlot(0, recipeResult.copy());
                            else
                                outputHandler.getStackInSlot(0).grow(recipeResult.getCount());
                            recipeResult = ItemStack.EMPTY;
                        }
                    }
                    delta = 1;
                }
                if (currentPistonPosition > 50) {
                    currentPistonPosition = 50;
                    delta = -1;
                    /*
                    if(!world.isRemote) {
                        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
                        markDirty();
                    }

                     */
                }
            }
        } else
        {
            //Checking for valid recipe
            checkValidRecipe();
        }
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

        } else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if(facing == EnumFacing.UP || facing == EnumFacing.DOWN)
                return (T) verticaalItemHandler;
            else
                return (T) horizontalItemHandler;
        } else
            return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if(compound.hasKey("delta"))
            delta = compound.getInteger("delta");
        if(compound.hasKey("workCountdown"))
            workCountdown = compound.getInteger("workCountdown");
        if(compound.hasKey("currentPistonPosition"))
            currentPistonPosition = compound.getInteger("currentPistonPosition");
        if(compound.hasKey("temperature"))
            temperature = compound.getInteger("temperature");
        if(compound.hasKey("burnTime"))
            burnTime = compound.getInteger("burnTime");
        if(compound.hasKey("initialBurnTime"))
            initialBurnTime = compound.getInteger("initialBurnTime");
        readFluidTankNBT("watertank", waterTank, compound);
        readFluidTankNBT("steamtank", steamTank, compound);
        if (compound.hasKey("inputHandler")) itemStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("inputHandler"));
        if (compound.hasKey("outputHandler")) outputHandler.deserializeNBT((NBTTagCompound) compound.getTag("outputHandler"));
        if (compound.hasKey("bucketHandler")) bucketHandler.deserializeNBT((NBTTagCompound) compound.getTag("bucketHandler"));
        if (compound.hasKey("fuelHandler")) fuelHandler.deserializeNBT((NBTTagCompound) compound.getTag("fuelHandler"));
        if (compound.hasKey("recipeResult")) recipeResult = new ItemStack((NBTTagCompound) compound.getTag("recipeResult"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);
        compound.setInteger("delta", delta);
        compound.setInteger("workCountdown", workCountdown);
        compound.setInteger("currentPistonPosition", currentPistonPosition);
        compound.setInteger("temperature", temperature);
        compound.setInteger("burnTime", burnTime);
        compound.setInteger("initialBurnTime", initialBurnTime);
        writeFluidTankToNBT("watertank", waterTank, compound);
        writeFluidTankToNBT("steamtank", steamTank, compound);
        compound.setTag("inputHandler", itemStackHandler.serializeNBT());
        compound.setTag("outputHandler", outputHandler.serializeNBT());
        compound.setTag("bucketHandler", bucketHandler.serializeNBT());
        compound.setTag("fuelHandler", fuelHandler.serializeNBT());
        compound.setTag("recipeResult", recipeResult.serializeNBT());
        return compound;
    }


}
