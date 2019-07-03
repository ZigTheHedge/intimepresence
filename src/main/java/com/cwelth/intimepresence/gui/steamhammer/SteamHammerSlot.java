package com.cwelth.intimepresence.gui.steamhammer;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SteamHammerSlot extends SlotItemHandler {
    boolean isOutput = false;

    public SteamHammerSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, boolean isOutput) {
        super(itemHandler, index, xPosition, yPosition);
        this.isOutput = isOutput;
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        if(isOutput)
            return false;
        else
            return true;
    }
}
