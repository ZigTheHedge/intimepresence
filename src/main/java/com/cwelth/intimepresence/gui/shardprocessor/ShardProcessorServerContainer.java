package com.cwelth.intimepresence.gui.shardprocessor;

import com.cwelth.intimepresence.gui.CommonContainer;
import com.cwelth.intimepresence.gui.ShardProcessorContainer;
import com.cwelth.intimepresence.gui.steamhammer.SteamHammerSlot;
import com.cwelth.intimepresence.tileentities.ShardProcessorTE;
import net.minecraft.inventory.IInventory;

public class ShardProcessorServerContainer extends ShardProcessorContainer {


    public ShardProcessorServerContainer(IInventory playerInventory, ShardProcessorTE te) {
        super(playerInventory, te);
    }

    @Override
    protected void addOwnSlots() {
        addSlotToContainer(new SteamHammerSlot(tileEntity.itemStackHandler, 0, 61, 74, false)); //Input
        addSlotToContainer(new SteamHammerSlot(tileEntity.enderPearlSlot, 0, 79, 74, false)); //Input
        addSlotToContainer(new SteamHammerSlot(tileEntity.dimshardsSlot, 0, 97, 74, false)); //Input
    }
}
