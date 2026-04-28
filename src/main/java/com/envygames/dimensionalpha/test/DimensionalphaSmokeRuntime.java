package com.envygames.dimensionalpha.test;

import com.envygames.dimensionalpha.DimensionalphaMod;
import com.envygames.dimensionalpha.ModBlocks;
import com.envygames.dimensionalpha.config.DimensionalphaConfig;
import com.envygames.dimensionalpha.event.PortalEvents;
import com.envygames.dimensionalpha.worldgen.ModDimensions;
import com.mojang.authlib.GameProfile;
import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

@EventBusSubscriber(modid = DimensionalphaMod.MOD_ID)
public final class DimensionalphaSmokeRuntime {
    private static final Logger LOGGER = LogManager.getLogger(DimensionalphaMod.MOD_ID);
    private static final String ENABLED_PROPERTY = "dimensionalpha.smokeTest";

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        if (!Boolean.getBoolean(ENABLED_PROPERTY)) {
            return;
        }

        MinecraftServer server = event.getServer();
        try {
            runSmoke(server);
            LOGGER.info("Dimension Alpha smoke test passed; stopping smoke server");
            server.halt(false);
        } catch (Throwable throwable) {
            LOGGER.error("Dimension Alpha smoke test failed", throwable);
            server.halt(true);
            throw new RuntimeException("Dimension Alpha smoke test failed", throwable);
        }
    }

    private static void runSmoke(MinecraftServer server) {
        ServerLevel overworld = require(server.getLevel(Level.OVERWORLD), "Overworld should be loaded");
        ServerLevel alpha = require(server.getLevel(ModDimensions.ALPHA_LEVEL), "Dimension Alpha should be loaded");
        alpha.getChunk(0, 0);

        BlockPos sourcePos = prepareSourceTeleporter(overworld);
        ServerPlayer player = createSmokePlayer(server, overworld);

        try {
            player.teleportTo(overworld, sourcePos.getX() + 0.5D, sourcePos.getY() + 1.0D, sourcePos.getZ() + 0.5D, 0.0F, 0.0F);
            useTeleporter(player, sourcePos);

            BlockPos alphaPad = new BlockPos(sourcePos.getX(), DimensionalphaConfig.alphaTeleporterY(), sourcePos.getZ());
            require(ModDimensions.ALPHA_LEVEL.equals(player.level().dimension()), "Player should travel to Dimension Alpha");
            require(alpha.getBlockState(alphaPad).is(ModBlocks.DIMENSION_ALPHA_TELEPORTER.get()), "Alpha destination pad should be created");

            useTeleporter(player, alphaPad);
            require(Level.OVERWORLD.equals(player.level().dimension()), "Player should return to the Overworld");
            require(player.blockPosition().distManhattan(sourcePos) <= 2, "Player should return near the source teleporter");
        } finally {
            server.getPlayerList().remove(player);
        }
    }

    private static BlockPos prepareSourceTeleporter(ServerLevel overworld) {
        BlockPos spawn = overworld.getSharedSpawnPos();
        int x = spawn.getX() + 4;
        int z = spawn.getZ() + 4;
        int y = clampPadY(overworld, overworld.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z));
        BlockPos sourcePos = new BlockPos(x, y, z);
        overworld.getChunkAt(sourcePos);
        overworld.setBlockAndUpdate(sourcePos.below(), Blocks.STONE.defaultBlockState());
        overworld.setBlockAndUpdate(sourcePos, ModBlocks.DIMENSION_ALPHA_TELEPORTER.get().defaultBlockState());
        overworld.setBlockAndUpdate(sourcePos.above(), Blocks.AIR.defaultBlockState());
        overworld.setBlockAndUpdate(sourcePos.above(2), Blocks.AIR.defaultBlockState());
        return sourcePos;
    }

    private static int clampPadY(ServerLevel level, int y) {
        return Math.max(level.getMinBuildHeight() + 1, Math.min(y, level.getMaxBuildHeight() - 3));
    }

    private static ServerPlayer createSmokePlayer(MinecraftServer server, ServerLevel level) {
        CommonListenerCookie cookie = CommonListenerCookie.createInitial(
                new GameProfile(UUID.randomUUID(), "dimension-alpha-smoke"),
                false
        );
        ServerPlayer player = new ServerPlayer(server, level, cookie.gameProfile(), cookie.clientInformation()) {
            @Override
            public boolean isSpectator() {
                return false;
            }

            @Override
            public boolean isCreative() {
                return true;
            }
        };
        Connection connection = new Connection(PacketFlow.SERVERBOUND);
        new EmbeddedChannel(connection);
        server.getPlayerList().placeNewPlayer(connection, player, cookie);
        return player;
    }

    private static void useTeleporter(ServerPlayer player, BlockPos pos) {
        PlayerInteractEvent.RightClickBlock event = new PlayerInteractEvent.RightClickBlock(
                player,
                InteractionHand.MAIN_HAND,
                pos,
                new BlockHitResult(Vec3.atCenterOf(pos), Direction.UP, pos, false)
        );
        PortalEvents.onRightClick(event);
    }

    private static <T> T require(T value, String message) {
        if (value == null) {
            throw new IllegalStateException(message);
        }
        return value;
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }

    private DimensionalphaSmokeRuntime() {
    }
}
