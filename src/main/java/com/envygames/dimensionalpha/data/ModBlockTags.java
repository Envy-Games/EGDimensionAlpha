package com.envygames.dimensionalpha.data;

import com.envygames.dimensionalpha.DimensionalphaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class ModBlockTags {
    public static final TagKey<Block> BLACKSTONE_ORE_REPLACEABLES = create("blackstone_ore_replaceables");
    public static final TagKey<Block> CALCITE_ORE_REPLACEABLES = create("calcite_ore_replaceables");
    public static final TagKey<Block> DIRT_ORE_REPLACEABLES = create("dirt_ore_replaceables");
    public static final TagKey<Block> END_STONE_ORE_REPLACEABLES = create("end_stone_ore_replaceables");
    public static final TagKey<Block> GRAVEL_ORE_REPLACEABLES = create("gravel_ore_replaceables");
    public static final TagKey<Block> OBSIDIAN_ORE_REPLACEABLES = create("obsidian_ore_replaceables");
    public static final TagKey<Block> RED_SAND_ORE_REPLACEABLES = create("red_sand_ore_replaceables");
    public static final TagKey<Block> SAND_ORE_REPLACEABLES = create("sand_ore_replaceables");
    public static final TagKey<Block> TERRACOTTA_ORE_REPLACEABLES = create("terracotta_ore_replaceables");
    public static final TagKey<Block> TUFF_ORE_REPLACEABLES = create("tuff_ore_replaceables");

    private static TagKey<Block> create(String path) {
        return TagKey.create(Registries.BLOCK, DimensionalphaMod.id(path));
    }

    private ModBlockTags() {
    }
}
