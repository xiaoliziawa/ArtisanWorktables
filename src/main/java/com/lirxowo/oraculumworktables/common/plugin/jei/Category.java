package com.lirxowo.oraculumworktables.common.plugin.jei;

import com.lirxowo.oraculumworktables.common.recipe.ArtisanRecipe;
import com.lirxowo.oraculumworktables.common.recipe.ArtisanRecipeShapeless;
import com.lirxowo.oraculumworktables.common.reference.EnumTier;
import com.lirxowo.oraculumworktables.common.reference.EnumType;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import javax.annotation.ParametersAreNonnullByDefault;

public class Category
    extends BaseCategory<ArtisanRecipe> {

  private final CategorySetupHandler categorySetupHandler;
  private final CategoryDrawHandler categoryDrawHandler;

  public Category(
      EnumTier tier,
      EnumType type,
      String titleKey,
      IDrawable background,
      IDrawable icon,
      RecipeType<ArtisanRecipe> recipeType,
      CategorySetupHandler categorySetupHandler,
      CategoryDrawHandler categoryDrawHandler
  ) {

    super(type, tier, titleKey, background, icon, recipeType);
    this.categorySetupHandler = categorySetupHandler;
    this.categoryDrawHandler = categoryDrawHandler;
  }

  @ParametersAreNonnullByDefault
  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, ArtisanRecipe recipe, IFocusGroup focuses) {

    this.categorySetupHandler.setup(recipe, builder);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void draw(ArtisanRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {

    this.background.draw(guiGraphics);
    this.categoryDrawHandler.draw(recipe, guiGraphics, this.background.getHeight());
  }

  @ParametersAreNonnullByDefault
  @Override
  public void getTooltip(ITooltipBuilder tooltip, ArtisanRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {

    if (recipe instanceof ArtisanRecipeShapeless) {

      int x = 117;
      int y = 4;

      if (this.tier == EnumTier.WORKSHOP) {
        x = 144;
        y = 29;
      }

      if (mouseX >= x && mouseX <= x + 9 && mouseY >= y && mouseY <= y + 9) {
        tooltip.add(Component.translatable("jei.oraculumworktables.tooltip.shapeless.recipe"));
      }
    }
  }
}
