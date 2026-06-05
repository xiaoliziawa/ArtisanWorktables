package com.lirxowo.oraculumworktables.common.block;

import com.lirxowo.oraculumworktables.common.reference.EnumTier;
import com.lirxowo.oraculumworktables.common.reference.EnumType;
import com.lirxowo.oraculumworktables.common.tile.WorktableBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class WorktableBlock
    extends BaseBlock {

  public static final VoxelShape VOXEL_SHAPE = Shapes.or(
      Block.box(0, 7, 0, 16, 15, 16), // top
      Block.box(1, 6, 1, 15, 7, 15), // inset
      Block.box(2, 2, 2, 4, 6, 4), // leg a
      Block.box(2, 2, 12, 4, 6, 14), // leg b
      Block.box(12, 2, 2, 14, 6, 4), // leg c
      Block.box(12, 2, 12, 14, 6, 14), // leg d
      Block.box(1, 0, 1, 5, 2, 5), // foot a
      Block.box(1, 0, 11, 5, 2, 15), // foot b
      Block.box(11, 0, 1, 15, 2, 5), // foot c
      Block.box(11, 0, 11, 15, 2, 15), // foot d
      Block.box(3, 3, 4, 4, 4, 12), // brace a
      Block.box(12, 3, 4, 13, 4, 12), // brace b
      Block.box(4, 3, 3, 12, 4, 4), // brace c
      Block.box(4, 3, 12, 12, 4, 13) // brace d
  );

  public WorktableBlock(EnumType type, SoundType soundType, float hardness, float resistance) {

    super(type, soundType, hardness, resistance);
  }

  @Override
  protected EnumTier getTier() {

    return EnumTier.WORKTABLE;
  }

  @Nonnull
  @ParametersAreNonnullByDefault
  @Override
  public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {

    return VOXEL_SHAPE;
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {

    return new WorktableBlockEntity(pos, state, this.getType());
  }
}
