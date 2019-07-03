package com.cwelth.intimepresence.proxy;

import com.cwelth.intimepresence.Config;
import com.cwelth.intimepresence.ModMain;
import com.cwelth.intimepresence.blocks.*;
import com.cwelth.intimepresence.fluids.AllFluids;
import com.cwelth.intimepresence.fluids.FluidMaterials;
import com.cwelth.intimepresence.items.*;
import com.cwelth.intimepresence.player.GhostPlayer;
import com.cwelth.intimepresence.player.GhostPlayerStorage;
import com.cwelth.intimepresence.player.IGhostPlayer;
import com.cwelth.intimepresence.recipies.SelfRecipies;
import com.cwelth.intimepresence.recipies.SteamHammerRecipe;
import com.cwelth.intimepresence.tileentities.*;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

import java.io.File;

@Mod.EventBusSubscriber
public class CommonProxy {

    public static Configuration config;

    public void preInit(FMLPreInitializationEvent e) {
        File directory = e.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "intimepresence.cfg"));
        Config.readConfig();

    }

    public void init(FMLInitializationEvent e) {
        CapabilityManager.INSTANCE.register(IGhostPlayer.class, new GhostPlayerStorage(), GhostPlayer.class);

    }

    public void postInit(FMLPostInitializationEvent e) {
        if (config.hasChanged()) {
            config.save();
        }
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        //Items
        event.getRegistry().register(new TimeBattery());
        event.getRegistry().register(new BionicEye());
        event.getRegistry().register(new DimensionalShards());
        event.getRegistry().register(new IronPlate());
        event.getRegistry().register(new Coke());
        event.getRegistry().register(new CrudeIronIngot());
        event.getRegistry().register(new CrudeIronPlate());
        event.getRegistry().register(new PumpBoer());
        event.getRegistry().register(new SteamPiston());
        event.getRegistry().register(new Reservoir());
        event.getRegistry().register(new DiamondShards());
        event.getRegistry().register(new GoldenPlate());

        //ItemBlocks
        event.getRegistry().register(new ItemBlock(AllBlocks.steamHammer).setRegistryName(AllBlocks.steamHammer.getRegistryName()));
        event.getRegistry().register(new ItemBlock(AllBlocks.steamFluidBlock).setRegistryName(AllBlocks.steamFluidBlock.getRegistryName()));
        event.getRegistry().register(new ItemBlock(AllBlocks.pump).setRegistryName(AllBlocks.pump.getRegistryName()));
        event.getRegistry().register(new ItemBlock(AllBlocks.obsidianCauldron).setRegistryName(AllBlocks.obsidianCauldron.getRegistryName()));
        event.getRegistry().register(new ItemCokeBlock(AllBlocks.cokeBlock).setRegistryName(AllBlocks.cokeBlock.getRegistryName()));
        event.getRegistry().register(new ItemBlock(AllBlocks.crudeIronBlock).setRegistryName(AllBlocks.crudeIronBlock.getRegistryName()));
        event.getRegistry().register(new ItemDimensionalOre(AllBlocks.dimensionalOre).setRegistryName(AllBlocks.dimensionalOre.getRegistryName()));
        event.getRegistry().register(new ItemBlock(AllBlocks.timeMachine).setRegistryName(AllBlocks.timeMachine.getRegistryName()));
        event.getRegistry().register(new ItemBlock(AllBlocks.shardProcessor).setRegistryName(AllBlocks.shardProcessor.getRegistryName()));
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        //Blocks
        event.getRegistry().register(new SteamHammer());
        event.getRegistry().register(new SteamFluidBlock().setCreativeTab(CreativeTabs.BREWING));
        event.getRegistry().register(new Pump());
        event.getRegistry().register(new ObsidianCauldron());
        event.getRegistry().register(new CokeBlock());
        event.getRegistry().register(new CrudeIronBlock());
        event.getRegistry().register(new DimensionalOre());
        event.getRegistry().register(new TimeMachine());
        event.getRegistry().register(new ShardProcessor());

        //TileEntities
        GameRegistry.registerTileEntity(SteamHammerTE.class, ModMain.MODID + "_steamhammerte");
        GameRegistry.registerTileEntity(PumpTE.class, ModMain.MODID + "_pumpte");
        GameRegistry.registerTileEntity(ObsidianCauldronTE.class, ModMain.MODID + "_obsidiancauldronte");
        GameRegistry.registerTileEntity(TimeMachineTE.class, ModMain.MODID + "_timemachinete");
        GameRegistry.registerTileEntity(ShardProcessorTE.class, ModMain.MODID + "_shardprocessorte");

        //Fluids
        FluidRegistry.registerFluid(AllFluids.fluidSteam);
        FluidRegistry.addBucketForFluid(AllFluids.fluidSteam);
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        SelfRecipies.initRecipies();
    }

    @SubscribeEvent
    public static void handleOreDicts(OreDictionary.OreRegisterEvent event)
    {
        if(event.getName().startsWith("plate"))
        {
            String oreInput = "ingot" + event.getName().substring(5);
            if(OreDictionary.doesOreNameExist(oreInput))
                SelfRecipies.steamHammerRecepies.addRecipe(new OreIngredient(oreInput), event.getOre(), 10);
        }
    }
}