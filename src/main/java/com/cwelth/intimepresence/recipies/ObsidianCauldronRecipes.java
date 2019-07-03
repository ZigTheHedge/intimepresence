package com.cwelth.intimepresence.recipies;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreIngredient;

import java.util.ArrayList;

public class ObsidianCauldronRecipes {
    public ArrayList<ObsidianCauldronRecipe> recipes = new ArrayList<>();

    ObsidianCauldronRecipes()
    {

    }

    public void addRecipe(ItemStack in1, ItemStack in2, ItemStack in3, ItemStack out, int workCycles)
    {
        ItemStack[] stacks = new ItemStack[] {in1, in2, in3};
        if(!isRecipeValid(stacks))
        {
            recipes.add(new ObsidianCauldronRecipe(in1, in2, in3, out, workCycles));
        }
    }

    public void addRecipe(Ingredient in1, int qty1, Ingredient in2, int qty2, Ingredient in3, int qty3, ItemStack out, int workCycles)
    {
        for(ItemStack stack1: in1.getMatchingStacks())
            for(ItemStack stack2: in2.getMatchingStacks())
                for(ItemStack stack3: in3.getMatchingStacks())
                {
                    ItemStack[] stacks = new ItemStack[] { stack1, stack2, stack3 };
                    if(isRecipeValid(stacks))return;
                }
        recipes.add(new ObsidianCauldronRecipe(in1, qty1, in2, qty2, in3, qty3, out, workCycles));
    }

    public boolean isRecipeValid(ItemStack[] in)
    {
        int matchFound = 0;
        for (ObsidianCauldronRecipe r : recipes) {
            matchFound = 0;
            for(int i = 0; i < 3; i++) {
                for(int j = 0; j < 3; j++) {
                    if(r.in[i] != null) {
                        if (r.in[i].isItemEqual(in[j]) && r.in[i].getCount() <= in[j].getCount() || (r.in[i].isEmpty() && in[j].isEmpty())) {
                            matchFound++;
                            break;
                        }
                    } else
                    {
                        if(r.inDict[i].apply(in[j]) && r.qty[i] <= in[j].getCount() || (r.inDict[i] == Ingredient.EMPTY && in[j].isEmpty()))
                        {
                            matchFound++;
                            break;
                        }
                    }
                }
            }
            if(matchFound == 3)return true;
        }
        return false;
    }

    public ObsidianCauldronRecipe getMatchedRecipe(ItemStack[] in)
    {
        int matchFound = 0;
        for (ObsidianCauldronRecipe r : recipes) {
            matchFound = 0;
            for(int i = 0; i < 3; i++) {
                for(int j = 0; j < 3; j++) {
                    if(r.in[i] != null) {
                        if (r.in[i].isItemEqual(in[j]) && r.in[i].getCount() <= in[j].getCount() || (r.in[i].isEmpty() && in[j].isEmpty())) {
                            matchFound++;
                            break;
                        }
                    } else
                    {
                        if(r.inDict[i].apply(in[j]) && r.qty[i] <= in[j].getCount() || (r.inDict[i] == Ingredient.EMPTY && in[j].isEmpty()))
                        {
                            matchFound++;
                            break;
                        }
                    }
                }
            }
            if(matchFound == 3)return r;
        }
        return null;
    }
}
