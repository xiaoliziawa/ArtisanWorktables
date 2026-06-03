package com.lirxowo.artisanworktables.common.recipe.serializer;

import com.lirxowo.artisanworktables.common.recipe.ArtisanRecipe;
import com.lirxowo.artisanworktables.common.recipe.ArtisanRecipeBuilder;
import com.lirxowo.artisanworktables.common.recipe.ToolEntry;
import com.lirxowo.artisanworktables.common.util.RecipeSerializerHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.util.GsonHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

public abstract class RecipeSerializerJsonReader<R extends ArtisanRecipe>
    implements IRecipeSerializerJsonReader<R> {

  protected final int maxWidth;
  protected final int maxHeight;

  public RecipeSerializerJsonReader(int maxWidth, int maxHeight) {

    this.maxWidth = maxWidth;
    this.maxHeight = maxHeight;
  }

  protected void read(ArtisanRecipeBuilder builder, ResourceLocation recipeId, JsonObject json) {

    String group = GsonHelper.getAsString(json, "group", "");

    ItemStack result = RecipeSerializerHelper.deserializeItem(GsonHelper.getAsJsonObject(json, "result"));
    NonNullList<ToolEntry> tools = this.deserializeTools(json);
    NonNullList<Ingredient> secondaryIngredients = this.deserializeSecondaryIngredients(json);
    boolean consumeSecondaryIngredients = GsonHelper.getAsBoolean(json, "consumeSecondaryIngredients", true);
    FluidStack fluidIngredient = this.deserializeFluidIngredient(json);
    NonNullList<ArtisanRecipe.ExtraOutputChancePair> extraOutput = this.deserializeExtraOutput(json);

    int minimumTier = GsonHelper.getAsInt(json, "minimumTier", 0);
    int maximumTier = GsonHelper.getAsInt(json, "maximumTier", 2);
    int experienceRequired = GsonHelper.getAsInt(json, "experienceRequired", 0);
    int levelRequired = GsonHelper.getAsInt(json, "levelRequired", 0);
    boolean consumeExperience = GsonHelper.getAsBoolean(json, "consumeExperience", true);

    builder
        .setRecipeId(recipeId)
        .setGroup(group)
        .setResult(result)
        .setTools(tools)
        .setSecondaryIngredients(secondaryIngredients)
        .setConsumeSecondaryIngredients(consumeSecondaryIngredients)
        .setFluidIngredient(fluidIngredient)
        .setExtraOutputs(extraOutput)
        .setMinimumTier(minimumTier)
        .setMaximumTier(maximumTier)
        .setExperienceRequired(experienceRequired)
        .setLevelRequired(levelRequired)
        .setConsumeExperience(consumeExperience);
  }

  protected NonNullList<ArtisanRecipe.ExtraOutputChancePair> deserializeExtraOutput(JsonObject json) {

    NonNullList<ArtisanRecipe.ExtraOutputChancePair> result = NonNullList.create();

    if (json.has("extraOutput")) {
      JsonArray jsonArray = GsonHelper.getAsJsonArray(json, "extraOutput");

      for (JsonElement jsonElement : jsonArray) {
        JsonObject itemObject = jsonElement.getAsJsonObject();
        ItemStack itemStack = RecipeSerializerHelper.deserializeItem(itemObject);
        float chance = GsonHelper.getAsFloat(itemObject, "chance", 1);
        result.add(new ArtisanRecipe.ExtraOutputChancePair(itemStack, chance));
      }
    }

    return result;
  }

  protected FluidStack deserializeFluidIngredient(@Nonnull JsonObject json) {

    FluidStack fluidIngredient;
    if (json.has("fluidIngredient")) {
      fluidIngredient = RecipeSerializerHelper.deserializeFluid(GsonHelper.getAsJsonObject(json, "fluidIngredient"), 1000);

    } else {
      fluidIngredient = FluidStack.EMPTY;
    }
    return fluidIngredient;
  }

  protected NonNullList<Ingredient> deserializeSecondaryIngredients(JsonObject json) {

    NonNullList<Ingredient> result = NonNullList.create();

    if (json.has("secondaryIngredients")) {
      JsonArray jsonArray = GsonHelper.getAsJsonArray(json, "secondaryIngredients");

      for (JsonElement jsonElement : jsonArray) {
        JsonObject ingredientObject = jsonElement.getAsJsonObject();
        Ingredient ingredient = Ingredient.fromJson(ingredientObject);
        result.add(ingredient);
      }
    }

    if (result.size() > 9) {
      throw new JsonParseException("Secondary ingredient count cannot be greater than 9, was " + result.size());
    }

    return result;
  }

  protected NonNullList<ToolEntry> deserializeTools(@Nonnull JsonObject json) {

    NonNullList<ToolEntry> result = NonNullList.create();

    JsonArray toolArray = GsonHelper.getAsJsonArray(json, "tools", new JsonArray());

    if (toolArray != null) {

      for (JsonElement jsonElement : toolArray) {
        JsonObject toolObject = jsonElement.getAsJsonObject();
        int damage = GsonHelper.getAsInt(toolObject, "damage", 1);
        boolean matchNbt = GsonHelper.getAsBoolean(toolObject, "matchNbt", false);

        Ingredient tool;

        if (matchNbt && toolObject.has("nbt")) {
          // Build an item-value ingredient carrying the expected NBT so it survives
          // packet sync; the NBT comparison (ignoring durability) happens in ToolEntry.
          ItemStack expected = RecipeSerializerHelper.deserializeItem(toolObject);
          tool = Ingredient.of(expected);

        } else {
          tool = Ingredient.fromJson(toolObject);
        }

        result.add(new ToolEntry(tool, damage, matchNbt));
      }

      if (result.size() > 3) {
        throw new JsonParseException("Tool count cannot be greater than 3, was " + result.size());
      }
    }

    return result;
  }
}
