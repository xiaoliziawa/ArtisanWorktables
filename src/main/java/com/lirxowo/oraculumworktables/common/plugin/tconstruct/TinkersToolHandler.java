package com.lirxowo.oraculumworktables.common.plugin.tconstruct;

import com.lirxowo.oraculumworktables.api.IToolHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Handles Tinkers' Construct tools, which use a custom NBT-based durability system
 * instead of vanilla item damage. Crucially, Tinkers tools do not get consumed when
 * they run out of durability; they enter a "broken" state and can be repaired.
 */
public class TinkersToolHandler
    implements IToolHandler {

  private static final Collection<ToolAction> TOOL_ACTIONS = TinkersToolHandler.getToolActions();

  @Override
  public boolean handles(ItemStack itemStack) {

    return itemStack.getItem() instanceof IModifiable;
  }

  @Override
  public boolean matches(ItemStack tool, ItemStack toolToMatch) {

    if (tool.getCount() != 1
        || !(tool.getItem() instanceof IModifiable)
        || toolToMatch.isEmpty()) {
      return false;
    }

    if (tool.getItem() == toolToMatch.getItem()) {
      return true;
    }

    ToolStack toolStack = ToolStack.from(tool);

    for (ToolAction toolAction : TOOL_ACTIONS) {

      if (ModifierUtil.canPerformAction(toolStack, toolAction)
          && toolToMatch.getItem().canPerformAction(toolToMatch, toolAction)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean canPerformAction(ItemStack itemStack, ToolAction toolAction) {

    return itemStack.getCount() == 1
        && itemStack.getItem() instanceof IModifiable
        && ModifierUtil.canPerformAction(ToolStack.from(itemStack), toolAction);
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

  private static Collection<ToolAction> getToolActions() {

    // Ensure Forge's stock tool actions exist before taking the live action view.
    ToolActions.DEFAULT_AXE_ACTIONS.size();
    return ToolAction.getActions();
  }
}
