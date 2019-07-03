package com.cwelth.intimepresence.network;

import com.cwelth.intimepresence.player.GhostPlayerProvider;
import com.cwelth.intimepresence.player.IGhostPlayer;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SyncTimer implements IMessage {
    public int timeLeft;

    public SyncTimer(){}

    public SyncTimer(int timeLeft)
    {
        this.timeLeft = timeLeft;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        timeLeft = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(timeLeft);
    }

    public static class Handler implements IMessageHandler<SyncTimer, IMessage> {
        @Override
        public IMessage onMessage(SyncTimer message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                Minecraft mc = Minecraft.getMinecraft();
                IGhostPlayer cap = mc.player.getCapability(GhostPlayerProvider.GHOST_PLAYER_CAPABILITY, null);
                if(cap != null)
                    cap.setPresenceTime(message.timeLeft);
            });
            return null;
        }
    }
}
