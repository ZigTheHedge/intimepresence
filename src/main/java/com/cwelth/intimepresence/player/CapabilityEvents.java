package com.cwelth.intimepresence.player;

import com.cwelth.intimepresence.Config;
import com.cwelth.intimepresence.ModMain;
import com.cwelth.intimepresence.gui.ClientPresenceTimeRenderer;
import com.cwelth.intimepresence.network.SyncAllCaps;
import com.cwelth.intimepresence.network.SyncTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.HashMap;

public class CapabilityEvents {
    public static final ResourceLocation GHOST_PLAYER_CAPABILITY = new ResourceLocation(ModMain.MODID, "ghost_player");
    private static HashMap<String, InventoryPlayer> plKeepInv = new HashMap<>();

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent event) {
        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(GHOST_PLAYER_CAPABILITY, new GhostPlayerProvider());
        }
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerLoggedInEvent event) {
        EntityPlayer player = event.player;
        IGhostPlayer cap = player.getCapability(GhostPlayerProvider.GHOST_PLAYER_CAPABILITY, null);
        if (cap != null && !player.world.isRemote) {
            if (cap.isFirstSpawn()) {
                cap.setFirstSpawn(false);
                cap.setPresenceTime(Config.initialTime);
                if (Config.spawnForced) {
                    MinecraftServer mc = event.player.getEntityWorld().getMinecraftServer();
                    mc.getPlayerList().transferPlayerToDimension((EntityPlayerMP) event.player, Config.spawnDimension, new TeleportHelper(((EntityPlayerMP) event.player).getServerWorld(), Config.initialDimension, new BlockPos(Config.spawnX, Config.spawnY, Config.spawnZ)));
                    event.player.setSpawnChunk(new BlockPos(Config.spawnX, Config.spawnY, Config.spawnZ), true, Config.spawnDimension);
                }
            }
            ModMain.network.sendTo(new SyncAllCaps(cap.writeToNBT(), player.getEntityId()), (EntityPlayerMP) player);
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        EntityPlayer player = event.getEntityPlayer();
        IGhostPlayer newCap = player.getCapability(GhostPlayerProvider.GHOST_PLAYER_CAPABILITY, null);
        IGhostPlayer oldCap = event.getOriginal().getCapability(GhostPlayerProvider.GHOST_PLAYER_CAPABILITY, null);
        newCap.copyPlayer(oldCap);
        if (event.isWasDeath())
            newCap.setPresenceTime(0);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END)
            if (!event.player.world.isRemote) {
                IGhostPlayer player = event.player.getCapability(GhostPlayerProvider.GHOST_PLAYER_CAPABILITY, null);
                if (!GhostUtils.allowedToRoam((EntityPlayerMP) event.player) && !event.player.isDead) {
                    player.tickPresence();
                    player.writeToNBT();
                    ModMain.network.sendTo(new SyncTimer(player.getPresenceTime()), (EntityPlayerMP)event.player);
                    if (player.getPresenceTime() == 0) {
                        if(!Config.hardcoreTimeLimit) {
                            MinecraftServer mc = event.player.getEntityWorld().getMinecraftServer();
                            mc.getPlayerList().transferPlayerToDimension((EntityPlayerMP) event.player, Config.initialDimension, new TeleportHelper(((EntityPlayerMP) event.player).getServerWorld(), Config.initialDimension, null));
                        } else
                        {
                            if(!GhostUtils.allowedToRoam(Config.initialDimension))
                            {
                                if(event.player instanceof EntityPlayerMP)
                                    ((EntityPlayerMP) event.player).connection.disconnect(new TextComponentString(I18n.format("ranoutoftimehardcore")));

                            } else
                                event.player.setHealth(0);
                        }
                    }
                }
            }
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerRespawnEvent event)
    {
        if(!event.player.world.isRemote)
        {
            IGhostPlayer player = event.player.getCapability(GhostPlayerProvider.GHOST_PLAYER_CAPABILITY, null);
            ModMain.network.sendTo(new SyncAllCaps(player.writeToNBT(), event.player.getEntityId()), (EntityPlayerMP) event.player);
        }
    }

    @SubscribeEvent
    public void onRenderGui(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE) return;
        new ClientPresenceTimeRenderer(Minecraft.getMinecraft());
    }


    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPlayerDeath(LivingDeathEvent event)
    {
        if(event.getEntity() instanceof EntityPlayer)
        {
            if(!event.getEntity().world.isRemote)
            {
                EntityPlayer player = (EntityPlayer)event.getEntity();
                if (!GhostUtils.allowedToRoam((EntityPlayerMP)player)) {
                    if (Config.keepInventoryOnDeathInForeignDimensions) {
                        InventoryPlayer inv = player.inventory;
                        InventoryPlayer keptItems = new InventoryPlayer(player);
                        keptItems.copyInventory(inv);

                        plKeepInv.put(player.getUniqueID().toString(), keptItems);
                    }
                }
            }
        }
    }


    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onEntityDropsEvent(PlayerDropsEvent event) {
        if (event.getEntityPlayer() instanceof EntityPlayerMP) {
            EntityPlayerMP playerEv = (EntityPlayerMP) event.getEntityPlayer();
            IGhostPlayer player = playerEv.getCapability(GhostPlayerProvider.GHOST_PLAYER_CAPABILITY, null);
            if (!GhostUtils.allowedToRoam(playerEv)) {
                if (Config.keepInventoryOnDeathInForeignDimensions) {
                    event.getDrops().clear();
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEntityJoinWorldEvent(EntityJoinWorldEvent event) {
        if (!event.getWorld().isRemote) {
            if(event.getEntity() instanceof EntityPlayer) {
                IGhostPlayer ghost = event.getEntity().getCapability(GhostPlayerProvider.GHOST_PLAYER_CAPABILITY, null);
                ModMain.network.sendTo(new SyncAllCaps(ghost.writeToNBT(), event.getEntity().getEntityId()), (EntityPlayerMP) event.getEntity());
                if(plKeepInv.containsKey(event.getEntity().getUniqueID().toString())){
                    ((EntityPlayer)event.getEntity()).inventory.copyInventory(plKeepInv.get(event.getEntity().getUniqueID().toString()));

                    plKeepInv.remove(event.getEntity().getUniqueID().toString());
                }
            }
        }
    }
}