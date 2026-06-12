package com.lirxowo.oraculumworktables.common.container.slot;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class CraftingExtraResultSlot
    extends ResultSlot {

  public CraftingExtraResultSlot(
      IItemHandler itemHandler,
      int index,
      int xPosition,
      int yPosition
  ) {

    super(itemHandler, index, xPosition, yPosition);
  }

  @Override
  public boolean mayPlace(@Nonnull ItemStack stack) {

    return false;
  }
}
