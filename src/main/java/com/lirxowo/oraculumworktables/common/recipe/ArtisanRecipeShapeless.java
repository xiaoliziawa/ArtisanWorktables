package com.lirxowo.oraculumworktables.common.recipe;

import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import com.lirxowo.oraculumworktables.common.reference.EnumType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ArtisanRecipeShapeless
    extends ArtisanRecipe {

      ArtisanRecipeShapeless(
      EnumType tableType,
      ResourceLocation recipeId,
      String group,
      NonNullList<ToolEntry> tools,
      ItemStack result,
      NonNullList<Ingredient> ingredients,
      NonNullList<Ingredient> secondaryIngredients,
      boolean consumeSecondaryIngredients,
      FluidStack fluidIngredient,
      NonNullList<ExtraOutputChancePair> extraOutputs,
      int minimumTier,
      int maximumTier,
      int experienceRequired,
      int levelRequired,
      boolean consumeExperience,
      String craftSound
  ) {

    super(tableType, recipeId, group, tools, result, ingredients, secondaryIngredients, consumeSecondaryIngredients, fluidIngredient, extraOutputs, minimumTier, maximumTier, experienceRequired, levelRequired, consumeExperience, craftSound);
  }

  @Nonnull
  @Override
  public RecipeSerializer<?> getSerializer() {

    return OraculumWorktablesMod.getProxy().getRegisteredSerializersShapeless().get(this.tableType);
  }

  @Nonnull
  @Override
  public RecipeType<?> getType() {

    return RecipeTypes.SHAPELESS_RECIPE_TYPES.get(this.tableType);
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {

    return this.ingredients.size() <= (width * height);
  }

  @Override
  public boolean matches(@Nonnull ArtisanInventory inventory, @Nonnull Level world) {

    if (!super.matches(inventory, world)) {
      return false;
    }

    ICraftingMatrixStackHandler craftingMatrix = inventory.getCraftingMatrix();

    int count = 0;
    List<ItemStack> itemList = new ArrayList<>();
    List<Ingredient> ingredients = this.getIngredients();

    for (int i = 0; i < craftingMatrix.getSlots(); i++) {
      ItemStack itemStack = craftingMatrix.getStackInSlot(i);

      if (!itemStack.isEmpty()) {
        count += 1;
        itemList.add(itemStack);
      }
    }

    if (count != ingredients.size()) {
      return false;
    }

    return RecipeMatcher.findMatches(itemList, ingredients) != null;
  }
}
