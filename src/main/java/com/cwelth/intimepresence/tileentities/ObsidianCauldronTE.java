package com.cwelth.intimepresence.tileentities;

import com.cwelth.intimepresence.gui.SidedItemHandler;
import com.cwelth.intimepresence.recipies.ObsidianCauldronRecipe;
import com.cwelth.intimepresence.recipies.ObsidianCauldronRecipes;
import com.cwelth.intimepresence.recipies.SelfRecipies;
import javafx.geometry.Side;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ObsidianCauldronTE extends CommonTE implements ITickable, ICapabilityProvider {

    public int workCycle = 0;
    public int workCycleTotal = 0;

    public boolean isGUIopened = false;

    public ItemStackHandler outputHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
            ObsidianCauldronTE.this.markDirty();
        }
    };

    public ItemStack recipeResult = ItemStack.EMPTY;
    public int liquidAmount = 0;

    public SidedItemHandler capHandler = new SidedItemHandler(itemStackHandler, outputHandler, 3);

    public ObsidianCauldronTE() {
        super(3);

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
            return (T) capHandler;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if(compound.hasKey("workCycle"))
            workCycle = compound.getInteger("workCycle");
        if(compound.hasKey("workCycleTotal"))
            workCycleTotal = compound.getInteger("workCycleTotal");
        if (compound.hasKey("inputHandler")) itemStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("inputHandler"));
        if (compound.hasKey("outputHandler")) outputHandler.deserializeNBT((NBTTagCompound) compound.getTag("outputHandler"));
        if (compound.hasKey("recipeResult")) recipeResult = new ItemStack((NBTTagCompound) compound.getTag("recipeResult"));
        if(compound.hasKey("liquidAmount"))
            liquidAmount = compound.getInteger("liquidAmount");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);
        recalculateLiquidAmount();
        compound.setInteger("workCycle", workCycle);
        compound.setInteger("workCycleTotal", workCycleTotal);
        compound.setTag("inputHandler", itemStackHandler.serializeNBT());
        compound.setTag("outputHandler", outputHandler.serializeNBT());
        compound.setTag("recipeResult", recipeResult.serializeNBT());
        compound.setInteger("liquidAmount", liquidAmount);
        return compound;
    }

    public void recalculateLiquidAmount()
    {
        ItemStack[] checkStacks = new ItemStack[]{itemStackHandler.getStackInSlot(0), itemStackHandler.getStackInSlot(1), itemStackHandler.getStackInSlot(2)};
        liquidAmount = 0;
        for (int i = 0; i < 3; i++) {
            liquidAmount += checkStacks[i].getCount();
        }
        liquidAmount = ((int)(liquidAmount / 192F * 100));
    }


    public void checkValidRecipe()
    {
        ItemStack[] checkStacks = new ItemStack[]{itemStackHandler.getStackInSlot(0), itemStackHandler.getStackInSlot(1), itemStackHandler.getStackInSlot(2)};
        if(SelfRecipies.obsidianCauldronRecipes.isRecipeValid(checkStacks))
        {
            ObsidianCauldronRecipe foundRecipe = SelfRecipies.obsidianCauldronRecipes.getMatchedRecipe(checkStacks);
            if((outputHandler.getStackInSlot(0).isEmpty() || (outputHandler.getStackInSlot(0).isItemEqual(foundRecipe.out) && outputHandler.getStackInSlot(0).getCount() + foundRecipe.out.getCount() <= outputHandler.getStackInSlot(0).getMaxStackSize())) && isLavaUnderneath()) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if(foundRecipe.in[i] != null) {
                            if (foundRecipe.in[i].isItemEqual(itemStackHandler.getStackInSlot(j)) && itemStackHandler.getStackInSlot(j).getCount() >= foundRecipe.in[i].getCount()) {
                                itemStackHandler.getStackInSlot(j).shrink(foundRecipe.in[i].getCount());
                                break;
                            }
                        } else
                        {
                            if(foundRecipe.inDict[i].apply(itemStackHandler.getStackInSlot(j)) && itemStackHandler.getStackInSlot(j).getCount() >= foundRecipe.qty[i])
                            {
                                itemStackHandler.getStackInSlot(j).shrink(foundRecipe.qty[i]);
                                break;
                            }
                        }
                    }
                }
                recipeResult = foundRecipe.out.copy();
                workCycleTotal = workCycle = foundRecipe.workCycles;
                world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
                ObsidianCauldronTE.this.markDirty();
            }
        }
    }

    public boolean isLavaUnderneath()
    {
        return world.getBlockState(pos.down()).getBlock() == Blocks.LAVA;
    }

    @Override
    public void update() {
        if (workCycle > 0 && isLavaUnderneath()) {
            workCycle--;
            if(!world.isRemote) {
                if (workCycle == 0) {
                    if (outputHandler.getStackInSlot(0).isEmpty()) {
                        outputHandler.setStackInSlot(0, recipeResult.copy());
                        recipeResult = ItemStack.EMPTY;
                    } else {
                        if (outputHandler.getStackInSlot(0).isItemEqual(recipeResult)) {
                            outputHandler.getStackInSlot(0).grow(recipeResult.getCount());
                        }
                    }
                }
            }
        } else {
            if(!world.isRemote)checkValidRecipe();
        }
    }


    @Override
    public boolean prepareGUIToBeOpened(boolean shouldOpen) {
        if(shouldOpen) {
            if(isGUIopened) return false;
            isGUIopened = true;
            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
            return true;
        } else {
            isGUIopened = false;
            return true;
        }
    }

    public int getLiquidAmount()
    {

        return liquidAmount;
    }
}
