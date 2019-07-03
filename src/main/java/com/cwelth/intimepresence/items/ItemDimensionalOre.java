package com.cwelth.intimepresence.items;

import com.cwelth.intimepresence.blocks.DimensionalOre;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemDimensionalOre extends ItemBlock {
    public ItemDimensionalOre(Block block) {
        super(block);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        String base = getUnlocalizedName();
        base += "." + DimensionalOre.EnumBlockType.values()[stack.getMetadata()].getName();
        return base;
    }
}
