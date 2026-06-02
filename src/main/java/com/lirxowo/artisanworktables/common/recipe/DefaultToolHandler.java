package com.lirxowo.artisanworktables.common.recipe;

import com.lirxowo.artisanworktables.api.IToolHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import net.minecraft.util.RandomSource;

public class DefaultToolHandler
    implements IToolHandler {

  public static final DefaultToolHandler INSTANCE = new DefaultToolHandler();

  @Override
  public boolean handles(ItemStack itemStack) {

    return itemStack.isDamageableItem();
  }

  @Override
  public boolean matches(ItemStack tool, ItemStack toolToMatch) {

    return (tool.getItem() == toolToMatch.getItem());
  }

  @Override
  public boolean canAcceptAllDamage(ItemStack itemStack, int damage) {

    return (itemStack.getDamageValue() + damage <= itemStack.getMaxDamage());
  }

  @Override
  public boolean applyDamage(Level world, ItemStack itemStack, int damage, @Nullable Player player, boolean simulate) {

    if (simulate) {
      return (itemStack.getDamageValue() + damage > itemStack.getMaxDamage());

    } else {
      boolean broken = itemStack.hurt(damage, RandomSource.create(), null)
          || itemStack.getDamageValue() == itemStack.getMaxDamage();

      if (broken) {
        itemStack.shrink(1);
      }

      return broken;
    }
  }
}
