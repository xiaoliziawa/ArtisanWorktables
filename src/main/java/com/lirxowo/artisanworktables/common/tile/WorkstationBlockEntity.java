package com.lirxowo.artisanworktables.common.tile;

import com.lirxowo.artisanworktables.ArtisanWorktablesMod;
import com.lirxowo.artisanworktables.ArtisanWorktablesModCommonConfig;
import com.lirxowo.artisanworktables.common.reference.EnumTier;
import com.lirxowo.artisanworktables.common.reference.EnumType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class WorkstationBlockEntity
    extends SecondaryInputBaseBlockEntity {

  public static final String NAME = "workstation";

  public WorkstationBlockEntity(BlockPos pos, BlockState state) {
    // serialization
    super(
        ArtisanWorktablesMod.TileEntityTypes.WORKSTATION,
        pos,
        state,
        ArtisanWorktablesMod.getProxy().getTileDataService()
    );
  }

  public WorkstationBlockEntity(BlockPos pos, BlockState state, EnumType type) {
    super(
        ArtisanWorktablesMod.TileEntityTypes.WORKSTATION,
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
    return EnumTier.WORKSTATION;
  }

  @Override
  protected int getMaxToolCount() {
    return 2;
  }

  @Override
  protected int getFluidTankCapacity() {
    return ArtisanWorktablesModCommonConfig.fluidCapacityWorkstation;
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
