package com.cwelth.intimepresence.network;

import com.cwelth.intimepresence.tileentities.SteamHammerTE;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SyncSteamHammer implements IMessage {
    public BlockPos tePos;
    public int delta;
    public int currentPistonPosition;
    public int workCountdown;

    public SyncSteamHammer(){}

    public SyncSteamHammer(SteamHammerTE te)
    {
        this.tePos = te.getPos();
        delta = te.delta;
        currentPistonPosition = te.currentPistonPosition;
        workCountdown = te.workCountdown;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        tePos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        delta = buf.readInt();
        currentPistonPosition = buf.readInt();
        workCountdown = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {

        buf.writeInt(tePos.getX());
        buf.writeInt(tePos.getY());
        buf.writeInt(tePos.getZ());

        buf.writeInt(delta);
        buf.writeInt(currentPistonPosition);
        buf.writeInt(workCountdown);


    }

    public static class Handler implements IMessageHandler<SyncSteamHammer, IMessage> {
        @Override
        public IMessage onMessage(SyncSteamHammer message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                Minecraft mc = Minecraft.getMinecraft();
                SteamHammerTE te = (SteamHammerTE)mc.world.getTileEntity(message.tePos);
                te.delta = message.delta;
                te.currentPistonPosition = message.currentPistonPosition;
                te.workCountdown = message.workCountdown;

            });
            return null;
        }
    }
}
