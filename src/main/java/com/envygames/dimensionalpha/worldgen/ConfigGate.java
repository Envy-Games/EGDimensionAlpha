package com.envygames.dimensionalpha.worldgen;

import com.envygames.dimensionalpha.config.DimensionalphaConfig;
import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum ConfigGate implements StringRepresentable {
    DIMENSION_ALPHA_OVERWORLD_RESOURCES,
    DIMENSION_ALPHA_NETHER_RESOURCES,
    DIMENSION_ALPHA_END_RESOURCES,
    NETHER_REALM_ORES,
    END_REALM_ORES,
    DIMENSION_ALPHA_EXTERNAL_RESOURCES;

    public static final Codec<ConfigGate> CODEC = StringRepresentable.fromEnum(ConfigGate::values);

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public boolean isEnabled() {
        return switch (this) {
            case DIMENSION_ALPHA_OVERWORLD_RESOURCES -> DimensionalphaConfig.dimensionAlphaOverworldResources();
            case DIMENSION_ALPHA_NETHER_RESOURCES -> DimensionalphaConfig.dimensionAlphaNetherResources();
            case DIMENSION_ALPHA_END_RESOURCES -> DimensionalphaConfig.dimensionAlphaEndResources();
            case NETHER_REALM_ORES -> DimensionalphaConfig.netherRealmOres();
            case END_REALM_ORES -> DimensionalphaConfig.endRealmOres();
            case DIMENSION_ALPHA_EXTERNAL_RESOURCES -> DimensionalphaConfig.dimensionAlphaExternalResources();
        };
    }
}
