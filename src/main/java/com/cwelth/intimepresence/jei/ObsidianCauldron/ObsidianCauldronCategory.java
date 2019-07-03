package com.cwelth.intimepresence.jei.ObsidianCauldron;

import com.cwelth.intimepresence.ModMain;
import com.cwelth.intimepresence.jei.ITPRecipeCategory;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ObsidianCauldronCategory extends ITPRecipeCategory<ObsidianCauldronWrapper> {
    private final static ResourceLocation guiTexture = new ResourceLocation(ModMain.MODID, "textures/gui/obsidiancauldron.png");
    private final static String blockName = "obsidiancauldron";

    public ObsidianCauldronCategory(IGuiHelper guiHelper) {
        super(guiHelper.drawableBuilder(guiTexture, 5, 5, 164, 96).build(), "tile.intimepresence."+blockName+".name");
    }

    @Override
    public String getUid() {
        return blockName;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, ObsidianCauldronWrapper wrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(0, true, 15, 10);
        guiItemStacks.init(1, true, 33, 10);
        guiItemStacks.init(2, true, 51, 10);
        guiItemStacks.init(3, false, 127, 53);

        guiItemStacks.set(0, ingredients.getInputs(ItemStack.class).get(0));
        guiItemStacks.set(1, ingredients.getInputs(ItemStack.class).get(1));
        guiItemStacks.set(2, ingredients.getInputs(ItemStack.class).get(2));

        guiItemStacks.set(3, ingredients.getOutputs(ItemStack.class).get(0));
    }
}
