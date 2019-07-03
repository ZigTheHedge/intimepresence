package com.cwelth.intimepresence.player;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class GhostPlayer implements IGhostPlayer {
    private int presenceTime = 0;
    private boolean hudInstalled = false;
    private boolean isFirstSpawn = true;

    @Override
    public void setPresenceTime(int newTime)
    {
        presenceTime = newTime;
    }
    @Override
    public void addPresenceTime(int addAmount)
    {
        this.setPresenceTime(getPresenceTime() + addAmount);
    }
    @Override
    public int getPresenceTime()
    {
        return presenceTime;
    }

    @Override
    public void tickPresence() {
        if(presenceTime > 0)presenceTime--;
    }

    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        nbtTag.setInteger("presenceTime", presenceTime);
        nbtTag.setBoolean("hudInstalled", hudInstalled);
        nbtTag.setBoolean("isFirstSpawn", isFirstSpawn);
        return nbtTag;
    }

    @Override
    public void readFromNBT(NBTBase nbtBase) {
        NBTTagCompound nbtTag = (NBTTagCompound)nbtBase;
        presenceTime = nbtTag.getInteger("presenceTime");
        hudInstalled = nbtTag.getBoolean("hudInstalled");
        isFirstSpawn = nbtTag.getBoolean("isFirstSpawn");
    }

    @Override
    public void copyPlayer(IGhostPlayer source)
    {
        presenceTime = source.getPresenceTime();
        hudInstalled = source.getHudInstalled();
        isFirstSpawn = source.isFirstSpawn();
    }

    @Override
    public boolean isFirstSpawn() {
        return isFirstSpawn;
    }

    @Override
    public void setFirstSpawn(boolean newFirstSpawn) {
        isFirstSpawn = newFirstSpawn;
    }

    @Override
    public String getPresenceString(boolean includeTicks) {
        //60 * 60 * 20 = 72000 - 1 hour

        int timeLeft = presenceTime;
        //hours

        int hours = (int)(timeLeft / 72000);

        //minutes
        timeLeft = (timeLeft % 72000);
        int minutes = (int)(timeLeft / 1200);

        //seconds
        timeLeft = (timeLeft % 1200);
        int seconds = (int)(timeLeft / 20);

        if(includeTicks)
            return String.format("%02d:%02d:%02d:%02d", hours, minutes, seconds, (timeLeft % 20));
        else
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    public void setHudInstalled(boolean value) {
        hudInstalled = value;
    }

    @Override
    public boolean getHudInstalled() {
        return hudInstalled;
    }
}
