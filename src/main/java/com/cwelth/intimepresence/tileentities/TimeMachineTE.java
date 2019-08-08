package com.cwelth.intimepresence.tileentities;

import com.cwelth.intimepresence.ModMain;
import com.cwelth.intimepresence.blocks.TimeMachine;
import com.cwelth.intimepresence.items.AllItems;
import com.cwelth.intimepresence.network.SyncTESRAnim;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class TimeMachineTE extends CommonTE implements ITickable {

    public int caseLevel = 0;
    public boolean isPowered = false;
    public ItemStack caseSlot = ItemStack.EMPTY;
    public boolean isOffline = true;
    public BlockPos attachedTE = null;
    public boolean isTimeBatteryPresent = false;
    public int tickDelay = 2;

    public TimeMachineTE() {
        super(0);
    }

    @Override
    public void update() {
        if(!world.isRemote)
        {
            if(tickDelay > 0)
            {
                tickDelay--;
                if(tickDelay == 0)
                {
                    tickDelay = 2;
                } else
                    return;
            }
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
                    world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
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
                        if(ate != null)
                        {
                            if(ate.timeStored > 0) {
                                int timeToShare = 10;
                                if (ate.timeStored < timeToShare) timeToShare = ate.timeStored;
                                NBTTagCompound nbt = caseSlot.getTagCompound();
                                nbt.setInteger("charge", nbt.getInteger("charge") + timeToShare);
                                ate.timeStored -= timeToShare;
                                ate.markDirty();
                                ate.sendUpdates();
                                if (isOffline) {
                                    isOffline = false;
                                    markDirty();
                                    sendUpdates();
                                    world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
                                }
                            } else
                            {
                                if(!isOffline) {
                                    isOffline = true;
                                    markDirty();
                                    sendUpdates();
                                    world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
                                }
                            }
                        } else
                        {
                            if(!isOffline) {
                                isOffline = true;
                                markDirty();
                                sendUpdates();
                                world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
                            }
                        }
                    } else
                    {
                        if(!isOffline) {
                            isOffline = true;
                            markDirty();
                            sendUpdates();
                            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
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
        //isOffline = (params[2] == 1);
        isTimeBatteryPresent = (params[3] == 1);
    }

    public void sendUpdates()
    {
        ModMain.network.sendToAllAround(new SyncTESRAnim(this, caseLevel, (isPowered)?1:0, (isOffline)?1:0, (isTimeBatteryPresent)?1:0),
                new NetworkRegistry.TargetPoint(world.provider.getDimension(),
                        (double)getPos().getX(),
                        (double)getPos().getY(),
                        (double)getPos().getZ(),
                        16D
                        ));
    }

    public void setActive(boolean isActive)
    {
        if(!world.isRemote) {
            isPowered = isActive;
            markDirty();
            sendUpdates();
        }
    }

    public ItemStack useItem(ItemStack is)
    {
        if(caseSlot.isEmpty()) {
            caseSlot = is.copy();
            isTimeBatteryPresent = true;
            setActive(isPowered);
            return ItemStack.EMPTY;
        } else
        {
            ItemStack ret = caseSlot.copy();
            caseSlot = ItemStack.EMPTY;
            isTimeBatteryPresent = false;
            setActive(isPowered);
            return ret;
        }
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
        if(compound.hasKey("isTimeBatteryPresent")) isTimeBatteryPresent = compound.getBoolean("isTimeBatteryPresent");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);
        compound.setInteger("caseLevel", caseLevel);
        compound.setBoolean("isPowered", isPowered);
        compound.setTag("caseSlot", caseSlot.serializeNBT());
        compound.setBoolean("isOffline", isOffline);
        if(attachedTE != null)compound.setTag("attachedTE", NBTUtil.createPosTag(attachedTE));
        compound.setBoolean("isTimeBatteryPresent", isTimeBatteryPresent);
        return compound;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }
}
