package com.envygames.dimensionalpha;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.item.BlockItem;

public class ModBlocks {
    // Create specialized registers under your modid
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks("dimensionalpha");
    public static final DeferredRegister.Items  ITEMS  = DeferredRegister.createItems ("dimensionalpha");

    // Register the teleporter block
    public static final DeferredBlock<Block> DIMENSION_ALPHA_TELEPORTER = BLOCKS.registerSimpleBlock(
            "dimension_alpha_teleporter",
            BlockBehaviour.Properties.of()
                    .destroyTime(50.0f)                // 50 ticks to break
                    .explosionResistance(1200.0f)      // same as obsidian
                    .sound(SoundType.STONE)
                    .lightLevel(state -> 15)           // emits full light
    );

    // Register its BlockItem so it shows up in inventories
    public static final DeferredItem<BlockItem> DIMENSION_ALPHA_TELEPORTER_ITEM =
            ITEMS.registerSimpleBlockItem("dimension_alpha_teleporter", DIMENSION_ALPHA_TELEPORTER);
}
