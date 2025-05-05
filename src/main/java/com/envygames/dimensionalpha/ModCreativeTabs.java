package com.envygames.dimensionalpha;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "dimensionalpha");

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> DIMENSION_ALPHA_TAB =
            CREATIVE_TABS.register("dimension_alpha_tab", () ->
                    CreativeModeTab.builder()
                            .title(Component.translatable("itemGroup.dimensionalpha.dimension_alpha_tab"))
                            .icon(() -> ModBlocks.DIMENSION_ALPHA_TELEPORTER_ITEM.get().getDefaultInstance())
                            .displayItems((parameters, output) -> {
                                output.accept(ModBlocks.DIMENSION_ALPHA_TELEPORTER_ITEM.get());
                                output.accept(ModBlocks.NETHERRACK_COAL_ORE_ITEM.get());
                                output.accept(ModBlocks.NETHERRACK_COPPER_ORE_ITEM.get());
                                output.accept(ModBlocks.NETHERRACK_DIAMOND_ORE_ITEM.get());
                                output.accept(ModBlocks.NETHERRACK_EMERALD_ORE_ITEM.get());
                                output.accept(ModBlocks.NETHERRACK_LAPIS_ORE_ITEM.get());
                                output.accept(ModBlocks.NETHERRACK_IRON_ORE_ITEM.get());
                                output.accept(ModBlocks.NETHERRACK_REDSTONE_ORE_ITEM.get());
                            })
                            .build()
            );
}
