package com.envygames.dimensionalpha;

import com.envygames.dimensionalpha.blockentity.ModBlockEntities;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod("dimensionalpha")
public class DimensionalphaMod {
    public DimensionalphaMod(IEventBus modBus, ModContainer container) {
        // Register our blocks & block-items on the mod event bus
        ModBlocks.BLOCKS.register(modBus);
        ModBlocks.ITEMS .register(modBus);
        ModBlockEntities.register(modBus);
        ModCreativeTabs.CREATIVE_TABS.register(modBus);
    }
}
