package com.cwelth.intimepresence.recipies;

import com.cwelth.intimepresence.blocks.AllBlocks;
import com.cwelth.intimepresence.items.AllItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

public class SelfRecipies {
    public static SteamHammerRecepies steamHammerRecepies = new SteamHammerRecepies();
    public static ObsidianCauldronRecipes obsidianCauldronRecipes = new ObsidianCauldronRecipes();

    public static void initRecipies()
    {
        //Steam Hammer
        steamHammerRecepies.addRecipe(new OreIngredient("gemDiamond"), new ItemStack(AllItems.diamondShards, 2), 13);
        steamHammerRecepies.addRecipe(new OreIngredient("oreDimensionalOre"), new ItemStack(AllItems.dimensionalShards, 2), 6);


        //Obsidian Cauldron
        obsidianCauldronRecipes.addRecipe(new OreIngredient("coal"), 4, new OreIngredient("sand"), 4, Ingredient.EMPTY, 0, new ItemStack(AllItems.coke), 1200);
        obsidianCauldronRecipes.addRecipe(new OreIngredient("fuelCoke"), 2, new OreIngredient("ingotIron"), 1, Ingredient.EMPTY, 0, new ItemStack(AllItems.crudeIronIngot), 300);
        obsidianCauldronRecipes.addRecipe(new OreIngredient("shardsDiamond"), 4, new OreIngredient("shardsDimensional"), 4, new OreIngredient("blockGlass"), 1, new ItemStack(AllItems.bionicEye), 500);
    }
}
