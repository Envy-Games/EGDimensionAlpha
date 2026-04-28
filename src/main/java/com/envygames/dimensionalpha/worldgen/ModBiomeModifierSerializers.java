package com.envygames.dimensionalpha.worldgen;

import com.envygames.dimensionalpha.DimensionalphaMod;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ModBiomeModifierSerializers {
    private static final DeferredRegister<MapCodec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, DimensionalphaMod.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends BiomeModifier>, MapCodec<ConfigurableAddFeaturesBiomeModifier>> CONFIGURABLE_ADD_FEATURES =
            BIOME_MODIFIER_SERIALIZERS.register("configurable_add_features", () -> RecordCodecBuilder.mapCodec(builder -> builder.group(
                    Biome.LIST_CODEC.fieldOf("biomes").forGetter(ConfigurableAddFeaturesBiomeModifier::biomes),
                    PlacedFeature.LIST_CODEC.fieldOf("features").forGetter(ConfigurableAddFeaturesBiomeModifier::features),
                    GenerationStep.Decoration.CODEC.fieldOf("step").forGetter(ConfigurableAddFeaturesBiomeModifier::step),
                    ConfigGate.CODEC.fieldOf("config_gate").forGetter(ConfigurableAddFeaturesBiomeModifier::configGate)
            ).apply(builder, ConfigurableAddFeaturesBiomeModifier::new)));

    public static void register(IEventBus bus) {
        BIOME_MODIFIER_SERIALIZERS.register(bus);
    }

    private ModBiomeModifierSerializers() {
    }
}
