package com.envygames.dimensionalpha.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public final class DimensionalphaConfig {
    public static final DimensionalphaConfig COMMON;
    public static final ModConfigSpec COMMON_SPEC;

    static {
        Pair<DimensionalphaConfig, ModConfigSpec> pair = new ModConfigSpec.Builder()
                .configure(DimensionalphaConfig::new);
        COMMON = pair.getLeft();
        COMMON_SPEC = pair.getRight();
    }

    private final ModConfigSpec.IntValue alphaTeleporterY;
    private final ModConfigSpec.IntValue overworldSearchRadius;
    private final ModConfigSpec.BooleanValue removeLinkedTeleporterOnBreak;
    private final ModConfigSpec.BooleanValue dimensionAlphaOverworldResources;
    private final ModConfigSpec.BooleanValue dimensionAlphaNetherResources;
    private final ModConfigSpec.BooleanValue dimensionAlphaEndResources;
    private final ModConfigSpec.BooleanValue netherRealmOres;
    private final ModConfigSpec.BooleanValue endRealmOres;
    private final ModConfigSpec.BooleanValue dimensionAlphaExternalResources;

    private DimensionalphaConfig(ModConfigSpec.Builder builder) {
        builder.push("teleporter");

        alphaTeleporterY = builder
                .comment("Y level used when creating Dimension Alpha destination teleporters.")
                .translation("dimensionalpha.configuration.teleporter.alpha_teleporter_y")
                .defineInRange("alpha_teleporter_y", 270, -63, 317);

        overworldSearchRadius = builder
                .comment("Horizontal radius used when searching for a safe Overworld return pad.")
                .translation("dimensionalpha.configuration.teleporter.overworld_search_radius")
                .defineInRange("overworld_search_radius", 2, 0, 16);

        removeLinkedTeleporterOnBreak = builder
                .comment("Whether breaking a linked teleporter also removes its partner block when that partner chunk is already loaded.")
                .translation("dimensionalpha.configuration.teleporter.remove_linked_teleporter_on_break")
                .define("remove_linked_teleporter_on_break", true);

        builder.pop();

        builder.push("generation");

        dimensionAlphaOverworldResources = builder
                .comment("Whether Dimension Alpha generates Overworld-themed resources such as stone ores, dirt, sand, terracotta, and concrete powder.")
                .translation("dimensionalpha.configuration.generation.dimension_alpha_overworld_resources")
                .worldRestart()
                .define("dimension_alpha_overworld_resources", true);

        dimensionAlphaNetherResources = builder
                .comment("Whether Dimension Alpha generates Nether-themed resources such as netherrack ores, quartz, ancient debris, blackstone, basalt, and related blocks.")
                .translation("dimensionalpha.configuration.generation.dimension_alpha_nether_resources")
                .worldRestart()
                .define("dimension_alpha_nether_resources", true);

        dimensionAlphaEndResources = builder
                .comment("Whether Dimension Alpha generates End-themed resources such as end stone ore variants.")
                .translation("dimensionalpha.configuration.generation.dimension_alpha_end_resources")
                .worldRestart()
                .define("dimension_alpha_end_resources", true);

        netherRealmOres = builder
                .comment("Whether this mod adds its netherrack ore variants to Nether biomes.")
                .translation("dimensionalpha.configuration.generation.nether_realm_ores")
                .worldRestart()
                .define("nether_realm_ores", true);

        endRealmOres = builder
                .comment("Whether this mod adds its end stone ore variants to End biomes.")
                .translation("dimensionalpha.configuration.generation.end_realm_ores")
                .worldRestart()
                .define("end_realm_ores", true);

        dimensionAlphaExternalResources = builder
                .comment("Whether optional compat resources from loaded integration mods generate in Dimension Alpha.")
                .translation("dimensionalpha.configuration.generation.dimension_alpha_external_resources")
                .worldRestart()
                .define("dimension_alpha_external_resources", true);

        builder.pop();
    }

    public static int alphaTeleporterY() {
        return COMMON.alphaTeleporterY.get();
    }

    public static int overworldSearchRadius() {
        return COMMON.overworldSearchRadius.get();
    }

    public static boolean removeLinkedTeleporterOnBreak() {
        return COMMON.removeLinkedTeleporterOnBreak.get();
    }

    public static boolean dimensionAlphaOverworldResources() {
        return COMMON.dimensionAlphaOverworldResources.get();
    }

    public static boolean dimensionAlphaNetherResources() {
        return COMMON.dimensionAlphaNetherResources.get();
    }

    public static boolean dimensionAlphaEndResources() {
        return COMMON.dimensionAlphaEndResources.get();
    }

    public static boolean netherRealmOres() {
        return COMMON.netherRealmOres.get();
    }

    public static boolean endRealmOres() {
        return COMMON.endRealmOres.get();
    }

    public static boolean dimensionAlphaExternalResources() {
        return COMMON.dimensionAlphaExternalResources.get();
    }
}
