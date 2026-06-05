package com.lirxowo.oraculumworktables.common.recipe;

import com.lirxowo.oraculumworktables.api.IToolHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ToolAction;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ToolEntry {

  private static final ThreadLocal<List<ItemStack>> TOOL_CACHE = ThreadLocal.withInitial(ArrayList::new);

  private static ItemStack cache(ItemStack itemStack) {

    if (itemStack.hasTag()) {
      return itemStack;
    }

    List<ItemStack> list = TOOL_CACHE.get();

    for (ItemStack stack : list) {

      if (stack.getItem() == itemStack.getItem()) {
        return stack;
      }
    }

    list.add(itemStack);
    return itemStack;
  }

  private final Ingredient tool;
  private final ItemStack[] toolItemStacks;
  private final int damage;
  private final boolean matchNbt;
  @Nullable
  private final ToolAction toolAction;

  public ToolEntry(Ingredient tool, int damage) {

    this(tool, damage, false);
  }

  public ToolEntry(Ingredient tool, int damage, boolean matchNbt) {

    this(tool, damage, matchNbt, null);
  }

  public ToolEntry(Ingredient tool, int damage, boolean matchNbt, @Nullable ToolAction toolAction) {

    this.tool = tool;
    this.damage = damage;
    this.matchNbt = matchNbt;
    this.toolAction = toolAction;

    ItemStack[] matchingStacks = tool.getItems();
    this.toolItemStacks = new ItemStack[matchingStacks.length];

    for (int i = 0; i < matchingStacks.length; i++) {
      this.toolItemStacks[i] = ToolEntry.cache(matchingStacks[i]);
    }
  }

  public ItemStack[] getToolStacks() {

    return this.toolItemStacks;
  }

  public Ingredient getTool() {

    return this.tool;
  }

  public int getDamage() {

    return this.damage;
  }

  public boolean matchNbt() {

    return this.matchNbt;
  }

  @Nullable
  public ToolAction getToolAction() {

    return this.toolAction;
  }

  public boolean matches(IToolHandler handler, ItemStack tool) {

    for (ItemStack toolItemStack : this.toolItemStacks) {

      if (handler.matches(tool, toolItemStack)
          && (!this.matchNbt || ToolEntry.nbtMatches(toolItemStack, tool))) {
        return true;
      }
    }

    return !this.matchNbt
        && this.toolAction != null
        && handler.canPerformAction(tool, this.toolAction);
  }

  /**
   * Compares the NBT of the expected and actual tool, ignoring durability (the
   * {@code Damage} tag) so that worn tools still match.
   */
  private static boolean nbtMatches(ItemStack expected, ItemStack actual) {

    return Objects.equals(ToolEntry.stripDamage(expected.getTag()), ToolEntry.stripDamage(actual.getTag()));
  }

  private static CompoundTag stripDamage(CompoundTag tag) {

    if (tag == null || tag.isEmpty()) {
      return null;
    }

    if (!tag.contains("Damage")) {
      return tag;
    }

    CompoundTag copy = tag.copy();
    copy.remove("Damage");
    return copy.isEmpty() ? null : copy;
  }
}
