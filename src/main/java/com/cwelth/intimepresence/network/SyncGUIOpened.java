package com.cwelth.intimepresence.network;

import com.cwelth.intimepresence.tileentities.CommonTE;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SyncGUIOpened implements IMessage {
    public BlockPos tePos;

    public SyncGUIOpened(){}

    public SyncGUIOpened(CommonTE te)
    {
        this.tePos = te.getPos();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        tePos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {

        buf.writeInt(tePos.getX());
        buf.writeInt(tePos.getY());
        buf.writeInt(tePos.getZ());
    }

    public static class Handler implements IMessageHandler<SyncGUIOpened, IMessage> {
        @Override
        public IMessage onMessage(SyncGUIOpened message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> {
                World world = player.world;
                CommonTE te = (CommonTE)world.getTileEntity(message.tePos);
                if(te != null)
                    te.prepareGUIToBeOpened(false);
            });
            return null;

        }
    }
}
