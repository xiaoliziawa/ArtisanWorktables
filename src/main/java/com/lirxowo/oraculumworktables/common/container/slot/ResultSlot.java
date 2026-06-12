package com.lirxowo.oraculumworktables.common.container.slot;

import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public abstract class ResultSlot
    extends SlotItemHandler {

  public ResultSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {

    super(itemHandler, index, xPosition, yPosition);
  }
}
