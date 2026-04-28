package com.envygames.dimensionalpha.data;

import com.envygames.dimensionalpha.DimensionalphaMod;
import com.envygames.dimensionalpha.worldgen.ConfigGate;
import com.envygames.dimensionalpha.worldgen.ConfigurableAddFeaturesBiomeModifier;
import com.envygames.dimensionalpha.worldgen.ModDimensions;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

public final class ModWorldgenProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DIMENSION_TYPE, ModWorldgenProvider::bootstrapDimensionTypes)
            .add(Registries.BIOME, ModWorldgenProvider::bootstrapBiomes)
            .add(Registries.CONFIGURED_FEATURE, ModWorldgenProvider::bootstrapConfiguredFeatures)
            .add(Registries.PLACED_FEATURE, ModWorldgenProvider::bootstrapPlacedFeatures)
            .add(Registries.LEVEL_STEM, ModWorldgenProvider::bootstrapDimensions)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ModWorldgenProvider::bootstrapBiomeModifiers);

    private static final String[] DIMENSION_ALPHA_OVERWORLD_FEATURES = {
            "coal_ore", "copper_ore", "iron_ore", "gold_ore", "redstone_ore", "lapis_ore", "diamond_ore", "emerald_ore",
            "red_terracotta_ore", "orange_terracotta_ore", "yellow_terracotta_ore", "brown_terracotta_ore",
            "white_terracotta_ore", "light_gray_terracotta_ore", "green_terracotta_ore", "blue_terracotta_ore",
            "magenta_terracotta_ore", "light_blue_terracotta_ore", "lime_terracotta_ore", "pink_terracotta_ore",
            "gray_terracotta_ore", "cyan_terracotta_ore", "purple_terracotta_ore", "black_terracotta_ore",
            "raw_iron_block", "andesite_block", "granite_block", "diorite_block", "sandstone_block", "red_sandstone_block",
            "white_concrete_powder_ore", "orange_concrete_powder_ore", "magenta_concrete_powder_ore",
            "light_blue_concrete_powder_ore", "yellow_concrete_powder_ore", "lime_concrete_powder_ore",
            "pink_concrete_powder_ore", "gray_concrete_powder_ore", "light_gray_concrete_powder_ore",
            "cyan_concrete_powder_ore", "purple_concrete_powder_ore", "blue_concrete_powder_ore",
            "brown_concrete_powder_ore", "green_concrete_powder_ore", "red_concrete_powder_ore",
            "black_concrete_powder_ore", "coarse_dirt_ore", "rooted_dirt_ore", "podzol_ore", "clay_ore", "mud_ore"
    };
    private static final String[] DIMENSION_ALPHA_NETHER_FEATURES = {
            "nether_quartz_ore", "ancient_debris", "nether_gold_ore",
            "netherrack_coal_ore_nether", "netherrack_copper_ore_nether", "netherrack_diamond_ore_nether",
            "netherrack_emerald_ore_nether", "netherrack_iron_ore_nether", "netherrack_lapis_ore_nether",
            "netherrack_redstone_ore_nether", "gilded_blackstone", "raw_gold_block", "quartz_block",
            "basalt_block", "smooth_basalt_block", "polished_basalt_block", "crying_obsidian_block"
    };
    private static final String[] DIMENSION_ALPHA_END_FEATURES = {
            "endstone_coal_ore", "endstone_iron_ore", "endstone_emerald_ore", "endstone_diamond_ore"
    };
    private static final String[] NETHER_REALM_FEATURES = {
            "netherrack_coal_ore_nether", "netherrack_copper_ore_nether", "netherrack_diamond_ore_nether",
            "netherrack_emerald_ore_nether", "netherrack_iron_ore_nether", "netherrack_lapis_ore_nether",
            "netherrack_redstone_ore_nether"
    };
    private static final String[] END_REALM_FEATURES = {
            "endstone_coal_ore", "endstone_iron_ore", "endstone_emerald_ore", "endstone_diamond_ore"
    };

    private static final OreDefinition[] ORES = {
            new OreDefinition("ancient_debris", 2, "minecraft:base_stone_nether", "minecraft:ancient_debris", 12, 57, 127),
            new OreDefinition("andesite_block", 16, "dimensionalpha:tuff_ore_replaceables", "minecraft:andesite", 6, -3, 11),
            new OreDefinition("basalt_block", 16, "dimensionalpha:blackstone_ore_replaceables", "minecraft:basalt", 6, 12, 26),
            new OreDefinition("black_concrete_powder_ore", 16, "dimensionalpha:gravel_ore_replaceables", "minecraft:black_concrete_powder", 4, -58, -49),
            new OreDefinition("black_terracotta_ore", 16, "dimensionalpha:terracotta_ore_replaceables", "minecraft:black_terracotta", 4, 27, 36),
            new OreDefinition("blue_concrete_powder_ore", 16, "dimensionalpha:gravel_ore_replaceables", "minecraft:blue_concrete_powder", 4, -58, -49),
            new OreDefinition("blue_terracotta_ore", 16, "dimensionalpha:terracotta_ore_replaceables", "minecraft:blue_terracotta", 4, 27, 36),
            new OreDefinition("brown_concrete_powder_ore", 16, "dimensionalpha:gravel_ore_replaceables", "minecraft:brown_concrete_powder", 4, -58, -49),
            new OreDefinition("brown_terracotta_ore", 16, "dimensionalpha:terracotta_ore_replaceables", "minecraft:brown_terracotta", 4, 27, 36),
            new OreDefinition("clay_ore", 16, "dimensionalpha:dirt_ore_replaceables", "minecraft:clay", 4, -48, -39),
            new OreDefinition("coal_ore", 32, "minecraft:stone_ore_replaceables", "minecraft:coal_ore", 16, 128, 268),
            new OreDefinition("coarse_dirt_ore", 16, "dimensionalpha:dirt_ore_replaceables", "minecraft:coarse_dirt", 4, -48, -39),
            new OreDefinition("copper_ore", 32, "minecraft:stone_ore_replaceables", "minecraft:copper_ore", 12, 128, 268),
            new OreDefinition("crying_obsidian_block", 24, "dimensionalpha:obsidian_ore_replaceables", "minecraft:crying_obsidian", 6, -63, -59),
            new OreDefinition("cyan_concrete_powder_ore", 16, "dimensionalpha:gravel_ore_replaceables", "minecraft:cyan_concrete_powder", 4, -58, -49),
            new OreDefinition("cyan_terracotta_ore", 16, "dimensionalpha:terracotta_ore_replaceables", "minecraft:cyan_terracotta", 4, 27, 36),
            new OreDefinition("diamond_ore", 16, "minecraft:stone_ore_replaceables", "minecraft:diamond_ore", 4, 128, 268),
            new OreDefinition("diorite_block", 24, "dimensionalpha:calcite_ore_replaceables", "minecraft:diorite", 6, -18, -4),
            new OreDefinition("emerald_ore", 12, "minecraft:stone_ore_replaceables", "minecraft:emerald_ore", 4, 128, 268),
            new OreDefinition("endstone_coal_ore", 32, "dimensionalpha:end_stone_ore_replaceables", "dimensionalpha:endstone_coal_ore", 16, -64, 280),
            new OreDefinition("endstone_diamond_ore", 8, "dimensionalpha:end_stone_ore_replaceables", "dimensionalpha:endstone_diamond_ore", 4, -64, 280),
            new OreDefinition("endstone_emerald_ore", 8, "dimensionalpha:end_stone_ore_replaceables", "dimensionalpha:endstone_emerald_ore", 2, -64, 280),
            new OreDefinition("endstone_iron_ore", 32, "dimensionalpha:end_stone_ore_replaceables", "dimensionalpha:endstone_iron_ore", 16, -64, 280),
            new OreDefinition("gilded_blackstone", 24, "dimensionalpha:blackstone_ore_replaceables", "minecraft:gilded_blackstone", 6, 12, 26),
            new OreDefinition("gold_ore", 24, "minecraft:stone_ore_replaceables", "minecraft:gold_ore", 24, 128, 268),
            new OreDefinition("granite_block", 16, "dimensionalpha:tuff_ore_replaceables", "minecraft:granite", 4, -3, 11),
            new OreDefinition("gray_concrete_powder_ore", 16, "dimensionalpha:gravel_ore_replaceables", "minecraft:gray_concrete_powder", 4, -58, -49),
            new OreDefinition("gray_terracotta_ore", 16, "dimensionalpha:terracotta_ore_replaceables", "minecraft:gray_terracotta", 4, 27, 36),
            new OreDefinition("green_concrete_powder_ore", 16, "dimensionalpha:gravel_ore_replaceables", "minecraft:green_concrete_powder", 4, -58, -49),
            new OreDefinition("green_terracotta_ore", 16, "dimensionalpha:terracotta_ore_replaceables", "minecraft:green_terracotta", 4, 27, 36),
            new OreDefinition("iron_ore", 32, "minecraft:stone_ore_replaceables", "minecraft:iron_ore", 32, 128, 268),
            new OreDefinition("lapis_ore", 12, "minecraft:stone_ore_replaceables", "minecraft:lapis_ore", 8, 128, 268),
            new OreDefinition("light_blue_concrete_powder_ore", 16, "dimensionalpha:gravel_ore_replaceables", "minecraft:light_blue_concrete_powder", 4, -58, -49),
            new OreDefinition("light_blue_terracotta_ore", 16, "dimensionalpha:terracotta_ore_replaceables", "minecraft:light_blue_terracotta", 4, 27, 36),
            new OreDefinition("light_gray_concrete_powder_ore", 16, "dimensionalpha:gravel_ore_replaceables", "minecraft:light_gray_concrete_powder", 4, -58, -49),
            new OreDefinition("light_gray_terracotta_ore", 16, "dimensionalpha:terracotta_ore_replaceables", "minecraft:light_gray_terracotta", 4, 27, 36),
            new OreDefinition("lime_concrete_powder_ore", 16, "dimensionalpha:gravel_ore_replaceables", "minecraft:lime_concrete_powder", 4, -58, -49),
            new OreDefinition("lime_terracotta_ore", 16, "dimensionalpha:terracotta_ore_replaceables", "minecraft:lime_terracotta", 4, 27, 36),
            new OreDefinition("magenta_concrete_powder_ore", 16, "dimensionalpha:gravel_ore_replaceables", "minecraft:magenta_concrete_powder", 4, -58, -49),
            new OreDefinition("magenta_terracotta_ore", 16, "dimensionalpha:terracotta_ore_replaceables", "minecraft:magenta_terracotta", 4, 27, 36),
            new OreDefinition("mud_ore", 16, "dimensionalpha:dirt_ore_replaceables", "minecraft:mud", 4, -48, -39),
            new OreDefinition("netherrack_coal_ore_nether", 24, "minecraft:base_stone_nether", "dimensionalpha:netherrack_coal_ore", 6, 57, 127),
            new OreDefinition("netherrack_copper_ore_nether", 16, "minecraft:base_stone_nether", "dimensionalpha:netherrack_copper_ore", 6, 57, 127),
            new OreDefinition("netherrack_diamond_ore_nether", 8, "minecraft:base_stone_nether", "dimensionalpha:netherrack_diamond_ore", 6, 57, 127),
            new OreDefinition("netherrack_emerald_ore_nether", 4, "minecraft:base_stone_nether", "dimensionalpha:netherrack_emerald_ore", 3, 57, 127),
            new OreDefinition("netherrack_iron_ore_nether", 24, "minecraft:base_stone_nether", "dimensionalpha:netherrack_iron_ore", 24, 57, 127),
            new OreDefinition("netherrack_lapis_ore_nether", 8, "minecraft:base_stone_nether", "dimensionalpha:netherrack_lapis_ore", 1, 57, 127),
            new OreDefinition("netherrack_redstone_ore_nether", 12, "minecraft:base_stone_nether", "dimensionalpha:netherrack_redstone_ore", 12, 57, 127),
            new OreDefinition("nether_gold_ore", 24, "minecraft:base_stone_nether", "minecraft:nether_gold_ore", 16, 57, 127),
            new OreDefinition("nether_quartz_ore", 32, "minecraft:base_stone_nether", "minecraft:nether_quartz_ore", 16, 57, 127),
            new OreDefinition("orange_concrete_powder_ore", 16, "dimensionalpha:gravel_ore_replaceables", "minecraft:orange_concrete_powder", 4, -58, -49),
            new OreDefinition("orange_terracotta_ore", 16, "dimensionalpha:terracotta_ore_replaceables", "minecraft:orange_terracotta", 4, 27, 36),
            new OreDefinition("pink_concrete_powder_ore", 16, "dimensionalpha:gravel_ore_replaceables", "minecraft:pink_concrete_powder", 4, -58, -49),
            new OreDefinition("pink_terracotta_ore", 16, "dimensionalpha:terracotta_ore_replaceables", "minecraft:pink_terracotta", 4, 27, 36),
            new OreDefinition("podzol_ore", 16, "dimensionalpha:dirt_ore_replaceables", "minecraft:podzol", 4, -48, -39),
            new OreDefinition("polished_basalt_block", 12, "dimensionalpha:blackstone_ore_replaceables", "minecraft:polished_basalt", 4, 12, 26),
            new OreDefinition("purple_concrete_powder_ore", 16, "dimensionalpha:gravel_ore_replaceables", "minecraft:purple_concrete_powder", 4, -58, -49),
            new OreDefinition("purple_terracotta_ore", 16, "dimensionalpha:terracotta_ore_replaceables", "minecraft:purple_terracotta", 4, 27, 36),
            new OreDefinition("quartz_block", 24, "dimensionalpha:calcite_ore_replaceables", "minecraft:quartz_block", 6, -18, -4),
            new OreDefinition("raw_gold_block", 12, "dimensionalpha:blackstone_ore_replaceables", "minecraft:raw_gold_block", 4, 12, 26),
            new OreDefinition("raw_iron_block", 12, "dimensionalpha:tuff_ore_replaceables", "minecraft:raw_iron_block", 4, -3, 11),
            new OreDefinition("redstone_ore", 24, "minecraft:stone_ore_replaceables", "minecraft:redstone_ore", 12, 128, 268),
            new OreDefinition("red_concrete_powder_ore", 16, "dimensionalpha:gravel_ore_replaceables", "minecraft:red_concrete_powder", 4, -58, -49),
            new OreDefinition("red_sandstone_block", 24, "dimensionalpha:red_sand_ore_replaceables", "minecraft:red_sandstone", 6, -28, -19),
            new OreDefinition("red_terracotta_ore", 16, "dimensionalpha:terracotta_ore_replaceables", "minecraft:red_terracotta", 4, 27, 36),
            new OreDefinition("rooted_dirt_ore", 16, "dimensionalpha:dirt_ore_replaceables", "minecraft:rooted_dirt", 4, -48, -39),
            new OreDefinition("sandstone_block", 24, "dimensionalpha:sand_ore_replaceables", "minecraft:sandstone", 6, -38, -29),
            new OreDefinition("smooth_basalt_block", 12, "dimensionalpha:blackstone_ore_replaceables", "minecraft:smooth_basalt", 4, 12, 26),
            new OreDefinition("white_concrete_powder_ore", 16, "dimensionalpha:gravel_ore_replaceables", "minecraft:white_concrete_powder", 4, -58, -49),
            new OreDefinition("white_terracotta_ore", 16, "dimensionalpha:terracotta_ore_replaceables", "minecraft:white_terracotta", 4, 27, 36),
            new OreDefinition("yellow_concrete_powder_ore", 16, "dimensionalpha:gravel_ore_replaceables", "minecraft:yellow_concrete_powder", 4, -58, -49),
            new OreDefinition("yellow_terracotta_ore", 16, "dimensionalpha:terracotta_ore_replaceables", "minecraft:yellow_terracotta", 4, 27, 36)
    };

    private static void bootstrapDimensionTypes(BootstrapContext<DimensionType> context) {
        context.register(ModDimensions.ALPHA_DIMENSION_TYPE, new DimensionType(
                OptionalLong.of(6000L),
                true,
                false,
                false,
                true,
                1.0D,
                true,
                true,
                -64,
                384,
                384,
                BlockTags.INFINIBURN_OVERWORLD,
                BuiltinDimensionTypes.OVERWORLD_EFFECTS,
                1.0F,
                new DimensionType.MonsterSettings(true, false, ConstantInt.of(0), 0)
        ));
    }

    private static void bootstrapBiomes(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);
        context.register(ModDimensions.EG_SURFACE, new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(0.8F)
                .downfall(0.0F)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .fogColor(12638463)
                        .waterColor(4159204)
                        .waterFogColor(329011)
                        .skyColor(8103167)
                        .build())
                .mobSpawnSettings(new MobSpawnSettings.Builder().build())
                .generationSettings(new BiomeGenerationSettings.Builder(placedFeatures, carvers).build())
                .build());
    }

    private static void bootstrapConfiguredFeatures(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        for (OreDefinition ore : ORES) {
            context.register(configuredKey(ore.name), new ConfiguredFeature<>(
                    Feature.ORE,
                    new OreConfiguration(new TagMatchTest(blockTag(ore.targetTag)), block(ore.block).defaultBlockState(), ore.size)
            ));
        }
    }

    private static void bootstrapDimensions(BootstrapContext<LevelStem> context) {
        HolderGetter<DimensionType> dimensionTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<StructureSet> structureSets = context.lookup(Registries.STRUCTURE_SET);
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);

        FlatLevelGeneratorSettings settings = new FlatLevelGeneratorSettings(
                Optional.of(HolderSet.direct(structureSets.getOrThrow(BuiltinStructureSets.VILLAGES))),
                biomes.getOrThrow(ModDimensions.EG_SURFACE),
                FlatLevelGeneratorSettings.createLakesList(placedFeatures)
        );
        settings.getLayersInfo().add(new FlatLayerInfo(1, Blocks.BEDROCK));
        settings.getLayersInfo().add(new FlatLayerInfo(5, Blocks.OBSIDIAN));
        settings.getLayersInfo().add(new FlatLayerInfo(10, Blocks.GRAVEL));
        settings.getLayersInfo().add(new FlatLayerInfo(10, Blocks.DIRT));
        settings.getLayersInfo().add(new FlatLayerInfo(10, Blocks.SAND));
        settings.getLayersInfo().add(new FlatLayerInfo(10, Blocks.RED_SAND));
        settings.getLayersInfo().add(new FlatLayerInfo(15, Blocks.CALCITE));
        settings.getLayersInfo().add(new FlatLayerInfo(15, Blocks.TUFF));
        settings.getLayersInfo().add(new FlatLayerInfo(15, Blocks.BLACKSTONE));
        settings.getLayersInfo().add(new FlatLayerInfo(10, Blocks.TERRACOTTA));
        settings.getLayersInfo().add(new FlatLayerInfo(20, Blocks.END_STONE));
        settings.getLayersInfo().add(new FlatLayerInfo(71, Blocks.NETHERRACK));
        settings.getLayersInfo().add(new FlatLayerInfo(71, Blocks.DEEPSLATE));
        settings.getLayersInfo().add(new FlatLayerInfo(70, Blocks.STONE));
        settings.getLayersInfo().add(new FlatLayerInfo(1, Blocks.GRASS_BLOCK));
        settings.getLayersInfo().add(new FlatLayerInfo(50, Blocks.AIR));
        settings.setDecoration();
        settings.updateLayers();

        context.register(ModDimensions.ALPHA_LEVEL_STEM, new LevelStem(
                dimensionTypes.getOrThrow(ModDimensions.ALPHA_DIMENSION_TYPE),
                new FlatLevelSource(settings)
        ));
    }

    private static void bootstrapBiomeModifiers(BootstrapContext<BiomeModifier> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);

        HolderSet<Biome> alphaSurface = HolderSet.direct(biomes.getOrThrow(ModDimensions.EG_SURFACE));
        registerBiomeModifier(context, "add_overworld_resources_to_dimensionalpha", alphaSurface,
                features(placedFeatures, DIMENSION_ALPHA_OVERWORLD_FEATURES), ConfigGate.DIMENSION_ALPHA_OVERWORLD_RESOURCES);
        registerBiomeModifier(context, "add_nether_resources_to_dimensionalpha", alphaSurface,
                features(placedFeatures, DIMENSION_ALPHA_NETHER_FEATURES), ConfigGate.DIMENSION_ALPHA_NETHER_RESOURCES);
        registerBiomeModifier(context, "add_end_resources_to_dimensionalpha", alphaSurface,
                features(placedFeatures, DIMENSION_ALPHA_END_FEATURES), ConfigGate.DIMENSION_ALPHA_END_RESOURCES);
        registerBiomeModifier(context, "add_ores_to_nether", biomes.getOrThrow(BiomeTags.IS_NETHER),
                features(placedFeatures, NETHER_REALM_FEATURES), ConfigGate.NETHER_REALM_ORES);
        registerBiomeModifier(context, "add_ores_to_end", biomes.getOrThrow(BiomeTags.IS_END),
                features(placedFeatures, END_REALM_FEATURES), ConfigGate.END_REALM_ORES);
    }

    private static void registerBiomeModifier(
            BootstrapContext<BiomeModifier> context,
            String name,
            HolderSet<Biome> biomes,
            HolderSet<PlacedFeature> features,
            ConfigGate configGate
    ) {
        context.register(biomeModifierKey(name), new ConfigurableAddFeaturesBiomeModifier(
                biomes,
                features,
                GenerationStep.Decoration.UNDERGROUND_ORES,
                configGate
        ));
    }

    private static void bootstrapPlacedFeatures(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        for (OreDefinition ore : ORES) {
            context.register(placedKey(ore.name), new PlacedFeature(
                    configuredFeatures.getOrThrow(configuredKey(ore.name)),
                    List.of(
                            CountPlacement.of(ore.count),
                            InSquarePlacement.spread(),
                            HeightRangePlacement.uniform(VerticalAnchor.absolute(ore.minY), VerticalAnchor.absolute(ore.maxY))
                    )
            ));
        }
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> configuredKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, DimensionalphaMod.id(name));
    }

    private static ResourceKey<PlacedFeature> placedKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, DimensionalphaMod.id(name));
    }

    private static ResourceKey<BiomeModifier> biomeModifierKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, DimensionalphaMod.id(name));
    }

    private static HolderSet<PlacedFeature> features(HolderGetter<PlacedFeature> placedFeatures, String... names) {
        List<Holder.Reference<PlacedFeature>> holders = Arrays.stream(names)
                .map(name -> placedFeatures.getOrThrow(placedKey(name)))
                .toList();
        return HolderSet.direct(holders);
    }

    private static TagKey<Block> blockTag(String id) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.parse(id));
    }

    private static Block block(String id) {
        return BuiltInRegistries.BLOCK.get(ResourceLocation.parse(id));
    }

    private ModWorldgenProvider() {
    }

    private record OreDefinition(String name, int size, String targetTag, String block, int count, int minY, int maxY) {
    }
}
