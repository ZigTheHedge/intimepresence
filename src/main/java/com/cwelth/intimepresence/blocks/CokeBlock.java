package com.cwelth.intimepresence.blocks;

import com.cwelth.intimepresence.ModMain;
import net.minecraft.block.material.Material;

public class CokeBlock extends SimpleBlock {
    public CokeBlock() {
        super(Material.ROCK, "cokeblock");
        setHardness(3F);
        setCreativeTab(ModMain.itpCreativeTab);
    }

}
