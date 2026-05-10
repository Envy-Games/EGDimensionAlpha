package com.envygames.dimensionalpha.client;

import com.envygames.dimensionalpha.blockentity.ModBlockEntities;
import com.envygames.dimensionalpha.client.renderer.TeleporterBlockEntityRenderer;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public final class ClientModEvents {
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.TELEPORTER.get(), TeleporterBlockEntityRenderer::new);
    }

    private ClientModEvents() {}
}
