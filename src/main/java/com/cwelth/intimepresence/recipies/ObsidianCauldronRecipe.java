package com.cwelth.intimepresence.recipies;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class ObsidianCauldronRecipe {
    public ItemStack[] in = new ItemStack[3];
    public Ingredient[] inDict = new Ingredient[3];
    public int[] qty = new int[3];
    public ItemStack out;
    public int workCycles;

    public ObsidianCauldronRecipe(ItemStack in1, ItemStack in2, ItemStack in3, ItemStack out, int workCycles){
        this.in[0] = in1;
        this.in[1] = in2;
        this.in[2] = in3;
        this.inDict[0] = null;
        this.inDict[1] = null;
        this.inDict[2] = null;
        this.out = out;
        this.workCycles = workCycles;
    }

    public ObsidianCauldronRecipe(Ingredient in1, int qty1, Ingredient in2, int qty2, Ingredient in3, int qty3, ItemStack out, int workCycles){
        this.in[0] = null;
        this.in[1] = null;
        this.in[2] = null;
        this.inDict[0] = in1;
        this.qty[0] = qty1;
        this.inDict[1] = in2;
        this.qty[1] = qty2;
        this.inDict[2] = in3;
        this.qty[2] = qty3;
        this.out = out;
        this.workCycles = workCycles;
    }
}
