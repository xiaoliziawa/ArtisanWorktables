package com.lirxowo.artisanworktables.common.plugin.kubejs;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.util.JsonIO;

/**
 * Passes a script-provided value straight through to the recipe JSON. Used for the
 * worktable's custom-shaped fields (tools, extraOutput, fluidIngredient) so the script
 * supplies the mod's native JSON structure verbatim.
 */
public class JsonPassthroughComponent
    implements RecipeComponent<JsonElement> {

  public static final JsonPassthroughComponent INSTANCE = new JsonPassthroughComponent();

  @Override
  public String componentType() {
    return "artisanworktables:json";
  }

  @Override
  public Class<?> componentClass() {
    return JsonElement.class;
  }

  @Override
  public JsonElement write(RecipeJS recipe, JsonElement value) {
    return value == null ? JsonNull.INSTANCE : value;
  }

  @Override
  public JsonElement read(RecipeJS recipe, Object from) {
    if (from instanceof JsonElement element) {
      return element;
    }
    return JsonIO.of(from);
  }

  @Override
  public String toString() {
    return this.componentType();
  }
}
