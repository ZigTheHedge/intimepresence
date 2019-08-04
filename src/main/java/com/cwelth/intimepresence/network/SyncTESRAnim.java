package com.cwelth.intimepresence.network;

import com.cwelth.intimepresence.tileentities.CommonTE;
import com.cwelth.intimepresence.tileentities.SteamHammerTE;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SyncTESRAnim implements IMessage {
    public BlockPos tePos;
    public int paramsCount = 0;
    public int[] params;

    public SyncTESRAnim(){}

    public SyncTESRAnim(CommonTE te, int ... params)
    {
        this.tePos = te.getPos();
        paramsCount = params.length;
        this.params = new int[paramsCount];
        for(int i = 0; i < paramsCount; i++)
            this.params[i] = params[i];
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        tePos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        paramsCount = buf.readInt();
        this.params = new int[paramsCount];
        for(int i = 0; i < paramsCount; i++)
            this.params[i] = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {

        buf.writeInt(tePos.getX());
        buf.writeInt(tePos.getY());
        buf.writeInt(tePos.getZ());

        buf.writeInt(paramsCount);
        for(int i = 0; i < paramsCount; i++)
            buf.writeInt(params[i]);

    }

    public static class Handler implements IMessageHandler<SyncSteamHammer, IMessage> {
        @Override
        public IMessage onMessage(SyncSteamHammer message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                Minecraft mc = Minecraft.getMinecraft();
                CommonTE te = (CommonTE)mc.world.getTileEntity(message.tePos);
                te.
            });
            return null;
        }
    }
}
