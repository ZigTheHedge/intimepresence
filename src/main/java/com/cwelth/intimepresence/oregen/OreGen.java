package com.cwelth.intimepresence.oregen;

import com.cwelth.intimepresence.Config;
import com.cwelth.intimepresence.blocks.AllBlocks;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Map;
import java.util.Random;


public class OreGen implements IWorldGenerator {


    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if(!Config.genOreEnabled)return;
        int dim = world.provider.getDimension();
        if(dim == -1) {
            if (Config.genOreInNether)
                runGenerator(AllBlocks.dimensionalOre.getStateFromMeta(1), Config.dim01amount, Config.dim01chance, Config.dim01minY, Config.dim01maxY, BlockMatcher.forBlock(Blocks.NETHERRACK), world, random, chunkX, chunkZ);
        } else if(dim == 1) {
            if(Config.genOreInTheEnd)
                runGenerator(AllBlocks.dimensionalOre.getStateFromMeta(2), Config.dim1amount, Config.dim1chance, Config.dim1minY, Config.dim1maxY, BlockMatcher.forBlock(Blocks.END_STONE), world, random, chunkX, chunkZ);
        } else if(dim == 0) {
            if(Config.genOreInOverworld)
                runGenerator(AllBlocks.dimensionalOre.getStateFromMeta(0), Config.dim0amount, Config.dim0chance, Config.dim0minY, Config.dim0maxY, BlockMatcher.forBlock(Blocks.STONE), world, random, chunkX, chunkZ);
        } else
        {
            if(Config.genOreInForeignDimensions) {
                Block biq = Block.REGISTRY.getObject(new ResourceLocation(Config.blockToReplace));
                BlockStateMatcher biqMatcher = BlockStateMatcher.forBlock(biq);
                int meta;

                int colonsinstring = 0;
                for(int i = 0; i < Config.blockToReplace.length(); i++)
                    if(Config.blockToReplace.charAt(i) == ':')
                        colonsinstring++;
                if(colonsinstring > 0)
                {
                    meta = Integer.parseInt(Config.blockToReplace.substring(Config.blockToReplace.lastIndexOf(':') + 1));
                    IBlockState biqState = biq.getStateFromMeta(meta);
                    for(Map.Entry<IProperty<?>, Comparable<?>> property : biqState.getProperties().entrySet())
                        biqMatcher = biqMatcher.where((IProperty)property.getKey(), Predicates.equalTo(biqState.getValue(property.getKey())));
                }
                runGenerator(AllBlocks.dimensionalOre.getStateFromMeta(0), Config.dimXamount, Config.dimXchance, Config.dimXminY, Config.dimXmaxY, biqMatcher, world, random, chunkX, chunkZ);
            }

        }
    }

    private void runGenerator(IBlockState blockToGen, int blockAmount, int chancesToSpawn, int minHeight, int maxHeight, Predicate<IBlockState> blockToReplace, World world, Random rand, int chunk_X, int chunk_Z){
        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

        WorldGenMinable generator = new WorldGenMinable(blockToGen, blockAmount, blockToReplace);
        int heightdiff = maxHeight - minHeight +1;
        for (int i=0; i<chancesToSpawn; i++){
            int x = chunk_X * 16 + rand.nextInt(16);
            int y = minHeight + rand.nextInt(heightdiff);
            int z = chunk_Z * 16 + rand.nextInt(16);

            generator.generate(world, rand, new BlockPos(x, y, z));
        }
    }
}
