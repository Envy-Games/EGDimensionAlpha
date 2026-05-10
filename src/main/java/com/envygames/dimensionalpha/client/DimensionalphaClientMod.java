package com.envygames.dimensionalpha.client;

import com.envygames.dimensionalpha.DimensionalphaMod;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(value = DimensionalphaMod.MOD_ID, dist = Dist.CLIENT)
public final class DimensionalphaClientMod {
    public DimensionalphaClientMod(IEventBus modBus) {
        modBus.addListener(ClientModEvents::registerRenderers);
    }
}
