package com.cwelth.intimepresence.player;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;

public class TeleportHelper extends Teleporter {
    private WorldServer worldServer;
    private int dimID;
    private BlockPos targetPosition;

    public TeleportHelper(WorldServer worldIn, int dimID, @Nullable BlockPos targetPosition) {
        super(worldIn);
        this.worldServer = worldIn;
        this.dimID = dimID;
        this.targetPosition = targetPosition;
    }

    @Override
    public void placeInPortal(Entity entityIn, float rotationYaw) {
        MinecraftServer mc = entityIn.getEntityWorld().getMinecraftServer();
        WorldServer targetDimension = mc.getWorld(dimID);
        BlockPos spawn = targetDimension.getSpawnPoint();
        BlockPos targetPlace;
        if(targetPosition == null) {
            if(entityIn instanceof EntityPlayerMP)
            {
                targetPlace = ((EntityPlayerMP)entityIn).getBedLocation();
                if(targetPlace == null)
                    targetPlace = targetDimension.getTopSolidOrLiquidBlock(spawn);
            } else
                targetPlace = targetDimension.getTopSolidOrLiquidBlock(spawn);
        }
        else
            targetPlace = targetPosition;
        entityIn.moveToBlockPosAndAngles(targetPlace, entityIn.rotationYaw, entityIn.rotationPitch);
        entityIn.motionX = entityIn.motionY = entityIn.motionZ = 0D;
    }
}
