package com.envygames.dimensionalpha.block;

import com.envygames.dimensionalpha.blockentity.TeleporterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class DimensionAlphaTeleporterBlock extends Block implements EntityBlock {
    private static final Logger LOGGER = LogManager.getLogger("Dimensionalpha");

    public DimensionAlphaTeleporterBlock(BlockBehaviour.Properties props) {
        super(props);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new TeleporterBlockEntity(pos, state);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState oldState,
                         @NotNull Level world,
                         @NotNull BlockPos pos,
                         BlockState newState,
                         boolean isMoving) {
        try {
            // Only act when the block actually changes (Minecraft convention)…
            if (!oldState.is(newState.getBlock())) {
                // …and only on the server to avoid client-side duplication / NPEs.
                if (!world.isClientSide) {
                    BlockEntity be = world.getBlockEntity(pos);
                    if (be instanceof TeleporterBlockEntity tbe) {
                        final ResourceKey<Level> partnerDim = tbe.linkedDimension();
                        final BlockPos           partnerPos = tbe.linkedPosition();

                        LOGGER.info("Teleporter broken at {} in {}, clearing its link to {} at {}",
                                pos, world.dimension(), partnerDim, partnerPos);
                        tbe.clearLink();

                        if (partnerDim != null && partnerPos != null) {
                            ServerLevel destWorld = Objects.requireNonNull(world.getServer()).getLevel(partnerDim);
                            if (destWorld != null) {
                                // Avoid force-loading: only operate if the chunk is present
                                if (destWorld.isLoaded(partnerPos)) {
                                    BlockEntity partnerBE = destWorld.getBlockEntity(partnerPos);
                                    if (partnerBE instanceof TeleporterBlockEntity ptbe) {
                                        LOGGER.info("Clearing link on partner teleporter at {} in {}", partnerPos, partnerDim);
                                        ptbe.clearLink();

                                        // Only remove the partner block if it's actually the teleporter
                                        BlockState partnerState = destWorld.getBlockState(partnerPos);
                                        if (partnerState.getBlock() instanceof DimensionAlphaTeleporterBlock
                                            // or: partnerState.is(ModBlocks.TELEPORTER.get())
                                        ) {
                                            destWorld.setBlockAndUpdate(partnerPos, Blocks.AIR.defaultBlockState());
                                            LOGGER.info("Removed partner teleporter block at {} in {}", partnerPos, partnerDim);
                                        } else {
                                            LOGGER.warn("Partner at {} in {} is not a teleporter block; skipping removal",
                                                    partnerPos, partnerDim);
                                        }
                                    } else {
                                        LOGGER.warn("No teleporter BE found at partner pos {} in {}", partnerPos, partnerDim);
                                    }
                                } else {
                                    LOGGER.warn("Partner chunk not loaded at {} in {}; skipping partner cleanup to avoid force-load",
                                            partnerPos, partnerDim);
                                }
                            } else {
                                LOGGER.warn("Could not access partner dimension {}", partnerDim);
                            }
                        }
                    }
                }
            }
        } finally {
            super.onRemove(oldState, world, pos, newState, isMoving);
        }
    }
}
