package com.cwelth.intimepresence.jei.ObsidianCauldron;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ObsidianCauldronWrapper implements IRecipeWrapper {

    public final List<List<ItemStack>> inputs;
    public final List<List<ItemStack>> outputs;

    public ObsidianCauldronWrapper(List<ItemStack> input1, List<ItemStack> input2, List<ItemStack> input3, List<ItemStack> output)
    {
        List<List<ItemStack>> allinput = new ArrayList<>();
        allinput.add(input1);
        allinput.add(input2);
        allinput.add(input3);

        this.inputs = allinput;
        this.outputs = Collections.singletonList(output);
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(ItemStack.class, this.inputs);
        ingredients.setOutputLists(ItemStack.class, this.outputs);
    }
}
