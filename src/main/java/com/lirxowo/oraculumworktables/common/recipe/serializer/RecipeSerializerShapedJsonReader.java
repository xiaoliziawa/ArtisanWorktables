package com.lirxowo.oraculumworktables.common.recipe.serializer;

import com.lirxowo.oraculumworktables.common.recipe.ArtisanRecipeBuilder;
import com.lirxowo.oraculumworktables.common.recipe.ArtisanRecipeShaped;
import com.lirxowo.oraculumworktables.common.reference.EnumType;
import com.lirxowo.oraculumworktables.common.util.RecipeSerializerHelper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.util.GsonHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Map;

public class RecipeSerializerShapedJsonReader
    extends RecipeSerializerJsonReader<ArtisanRecipeShaped> {

  private final EnumType type;

  public RecipeSerializerShapedJsonReader(EnumType type, int maxWidth, int maxHeight) {

    super(maxWidth, maxHeight);
    this.type = type;
  }

  @Override
  @Nonnull
  public ArtisanRecipeShaped read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {

    ArtisanRecipeBuilder builder = new ArtisanRecipeBuilder();
    this.read(builder, recipeId, json);

    Map<String, Ingredient> keys = RecipeSerializerHelper.deserializeKey(GsonHelper.getAsJsonObject(json, "key"));

    String[] pattern = RecipeSerializerHelper.shrink(RecipeSerializerHelper.patternFromJson(GsonHelper.getAsJsonArray(json, "pattern"), this.maxWidth, this.maxHeight));
    int width = pattern[0].length();
    int height = pattern.length;
    NonNullList<Ingredient> ingredients = RecipeSerializerHelper.deserializeIngredients(pattern, keys, width, height);

    boolean mirrored = GsonHelper.getAsBoolean(json, "mirrored", true);

    try {
      return builder
          .setIngredients(ingredients)
          .setMirrored(mirrored)
          .setWidth(width)
          .setHeight(height)
          .buildShaped(this.type);

    } catch (Exception e) {
      throw new JsonParseException("Error creating recipe: " + recipeId, e);
    }
  }
}
