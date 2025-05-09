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
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
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
        if (!oldState.is(newState.getBlock())) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof TeleporterBlockEntity tbe) {
                // Capture partner teleport pad info
                ResourceKey<Level> partnerDim = tbe.linkedDimension();
                BlockPos partnerPos = tbe.linkedPosition();

                LOGGER.info("Teleporter broken at {} in {}, clearing its link to {} at {}",
                        pos, world.dimension(), partnerDim, partnerPos);
                tbe.clearLink();

                if (partnerDim != null && partnerPos != null) {
                    ServerLevel dest = Objects.requireNonNull(world.getServer()).getLevel(partnerDim);
                    if (dest != null) {
                        // Clear the partner teleport pad's link
                        BlockEntity partnerBE = dest.getBlockEntity(partnerPos);
                        if (partnerBE instanceof TeleporterBlockEntity ptbe) {
                            LOGGER.info("Clearing link on partner teleporter at {} in {}",
                                    partnerPos, partnerDim);
                            ptbe.clearLink();
                        } else {
                            LOGGER.warn("No teleporter BE found at partner pos {} in {}", partnerPos, partnerDim);
                        }

                        // *** REMOVE THE PARTNER TELEPORT PAD ***
                        dest.setBlockAndUpdate(partnerPos, Blocks.AIR.defaultBlockState());
                        LOGGER.info("Removed partner teleporter block at {} in {}", partnerPos, partnerDim);
                    } else {
                        LOGGER.warn("Could not load partner dimension {}", partnerDim);
                    }
                }
            }
        }

        super.onRemove(oldState, world, pos, newState, isMoving);
    }
}
