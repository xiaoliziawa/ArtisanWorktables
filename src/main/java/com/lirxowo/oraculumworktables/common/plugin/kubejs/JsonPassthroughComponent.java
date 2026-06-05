package com.lirxowo.oraculumworktables.common.plugin.kubejs;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
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
  private static final String COMPONENT_TYPE = OraculumWorktablesMod.MOD_ID + ":json";

  @Override
  public String componentType() {
    return COMPONENT_TYPE;
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
