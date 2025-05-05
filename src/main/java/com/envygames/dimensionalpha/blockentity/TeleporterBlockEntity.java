package com.envygames.dimensionalpha.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TeleporterBlockEntity extends BlockEntity {

    private ResourceKey<Level> linkedDim;
    private BlockPos           linkedPos;

    public TeleporterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TELEPORTER.get(), pos, state);
    }

    /* ---------- helpers ---------- */

    public boolean isLinked() { return linkedDim != null && linkedPos != null; }

    public void linkTo(ResourceKey<Level> dim, BlockPos pos) {
        this.linkedDim = dim;
        this.linkedPos = pos.immutable();
        setChanged();
    }

    public ResourceKey<Level> linkedDimension() { return linkedDim; }
    public BlockPos           linkedPosition () { return linkedPos; }

    public void clearLink() {
        linkedDim = null;
        linkedPos = null;
        setChanged();
    }

    /* ---------- NBT ---------- */

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookups) {
        super.saveAdditional(tag, lookups);
        if (isLinked()) {
            tag.putString("Dim", linkedDim.location().toString());
            tag.putLong  ("Pos", linkedPos.asLong());
        }
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookups) {
        super.loadAdditional(tag, lookups);
        if (tag.contains("Dim") && tag.contains("Pos")) {
            linkedDim = ResourceKey.create(
                    Registries.DIMENSION,
                    Objects.requireNonNull(ResourceLocation.tryParse(tag.getString("Dim"))));
            linkedPos = BlockPos.of(tag.getLong("Pos"));
        }
    }
}
