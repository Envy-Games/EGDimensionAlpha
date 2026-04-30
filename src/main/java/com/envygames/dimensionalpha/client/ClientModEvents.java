package com.envygames.dimensionalpha.client;

import com.envygames.dimensionalpha.DimensionalphaMod;
import com.envygames.dimensionalpha.blockentity.ModBlockEntities;
import com.envygames.dimensionalpha.client.renderer.TeleporterBlockEntityRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = DimensionalphaMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.TELEPORTER.get(), TeleporterBlockEntityRenderer::new);
    }

    private ClientModEvents() {}
}
