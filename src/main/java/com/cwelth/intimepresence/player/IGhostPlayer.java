package com.cwelth.intimepresence.player;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public interface IGhostPlayer {
    void setPresenceTime(int newTime);
    void addPresenceTime(int addAmount);
    void tickPresence();
    int getPresenceTime();
    String getPresenceString(boolean includeTicks);
    NBTTagCompound writeToNBT();
    void readFromNBT(NBTBase nbtBase);
    void copyPlayer(IGhostPlayer source);
    void setHudInstalled(boolean value);
    boolean getHudInstalled();
    void setFirstSpawn(boolean newFirstSpawn);
    boolean isFirstSpawn();
}
