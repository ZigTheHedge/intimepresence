package com.cwelth.intimepresence.gui.obsidiancauldron;

import com.cwelth.intimepresence.gui.CommonContainer;
import com.cwelth.intimepresence.gui.steamhammer.SteamHammerSlot;
import com.cwelth.intimepresence.tileentities.ObsidianCauldronTE;
import com.cwelth.intimepresence.tileentities.SteamHammerTE;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.IItemHandler;

public class ObsidianCauldronServerContainer extends CommonContainer<ObsidianCauldronTE> {
    public ObsidianCauldronServerContainer(IInventory playerInventory, ObsidianCauldronTE te) {
        super(playerInventory, te);
    }

    @Override
    protected void addOwnSlots() {
        IItemHandler inputHandler = this.tileEntity.itemStackHandler;
        IItemHandler outputHandler = this.tileEntity.outputHandler;

        addSlotToContainer(new SteamHammerSlot(inputHandler, 0, 21, 16, false)); //Input
        addSlotToContainer(new SteamHammerSlot(inputHandler, 1, 39, 16, false)); //Input
        addSlotToContainer(new SteamHammerSlot(inputHandler, 2, 57, 16, false)); //Input
        addSlotToContainer(new SteamHammerSlot(outputHandler, 0, 133, 59, true)); //Output
    }
}
