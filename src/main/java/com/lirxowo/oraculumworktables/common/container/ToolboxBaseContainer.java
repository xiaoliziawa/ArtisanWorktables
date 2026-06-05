package com.lirxowo.oraculumworktables.common.container;

import com.lirxowo.oraculumworktables.common.tile.ToolboxBlockEntity;
import com.lirxowo.oraculum.gui.ContainerBase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.Objects;

public abstract class ToolboxBaseContainer
    extends ContainerBase {

  private static final int NUM_ROWS = 3;

  private ToolboxBlockEntity tile;

  public ToolboxBaseContainer(MenuType<? extends ToolboxBaseContainer> containerType, int id, Level world, BlockPos blockPos, Inventory playerInventory, Player player) {

    super(containerType, id, playerInventory);

    this.tile = Objects.requireNonNull((ToolboxBlockEntity) world.getBlockEntity(blockPos));

    for (int j = 0; j < NUM_ROWS; ++j) {
      for (int k = 0; k < 9; ++k) {
        this.containerSlotAdd(new SlotItemHandler(this.tile.getItemStackHandler(), k + j * 9, 8 + k * 18, 17 + j * 18));
      }
    }

    this.containerPlayerInventoryAdd();
    this.containerPlayerHotbarAdd();
  }

  @Override
  public boolean stillValid(@Nonnull Player player) {

    return this.tile.canPlayerUse(player);
  }

  @Nonnull
  @Override
  public ItemStack quickMoveStack(@Nonnull Player player, int index) {

    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = this.slots.get(index);

    if (slot != null && slot.hasItem()) {
      ItemStack stackToTransfer = slot.getItem();
      itemstack = stackToTransfer.copy();

      if (index < NUM_ROWS * 9) {

        if (!this.moveItemStackTo(stackToTransfer, NUM_ROWS * 9, this.slots.size(), true)) {
          return ItemStack.EMPTY;
        }

      } else if (!this.moveItemStackTo(stackToTransfer, 0, NUM_ROWS * 9, false)) {
        return ItemStack.EMPTY;
      }

      if (stackToTransfer.isEmpty()) {
        slot.set(ItemStack.EMPTY);

      } else {
        slot.setChanged();
      }
    }

    return itemstack;
  }
}
