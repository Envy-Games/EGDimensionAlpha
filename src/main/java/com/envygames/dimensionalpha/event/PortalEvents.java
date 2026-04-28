package com.envygames.dimensionalpha.event;

import com.envygames.dimensionalpha.DimensionalphaMod;
import com.envygames.dimensionalpha.ModBlocks;
import com.envygames.dimensionalpha.blockentity.TeleporterBlockEntity;
import com.envygames.dimensionalpha.config.DimensionalphaConfig;
import com.envygames.dimensionalpha.worldgen.ModDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = DimensionalphaMod.MOD_ID)
public class PortalEvents {
    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickBlock evt) {
        if (evt.getHand() != InteractionHand.MAIN_HAND || evt.getLevel().isClientSide()) return;

        BlockPos pos = evt.getPos();
        BlockState state = evt.getLevel().getBlockState(pos);
        if (!state.is(ModBlocks.DIMENSION_ALPHA_TELEPORTER.get())) return;

        ServerLevel here = (ServerLevel) evt.getLevel();
        ServerPlayer player = (ServerPlayer) evt.getEntity();

        if (!(here.getBlockEntity(pos) instanceof TeleporterBlockEntity be)) return;

        ServerLevel dest = isAlphaDimension(here)
                ? here.getServer().getLevel(Level.OVERWORLD)
                : here.getServer().getLevel(ModDimensions.ALPHA_LEVEL);
        if (dest == null) return;

        if (!be.isLinked()) {
            BlockPos destPos;
            if (isAlphaDimension(dest)) {
                destPos = new BlockPos(pos.getX(), DimensionalphaConfig.alphaTeleporterY(), pos.getZ());
                destPos = ensurePadAndHeadroom(dest, destPos);
            } else {
                destPos = pickOverworldSurfacePad(dest, pos.getX(), pos.getZ());
                destPos = ensurePadAndHeadroom(dest, destPos);
            }

            if (!(dest.getBlockEntity(destPos) instanceof TeleporterBlockEntity destBE)) return;

            be.linkTo(dest.dimension(), destPos);
            destBE.linkTo(here.dimension(), pos);
        }

        ResourceKey<Level> linkedDimension = be.linkedDimension();
        BlockPos linkPos = be.linkedPosition();
        if (linkedDimension == null || linkPos == null || player.getServer() == null) return;

        ServerLevel target = player.getServer().getLevel(linkedDimension);
        if (target == null) return;

        if (isAlphaDimension(target)) {
            int alphaTeleporterY = DimensionalphaConfig.alphaTeleporterY();
            if (linkPos.getY() != alphaTeleporterY) {
                linkPos = new BlockPos(linkPos.getX(), alphaTeleporterY, linkPos.getZ());
            }
            linkPos = ensurePadAndHeadroom(target, linkPos);
        } else if (Level.OVERWORLD.equals(target.dimension())) {
            linkPos = ensurePadAndHeadroom(target, linkPos);
        }

        player.teleportTo(
                target,
                linkPos.getX() + 0.5,
                linkPos.getY() + 1.0,
                linkPos.getZ() + 0.5,
                player.getYRot(),
                player.getXRot()
        );

        evt.setCancellationResult(InteractionResult.SUCCESS);
        evt.setCanceled(true);
    }

    private static boolean isAlphaDimension(ServerLevel level) {
        return ModDimensions.ALPHA_LEVEL.equals(level.dimension());
    }

    private static BlockPos pickOverworldSurfacePad(ServerLevel level, int x, int z) {
        int radius = DimensionalphaConfig.overworldSearchRadius();
        for (int distance = 0; distance <= radius; distance++) {
            for (int dx = -distance; dx <= distance; dx++) {
                for (int dz = -distance; dz <= distance; dz++) {
                    if (Math.max(Math.abs(dx), Math.abs(dz)) != distance) {
                        continue;
                    }

                    BlockPos pad = surfacePadAt(level, x + dx, z + dz);
                    if (isGoodSurface(level, pad)) return pad;
                }
            }
        }
        return surfacePadAt(level, x, z);
    }

    private static BlockPos surfacePadAt(ServerLevel level, int x, int z) {
        int yFree = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
        return new BlockPos(x, clampPadY(level, yFree - 1), z);
    }

    private static boolean isGoodSurface(ServerLevel level, BlockPos padPos) {
        BlockState below = level.getBlockState(padPos.below());
        boolean supportSolid = !below.getCollisionShape(level, padPos.below()).isEmpty();
        boolean dry = below.getFluidState().isEmpty();
        boolean headroom = isPassable(level, padPos.above()) && isPassable(level, padPos.above(2));
        return supportSolid && dry && headroom;
    }

    private static boolean isPassable(ServerLevel level, BlockPos pos) {
        return level.getBlockState(pos).getCollisionShape(level, pos).isEmpty();
    }

    private static BlockPos clampPadPos(ServerLevel level, BlockPos padPos) {
        return new BlockPos(padPos.getX(), clampPadY(level, padPos.getY()), padPos.getZ());
    }

    private static int clampPadY(ServerLevel level, int y) {
        return Math.max(level.getMinBuildHeight() + 1, Math.min(y, level.getMaxBuildHeight() - 3));
    }

    // Load/generate the destination chunk only during active travel, then ensure the landing pad is usable.
    private static BlockPos ensurePadAndHeadroom(ServerLevel level, BlockPos requestedPadPos) {
        BlockPos padPos = clampPadPos(level, requestedPadPos);
        level.getChunkAt(padPos);

        BlockPos support = padPos.below();
        BlockState below = level.getBlockState(support);
        if (!below.getFluidState().isEmpty() || below.getCollisionShape(level, support).isEmpty()) {
            level.setBlockAndUpdate(support, Blocks.STONE.defaultBlockState());
        }

        if (!level.getBlockState(padPos).is(ModBlocks.DIMENSION_ALPHA_TELEPORTER.get())) {
            level.setBlockAndUpdate(padPos, ModBlocks.DIMENSION_ALPHA_TELEPORTER.get().defaultBlockState());
        }

        BlockPos one = padPos.above();
        BlockPos two = one.above();
        if (!isPassable(level, one)) level.setBlockAndUpdate(one, Blocks.AIR.defaultBlockState());
        if (!isPassable(level, two)) level.setBlockAndUpdate(two, Blocks.AIR.defaultBlockState());
        return padPos;
    }
}
