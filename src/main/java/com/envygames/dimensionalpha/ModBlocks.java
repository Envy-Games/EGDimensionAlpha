package com.envygames.dimensionalpha;

import com.envygames.dimensionalpha.block.DimensionAlphaTeleporterBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;


public class ModBlocks {
    // Create specialized registers under the dimensionalpha modid via DeferredRegister
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks("dimensionalpha");
    public static final DeferredRegister.Items  ITEMS  = DeferredRegister.createItems ("dimensionalpha");

    // === Dimension Alpha Blocks ===

    public static final DeferredBlock<DimensionAlphaTeleporterBlock> DIMENSION_ALPHA_TELEPORTER = BLOCKS.register(
                    "dimension_alpha_teleporter",
                    () -> new DimensionAlphaTeleporterBlock(
                            BlockBehaviour.Properties.of()
                                    .destroyTime(50.0f)           // obsidian‑like
                                    .explosionResistance(1200.0f)
                                    .sound(SoundType.METAL)
                                    .lightLevel(s -> 15)          // full‑bright
                    )
            );

    // === Netherrack Ores - Vanilla Variants ===

    public static final DeferredBlock<Block> NETHERRACK_COAL_ORE = BLOCKS.registerSimpleBlock(
                    "netherrack_coal_ore",
                    Properties.of()
                            .destroyTime(3.0f)                       // same as minecraft:coal_ore
                            .explosionResistance(3.0f)               // same as vanilla ores?
                            .requiresCorrectToolForDrops()           // Links to tags/block/tool.json to set tool type
                            .lootFrom(() -> Blocks.COAL_ORE)         // copy coal‐ore’s loot table (drops coal + Fortune) :contentReference[oaicite:0]{index=0}
                            .sound(SoundType.NETHERRACK)                  // The "ting" noise as its described
    );

    public static final DeferredBlock<Block> NETHERRACK_COPPER_ORE = BLOCKS.registerSimpleBlock(
                    "netherrack_copper_ore",
                    Properties.of()
                            .destroyTime(3.0f)
                            .explosionResistance(3.0f)
                            .requiresCorrectToolForDrops()
                            .lootFrom(() -> Blocks.COPPER_ORE)
                            .sound(SoundType.NETHERRACK)
    );

    public static final DeferredBlock<Block> NETHERRACK_DIAMOND_ORE = BLOCKS.registerSimpleBlock(
            "netherrack_diamond_ore",
            Properties.of()
                    .destroyTime(3.0f)
                    .explosionResistance(3.0f)
                    .requiresCorrectToolForDrops()
                    .lootFrom(() -> Blocks.DEEPSLATE_DIAMOND_ORE)
                    .sound(SoundType.NETHERRACK)
    );

    public static final DeferredBlock<Block> NETHERRACK_EMERALD_ORE = BLOCKS.registerSimpleBlock(
            "netherrack_emerald_ore",
            Properties.of()
                    .destroyTime(3.0f)
                    .explosionResistance(3.0f)
                    .requiresCorrectToolForDrops()
                    .lootFrom(() -> Blocks.DEEPSLATE_EMERALD_ORE)
                    .sound(SoundType.NETHERRACK)
    );

    public static final DeferredBlock<Block> NETHERRACK_LAPIS_ORE = BLOCKS.registerSimpleBlock(
            "netherrack_lapis_ore",
            Properties.of()
                    .destroyTime(3.0f)
                    .explosionResistance(3.0f)
                    .requiresCorrectToolForDrops()
                    .lootFrom(() -> Blocks.LAPIS_ORE)
                    .sound(SoundType.NETHERRACK)
    );

    public static final DeferredBlock<Block> NETHERRACK_IRON_ORE = BLOCKS.registerSimpleBlock(
            "netherrack_iron_ore",
            Properties.of()
                    .destroyTime(3.0f)
                    .explosionResistance(3.0f)
                    .requiresCorrectToolForDrops()
                    .lootFrom(() -> Blocks.IRON_ORE)
                    .sound(SoundType.NETHERRACK)
    );

    public static final DeferredBlock<Block> NETHERRACK_REDSTONE_ORE = BLOCKS.registerSimpleBlock(
            "netherrack_redstone_ore",
            Properties.of()
                    .destroyTime(3.0f)
                    .explosionResistance(3.0f)
                    .requiresCorrectToolForDrops()
                    .lootFrom(() -> Blocks.REDSTONE_ORE)
                    .sound(SoundType.NETHERRACK)
    );

    // === Endstone Ores - Vanilla Variants ===

    public static final DeferredBlock<Block> ENDSTONE_COAL_ORE = BLOCKS.registerSimpleBlock(
            "endstone_coal_ore",
            Properties.of()
                    .destroyTime(3.0f)
                    .explosionResistance(3.0f)
                    .requiresCorrectToolForDrops()
                    .lootFrom(() -> Blocks.COAL_ORE)
                    .sound(SoundType.STONE)
    );
    public static final DeferredBlock<Block> ENDSTONE_IRON_ORE = BLOCKS.registerSimpleBlock(
            "endstone_iron_ore",
            Properties.of()
                    .destroyTime(3.0f)
                    .explosionResistance(3.0f)
                    .requiresCorrectToolForDrops()
                    .lootFrom(() -> Blocks.IRON_ORE)
                    .sound(SoundType.STONE)
    );
    public static final DeferredBlock<Block> ENDSTONE_EMERALD_ORE = BLOCKS.registerSimpleBlock(
            "endstone_emerald_ore",
            Properties.of()
                    .destroyTime(3.0f)
                    .explosionResistance(3.0f)
                    .requiresCorrectToolForDrops()
                    .lootFrom(() -> Blocks.EMERALD_ORE)
                    .sound(SoundType.STONE)
    );
    public static final DeferredBlock<Block> ENDSTONE_DIAMOND_ORE = BLOCKS.registerSimpleBlock(
            "endstone_diamond_ore",
            Properties.of()
                    .destroyTime(3.0f)
                    .explosionResistance(3.0f)
                    .requiresCorrectToolForDrops()
                    .lootFrom(() -> Blocks.DIAMOND_ORE)
                    .sound(SoundType.STONE)
    );

    // Register its BlockItem so it shows up in inventories
    // BlockItems for the DimensionAlpha Blocks (Teleporter)
    public static final DeferredItem<BlockItem> DIMENSION_ALPHA_TELEPORTER_ITEM =
            ITEMS.registerSimpleBlockItem("dimension_alpha_teleporter", DIMENSION_ALPHA_TELEPORTER);
    // BlockItems for the Netherrack Ores
    public static final DeferredItem<BlockItem> NETHERRACK_COAL_ORE_ITEM =
            ITEMS.registerSimpleBlockItem("netherrack_coal_ore", NETHERRACK_COAL_ORE);
    public static final DeferredItem<BlockItem> NETHERRACK_COPPER_ORE_ITEM =
            ITEMS.registerSimpleBlockItem("netherrack_copper_ore", NETHERRACK_COPPER_ORE);
    public static final DeferredItem<BlockItem> NETHERRACK_DIAMOND_ORE_ITEM =
            ITEMS.registerSimpleBlockItem("netherrack_diamond_ore", NETHERRACK_DIAMOND_ORE);
    public static final DeferredItem<BlockItem> NETHERRACK_EMERALD_ORE_ITEM =
            ITEMS.registerSimpleBlockItem("netherrack_emerald_ore", NETHERRACK_EMERALD_ORE);
    public static final DeferredItem<BlockItem> NETHERRACK_LAPIS_ORE_ITEM =
            ITEMS.registerSimpleBlockItem("netherrack_lapis_ore", NETHERRACK_LAPIS_ORE);
    public static final DeferredItem<BlockItem> NETHERRACK_IRON_ORE_ITEM =
            ITEMS.registerSimpleBlockItem("netherrack_iron_ore", NETHERRACK_IRON_ORE);
    public static final DeferredItem<BlockItem> NETHERRACK_REDSTONE_ORE_ITEM =
            ITEMS.registerSimpleBlockItem("netherrack_redstone_ore", NETHERRACK_REDSTONE_ORE);
    // BlockItems for the Endstone Ores
    public static final DeferredItem<BlockItem> ENDSTONE_COAL_ORE_ITEM =
            ITEMS.registerSimpleBlockItem("endstone_coal_ore", ENDSTONE_COAL_ORE);
    public static final DeferredItem<BlockItem> ENDSTONE_IRON_ORE_ITEM =
            ITEMS.registerSimpleBlockItem("endstone_iron_ore", ENDSTONE_IRON_ORE);
    public static final DeferredItem<BlockItem> ENDSTONE_EMERALD_ORE_ITEM =
            ITEMS.registerSimpleBlockItem("endstone_emerald_ore", ENDSTONE_EMERALD_ORE);
    public static final DeferredItem<BlockItem> ENDSTONE_DIAMOND_ORE_ITEM =
            ITEMS.registerSimpleBlockItem("endstone_diamond_ore", ENDSTONE_DIAMOND_ORE);
}
