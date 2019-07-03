package com.cwelth.intimepresence.jei.SteamHammer;

import com.cwelth.intimepresence.ModMain;
import com.cwelth.intimepresence.fluids.AllFluids;
import com.cwelth.intimepresence.jei.ITPRecipeCategory;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class SteamHammerCategory extends ITPRecipeCategory<SteamHammerWrapper> {
    private final static ResourceLocation guiTexture = new ResourceLocation(ModMain.MODID, "textures/gui/steamhammer.png");
    private final static String blockName = "steamhammer";

    public SteamHammerCategory(IGuiHelper guiHelper) {
        super(guiHelper.drawableBuilder(guiTexture, 5, 5, 164, 96).build(), "tile.intimepresence."+blockName+".name");
    }

    @Override
    public String getUid() {
        return blockName;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, SteamHammerWrapper wrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

        guiItemStacks.init(0, true, 46, 19);
        guiItemStacks.init(1, false, 100, 19);
        guiFluidStacks.init(0, true, 128, 20, 16, 52, 10000, false, null);

        guiItemStacks.set(0, ingredients.getInputs(ItemStack.class).get(0));
        guiItemStacks.set(1, ingredients.getOutputs(ItemStack.class).get(0));
        guiFluidStacks.set(0, ingredients.getInputs(FluidStack.class).get(0));
    }
}
