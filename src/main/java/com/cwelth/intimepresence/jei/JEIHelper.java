package com.cwelth.intimepresence.jei;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class JEIHelper {

    public static List<ItemStack> getOreList(Ingredient oreList){
        return getOreList(oreList, 1);
    }

    public static List<ItemStack> getOreList(Ingredient oreList, int qty)
    {
        List<ItemStack> retList = new ArrayList<>();
        for(ItemStack stack: oreList.getMatchingStacks())
        {
            ItemStack toRet = stack.copy();
            toRet.setCount(qty);
            retList.add(toRet);
        }
        return retList;
    }
}
