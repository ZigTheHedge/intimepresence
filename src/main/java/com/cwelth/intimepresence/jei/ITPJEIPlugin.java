package com.cwelth.intimepresence.jei;

import com.cwelth.intimepresence.blocks.AllBlocks;
import com.cwelth.intimepresence.jei.ObsidianCauldron.ObsidianCauldronCategory;
import com.cwelth.intimepresence.jei.ObsidianCauldron.ObsidianCauldronMaker;
import com.cwelth.intimepresence.jei.SteamHammer.SteamHammerCategory;
import com.cwelth.intimepresence.jei.SteamHammer.SteamHammerMaker;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class ITPJEIPlugin implements IModPlugin {
    public static IJeiHelpers helper;

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        final IJeiHelpers jeiHelpers = registry.getJeiHelpers();
        final IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

        registry.addRecipeCategories(new ObsidianCauldronCategory(guiHelper));
        registry.addRecipeCategories(new SteamHammerCategory(guiHelper));
    }

    @Override
    public void register(IModRegistry registry) {
        IIngredientRegistry ingredientRegistry = registry.getIngredientRegistry();
        IJeiHelpers jeiHelpers = registry.getJeiHelpers();

        registry.addRecipes(ObsidianCauldronMaker.getObsidianCauldron(jeiHelpers), "obsidiancauldron");
        registry.addRecipeCatalyst(new ItemStack(AllBlocks.obsidianCauldron), "obsidiancauldron");
        registry.addRecipes(SteamHammerMaker.getSteamHammer(jeiHelpers), "steamhammer");
        registry.addRecipeCatalyst(new ItemStack(AllBlocks.steamHammer), "steamhammer");
    }
}
