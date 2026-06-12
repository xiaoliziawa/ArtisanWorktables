package com.lirxowo.oraculumworktables.common.container.slot;

import com.lirxowo.oraculum.inventory.spi.PredicateSlotItemHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class CraftingToolSlot
    extends PredicateSlotItemHandler {

  private final Runnable slotChangeListener;

  public CraftingToolSlot(
      Runnable slotChangeListener,
      Predicate<ItemStack> predicate,
      IItemHandler itemHandler,
      int index,
      int xPosition,
      int yPosition
  ) {

    super(predicate, itemHandler, index, xPosition, yPosition);
    this.slotChangeListener = slotChangeListener;
  }

  @Override
  public void setChanged() {

    super.setChanged();
    this.slotChangeListener.run();
  }

  @Override
  public boolean mayPlace(@Nonnull ItemStack stack) {

    return super.mayPlace(stack);
  }

  @Override
  public boolean mayPickup(Player player) {

    return super.mayPickup(player);
  }
}
