package com.lirxowo.oraculumworktables.common.block;

import com.lirxowo.oraculumworktables.common.reference.EnumTier;
import com.lirxowo.oraculumworktables.common.reference.EnumType;
import com.lirxowo.oraculumworktables.common.tile.WorkstationBlockEntity;
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

public class WorkstationBlock
    extends BaseBlock {

  public static final VoxelShape VOXEL_SHAPE = Shapes.or(
      Block.box(0, 8, 0, 16, 16, 16), // top
      Block.box(1, 7, 1, 15, 8, 15), // inset
      Block.box(3, 4, 3, 13, 7, 13), // leg
      Block.box(1, 0, 1, 15, 4, 15) // bottom
  );

  public WorkstationBlock(EnumType type, SoundType soundType, float hardness, float resistance) {

    super(type, soundType, hardness, resistance);
  }

  @Override
  protected EnumTier getTier() {

    return EnumTier.WORKSTATION;
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

    return new WorkstationBlockEntity(pos, state, this.getType());
  }
}
