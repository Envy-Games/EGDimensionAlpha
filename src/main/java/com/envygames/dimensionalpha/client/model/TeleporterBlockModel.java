package com.envygames.dimensionalpha.client.model;

import com.envygames.dimensionalpha.DimensionalphaMod;
import com.envygames.dimensionalpha.blockentity.TeleporterBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public final class TeleporterBlockModel extends DefaultedBlockGeoModel<TeleporterBlockEntity> {
    public TeleporterBlockModel() {
        super(DimensionalphaMod.id("dimension_alpha_teleporter"));
    }

    @Override
    public RenderType getRenderType(TeleporterBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(texture);
    }
}
