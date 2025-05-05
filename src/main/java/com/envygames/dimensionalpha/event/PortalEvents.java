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
        // only main‐hand, server side
        if (evt.getHand() != InteractionHand.MAIN_HAND || evt.getLevel().isClientSide())
            return;

        BlockPos pos = evt.getPos();
        BlockState state = evt.getLevel().getBlockState(pos);
        if (!state.is(ModBlocks.DIMENSION_ALPHA_TELEPORTER.get()))
            return;

        ServerLevel here = (ServerLevel) evt.getLevel();
        ServerPlayer player = (ServerPlayer) evt.getEntity();

        TeleporterBlockEntity be = (TeleporterBlockEntity) here.getBlockEntity(pos);
        if (be == null) return;

        // pick the opposite dimension
        ServerLevel dest = (here.dimension() == ALPHA_KEY)
                ? here.getServer().getLevel(Level.OVERWORLD)
                : here.getServer().getLevel(ALPHA_KEY);
        if (dest == null) return;

        // if not yet linked, place the partner pad & link
        if (!be.isLinked()) {
            BlockPos destPos;
            if (dest.dimension() == ALPHA_KEY) {
                // force pad at Y=270 in Alpha
                destPos = new BlockPos(pos.getX(), 270, pos.getZ());
            } else {
                // use heightmap in Overworld
                destPos = findTopSolidBlock(dest, pos.getX(), pos.getZ());
            }

            // place or replace the pad block
            BlockState destState = dest.getBlockState(destPos);
            if (!destState.is(ModBlocks.DIMENSION_ALPHA_TELEPORTER.get())) {
                dest.setBlockAndUpdate(
                        destPos,
                        ModBlocks.DIMENSION_ALPHA_TELEPORTER.get().defaultBlockState()
                );
            }

            // now safely fetch its block entity
            TeleporterBlockEntity destBE =
                    (TeleporterBlockEntity) dest.getBlockEntity(destPos);
            if (destBE == null) return;

            be.linkTo(dest.dimension(), destPos);
            destBE.linkTo(here.dimension(), pos);
        }

        // perform teleport: always stand one block above the pad
        ServerLevel targetLevel = Objects.requireNonNull(player.getServer())
                .getLevel(be.linkedDimension());
        if (targetLevel == null) return;

        BlockPos linkPos = be.linkedPosition();
        double ty = linkPos.getY() + 1.0;

        player.teleportTo(
                targetLevel,
                linkPos.getX() + 0.5,
                ty,
                linkPos.getZ() + 0.5,
                player.getYRot(),
                player.getXRot()
        );

        evt.setCancellationResult(InteractionResult.SUCCESS);
        evt.setCanceled(true);
    }

    private static BlockPos findTopSolidBlock(ServerLevel lvl, int x, int z) {
        int y = lvl.getHeightmapPos(
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                new BlockPos(x, 0, z)
        ).getY();
        return new BlockPos(x, y, z);
    }
}
