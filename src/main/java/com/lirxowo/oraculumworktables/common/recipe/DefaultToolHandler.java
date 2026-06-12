package com.lirxowo.oraculumworktables.common.recipe;

import com.lirxowo.oraculumworktables.api.IToolHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

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
    }

    if (world instanceof ServerLevel serverLevel) {
      // Authoritative durability change; respects the unbreaking enchantment and shrinks on break.
      itemStack.hurtAndBreak(damage, serverLevel, player, item -> {});
      return itemStack.isEmpty();
    }

    // Client-side prediction.
    int newDamage = itemStack.getDamageValue() + damage;

    if (newDamage >= itemStack.getMaxDamage()) {
      itemStack.shrink(1);
      return true;
    }

    itemStack.setDamageValue(newDamage);
    return false;
  }
}
