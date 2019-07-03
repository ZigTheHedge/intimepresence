package com.cwelth.intimepresence.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemCokeBlock extends ItemBlock {
    public ItemCokeBlock(Block block) {
        super(block);
    }

    @Override
    public int getItemBurnTime(ItemStack itemStack) {
        return 86400;
    }
}
