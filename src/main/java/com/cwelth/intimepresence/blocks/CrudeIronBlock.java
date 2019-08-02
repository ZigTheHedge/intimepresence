package com.cwelth.intimepresence.blocks;

import com.cwelth.intimepresence.ModMain;
import net.minecraft.block.material.Material;

public class CrudeIronBlock extends SimpleBlock {
    public CrudeIronBlock() {
        super(Material.ROCK, "crudeironblock");
        setHardness(3F);
        setCreativeTab(ModMain.itpCreativeTab);
    }
}
