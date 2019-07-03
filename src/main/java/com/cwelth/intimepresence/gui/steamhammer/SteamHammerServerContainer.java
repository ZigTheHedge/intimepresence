package com.cwelth.intimepresence.gui.steamhammer;

import com.cwelth.intimepresence.gui.CommonContainer;
import com.cwelth.intimepresence.tileentities.SteamHammerTE;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.IItemHandler;

public class SteamHammerServerContainer extends CommonContainer<SteamHammerTE> {

    public SteamHammerServerContainer(IInventory playerInventory, SteamHammerTE te) {
        super(playerInventory, te);
    }

    @Override
    protected void addOwnSlots() {
        IItemHandler inputHandler = this.tileEntity.itemStackHandler;
        IItemHandler outputHandler = this.tileEntity.outputHandler;
        IItemHandler bucketHandler = this.tileEntity.bucketHandler;
        IItemHandler fuelHandler = this.tileEntity.fuelHandler;

        addSlotToContainer(new SteamHammerSlot(inputHandler, 0, 52, 25, false)); //Input
        addSlotToContainer(new SteamHammerSlot(outputHandler, 0, 106, 25, true)); //Output
        addSlotToContainer(new SteamHammerSlot(bucketHandler, 0, 25, 83,false)); //Water input
        addSlotToContainer(new SteamHammerSlot(fuelHandler, 0, 133, 83, false)); //Burning item
    }
}
