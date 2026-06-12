package com.lirxowo.oraculumworktables.common.container.slot;

import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class CraftingIngredientSlot
    extends SlotItemHandler {

  private final Runnable slotChangeListener;

  public CraftingIngredientSlot(
      Runnable slotChangeListener,
      IItemHandler itemHandler,
      int index,
      int xPosition,
      int yPosition
  ) {

    super(itemHandler, index, xPosition, yPosition);
    this.slotChangeListener = slotChangeListener;
  }

  @Override
  public void setChanged() {

    super.setChanged();

    this.slotChangeListener.run();
  }
}
