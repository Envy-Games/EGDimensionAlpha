package com.envygames.dimensionalpha.blockentity;

import com.envygames.dimensionalpha.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlockEntities {

    /** Single deferred register that targets the vanilla BLOCK_ENTITY_TYPE registry */
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, "dimensionalpha");

    /** This is what a block entity register "DeferredHolder" looks like. This hooks into the main mod class. */
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TeleporterBlockEntity>> TELEPORTER =
            BLOCK_ENTITY_TYPES.register(
                    "dimension_alpha_teleporter",
                    () -> BlockEntityType.Builder
                            .of(TeleporterBlockEntity::new,
                                    ModBlocks.DIMENSION_ALPHA_TELEPORTER.get())
                            .build(null));

    /** Convenience hook for the main mod class */
    public static void register(IEventBus bus) {
        BLOCK_ENTITY_TYPES.register(bus);
    }

    private ModBlockEntities() {}  // no instantiation
}