package com.cwelth.intimepresence.jei.SteamHammer;

import com.cwelth.intimepresence.fluids.AllFluids;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SteamHammerWrapper implements IRecipeWrapper {

    public final List<List<ItemStack>> inputs;
    public final List<List<ItemStack>> outputs;
    public final List<List<FluidStack>> fluids;

    public SteamHammerWrapper(List<ItemStack> input, int steamAmount, List<ItemStack> output)
    {
        this.inputs = Collections.singletonList(input);
        this.outputs = Collections.singletonList(output);
        List<FluidStack> fluid = new ArrayList<>();
        fluid.add(new FluidStack(AllFluids.fluidSteam, steamAmount));
        this.fluids = Collections.singletonList(fluid);
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(ItemStack.class, this.inputs);
        ingredients.setInputLists(FluidStack.class, this.fluids);
        ingredients.setOutputLists(ItemStack.class, this.outputs);
    }
}
