package com.envygames.dimensionalpha.data;

import com.envygames.dimensionalpha.DimensionalphaMod;
import com.envygames.dimensionalpha.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public final class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            @Nullable ExistingFileHelper existingFileHelper
    ) {
        super(output, lookupProvider, DimensionalphaMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                ModBlocks.NETHERRACK_COAL_ORE.get(),
                ModBlocks.NETHERRACK_COPPER_ORE.get(),
                ModBlocks.NETHERRACK_DIAMOND_ORE.get(),
                ModBlocks.NETHERRACK_EMERALD_ORE.get(),
                ModBlocks.NETHERRACK_IRON_ORE.get(),
                ModBlocks.NETHERRACK_LAPIS_ORE.get(),
                ModBlocks.NETHERRACK_REDSTONE_ORE.get(),
                ModBlocks.ENDSTONE_COAL_ORE.get(),
                ModBlocks.ENDSTONE_IRON_ORE.get(),
                ModBlocks.ENDSTONE_EMERALD_ORE.get(),
                ModBlocks.ENDSTONE_DIAMOND_ORE.get()
        );

        tag(ModBlockTags.BLACKSTONE_ORE_REPLACEABLES).add(Blocks.BLACKSTONE);
        tag(ModBlockTags.CALCITE_ORE_REPLACEABLES).add(Blocks.CALCITE);
        tag(ModBlockTags.DIRT_ORE_REPLACEABLES).add(Blocks.DIRT);
        tag(ModBlockTags.END_STONE_ORE_REPLACEABLES).add(Blocks.END_STONE);
        tag(ModBlockTags.GRAVEL_ORE_REPLACEABLES).add(Blocks.GRAVEL);
        tag(ModBlockTags.OBSIDIAN_ORE_REPLACEABLES).add(Blocks.OBSIDIAN);
        tag(ModBlockTags.RED_SAND_ORE_REPLACEABLES).add(Blocks.RED_SAND);
        tag(ModBlockTags.SAND_ORE_REPLACEABLES).add(Blocks.SAND);
        tag(ModBlockTags.TERRACOTTA_ORE_REPLACEABLES).add(Blocks.TERRACOTTA);
        tag(ModBlockTags.TUFF_ORE_REPLACEABLES).add(Blocks.TUFF);
    }
}
