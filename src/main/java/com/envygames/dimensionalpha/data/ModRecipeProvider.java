package com.envygames.dimensionalpha.data;

import com.envygames.dimensionalpha.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public final class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.DIMENSION_ALPHA_TELEPORTER_ITEM.get())
                .pattern("IRI")
                .pattern("IBI")
                .pattern("iri")
                .define('I', Blocks.IRON_BLOCK)
                .define('R', Blocks.REDSTONE_BLOCK)
                .define('B', Blocks.BLACKSTONE)
                .define('i', Items.IRON_INGOT)
                .define('r', Items.REDSTONE)
                .unlockedBy("has_redstone_block", has(Blocks.REDSTONE_BLOCK))
                .save(output);
    }
}
