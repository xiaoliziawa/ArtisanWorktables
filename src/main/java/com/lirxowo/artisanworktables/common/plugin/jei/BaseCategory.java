package com.lirxowo.artisanworktables.common.plugin.jei;

import com.lirxowo.artisanworktables.common.reference.EnumTier;
import com.lirxowo.artisanworktables.common.reference.EnumType;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;

import javax.annotation.Nonnull;

public abstract class BaseCategory<R>
    implements IRecipeCategory<R> {

  protected final EnumType type;
  protected final EnumTier tier;
  protected final String titleKey;
  protected final IDrawable background;
  protected final IDrawable icon;
  protected final RecipeType<R> recipeType;

  public BaseCategory(
      EnumType type,
      EnumTier tier,
      String titleKey,
      IDrawable background,
      IDrawable icon,
      RecipeType<R> recipeType
  ) {

    this.type = type;
    this.tier = tier;
    this.titleKey = titleKey;
    this.background = background;
    this.icon = icon;
    this.recipeType = recipeType;
  }

  @Nonnull
  @Override
  public RecipeType<R> getRecipeType() {

    return this.recipeType;
  }

  @Nonnull
  @Override
  public Component getTitle() {

    return Component.translatable(this.titleKey);
  }

  @Nonnull
  @Override
  public IDrawable getBackground() {

    return this.background;
  }

  @Nonnull
  @Override
  public IDrawable getIcon() {

    return this.icon;
  }
}
