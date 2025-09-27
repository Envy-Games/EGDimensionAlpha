// src/main/java/com/envygames/dimensionalpha/event/PortalEvents.java
package com.envygames.dimensionalpha.event;

import com.envygames.dimensionalpha.ModBlocks;
import com.envygames.dimensionalpha.blockentity.TeleporterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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

import java.util.Objects;

@EventBusSubscriber(modid = "dimensionalpha")
public class PortalEvents {

    private static final ResourceKey<Level> ALPHA_KEY = ResourceKey.create(
            Registries.DIMENSION,
            ResourceLocation.parse("dimensionalpha:eg_dimension_alpha")
    );

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickBlock evt) {
        if (evt.getHand() != InteractionHand.MAIN_HAND || evt.getLevel().isClientSide()) return;

        final BlockPos pos = evt.getPos();
        final BlockState state = evt.getLevel().getBlockState(pos);
        if (!state.is(ModBlocks.DIMENSION_ALPHA_TELEPORTER.get())) return;

        final ServerLevel here = (ServerLevel) evt.getLevel();
        final ServerPlayer player = (ServerPlayer) evt.getEntity();

        final TeleporterBlockEntity be = (TeleporterBlockEntity) here.getBlockEntity(pos);
        if (be == null) return;

        final ServerLevel dest = (here.dimension() == ALPHA_KEY)
                ? here.getServer().getLevel(Level.OVERWORLD)
                : here.getServer().getLevel(ALPHA_KEY);
        if (dest == null) return;

        // Create link if missing
        if (!be.isLinked()) {
            BlockPos destPos;
            if (dest.dimension() == ALPHA_KEY) {
                // Overworld -> Alpha: fixed Y=270
                destPos = new BlockPos(pos.getX(), 270, pos.getZ());
                ensurePadAndHeadroom(dest, destPos);
            } else {
                // Alpha -> Overworld: choose safe surface near X/Z
                destPos = pickOverworldSurfacePad(dest, pos.getX(), pos.getZ());
                ensurePadAndHeadroom(dest, destPos);
            }

            TeleporterBlockEntity destBE = (TeleporterBlockEntity) dest.getBlockEntity(destPos);
            if (destBE == null) return;

            be.linkTo(dest.dimension(), destPos);
            destBE.linkTo(here.dimension(), pos);
        }

        // Resolve target and assert destination usability
        assert be.linkedDimension() != null;
        final ServerLevel target = Objects.requireNonNull(player.getServer()).getLevel(be.linkedDimension());
        if (target == null) return;

        BlockPos linkPos = be.linkedPosition();

        if (target.dimension() == ALPHA_KEY) {
            assert linkPos != null;
            if (linkPos.getY() != 270) linkPos = new BlockPos(linkPos.getX(), 270, linkPos.getZ());
            ensurePadAndHeadroom(target, linkPos);
        } else if (target.dimension() == Level.OVERWORLD) {
            // Do not relocate once linked; just assert pad/headroom at stored position
            ensurePadAndHeadroom(target, linkPos);
        }

        assert linkPos != null;
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

    // --- helpers ---

    // Choose a safe surface at/near x,z (bounded small search).
    private static BlockPos pickOverworldSurfacePad(ServerLevel level, int x, int z) {
        // Try exact column first, then a tiny ring set (deterministic order).
        int[][] OFF = {
                {0,0}, {1,0}, {-1,0}, {0,1}, {0,-1},
                {1,1}, {-1,1}, {1,-1}, {-1,-1},
                {2,0}, {-2,0}, {0,2}, {0,-2}
        };
        for (int[] d : OFF) {
            BlockPos pad = surfacePadAt(level, x + d[0], z + d[1]);
            if (isGoodSurface(level, pad)) return pad;
        }
        // Fallback: exact column even if suboptimal; will be asserted later.
        return surfacePadAt(level, x, z);
    }

    // Compute pad position for a column using MOTION_BLOCKING_NO_LEAVES.
    private static BlockPos surfacePadAt(ServerLevel level, int x, int z) {
        int yFree = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
        int padY = Math.max(level.getMinBuildHeight(), Math.min(yFree - 1, level.getMaxBuildHeight() - 1));
        return new BlockPos(x, padY, z);
    }

    // Dry solid support below, and two airlike blocks above.
    private static boolean isGoodSurface(ServerLevel level, BlockPos padPos) {
        BlockState below = level.getBlockState(padPos.below());
        boolean supportSolid = !below.getCollisionShape(level, padPos.below()).isEmpty();
        boolean dry = below.getFluidState().isEmpty();
        boolean headroom =
                isPassable(level, padPos.above()) &&
                        isPassable(level, padPos.above(2));
        return supportSolid && dry && headroom;
    }

    private static boolean isPassable(ServerLevel level, BlockPos pos) {
        return level.getBlockState(pos).getCollisionShape(level, pos).isEmpty();
    }

    // Minimal updates: load chunk, ensure support, set pad if needed, clear only obstructing headroom.
    private static void ensurePadAndHeadroom(ServerLevel level, BlockPos padPos) {
        level.getChunkAt(padPos);

        // Support
        BlockPos support = padPos.below();
        BlockState below = level.getBlockState(support);
        if (below.getFluidState().isEmpty() && !below.getCollisionShape(level, support).isEmpty()) {
            // ok
        } else {
            level.setBlockAndUpdate(support, Blocks.STONE.defaultBlockState());
        }

        // Pad
        if (!level.getBlockState(padPos).is(ModBlocks.DIMENSION_ALPHA_TELEPORTER.get())) {
            level.setBlockAndUpdate(padPos, ModBlocks.DIMENSION_ALPHA_TELEPORTER.get().defaultBlockState());
        }

        // Headroom (two blocks above pad)
        BlockPos one = padPos.above();
        BlockPos two = one.above();
        if (!isPassable(level, one)) level.setBlockAndUpdate(one, Blocks.AIR.defaultBlockState());
        if (!isPassable(level, two)) level.setBlockAndUpdate(two, Blocks.AIR.defaultBlockState());
    }
}
