package com.cwelth.intimepresence.recipies;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class SteamHammerRecipe {
    public ItemStack in;
    public Ingredient inDict;
    public ItemStack out;
    public int workCycles;

    public SteamHammerRecipe(ItemStack in, ItemStack out, int workCycles){
        this.in = in;
        this.inDict = null;
        this.out = out;
        this.workCycles = workCycles;
    }

    public SteamHammerRecipe(Ingredient in, ItemStack out, int workCycles){
        this.in = null;
        this.inDict = in;
        this.out = out;
        this.workCycles = workCycles;
    }
}
