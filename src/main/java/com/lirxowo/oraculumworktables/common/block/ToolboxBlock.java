package com.lirxowo.oraculumworktables.common.block;

import com.lirxowo.oraculumworktables.common.tile.ToolboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class ToolboxBlock
    extends ToolboxBaseBlock {

  public static final String NAME = "toolbox";

  public ToolboxBlock() {

    super(2, 3);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {

    return new ToolboxBlockEntity(pos, state);
  }
}
