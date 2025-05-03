package com.envygames.dimensionalpha.event;

import com.envygames.dimensionalpha.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = "dimensionalpha")
public class PortalEvents {
    private static final ResourceKey<Level> ALPHA_KEY =
            ResourceKey.create(
                    Registries.DIMENSION,
                    // public factory instead of the private constructor
                    ResourceLocation.parse("dimensionalpha:eg_dimension_alpha")
            );

    // NBT tag under which we'll stash the original Overworld pad coords
    private static final String ORIGIN_DATA_TAG = "dimensionalpha:origin_pos";

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickBlock evt) {
        // only main‐hand on server
        if (evt.getHand() != InteractionHand.MAIN_HAND || evt.getLevel().isClientSide()) {
            return;
        }

        BlockPos pos = evt.getPos();
        BlockState state = evt.getLevel().getBlockState(pos);
        // only fire on your teleporter block
        if (!state.is(ModBlocks.DIMENSION_ALPHA_TELEPORTER.get())) {
            return;
        }

        ServerLevel current = (ServerLevel) evt.getLevel();
        MinecraftServer server = current.getServer();
        ResourceKey<Level> here = current.dimension();
        // decide destination dimension
        ServerLevel dest = (here == ALPHA_KEY)
                ? server.getLevel(Level.OVERWORLD)
                : server.getLevel(ALPHA_KEY);

        if (dest == null) {
            return;
        }

        // ensure we have the server‐side player
        if (!(evt.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        double targetX, targetY, targetZ;
        CompoundTag allData = player.getPersistentData();
        CompoundTag originData = allData.contains(ORIGIN_DATA_TAG)
                ? allData.getCompound(ORIGIN_DATA_TAG)
                : new CompoundTag();

        if (here != ALPHA_KEY) {
            // ───────────────────────────
            // Overworld → Alpha:
            //   • record the Overworld pad's BlockPos in NBT
            //   • teleport into Alpha at Y=281, same X/Z
            originData.putInt("x", pos.getX());
            originData.putInt("y", pos.getY());
            originData.putInt("z", pos.getZ());
            allData.put(ORIGIN_DATA_TAG, originData);

            targetX = pos.getX() + 0.5;
            targetY = 281.0;             // one block above your Alpha pad at Y=280
            targetZ = pos.getZ() + 0.5;
        } else {
            // ───────────────────────────
            // Alpha → Overworld:
            //   • pull back the saved pad coords
            //   • teleport back to that exact pad (one block up so you land on it)
            int ox = originData.getInt("x");
            int oy = originData.getInt("y");
            int oz = originData.getInt("z");
            targetX = ox + 0.5;
            targetY = oy + 1.0;
            targetZ = oz + 0.5;

            // clear out the saved data if you like:
            allData.remove(ORIGIN_DATA_TAG);
        }

        // do the teleport
        player.teleportTo(dest,
                targetX, targetY, targetZ,
                player.getYRot(), player.getXRot());
        evt.setCanceled(true);
        evt.setCancellationResult(InteractionResult.SUCCESS);
    }
}
