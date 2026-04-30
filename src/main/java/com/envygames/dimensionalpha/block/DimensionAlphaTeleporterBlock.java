package com.envygames.dimensionalpha.block;

import com.envygames.dimensionalpha.DimensionalphaMod;
import com.envygames.dimensionalpha.blockentity.TeleporterBlockEntity;
import com.envygames.dimensionalpha.config.DimensionalphaConfig;
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
    private static final Logger LOGGER = LogManager.getLogger(DimensionalphaMod.MOD_ID);

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
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public void onRemove(BlockState oldState,
                         @NotNull Level world,
                         @NotNull BlockPos pos,
                         BlockState newState,
                         boolean isMoving) {
        try {
            if (oldState.is(newState.getBlock()) || world.isClientSide) {
                return;
            }

            BlockEntity be = world.getBlockEntity(pos);
            if (!(be instanceof TeleporterBlockEntity tbe)) {
                return;
            }

            ResourceKey<Level> partnerDim = tbe.linkedDimension();
            BlockPos partnerPos = tbe.linkedPosition();

            LOGGER.info("Teleporter broken at {} in {}, clearing its link to {} at {}",
                    pos, world.dimension(), partnerDim, partnerPos);
            tbe.clearLink();

            if (partnerDim == null || partnerPos == null) {
                return;
            }

            ServerLevel destWorld = Objects.requireNonNull(world.getServer()).getLevel(partnerDim);
            if (destWorld == null) {
                LOGGER.warn("Could not access partner dimension {}", partnerDim);
                return;
            }

            if (!destWorld.isLoaded(partnerPos)) {
                LOGGER.warn("Partner chunk not loaded at {} in {}; skipping partner cleanup to avoid force-load",
                        partnerPos, partnerDim);
                return;
            }

            BlockEntity partnerBE = destWorld.getBlockEntity(partnerPos);
            if (!(partnerBE instanceof TeleporterBlockEntity ptbe)) {
                LOGGER.warn("No teleporter BE found at partner pos {} in {}", partnerPos, partnerDim);
                return;
            }

            LOGGER.info("Clearing link on partner teleporter at {} in {}", partnerPos, partnerDim);
            ptbe.clearLink();

            if (!DimensionalphaConfig.removeLinkedTeleporterOnBreak()) {
                return;
            }

            BlockState partnerState = destWorld.getBlockState(partnerPos);
            if (partnerState.getBlock() instanceof DimensionAlphaTeleporterBlock) {
                destWorld.setBlockAndUpdate(partnerPos, Blocks.AIR.defaultBlockState());
                LOGGER.info("Removed partner teleporter block at {} in {}", partnerPos, partnerDim);
            } else {
                LOGGER.warn("Partner at {} in {} is not a teleporter block; skipping removal",
                        partnerPos, partnerDim);
            }
        } finally {
            super.onRemove(oldState, world, pos, newState, isMoving);
        }
    }
}
