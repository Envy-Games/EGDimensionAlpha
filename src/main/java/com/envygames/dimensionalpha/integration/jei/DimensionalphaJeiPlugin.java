package com.envygames.dimensionalpha.integration.jei;

import com.envygames.dimensionalpha.DimensionalphaMod;
import com.envygames.dimensionalpha.ModBlocks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public final class DimensionalphaJeiPlugin implements IModPlugin {
    private static final ResourceLocation PLUGIN_ID = DimensionalphaMod.id("jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addIngredientInfo(
                ModBlocks.DIMENSION_ALPHA_TELEPORTER_ITEM.get().getDefaultInstance(),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.dimensionalpha.dimension_alpha_teleporter.description")
        );
    }
}
