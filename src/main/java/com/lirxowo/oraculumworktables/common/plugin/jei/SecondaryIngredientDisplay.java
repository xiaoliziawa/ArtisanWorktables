package com.lirxowo.oraculumworktables.common.plugin.jei;

import com.lirxowo.oraculumworktables.common.recipe.ArtisanRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;

final class SecondaryIngredientDisplay {

  private static final String TOOLTIP_CONSUMED_KEY = "jei.oraculum_worktables.secondary.consumed";
  private static final String TOOLTIP_NOT_CONSUMED_KEY = "jei.oraculum_worktables.secondary.not_consumed";
  private static final String LABEL_CONSUMED_KEY = "jei.oraculum_worktables.secondary.status.consumed";
  private static final String LABEL_NOT_CONSUMED_KEY = "jei.oraculum_worktables.secondary.status.not_consumed";
  private static final int CONSUMED_COLOR = 0xFFFF5555;
  private static final int NOT_CONSUMED_COLOR = 0xFF55FF55;

  private SecondaryIngredientDisplay() {
  }

  public static Component tooltip(ArtisanRecipe recipe) {

    return Component.translatable(tooltipKey(recipe)).withStyle(formatting(recipe));
  }

  public static String label(ArtisanRecipe recipe) {

    return I18n.get(labelKey(recipe));
  }

  public static int color(ArtisanRecipe recipe) {

    return recipe.consumeSecondaryIngredients() ? CONSUMED_COLOR : NOT_CONSUMED_COLOR;
  }

  private static String tooltipKey(ArtisanRecipe recipe) {

    return recipe.consumeSecondaryIngredients() ? TOOLTIP_CONSUMED_KEY : TOOLTIP_NOT_CONSUMED_KEY;
  }

  private static String labelKey(ArtisanRecipe recipe) {

    return recipe.consumeSecondaryIngredients() ? LABEL_CONSUMED_KEY : LABEL_NOT_CONSUMED_KEY;
  }

  private static ChatFormatting formatting(ArtisanRecipe recipe) {

    return recipe.consumeSecondaryIngredients() ? ChatFormatting.RED : ChatFormatting.GREEN;
  }
}
