package com.envygames.dimensionalpha.block;

import com.envygames.dimensionalpha.blockentity.TeleporterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The in‑world frame of the Dimension‑Alpha teleporter.
 *
 * ✓ Implements {@link EntityBlock} so Minecraft knows it carries a BlockEntity.
 * ✓ Very small – most logic lives in {@link TeleporterBlockEntity} or in your event handler.
 */
public class DimensionAlphaTeleporterBlock extends Block implements EntityBlock {

    /** Plain constructor – the {@code Properties} are supplied from ModBlocks. */
    public DimensionAlphaTeleporterBlock(BlockBehaviour.Properties props) {
        super(props);
    }

    /**
     * Factory method: every time the block appears in a chunk, create a fresh
     * {@link TeleporterBlockEntity}.  NeoForge calls this on world load and on placement.
     */
    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new TeleporterBlockEntity(pos, state);
    }

    /** Tell the renderer to use the baked block‑model JSON (normal blocks do this). */
    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

}
