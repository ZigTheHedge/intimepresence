package com.cwelth.intimepresence.network;

import com.cwelth.intimepresence.tileentities.CommonTE;
import com.cwelth.intimepresence.tileentities.ObsidianCauldronTE;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SyncObsidianCauldron implements IMessage {
    public BlockPos tePos;
    public ItemStack[] stacks = new ItemStack[3];

    public SyncObsidianCauldron(){}

    public SyncObsidianCauldron(ObsidianCauldronTE te)
    {
        this.tePos = te.getPos();
        for(int i = 0; i < 3; i++)
            stacks[i] = te.itemStackHandler.getStackInSlot(i);

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

    public static class Handler implements IMessageHandler<SyncObsidianCauldron, IMessage> {
        @Override
        public IMessage onMessage(SyncObsidianCauldron message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                Minecraft mc = Minecraft.getMinecraft();
                ObsidianCauldronTE te = (ObsidianCauldronTE)mc.world.getTileEntity(message.tePos);
                if(te != null) {
                    for(int i = 0; i < 3; i++)
                        te.itemStackHandler.setStackInSlot(i, message.stacks[i]);
                }
            });
            return null;
        }
    }
}
