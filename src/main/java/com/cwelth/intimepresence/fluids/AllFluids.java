package com.cwelth.intimepresence.fluids;

import com.cwelth.intimepresence.ModMain;
import net.minecraft.util.ResourceLocation;

public class AllFluids {

    public static final InTimeFluid fluidSteam = (InTimeFluid) new InTimeFluid("steamfluid",
            new ResourceLocation(ModMain.MODID,"fluids/steam_still"),
            new ResourceLocation(ModMain.MODID, "fluids/steam_flow")
            )
            .setMaterial(FluidMaterials.materialSteam)
            .setDensity(-400)
            .setGaseous(true)
            .setLuminosity(0)
            .setViscosity(100)
            .setTemperature(300);
}
