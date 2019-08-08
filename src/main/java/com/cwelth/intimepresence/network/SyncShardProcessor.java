package com.cwelth.intimepresence.network;

import com.cwelth.intimepresence.tileentities.ObsidianCauldronTE;
import com.cwelth.intimepresence.tileentities.ShardProcessorTE;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SyncShardProcessor implements IMessage {
    public BlockPos tePos;
    public ItemStack[] stacks = new ItemStack[3];

    public SyncShardProcessor(){}

    public SyncShardProcessor(ShardProcessorTE te)
    {
        this.tePos = te.getPos();
        stacks[0] = te.itemStackHandler.getStackInSlot(0);
        stacks[1] = te.dimshardsSlot.getStackInSlot(0);
        stacks[2] = te.enderPearlSlot.getStackInSlot(0);

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        tePos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        for(int i = 0; i < 3; i++)
            this.stacks[i] = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {

        buf.writeInt(tePos.getX());
        buf.writeInt(tePos.getY());
        buf.writeInt(tePos.getZ());

        for(int i = 0; i < 3; i++)
            ByteBufUtils.writeItemStack(buf, stacks[i]);
    }

    public static class Handler implements IMessageHandler<SyncShardProcessor, IMessage> {
        @Override
        public IMessage onMessage(SyncShardProcessor message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                Minecraft mc = Minecraft.getMinecraft();
                ShardProcessorTE te = (ShardProcessorTE)mc.world.getTileEntity(message.tePos);
                if(te != null) {
                    te.itemStackHandler.setStackInSlot(0, message.stacks[0]);
                    te.dimshardsSlot.setStackInSlot(0, message.stacks[1]);
                    te.enderPearlSlot.setStackInSlot(0, message.stacks[2]);
                }
            });
            return null;
        }
    }
}
