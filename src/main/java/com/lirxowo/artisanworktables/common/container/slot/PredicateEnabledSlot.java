package com.lirxowo.artisanworktables.common.container.slot;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class PredicateEnabledSlot
    extends SlotItemHandler {

  public interface Predicate {

    boolean isEnabled();
  }

  private final Predicate predicate;

  public PredicateEnabledSlot(
      Predicate predicate,
      IItemHandler itemHandler,
      int index,
      int xPosition,
      int yPosition
  ) {

    super(itemHandler, index, xPosition, yPosition);
    this.predicate = predicate;
  }

  @Nonnull
  @Override
  public ItemStack getItem() {

    if (this.isActive()) {
      return super.getItem();
    }

    return ItemStack.EMPTY;
  }

  @Override
  public void set(@Nonnull ItemStack stack) {

    if (this.isActive()) {
      super.set(stack);
    }
  }

  @Override
  public void onQuickCraft(@Nonnull ItemStack oldStack, @Nonnull ItemStack newStack) {

    if (this.isActive()) {
      super.onQuickCraft(oldStack, newStack);
    }
  }

  @Override
  public int getMaxStackSize() {

    if (this.isActive()) {
      return super.getMaxStackSize();
    }

    return 0;
  }

  @Override
  public int getMaxStackSize(@Nonnull ItemStack stack) {

    if (this.isActive()) {
      return super.getMaxStackSize(stack);
    }

    return 0;
  }

  @Override
  public boolean mayPickup(Player player) {

    if (this.isActive()) {
      return super.mayPickup(player);
    }

    return false;
  }

  @Nonnull
  @Override
  public ItemStack remove(int amount) {

    if (this.isActive()) {
      return super.remove(amount);
    }

    return ItemStack.EMPTY;
  }

  @Override
  public boolean isSameInventory(@Nonnull Slot other) {

    if (this.isActive()) {
      return super.isSameInventory(other);
    }

    return false;
  }

  @Override
  public boolean isActive() {

    return this.predicate.isEnabled();
  }
}
