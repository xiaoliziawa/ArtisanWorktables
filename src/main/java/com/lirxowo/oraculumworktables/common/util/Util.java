package com.lirxowo.oraculumworktables.common.util;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import net.minecraft.util.RandomSource;

public class Util {

  public static final RandomSource RANDOM = RandomSource.create();

  /**
   * Spawns an {@link ItemStack} in the world, directly above the given position.
   * <p>
   * Server only.
   *
   * @param world     the world
   * @param itemStack the {@link ItemStack} to spawn
   * @param pos       the position to spawn
   * @author codetaylor
   */
  public static void spawnStackOnTop(Level world, ItemStack itemStack, BlockPos pos, double offsetY) {

    ItemEntity entityItem = new ItemEntity(
        world,
        pos.getX() + 0.5,
        pos.getY() + 0.5 + offsetY,
        pos.getZ() + 0.5,
        itemStack
    );
    entityItem.setDeltaMovement(0, 0.1, 0);

    world.addFreshEntity(entityItem);
  }

  /**
   * Container sensitive decrease stack.
   * <p>
   * ie. bucket
   *
   * @param itemStack      the {@link ItemStack}
   * @param amount         decrease amount
   * @param checkContainer check for container
   * @return the resulting {@link ItemStack}
   * @author codetaylor
   */
  public static ItemStack decrease(ItemStack itemStack, int amount, boolean checkContainer) {

    if (itemStack.isEmpty()) {
      return ItemStack.EMPTY;
    }

    if (itemStack.getCount() - amount <= 0) {

      if (checkContainer && itemStack.hasCraftingRemainingItem()) {
        return itemStack.getCraftingRemainingItem();

      } else {
        return ItemStack.EMPTY;
      }
    }

    itemStack.shrink(amount);

    return itemStack;
  }

  @Nonnull
  public static ItemStack getContainerItem(@Nonnull ItemStack stack) {

    if (stack.hasCraftingRemainingItem()) {
      stack = stack.getCraftingRemainingItem();

      if (!stack.isEmpty() && stack.isDamageableItem() && stack.getDamageValue() <= stack.getMaxDamage()) {
        return ItemStack.EMPTY;
      }
      return stack;
    }
    return ItemStack.EMPTY;
  }

}
