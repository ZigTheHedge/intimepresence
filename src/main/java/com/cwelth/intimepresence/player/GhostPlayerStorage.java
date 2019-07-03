package com.cwelth.intimepresence.player;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class GhostPlayerStorage implements Capability.IStorage<IGhostPlayer> {

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<IGhostPlayer> capability, IGhostPlayer iBioPlayerExt, EnumFacing enumFacing) {
        return iBioPlayerExt.writeToNBT();
    }

    @Override
    public void readNBT(Capability<IGhostPlayer> capability, IGhostPlayer iBioPlayerExt, EnumFacing enumFacing, NBTBase nbtBase) {
        iBioPlayerExt.readFromNBT(nbtBase);
    }
}
