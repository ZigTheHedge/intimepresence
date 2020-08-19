package com.cwelth.intimepresence.network;

import com.cwelth.intimepresence.player.GhostPlayerProvider;
import com.cwelth.intimepresence.player.IGhostPlayer;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SyncAllCaps implements IMessage {
    public NBTTagCompound nbt;
    public int entId;

    public SyncAllCaps(){}

    public SyncAllCaps(NBTTagCompound tag, int eId){
        nbt = tag;
        entId = eId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entId = ByteBufUtils.readVarInt(buf, 5);
        nbt = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeVarInt(buf, entId, 5);
        ByteBufUtils.writeTag(buf, nbt);
    }

    public static class Handler implements IMessageHandler<SyncAllCaps, IMessage> {

        @Override
        public IMessage onMessage(SyncAllCaps message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                Minecraft mc = Minecraft.getMinecraft();
                if(mc.player != null){
                    World world = mc.player.world;
                    Entity ent = world.getEntityByID(message.entId);
                    if(ent instanceof EntityPlayer){
                        IGhostPlayer cap = ((EntityPlayer)ent).getCapability(GhostPlayerProvider.GHOST_PLAYER_CAPABILITY, null);
                        if(cap != null)
                            cap.readFromNBT(message.nbt);
                    }
                }
            });
            return null;
        }
    }
}
