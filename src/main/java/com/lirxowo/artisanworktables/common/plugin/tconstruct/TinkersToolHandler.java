package com.lirxowo.artisanworktables.common.plugin.tconstruct;

import com.lirxowo.artisanworktables.api.IToolHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.annotation.Nullable;

/**
 * Handles Tinkers' Construct tools, which use a custom NBT-based durability system
 * instead of vanilla item damage. Crucially, Tinkers tools do not get consumed when
 * they run out of durability; they enter a "broken" state and can be repaired.
 */
public class TinkersToolHandler
    implements IToolHandler {

  @Override
  public boolean handles(ItemStack itemStack) {

    return itemStack.getItem() instanceof IModifiable;
  }

  @Override
  public boolean matches(ItemStack tool, ItemStack toolToMatch) {

    return tool.getItem() == toolToMatch.getItem();
  }

  @Override
  public boolean canAcceptAllDamage(ItemStack itemStack, int damage) {

    ToolStack tool = ToolStack.from(itemStack);

    if (tool.isUnbreakable()) {
      return true;
    }

    return tool.getCurrentDurability() >= damage;
  }

  @Override
  public boolean applyDamage(Level world, ItemStack itemStack, int damage, @Nullable Player player, boolean simulate) {

    ToolStack tool = ToolStack.from(itemStack);

    if (tool.isUnbreakable()) {
      return false;
    }

    if (simulate) {
      return tool.getCurrentDurability() <= damage;
    }

    return ToolDamageUtil.damage(tool, damage, player, itemStack);
  }
}
