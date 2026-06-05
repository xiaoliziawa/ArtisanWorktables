package com.lirxowo.oraculumworktables.common.tile;

import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import com.lirxowo.oraculumworktables.OraculumWorktablesModCommonConfig;
import com.lirxowo.oraculumworktables.common.reference.EnumTier;
import com.lirxowo.oraculumworktables.common.reference.EnumType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class WorkshopBlockEntity
    extends SecondaryInputBaseBlockEntity {

  public static final String NAME = "workshop";

  public WorkshopBlockEntity(BlockPos pos, BlockState state) {
    // serialization
    super(
        OraculumWorktablesMod.TileEntityTypes.WORKSHOP,
        pos,
        state,
        OraculumWorktablesMod.getProxy().getTileDataService()
    );
  }

  public WorkshopBlockEntity(BlockPos pos, BlockState state, EnumType type) {
    super(
        OraculumWorktablesMod.TileEntityTypes.WORKSHOP,
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
    return EnumTier.WORKSHOP;
  }

  @Override
  protected int getMaxToolCount() {
    return 3;
  }

  @Override
  protected int getFluidTankCapacity() {
    return OraculumWorktablesModCommonConfig.fluidCapacityWorkshop;
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
