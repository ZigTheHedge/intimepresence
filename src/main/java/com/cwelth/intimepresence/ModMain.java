package com.cwelth.intimepresence;

import com.cwelth.intimepresence.blocks.AllBlocks;
import com.cwelth.intimepresence.commands.ITP_AddTime;
import com.cwelth.intimepresence.commands.ITP_SubTime;
import com.cwelth.intimepresence.gui.ITPCreativeTab;
import com.cwelth.intimepresence.gui.GUIHandler;
import com.cwelth.intimepresence.items.AllItems;
import com.cwelth.intimepresence.network.*;
import com.cwelth.intimepresence.oregen.OreGen;
import com.cwelth.intimepresence.player.CapabilityEvents;
import com.cwelth.intimepresence.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.util.logging.Logger;

@Mod(modid = ModMain.MODID, name = ModMain.NAME, version = ModMain.VERSION, dependencies = "")
public class ModMain {

    public static final String NAME = "In Time Presence";
    public static final String MODID = "intimepresence";
    public static final String VERSION = "1.26";
    public static final CreativeTabs itpCreativeTab = new ITPCreativeTab();


    public static final Logger logger = Logger.getLogger(MODID);

    @Mod.Instance(ModMain.MODID)
    public static ModMain instance;

    public static SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

    @SidedProxy(clientSide = "com.cwelth.intimepresence.proxy.ClientProxy", serverSide = "com.cwelth.intimepresence.proxy.CommonProxy")
    public static CommonProxy proxy;

    static {
        FluidRegistry.enableUniversalBucket();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e)
    {
        proxy.preInit(e);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        MinecraftForge.EVENT_BUS.register(new EventHandlers());
        MinecraftForge.EVENT_BUS.register(new CapabilityEvents());

        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GUIHandler());

        network.registerMessage(SyncAllCaps.Handler.class, SyncAllCaps.class, 1, Side.CLIENT);
        network.registerMessage(SyncTimer.Handler.class, SyncTimer.class, 2, Side.CLIENT);
        network.registerMessage(SyncGUIOpened.Handler.class, SyncGUIOpened.class, 3, Side.SERVER);
        network.registerMessage(SyncTESRAnim.Handler.class, SyncTESRAnim.class, 4, Side.CLIENT);
        network.registerMessage(SyncObsidianCauldron.Handler.class, SyncObsidianCauldron.class, 5, Side.CLIENT);
        network.registerMessage(SyncShardProcessor.Handler.class, SyncShardProcessor.class, 6, Side.CLIENT);

        GameRegistry.registerWorldGenerator(new OreGen(), 0);

        AllItems.registerOreDictionary();
        AllBlocks.registerOreDictionary();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e)
    {
        proxy.postInit(e);
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new ITP_AddTime());
        event.registerServerCommand(new ITP_SubTime());
    }
}