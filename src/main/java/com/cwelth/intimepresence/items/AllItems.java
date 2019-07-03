package com.cwelth.intimepresence.items;

import com.cwelth.intimepresence.blocks.AllBlocks;
import com.cwelth.intimepresence.fluids.AllFluids;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class AllItems {

    @GameRegistry.ObjectHolder("intimepresence:timebattery")
    public static TimeBattery timeBattery;

    @GameRegistry.ObjectHolder("intimepresence:bioniceye")
    public static BionicEye bionicEye;

    @GameRegistry.ObjectHolder("intimepresence:dimshards")
    public static DimensionalShards dimensionalShards;

    @GameRegistry.ObjectHolder("intimepresence:ironplate")
    public static IronPlate ironPlate;

    @GameRegistry.ObjectHolder("intimepresence:coke")
    public static Coke coke;

    @GameRegistry.ObjectHolder("intimepresence:crudeironingot")
    public static CrudeIronIngot crudeIronIngot;

    @GameRegistry.ObjectHolder("intimepresence:crudeironplate")
    public static CrudeIronPlate crudeIronPlate;

    @GameRegistry.ObjectHolder("intimepresence:boer")
    public static PumpBoer pumpBoer;

    @GameRegistry.ObjectHolder("intimepresence:piston")
    public static SteamPiston steamPiston;

    @GameRegistry.ObjectHolder("intimepresence:reservoir")
    public static Reservoir reservoir;

    @GameRegistry.ObjectHolder("intimepresence:diamondshards")
    public static DiamondShards diamondShards;

    @GameRegistry.ObjectHolder("intimepresence:goldenplate")
    public static GoldenPlate goldenPlate;

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        timeBattery.initModel();
        bionicEye.initModel();
        dimensionalShards.initModel();
        ironPlate.initModel();
        coke.initModel();
        crudeIronIngot.initModel();
        crudeIronPlate.initModel();
        pumpBoer.initModel();
        steamPiston.initModel();
        reservoir.initModel();
        diamondShards.initModel();
        goldenPlate.initModel();
    }

    public static void registerOreDictionary()
    {
        OreDictionary.registerOre("plateIron", new ItemStack(AllItems.ironPlate));
        OreDictionary.registerOre("coal", new ItemStack(Items.COAL));
        OreDictionary.registerOre("fuelCoke", new ItemStack(AllItems.coke));
        OreDictionary.registerOre("ingotCrudeIron", new ItemStack(AllItems.crudeIronIngot));
        OreDictionary.registerOre("plateCrudeIron", new ItemStack(AllItems.crudeIronPlate));
        OreDictionary.registerOre("shardsDiamond", new ItemStack(AllItems.diamondShards));
        OreDictionary.registerOre("shardsDimensional", new ItemStack(AllItems.dimensionalShards));
        OreDictionary.registerOre("plateGold", new ItemStack(AllItems.goldenPlate));
    }

}
