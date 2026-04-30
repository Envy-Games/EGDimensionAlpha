package com.envygames.dimensionalpha.client.renderer;

import com.envygames.dimensionalpha.blockentity.TeleporterBlockEntity;
import com.envygames.dimensionalpha.client.model.TeleporterBlockModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public final class TeleporterBlockEntityRenderer extends GeoBlockRenderer<TeleporterBlockEntity> {
    public TeleporterBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(new TeleporterBlockModel());
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
