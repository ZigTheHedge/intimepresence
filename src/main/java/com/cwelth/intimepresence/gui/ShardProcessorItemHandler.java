package com.cwelth.intimepresence.gui;

import com.cwelth.intimepresence.items.AllItems;
import com.cwelth.intimepresence.tileentities.ShardProcessorTE;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class ShardProcessorItemHandler extends ItemStackHandler {
    ShardProcessorTE te = null;

    public ShardProcessorItemHandler(ShardProcessorTE te)
    {
        this.te = te;
    }


    @Override
    public int getSlots() {
        return 1;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if(stack.getItem() == Items.ENDER_PEARL)
            te.enderPearlSlot.setStackInSlot(0, stack);
        else if(stack.getItem() == AllItems.dimensionalShards)
            te.dimshardsSlot.setStackInSlot(0, stack);
        else
            te.itemStackHandler.setStackInSlot(0, stack);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if(stack.getItem() == Items.ENDER_PEARL)
            return te.enderPearlSlot.insertItem(slot, stack, simulate);
        else if(stack.getItem() == AllItems.dimensionalShards)
            return te.dimshardsSlot.insertItem(slot, stack, simulate);
        else
            return te.itemStackHandler.insertItem(slot, stack, simulate);
    }
}
