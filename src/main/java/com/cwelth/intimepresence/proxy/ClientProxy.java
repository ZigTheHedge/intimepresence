package com.cwelth.intimepresence.proxy;

import com.cwelth.intimepresence.ModMain;
import com.cwelth.intimepresence.blocks.AllBlocks;
import com.cwelth.intimepresence.items.AllItems;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        OBJLoader.INSTANCE.addDomain(ModMain.MODID);
        super.preInit(e);
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        AllItems.initModels();
        AllBlocks.initModels();
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        AllBlocks.initBlockItemModels();
        super.postInit(e);
    }

    @Override
    public void init(FMLInitializationEvent e) {

        super.init(e);
    }
}
