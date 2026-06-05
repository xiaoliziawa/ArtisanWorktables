package com.lirxowo.oraculumworktables.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolAction;

import javax.annotation.Nullable;

public interface IToolHandler {

  /**
   * @param itemStack the tool
   * @return true if this tool handler is responsible for the given tool
   */
  boolean handles(ItemStack itemStack);

  /**
   * @param tool        the tool
   * @param toolToMatch the tool to match
   * @return true if the given tool matches the tool to match
   */
  boolean matches(ItemStack tool, ItemStack toolToMatch);

  default boolean canPerformAction(ItemStack itemStack, ToolAction toolAction) {

    return itemStack.getItem().canPerformAction(itemStack, toolAction);
  }

  /**
   * @param itemStack the tool
   * @param damage    the damage to be applied to the tool
   * @return true if the tool can accept all given damage
   */
  boolean canAcceptAllDamage(ItemStack itemStack, int damage);

  /**
   * @param world     the world
   * @param itemStack the tool
   * @param damage    the damage
   * @param player    the player
   * @param simulate  if true, no damage will actually be applied
   * @return true if the tool is broken as a result of the applied damage
   */
  boolean applyDamage(Level world, ItemStack itemStack, int damage, @Nullable Player player, boolean simulate);

}
