package com.envygames.dimensionalpha.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class TeleporterBlockEntity extends BlockEntity implements GeoBlockEntity {
    private static final String NBT_DIM = "Dim";
    private static final String NBT_POS = "Pos";
    private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("teleporter.idle");

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);

    private @Nullable ResourceKey<Level> linkedDim;
    private @Nullable BlockPos linkedPos;

    public TeleporterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TELEPORTER.get(), pos, state);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "portal", 0, this::portalAnimation));
    }

    private PlayState portalAnimation(AnimationState<TeleporterBlockEntity> state) {
        return state.setAndContinue(IDLE_ANIMATION);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return animationCache;
    }

    public boolean isLinked() {
        return linkedDim != null && linkedPos != null;
    }

    public void linkTo(ResourceKey<Level> dim, BlockPos pos) {
        linkedDim = dim;
        linkedPos = pos.immutable();
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public @Nullable ResourceKey<Level> linkedDimension() {
        return linkedDim;
    }

    public @Nullable BlockPos linkedPosition() {
        return linkedPos;
    }

    public void clearLink() {
        linkedDim = null;
        linkedPos = null;
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookups) {
        super.saveAdditional(tag, lookups);
        ResourceKey<Level> dim = linkedDim;
        BlockPos pos = linkedPos;
        if (dim != null && pos != null) {
            tag.putString(NBT_DIM, dim.location().toString());
            tag.putLong(NBT_POS, pos.asLong());
        }
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookups) {
        super.loadAdditional(tag, lookups);
        if (tag.contains(NBT_DIM, Tag.TAG_STRING) && tag.contains(NBT_POS, Tag.TAG_LONG)) {
            ResourceLocation id = ResourceLocation.tryParse(tag.getString(NBT_DIM));
            if (id != null) {
                linkedDim = ResourceKey.create(Registries.DIMENSION, id);
                linkedPos = BlockPos.of(tag.getLong(NBT_POS));
                return;
            }
        }

        linkedDim = null;
        linkedPos = null;
    }
}
