package com.lirxowo.oraculumworktables.common.recipe;

import net.minecraft.world.item.crafting.Ingredient;

import java.util.Collection;

public interface ISecondaryIngredientMatcher {

  ISecondaryIngredientMatcher FALSE = requiredIngredients -> false;

  boolean matches(Collection<Ingredient> requiredIngredients);
}
