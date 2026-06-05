package com.lirxowo.oraculumworktables.common.block;

import com.lirxowo.oraculumworktables.common.reference.EnumTier;
import com.lirxowo.oraculumworktables.common.reference.EnumType;
import com.lirxowo.oraculumworktables.common.tile.WorktableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class MageWorktableBlock
    extends MageBaseBlock {

  public MageWorktableBlock(SoundType soundType, float hardness, float resistance) {

    super(EnumType.MAGE, soundType, hardness, resistance);
  }

  @Override
  protected EnumTier getTier() {

    return EnumTier.WORKTABLE;
  }

  @Nonnull
  @ParametersAreNonnullByDefault
  @Override
  public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {

    return WorktableBlock.VOXEL_SHAPE;
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {

    return new WorktableBlockEntity(pos, state, this.getType());
  }
}
