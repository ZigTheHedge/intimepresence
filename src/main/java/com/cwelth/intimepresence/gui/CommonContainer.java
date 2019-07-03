package com.cwelth.intimepresence.gui;

import com.cwelth.intimepresence.tileentities.CommonTE;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public abstract class CommonContainer <TE extends CommonTE> extends Container {
    protected TE tileEntity;
    protected int ownSlotsCount = 0;

    public CommonContainer(IInventory playerInventory, TE te) {
        this.tileEntity = te;

        addOwnSlots();
        ownSlotsCount = super.inventorySlots.size();
        addPlayerSlots(playerInventory);
    }



    private void addPlayerSlots(IInventory playerInventory) {
        // Slots for the main inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 7 + col * 18;
                int y = row * 18 + 105;
                this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 10 - 1, x, y));
            }
        }

        // Slots for the hotbar
        for (int row = 0; row < 9; ++row) {
            int x = 7 + row * 18;
            int y = 58 + 105;
            this.addSlotToContainer(new Slot(playerInventory, row, x, y));
        }
    }

    protected abstract void addOwnSlots();

    @Nullable
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = null;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < ownSlotsCount) {
                if (!this.mergeItemStack(itemstack1, ownSlotsCount, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, ownSlotsCount, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return tileEntity.canInteractWith(playerIn);
    }

}
