package com.cwelth.intimepresence.gui;

import com.cwelth.intimepresence.items.AllItems;
import com.cwelth.intimepresence.tileentities.SteamHammerTE;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nullable;

public abstract class SteamHammerContainer extends CommonContainer<SteamHammerTE> {


    public SteamHammerContainer(IInventory playerInventory, SteamHammerTE te) {
        super(playerInventory, te);
    }

    @Nullable
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < ownSlotsCount) {
                if (!this.mergeItemStack(itemstack1, ownSlotsCount, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if(itemstack1.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null))
                {
                    if(!this.mergeItemStack(itemstack1, 2, 3, false))
                        return ItemStack.EMPTY;
                } else if(TileEntityFurnace.getItemBurnTime(itemstack1) > 0)
                {
                    if(!this.mergeItemStack(itemstack1, 3, 4, false))
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
