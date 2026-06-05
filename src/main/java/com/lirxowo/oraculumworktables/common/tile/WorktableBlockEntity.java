package com.lirxowo.oraculumworktables.common.tile;

import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import com.lirxowo.oraculumworktables.OraculumWorktablesModCommonConfig;
import com.lirxowo.oraculumworktables.common.reference.EnumTier;
import com.lirxowo.oraculumworktables.common.reference.EnumType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class WorktableBlockEntity
    extends BaseBlockEntity {

  public static final String NAME = "worktable";

  public WorktableBlockEntity(BlockPos pos, BlockState state) {
    // serialization
    super(
        OraculumWorktablesMod.TileEntityTypes.WORKTABLE,
        pos,
        state,
        OraculumWorktablesMod.getProxy().getTileDataService()
    );
  }

  public WorktableBlockEntity(BlockPos pos, BlockState state, EnumType type) {
    super(
        OraculumWorktablesMod.TileEntityTypes.WORKTABLE,
        pos,
        state,
        OraculumWorktablesMod.getProxy().getTileDataService(),
        type
    );
  }

  // ---------------------------------------------------------------------------
  // Implementation
  // ---------------------------------------------------------------------------

  @Override
  public EnumTier getTableTier() {
    return EnumTier.WORKTABLE;
  }

  @Override
  protected int getMaxToolCount() {
    return 1;
  }

  @Override
  protected int getFluidTankCapacity() {
    return OraculumWorktablesModCommonConfig.fluidCapacityWorktable;
  }

  @Override
  protected int getCraftingMatrixWidth() {
    return 3;
  }

  @Override
  protected int getCraftingMatrixHeight() {
    return 3;
  }
}
