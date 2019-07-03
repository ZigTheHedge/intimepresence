package com.cwelth.intimepresence.recipies;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

import java.util.ArrayList;

public class SteamHammerRecepies {
    public ArrayList<SteamHammerRecipe> recipes = new ArrayList<>();

    public SteamHammerRecepies()
    {

    }

    public void addRecipe(ItemStack in, ItemStack out, int workCycles)
    {
        if(isItemValid(in))
        {
            return;
        }
        recipes.add(new SteamHammerRecipe(in, out, workCycles));
    }

    public void addRecipe(Ingredient in, ItemStack out, int workCycles)
    {
        for(ItemStack stack: in.getMatchingStacks())
        {
            if(isItemValid(stack)){
                return;
            }
        }
        recipes.add(new SteamHammerRecipe(in, out, workCycles));
    }

    public boolean isItemValid(ItemStack in)
    {
        if(in.isEmpty()) return false;
        for(SteamHammerRecipe r : recipes)
        {
            if(r.in != null) {
                if (r.in.isItemEqual(in)) return true;
            } else
            {
                if(r.inDict.apply(in))return true;
            }
        }
        return false;
    }

    public ItemStack getOutput(ItemStack in)
    {
        for(SteamHammerRecipe r : recipes)
        {
            if(r.in != null) {
                if (r.in.isItemEqual(in)) return r.out;
            } else
            {
                if(r.inDict.apply(in))return r.out;
            }
        }
        return ItemStack.EMPTY;
    }

    public int getWorkCycles(ItemStack in)
    {
        for(SteamHammerRecipe r : recipes)
        {
            if(r.in != null) {
                if (r.in.isItemEqual(in)) return r.workCycles;
            } else
            {
                if(r.inDict.apply(in))return r.workCycles;
            }
        }
        return 0;
    }
}
