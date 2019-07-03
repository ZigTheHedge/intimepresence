package com.cwelth.intimepresence.gui;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class SidedItemHandler extends ItemStackHandler {

    private ItemStackHandler inputHandler;
    private ItemStackHandler outputHandler;
    private int inputSlots;

    public SidedItemHandler(ItemStackHandler inHandler, ItemStackHandler outHandler, int inputSlots)
    {
        super();
        inputHandler = inHandler;
        outputHandler = outHandler;
        this.inputSlots = inputSlots;
    }

    @Override
    public int getSlots() {
        return outputHandler.getSlots() + inputHandler.getSlots();
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if(slot < inputSlots)
            inputHandler.setStackInSlot(slot, stack);
        else
            outputHandler.setStackInSlot(slot-inputSlots, stack);
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        if(slot < inputSlots)
            return inputHandler.getStackInSlot(slot);
        else
            return outputHandler.getStackInSlot(slot-inputSlots);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if(slot < inputSlots)
            return inputHandler.insertItem(slot, stack, simulate);
        else
            return stack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if(slot < inputSlots)
            return ItemStack.EMPTY;
        else
            return outputHandler.extractItem(slot-inputSlots, amount, simulate);
    }
}
