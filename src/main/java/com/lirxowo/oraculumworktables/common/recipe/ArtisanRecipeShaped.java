package com.lirxowo.oraculumworktables.common.recipe;

import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import com.lirxowo.oraculumworktables.common.reference.EnumType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.List;

public class ArtisanRecipeShaped
    extends ArtisanRecipe {

  protected final boolean mirrored;
  protected final ShapedRecipePattern pattern;

  /* package */ ArtisanRecipeShaped(
      EnumType tableType,
      ResourceLocation recipeId,
      String group,
      NonNullList<ToolEntry> tools,
      ItemStack result,
      ShapedRecipePattern pattern,
      NonNullList<Ingredient> secondaryIngredients,
      boolean consumeSecondaryIngredients,
      FluidStack fluidIngredient,
      NonNullList<ExtraOutputChancePair> extraOutputs,
      boolean mirrored,
      int minimumTier,
      int maximumTier,
      int experienceRequired,
      int levelRequired,
      boolean consumeExperience,
      String craftSound
  ) {

    super(tableType, recipeId, group, tools, result, pattern.ingredients(), secondaryIngredients, consumeSecondaryIngredients, fluidIngredient, extraOutputs, minimumTier, maximumTier, experienceRequired, levelRequired, consumeExperience, craftSound);
    this.pattern = pattern;
    this.mirrored = mirrored;
  }

  public ShapedRecipePattern getPattern() {

    return this.pattern;
  }

  public boolean isMirrored() {

    return this.mirrored;
  }

  public int getWidth() {

    return this.pattern.width();
  }

  public int getHeight() {

    return this.pattern.height();
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {

    return this.getWidth() <= width && this.getHeight() <= height;
  }

  @Nonnull
  @Override
  public RecipeSerializer<?> getSerializer() {

    return OraculumWorktablesMod.getProxy().getRegisteredSerializersShaped().get(this.tableType);
  }

  @Nonnull
  @Override
  public RecipeType<?> getType() {

    return RecipeTypes.SHAPED_RECIPE_TYPES.get(this.tableType);
  }

  @Override
  public boolean matches(@Nonnull ArtisanInventory inventory, @Nonnull Level world) {

    if (!super.matches(inventory, world)) {
      return false;
    }

    ICraftingMatrixStackHandler craftingMatrix = inventory.getCraftingMatrix();
    int width = this.getWidth();
    int height = this.getHeight();

    for (int x = 0; x <= craftingMatrix.getWidth() - width; ++x) {

      for (int y = 0; y <= craftingMatrix.getHeight() - height; ++y) {

        if (this.checkMatch(this.ingredients, craftingMatrix, x, y, width, height, false)) {
          return true;
        }

        if (this.mirrored && this.checkMatch(this.ingredients, craftingMatrix, x, y, width, height, true)) {
          return true;
        }
      }
    }

    return false;
  }

  private boolean checkMatch(
      List<Ingredient> ingredients,
      ICraftingMatrixStackHandler craftingMatrix,
      int startX,
      int startY,
      int width,
      int height,
      boolean mirror
  ) {

    for (int x = 0; x < craftingMatrix.getWidth(); ++x) {

      for (int y = 0; y < craftingMatrix.getHeight(); ++y) {

        int subX = x - startX;
        int subY = y - startY;
        Ingredient ingredient = Ingredient.EMPTY;

        if (subX >= 0 && subY >= 0 && subX < width && subY < height) {

          if (mirror) {
            ingredient = ingredients.get(width - subX - 1 + subY * width);

          } else {
            ingredient = ingredients.get(subX + subY * width);
          }
        }

        if (!ingredient.test(craftingMatrix.getStackInSlot(x + y * craftingMatrix.getWidth()))) {
          return false;
        }
      }
    }

    return true;
  }
}
