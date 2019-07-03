package com.cwelth.intimepresence.jei.SteamHammer;

import com.cwelth.intimepresence.jei.JEIHelper;
import com.cwelth.intimepresence.jei.ObsidianCauldron.ObsidianCauldronWrapper;
import com.cwelth.intimepresence.recipies.ObsidianCauldronRecipe;
import com.cwelth.intimepresence.recipies.SelfRecipies;
import com.cwelth.intimepresence.recipies.SteamHammerRecipe;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SteamHammerMaker {
    public static List<SteamHammerWrapper> getSteamHammer(IJeiHelpers helpers){
        IStackHelper stackHelper = helpers.getStackHelper();
        List<SteamHammerWrapper> recipes = new ArrayList<>();

        for(SteamHammerRecipe r: SelfRecipies.steamHammerRecepies.recipes)
        {
            List<ItemStack> input;
            if(r.in == null) {
                input = JEIHelper.getOreList(r.inDict);
            } else {
                input = stackHelper.getSubtypes(r.in);
            }
            List<ItemStack> output = stackHelper.getSubtypes(r.out);
            recipes.add(new SteamHammerWrapper(input, r.workCycles*100, output));
        }

        return recipes;
    }
}

