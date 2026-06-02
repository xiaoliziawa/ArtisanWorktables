package com.lirxowo.artisanworktables.common.tile;

import com.lirxowo.artisanworktables.ArtisanWorktablesMod;
import com.lirxowo.artisanworktables.ArtisanWorktablesModCommonConfig;
import com.lirxowo.artisanworktables.common.reference.EnumTier;
import com.lirxowo.artisanworktables.common.reference.EnumType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class WorkshopBlockEntity
    extends SecondaryInputBaseBlockEntity {

  public static final String NAME = "workshop";

  public WorkshopBlockEntity(BlockPos pos, BlockState state) {
    // serialization
    super(
        ArtisanWorktablesMod.TileEntityTypes.WORKSHOP,
        pos,
        state,
        ArtisanWorktablesMod.getProxy().getTileDataService()
    );
  }

  public WorkshopBlockEntity(BlockPos pos, BlockState state, EnumType type) {
    super(
        ArtisanWorktablesMod.TileEntityTypes.WORKSHOP,
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
    return EnumTier.WORKSHOP;
  }

  @Override
  protected int getMaxToolCount() {
    return 3;
  }

  @Override
  protected int getFluidTankCapacity() {
    return ArtisanWorktablesModCommonConfig.fluidCapacityWorkshop;
  }

  @Override
  protected int getCraftingMatrixWidth() {
    return 5;
  }

  @Override
  protected int getCraftingMatrixHeight() {
    return 5;
  }
}
