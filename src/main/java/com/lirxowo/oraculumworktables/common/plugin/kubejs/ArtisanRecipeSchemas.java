package com.lirxowo.oraculumworktables.common.plugin.kubejs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BooleanComponent;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.MapRecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.util.TinyMap;

public interface ArtisanRecipeSchemas {

  RecipeKey<OutputItem> RESULT = ItemComponents.OUTPUT.key("result");
  RecipeKey<String[]> PATTERN = StringComponent.NON_EMPTY.asArray().key("pattern");
  RecipeKey<TinyMap<Character, InputItem>> KEY = MapRecipeComponent.ITEM_PATTERN_KEY.key("key");
  RecipeKey<InputItem[]> INGREDIENTS = ItemComponents.INPUT.asArray().key("ingredients");

  RecipeKey<Boolean> MIRRORED = BooleanComponent.BOOLEAN.key("mirrored").optional(true);
  RecipeKey<String> GROUP = StringComponent.ANY.key("group").optional("");

  RecipeKey<InputItem[]> SECONDARY_INGREDIENTS = ItemComponents.INPUT.asArray().key("secondaryIngredients").optional(new InputItem[0]);
  RecipeKey<Boolean> CONSUME_SECONDARY = BooleanComponent.BOOLEAN.key("consumeSecondaryIngredients").optional(true);

  RecipeKey<JsonElement> TOOLS = JsonPassthroughComponent.INSTANCE.key("tools").optional(new JsonArray());
  RecipeKey<JsonElement> EXTRA_OUTPUT = JsonPassthroughComponent.INSTANCE.key("extraOutput").optional(new JsonArray());
  RecipeKey<JsonElement> FLUID_INGREDIENT = JsonPassthroughComponent.INSTANCE.key("fluidIngredient").optional(JsonNull.INSTANCE);

  RecipeKey<Integer> MINIMUM_TIER = NumberComponent.INT.key("minimumTier").optional(0);
  RecipeKey<Integer> MAXIMUM_TIER = NumberComponent.INT.key("maximumTier").optional(2);
  RecipeKey<Integer> EXPERIENCE_REQUIRED = NumberComponent.INT.key("experienceRequired").optional(0);
  RecipeKey<Integer> LEVEL_REQUIRED = NumberComponent.INT.key("levelRequired").optional(0);
  RecipeKey<Boolean> CONSUME_EXPERIENCE = BooleanComponent.BOOLEAN.key("consumeExperience").optional(true);

  RecipeKey<String> CRAFT_SOUND = StringComponent.ANY.key("craftSound").optional("");

  RecipeSchema SHAPED = new RecipeSchema(
      RESULT, PATTERN, KEY, MIRRORED, GROUP,
      SECONDARY_INGREDIENTS, CONSUME_SECONDARY,
      TOOLS, EXTRA_OUTPUT, FLUID_INGREDIENT,
      MINIMUM_TIER, MAXIMUM_TIER, EXPERIENCE_REQUIRED, LEVEL_REQUIRED, CONSUME_EXPERIENCE,
      CRAFT_SOUND
  ).constructor(RESULT, PATTERN, KEY).uniqueOutputId(RESULT);

  RecipeSchema SHAPELESS = new RecipeSchema(
      RESULT, INGREDIENTS, GROUP,
      SECONDARY_INGREDIENTS, CONSUME_SECONDARY,
      TOOLS, EXTRA_OUTPUT, FLUID_INGREDIENT,
      MINIMUM_TIER, MAXIMUM_TIER, EXPERIENCE_REQUIRED, LEVEL_REQUIRED, CONSUME_EXPERIENCE,
      CRAFT_SOUND
  ).constructor(RESULT, INGREDIENTS).uniqueOutputId(RESULT);
}
