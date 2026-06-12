package com.lirxowo.oraculumworktables.common.tile;

import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import com.lirxowo.oraculumworktables.OraculumWorktablesModCommonConfig;
import com.lirxowo.oraculumworktables.common.reference.EnumTier;
import com.lirxowo.oraculumworktables.common.reference.EnumType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class WorkstationBlockEntity
    extends SecondaryInputBaseBlockEntity {

  public static final String NAME = "workstation";

  public WorkstationBlockEntity(BlockPos pos, BlockState state) {
    // serialization
    super(
        OraculumWorktablesMod.TileEntityTypes.WORKSTATION,
        pos,
        state,
        OraculumWorktablesMod.getProxy().getTileDataService()
    );
  }

  public WorkstationBlockEntity(BlockPos pos, BlockState state, EnumType type) {
    super(
        OraculumWorktablesMod.TileEntityTypes.WORKSTATION,
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
    return EnumTier.WORKSTATION;
  }

  @Override
  protected int getMaxToolCount() {
    return 2;
  }

  @Override
  protected int getFluidTankCapacity() {
    return OraculumWorktablesModCommonConfig.fluidCapacityWorkstation;
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
