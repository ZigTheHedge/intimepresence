package com.cwelth.intimepresence.gui;

import com.cwelth.intimepresence.items.AllItems;
import com.cwelth.intimepresence.tileentities.ShardProcessorTE;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public abstract class ShardProcessorContainer extends CommonContainer<ShardProcessorTE> {
    public ShardProcessorContainer(IInventory playerInventory, ShardProcessorTE te) {
        super(playerInventory, te);
    }

    @Nullable
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
//        super.transferStackInSlot(playerIn, index);
        ItemStack itemstack = null;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < ownSlotsCount) {
                if (!this.mergeItemStack(itemstack1, ownSlotsCount, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if(itemstack1.getItem() == Items.ENDER_PEARL)
                {
                    if(!this.mergeItemStack(itemstack1, 1, 2, false))
                        return ItemStack.EMPTY;
                } else if(itemstack1.getItem() == AllItems.dimensionalShards)
                {
                    if(!this.mergeItemStack(itemstack1, 2, 3, false))
                        return ItemStack.EMPTY;
                } else
                {
                    if(!this.mergeItemStack(itemstack1, 0, 1, false))
                        return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }
}
