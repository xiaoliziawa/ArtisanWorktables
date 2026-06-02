package com.lirxowo.artisanworktables.common.tile;

import com.lirxowo.artisanworktables.ArtisanWorktablesMod;
import com.lirxowo.artisanworktables.ArtisanWorktablesModCommonConfig;
import com.lirxowo.artisanworktables.common.reference.EnumTier;
import com.lirxowo.artisanworktables.common.reference.EnumType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class WorktableBlockEntity
    extends BaseBlockEntity {

  public static final String NAME = "worktable";

  public WorktableBlockEntity(BlockPos pos, BlockState state) {
    // serialization
    super(
        ArtisanWorktablesMod.TileEntityTypes.WORKTABLE,
        pos,
        state,
        ArtisanWorktablesMod.getProxy().getTileDataService()
    );
  }

  public WorktableBlockEntity(BlockPos pos, BlockState state, EnumType type) {
    super(
        ArtisanWorktablesMod.TileEntityTypes.WORKTABLE,
        pos,
        state,
        ArtisanWorktablesMod.getProxy().getTileDataService(),
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
    return ArtisanWorktablesModCommonConfig.fluidCapacityWorktable;
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
