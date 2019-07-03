package com.cwelth.intimepresence.jei.ObsidianCauldron;

import com.cwelth.intimepresence.jei.JEIHelper;
import com.cwelth.intimepresence.recipies.ObsidianCauldronRecipe;
import com.cwelth.intimepresence.recipies.SelfRecipies;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ObsidianCauldronMaker {
    public static List<ObsidianCauldronWrapper> getObsidianCauldron(IJeiHelpers helpers){
        IStackHelper stackHelper = helpers.getStackHelper();
        List<ObsidianCauldronWrapper> recipes = new ArrayList<>();

        for(ObsidianCauldronRecipe r: SelfRecipies.obsidianCauldronRecipes.recipes)
        {
            List<ItemStack> input1;
            List<ItemStack> input2;
            List<ItemStack> input3;
            if(r.in[0] == null) {
                input1 = JEIHelper.getOreList(r.inDict[0], r.qty[0]);
                input2 = JEIHelper.getOreList(r.inDict[1], r.qty[1]);
                input3 = JEIHelper.getOreList(r.inDict[2], r.qty[2]);
            } else {
                input1 = stackHelper.getSubtypes(r.in[0]);
                input2 = stackHelper.getSubtypes(r.in[1]);
                input3 = stackHelper.getSubtypes(r.in[2]);
            }
            List<ItemStack> output = stackHelper.getSubtypes(r.out);
            recipes.add(new ObsidianCauldronWrapper(input1, input2, input3, output));
        }

        return recipes;
    }
}
