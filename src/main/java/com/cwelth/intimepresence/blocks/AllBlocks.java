package com.cwelth.intimepresence.blocks;

import com.cwelth.intimepresence.ModMain;
import com.cwelth.intimepresence.items.AllItems;
import com.cwelth.intimepresence.items.CrudeIronPlate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class AllBlocks {

    @GameRegistry.ObjectHolder("intimepresence:steamhammer")
    public static SteamHammer steamHammer;

    @GameRegistry.ObjectHolder("intimepresence:pump")
    public static Pump pump;

    @GameRegistry.ObjectHolder("intimepresence:steamfluidblock")
    public static SteamFluidBlock steamFluidBlock;

    @GameRegistry.ObjectHolder("intimepresence:obsidiancauldron")
    public static ObsidianCauldron obsidianCauldron;

    @GameRegistry.ObjectHolder("intimepresence:cokeblock")
    public static CokeBlock cokeBlock;

    @GameRegistry.ObjectHolder("intimepresence:crudeironblock")
    public static CrudeIronBlock crudeIronBlock;

    @GameRegistry.ObjectHolder("intimepresence:dimensionalore")
    public static DimensionalOre dimensionalOre;

    @GameRegistry.ObjectHolder("intimepresence:timemachine")
    public static TimeMachine timeMachine;

    @GameRegistry.ObjectHolder("intimepresence:shardprocessor")
    public static ShardProcessor shardProcessor;

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        steamHammer.initModel();
        pump.initModel();
        steamFluidBlock.initModel();
        obsidianCauldron.initModel();
        cokeBlock.initModel();
        crudeIronBlock.initModel();
        dimensionalOre.initModel();
        timeMachine.initModel();
        shardProcessor.initModel();
    }

    @SideOnly(Side.CLIENT)
    public static void initBlockItemModels() {
        steamHammer.initItemModel();
        pump.initItemModel();
        steamFluidBlock.initItemModel();
        obsidianCauldron.initItemModel();
        cokeBlock.initItemModel();
        crudeIronBlock.initItemModel();
        dimensionalOre.initItemModel();
        timeMachine.initItemModel();
        shardProcessor.initItemModel();
    }

    public static void registerOreDictionary()
    {
        OreDictionary.registerOre("blockCoalCoke", new ItemStack(AllBlocks.cokeBlock));
        OreDictionary.registerOre("blockCrudeIron", new ItemStack(AllBlocks.crudeIronBlock));
        OreDictionary.registerOre("oreDimensionalOre", new ItemStack(AllBlocks.dimensionalOre, 1, 0));
        OreDictionary.registerOre("oreDimensionalOre", new ItemStack(AllBlocks.dimensionalOre, 1, 1));
        OreDictionary.registerOre("oreDimensionalOre", new ItemStack(AllBlocks.dimensionalOre, 1, 2));
    }
}
