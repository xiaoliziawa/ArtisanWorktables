package com.lirxowo.oraculumworktables.common.plugin.tetra;

import com.lirxowo.oraculumworktables.api.IToolHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import se.mickelus.tetra.TetraToolActions;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.properties.IToolProvider;

import javax.annotation.Nullable;
import java.util.Set;

public class TetraToolHandler
    implements IToolHandler {

  @Override
  public boolean handles(ItemStack itemStack) {

    return itemStack.getItem() instanceof IToolProvider;
  }

  @Override
  public boolean matches(ItemStack tool, ItemStack toolToMatch) {

    if (!(tool.getItem() instanceof IToolProvider toolProvider)
        || !toolProvider.canProvideTools(tool)) {
      return false;
    }

    if (tool.getItem() == toolToMatch.getItem()) {
      return true;
    }

    for (ToolAction toolAction : toolProvider.getTools(tool)) {

      if (toolToMatch.getItem().canPerformAction(toolToMatch, toolAction)) {
        return true;
      }

      if (toolAction == TetraToolActions.cut
          && TetraToolHandler.isSwordLike(toolToMatch)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean canPerformAction(ItemStack itemStack, ToolAction toolAction) {

    if (!(itemStack.getItem() instanceof IToolProvider toolProvider)
        || !toolProvider.canProvideTools(itemStack)) {
      return false;
    }

    Set<ToolAction> toolActions = toolProvider.getTools(itemStack);

    return toolActions.contains(toolAction)
        || TetraToolHandler.isSwordAction(toolAction)
        && toolActions.contains(TetraToolActions.cut);
  }

  @Override
  public boolean canAcceptAllDamage(ItemStack itemStack, int damage) {

    if (damage <= 0) {
      return true;
    }

    int maxDamage = itemStack.getMaxDamage();

    if (maxDamage <= 0) {
      return true;
    }

    int resultDamage = itemStack.getDamageValue() + damage;

    if (itemStack.getItem() instanceof IModularItem modularItem) {
      return !modularItem.isBroken(resultDamage, maxDamage);
    }

    return resultDamage < maxDamage;
  }

  @Override
  public boolean applyDamage(Level world, ItemStack itemStack, int damage, @Nullable Player player, boolean simulate) {

    if (damage <= 0 || itemStack.isEmpty()) {
      return false;
    }

    boolean wasBroken = TetraToolHandler.isBroken(itemStack);

    if (simulate) {
      return !wasBroken && !this.canAcceptAllDamage(itemStack, damage);
    }

    if (itemStack.getItem() instanceof IModularItem modularItem) {

      if (player != null) {
        modularItem.applyDamage(damage, itemStack, player);

      } else {
        TetraToolHandler.applyDamageWithoutEntity(itemStack, damage);
      }

      return !wasBroken && modularItem.isBroken(itemStack);
    }

    TetraToolHandler.applyDamageWithoutEntity(itemStack, damage);
    return !wasBroken && TetraToolHandler.isBroken(itemStack);
  }

  private static boolean isSwordLike(ItemStack itemStack) {

    return itemStack.getItem().canPerformAction(itemStack, ToolActions.SWORD_DIG)
        || itemStack.getItem().canPerformAction(itemStack, ToolActions.SWORD_SWEEP);
  }

  private static boolean isSwordAction(ToolAction toolAction) {

    return toolAction == ToolActions.SWORD_DIG
        || toolAction == ToolActions.SWORD_SWEEP;
  }

  private static boolean isBroken(ItemStack itemStack) {

    if (itemStack.getItem() instanceof IModularItem modularItem) {
      return modularItem.isBroken(itemStack);
    }

    int maxDamage = itemStack.getMaxDamage();
    return maxDamage > 0 && itemStack.getDamageValue() >= maxDamage - 1;
  }

  private static void applyDamageWithoutEntity(ItemStack itemStack, int damage) {

    int maxDamage = itemStack.getMaxDamage();

    if (maxDamage <= 0) {
      return;
    }

    itemStack.setDamageValue(Math.min(itemStack.getDamageValue() + damage, maxDamage - 1));
  }
}
