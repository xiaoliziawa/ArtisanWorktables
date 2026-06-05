package com.lirxowo.oraculumworktables.common.recipe;

import net.minecraft.world.item.crafting.Ingredient;

public class ToolIngredientEntry {

  private final Ingredient tool;
  private final int damage;

  public ToolIngredientEntry(Ingredient tool, int damage) {

    this.tool = tool;
    this.damage = damage;
  }

  public Ingredient getTool() {

    return this.tool;
  }

  public int getDamage() {

    return this.damage;
  }
}
