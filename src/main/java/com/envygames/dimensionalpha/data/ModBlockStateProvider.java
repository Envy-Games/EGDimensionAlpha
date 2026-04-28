package com.envygames.dimensionalpha.data;

import com.envygames.dimensionalpha.DimensionalphaMod;
import com.envygames.dimensionalpha.ModBlocks;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.minecraft.data.PackOutput;

public final class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, DimensionalphaMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ModelFile teleporterModel = models().getExistingFile(modLoc("block/dimension_alpha_teleporter"));
        simpleBlock(ModBlocks.DIMENSION_ALPHA_TELEPORTER.get(), teleporterModel);
        simpleBlockItem(ModBlocks.DIMENSION_ALPHA_TELEPORTER.get(), teleporterModel);

        simpleCubeBlock(ModBlocks.NETHERRACK_COAL_ORE);
        simpleCubeBlock(ModBlocks.NETHERRACK_COPPER_ORE);
        simpleCubeBlock(ModBlocks.NETHERRACK_DIAMOND_ORE);
        simpleCubeBlock(ModBlocks.NETHERRACK_EMERALD_ORE);
        simpleCubeBlock(ModBlocks.NETHERRACK_LAPIS_ORE);
        simpleCubeBlock(ModBlocks.NETHERRACK_IRON_ORE);
        simpleCubeBlock(ModBlocks.NETHERRACK_REDSTONE_ORE);
        simpleCubeBlock(ModBlocks.ENDSTONE_COAL_ORE);
        simpleCubeBlock(ModBlocks.ENDSTONE_IRON_ORE);
        simpleCubeBlock(ModBlocks.ENDSTONE_EMERALD_ORE);
        simpleCubeBlock(ModBlocks.ENDSTONE_DIAMOND_ORE);
    }

    private void simpleCubeBlock(DeferredBlock<? extends Block> block) {
        simpleBlockWithItem(block.get(), cubeAll(block.get()));
    }
}
