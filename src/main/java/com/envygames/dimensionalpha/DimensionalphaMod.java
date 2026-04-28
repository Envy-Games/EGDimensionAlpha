package com.envygames.dimensionalpha;

import com.envygames.dimensionalpha.blockentity.ModBlockEntities;
import com.envygames.dimensionalpha.config.DimensionalphaConfig;
import com.envygames.dimensionalpha.worldgen.ModBiomeModifierSerializers;
import net.neoforged.bus.api.IEventBus;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(DimensionalphaMod.MOD_ID)
public class DimensionalphaMod {
    public static final String MOD_ID = "dimensionalpha";

    public DimensionalphaMod(IEventBus modBus, ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, DimensionalphaConfig.COMMON_SPEC);

        // Register our blocks & block-items on the mod event bus
        ModBlocks.BLOCKS.register(modBus);
        ModBlocks.ITEMS .register(modBus);
        ModBlockEntities.register(modBus);
        ModBiomeModifierSerializers.register(modBus);
        ModCreativeTabs.CREATIVE_TABS.register(modBus);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
