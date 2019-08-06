package com.cwelth.intimepresence.tileentities;

import com.cwelth.intimepresence.ModMain;
import com.cwelth.intimepresence.items.AllItems;
import com.cwelth.intimepresence.items.TimeBattery;
import com.cwelth.intimepresence.network.SyncTESRAnim;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class TimeMachineTE extends CommonTE implements ITickable {

    public int caseLevel = 0;
    public boolean isPowered = false;
    public ItemStack caseSlot = ItemStack.EMPTY;
    public boolean isOffline = true;
    public BlockPos attachedTE = null;

    public TimeMachineTE() {
        super(0);
    }

    @Override
    public void update() {
        if(!world.isRemote)
        {
            if(isPowered)
            {
                if(caseLevel < 34) {
                    caseLevel++;
                    markDirty();
                    sendUpdates();
                }
                if(!isOffline) {
                    isOffline = true;
                    markDirty();
                    sendUpdates();
                }
            } else
            {
                if(caseLevel > 0) {
                    caseLevel--;
                    markDirty();
                    sendUpdates();
                } else
                {
                    if(attachedTE != null && !caseSlot.isEmpty() && caseSlot.getItem() == AllItems.timeBattery)
                    {
                        ShardProcessorTE ate = (ShardProcessorTE)world.getTileEntity(attachedTE);
                        if(ate != null && ate.timeStored >= 10) {
                            NBTTagCompound nbt = caseSlot.getTagCompound();
                            nbt.setInteger("charge", nbt.getInteger("charge") + 10);
                            ate.timeStored -= 10;
                            ate.markDirty();
                            ate.sendUpdates();
                            //ate.getWorld().notifyBlockUpdate(attachedTE, world.getBlockState(attachedTE), world.getBlockState(attachedTE), 3);
                            if(isOffline) {
                                isOffline = false;
                                markDirty();
                                world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 2);
                            }
                        } else
                        {
                            if(!isOffline) {
                                isOffline = true;
                                markDirty();
                                world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 2);
                            }
                        }
                    } else
                    {
                        if(!isOffline) {
                            isOffline = true;
                            markDirty();
                            world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 2);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void updateTEFromPacket(int[] params) {
        caseLevel = params[0];
        isPowered = (params[1] == 1);
        isOffline = (params[2] == 1);
    }

    public void sendUpdates()
    {
        ModMain.network.sendToAllAround(new SyncTESRAnim(this, caseLevel, (isPowered)?1:0, (isOffline)?1:0),
                new NetworkRegistry.TargetPoint(world.provider.getDimension(),
                        (double)getPos().getX(),
                        (double)getPos().getY(),
                        (double)getPos().getZ(),
                        16D
                        ));
    }

    public void setActive(boolean isActive)
    {
        isPowered = isActive;
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
        markDirty();
    }

    public ItemStack useItem(ItemStack is)
    {
        if(caseSlot.isEmpty()) {
            caseSlot = is.copy();
            setActive(isPowered);
            return ItemStack.EMPTY;
        } else
        {
            ItemStack ret = caseSlot.copy();
            caseSlot = ItemStack.EMPTY;
            setActive(isPowered);
            return ret;
        }
    }

    public void setOffline(boolean isOffline)
    {
        this.isOffline = isOffline;
        setActive(isPowered);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if(compound.hasKey("caseLevel")) caseLevel = compound.getInteger("caseLevel");
        if(compound.hasKey("isPowered")) isPowered = compound.getBoolean("isPowered");
        if(compound.hasKey("caseSlot"))  caseSlot = new ItemStack((NBTTagCompound)compound.getTag("caseSlot"));
        if(compound.hasKey("isOffline")) isOffline = compound.getBoolean("isOffline");
        if(compound.hasKey("attachedTE")) attachedTE = NBTUtil.getPosFromTag((NBTTagCompound)compound.getTag("attachedTE"));
        else attachedTE = null;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);
        compound.setInteger("caseLevel", caseLevel);
        compound.setBoolean("isPowered", isPowered);
        compound.setTag("caseSlot", caseSlot.serializeNBT());
        compound.setBoolean("isOffline", isOffline);
        if(attachedTE != null)compound.setTag("attachedTE", NBTUtil.createPosTag(attachedTE));
        return compound;
    }
}
