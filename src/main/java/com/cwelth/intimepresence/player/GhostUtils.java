package com.cwelth.intimepresence.player;

import com.cwelth.intimepresence.Config;
import net.minecraft.entity.player.EntityPlayerMP;

public class GhostUtils {

    public static boolean allowedToRoam(int dimension) {
        boolean retValue;
        if(Config.isWhitelist) retValue = false;
        else retValue = true;
        for (int dim : Config.dimensionsList) {
            if(Config.isWhitelist) {
                if (dimension == dim) retValue = true;
            } else
            {
                if(dimension == dim) retValue = false;
            }
        }
        return retValue;
    }

    public static boolean allowedToRoam(EntityPlayerMP player) {
        return allowedToRoam(player.dimension);
    }
}
