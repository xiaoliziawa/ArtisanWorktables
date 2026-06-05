package com.lirxowo.oraculumworktables.common.recipe.serializer;

import com.lirxowo.oraculumworktables.common.recipe.ArtisanRecipeBuilder;
import com.lirxowo.oraculumworktables.common.recipe.ArtisanRecipeShapeless;
import com.lirxowo.oraculumworktables.common.reference.EnumType;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.util.GsonHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class RecipeSerializerShapelessJsonReader
    extends RecipeSerializerJsonReader<ArtisanRecipeShapeless> {

  private final EnumType type;

  public RecipeSerializerShapelessJsonReader(EnumType type, int maxWidth, int maxHeight) {

    super(maxWidth, maxHeight);
    this.type = type;
  }

  @Override
  @Nonnull
  public ArtisanRecipeShapeless read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {

    ArtisanRecipeBuilder builder = new ArtisanRecipeBuilder();
    this.read(builder, recipeId, json);

    NonNullList<Ingredient> ingredients = this.readIngredients(GsonHelper.getAsJsonArray(json, "ingredients"));

    if (ingredients.isEmpty()) {
      throw new JsonParseException("No ingredients for shapeless recipe");

    } else if (ingredients.size() > this.maxWidth * this.maxHeight) {
      throw new JsonParseException("Too many ingredients for shapeless recipe the max is " + (this.maxWidth * this.maxHeight));
    }

    try {
      return builder
          .setIngredients(ingredients)
          .buildShapeless(this.type);

    } catch (Exception e) {
      throw new JsonParseException("Error creating recipe: " + recipeId, e);
    }
  }

  private NonNullList<Ingredient> readIngredients(JsonArray ingredientArray) {

    NonNullList<Ingredient> result = NonNullList.create();

    for (int i = 0; i < ingredientArray.size(); ++i) {
      Ingredient ingredient = Ingredient.fromJson(ingredientArray.get(i));

      if (!ingredient.isEmpty()) {
        result.add(ingredient);
      }
    }

    return result;
  }
}
