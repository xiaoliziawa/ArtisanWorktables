package com.lirxowo.artisanworktables.common.container.slot;

import com.lirxowo.artisanworktables.common.tile.BaseBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class CraftingResultSlot
    extends SlotItemHandler {

  private final BaseBlockEntity tile;
  private final Runnable slotChangeListener;

  public CraftingResultSlot(
      BaseBlockEntity tile,
      Runnable slotChangeListener,
      IItemHandler itemHandler,
      int index,
      int xPosition,
      int yPosition
  ) {

    super(itemHandler, index, xPosition, yPosition);
    this.tile = tile;
    this.slotChangeListener = slotChangeListener;
  }

  @Override
  public boolean mayPlace(@Nonnull ItemStack stack) {

    return false;
  }

  @Override
  public void onTake(@Nonnull Player player, @Nonnull ItemStack stack) {

    this.tile.onTakeResult(player);
    this.slotChangeListener.run();
  }
}
