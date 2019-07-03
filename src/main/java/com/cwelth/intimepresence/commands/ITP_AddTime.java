package com.cwelth.intimepresence.commands;

import com.cwelth.intimepresence.ModMain;
import com.cwelth.intimepresence.network.SyncAllCaps;
import com.cwelth.intimepresence.player.GhostPlayerProvider;
import com.cwelth.intimepresence.player.IGhostPlayer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class ITP_AddTime extends CommandBase {
    private String usage = "itp_addtime [player] ticks_to_add";

    @Override
    public String getName() {
        return "itp_addtime";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return usage;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length == 0 || args.length > 2) throw new WrongUsageException(usage, new Object[0]);
        if(args.length == 1)
        {
            if(sender instanceof EntityPlayerMP)
            {
                EntityPlayerMP entityPlayer = ((EntityPlayerMP)sender);
                IGhostPlayer player = entityPlayer.getCapability(GhostPlayerProvider.GHOST_PLAYER_CAPABILITY, null);
                player.setPresenceTime(player.getPresenceTime() + Integer.valueOf(args[0]));
                ModMain.network.sendTo(new SyncAllCaps(player.writeToNBT(), entityPlayer.getEntityId()), entityPlayer);
            } else
                throw new WrongUsageException("Sender is not a player", new Object[0]);
        } else {
            EntityPlayerMP entityPlayer = getPlayer(server, sender, args[0]);
            if(entityPlayer != null)
            {
                IGhostPlayer player = entityPlayer.getCapability(GhostPlayerProvider.GHOST_PLAYER_CAPABILITY, null);
                player.setPresenceTime(player.getPresenceTime() + Integer.valueOf(args[1]));
                ModMain.network.sendTo(new SyncAllCaps(player.writeToNBT(), entityPlayer.getEntityId()), entityPlayer);
            } else
                throw new WrongUsageException("No such player", new Object[0]);
        }
    }


    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return args.length == 0 ? Collections.<String>emptyList() : getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
    }
}
