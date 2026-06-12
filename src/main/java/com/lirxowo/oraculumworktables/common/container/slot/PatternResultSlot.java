package com.lirxowo.oraculumworktables.common.container.slot;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class PatternResultSlot
    extends PredicateEnabledSlot {

  private final Runnable slotChangeListener;

  public PatternResultSlot(
      Predicate predicate,
      Runnable slotChangeListener,
      IItemHandler itemHandler,
      int index,
      int xPosition,
      int yPosition
  ) {

    super(predicate, itemHandler, index, xPosition, yPosition);
    this.slotChangeListener = slotChangeListener;
  }

  @Override
  public void onTake(@Nonnull Player player, @Nonnull ItemStack stack) {

    this.slotChangeListener.run();
  }

  @Override
  public boolean mayPlace(@Nonnull ItemStack stack) {

    return false;
  }
}
