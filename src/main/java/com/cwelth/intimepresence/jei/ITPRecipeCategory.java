package com.cwelth.intimepresence.jei;

import com.cwelth.intimepresence.ModMain;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.util.Translator;

public abstract class ITPRecipeCategory<T extends IRecipeWrapper> implements IRecipeCategory<T> {
    private final IDrawable background;
    private final String localizedName;

    public ITPRecipeCategory(IDrawable background, String unlocalizedName){
        this.background = background;
        this.localizedName = Translator.translateToLocal(unlocalizedName);
    }

    @Override
    public String getTitle() {
        return localizedName;
    }

    @Override
    public String getModName() {
        return ModMain.NAME;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }
}
