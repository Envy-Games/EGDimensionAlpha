package com.envygames.dimensionalpha.worldgen;

import com.envygames.dimensionalpha.DimensionalphaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

public final class ModDimensions {
    public static final ResourceKey<Level> ALPHA_LEVEL = ResourceKey.create(
            Registries.DIMENSION,
            DimensionalphaMod.id("eg_dimension_alpha")
    );
    public static final ResourceKey<LevelStem> ALPHA_LEVEL_STEM = ResourceKey.create(
            Registries.LEVEL_STEM,
            DimensionalphaMod.id("eg_dimension_alpha")
    );
    public static final ResourceKey<DimensionType> ALPHA_DIMENSION_TYPE = ResourceKey.create(
            Registries.DIMENSION_TYPE,
            DimensionalphaMod.id("eg_dimension_alpha")
    );
    public static final ResourceKey<Biome> EG_SURFACE = ResourceKey.create(
            Registries.BIOME,
            DimensionalphaMod.id("eg_surface")
    );

    private ModDimensions() {
    }
}
