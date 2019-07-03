package com.cwelth.intimepresence.player;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class GhostPlayerProvider implements ICapabilitySerializable<NBTBase> {
    @CapabilityInject(IGhostPlayer.class)
    public static final Capability<IGhostPlayer> GHOST_PLAYER_CAPABILITY = null;
    private IGhostPlayer instance = GHOST_PLAYER_CAPABILITY.getDefaultInstance();

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == GHOST_PLAYER_CAPABILITY;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return capability == GHOST_PLAYER_CAPABILITY ? GHOST_PLAYER_CAPABILITY.<T> cast(this.instance) : null;
    }

    @Override
    public NBTBase serializeNBT() {
        return GHOST_PLAYER_CAPABILITY.getStorage().writeNBT(GHOST_PLAYER_CAPABILITY, this.instance, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        GHOST_PLAYER_CAPABILITY.getStorage().readNBT(GHOST_PLAYER_CAPABILITY, this.instance, null, nbt);
    }

}
