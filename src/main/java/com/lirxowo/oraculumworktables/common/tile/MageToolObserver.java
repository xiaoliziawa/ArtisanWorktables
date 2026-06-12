package com.lirxowo.oraculumworktables.common.tile;

import com.lirxowo.oraculumworktables.common.block.MageBaseBlock;
import com.lirxowo.oraculum.inventory.spi.ObservableStackHandler;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemStackHandler;

public class MageToolObserver
    implements ObservableStackHandler.IContentsChangedEventHandler {

  private final BaseBlockEntity tile;

  public MageToolObserver(BaseBlockEntity tile) {

    this.tile = tile;
  }

  @Override
  public void onContentsChanged(ItemStackHandler stackHandler, int slotIndex) {

    this.checkToolState(stackHandler);
  }

  public void checkToolState(ItemStackHandler stackHandler) {

    Level world = this.tile.getLevel();

    if (world == null || world.isClientSide) {
      return;
    }

    BlockPos pos = this.tile.getBlockPos();
    BlockState blockState = world.getBlockState(pos);

    if (blockState.getBlock() instanceof MageBaseBlock) {
      boolean empty = true;

      for (int i = 0; i < stackHandler.getSlots(); i++) {

        if (!stackHandler.getStackInSlot(i).isEmpty()) {
          empty = false;
          break;
        }
      }

      if (empty && blockState.getValue(MageBaseBlock.ACTIVE)) {
        world.setBlock(pos, blockState.setValue(MageBaseBlock.ACTIVE, false), 3);

      } else if (!empty && !blockState.getValue(MageBaseBlock.ACTIVE)) {
        world.setBlock(pos, blockState.setValue(MageBaseBlock.ACTIVE, true), 3);
      }
    }
  }
}
