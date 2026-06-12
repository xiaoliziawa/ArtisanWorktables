package com.lirxowo.oraculumworktables.common.plugin.kubejs;

import com.google.gson.JsonElement;
import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.minecraft.resources.ResourceLocation;

/**
 * Passes a script-provided value straight through to the recipe JSON. Used for the
 * worktable's custom-shaped fields (tools, extraOutput, fluidIngredient) so the script
 * supplies the mod's native JSON structure verbatim.
 */
public class JsonPassthroughComponent
    implements RecipeComponent<JsonElement> {

  public static final JsonPassthroughComponent INSTANCE = new JsonPassthroughComponent();

  public static final RecipeComponentType<JsonElement> TYPE = RecipeComponentType.unit(
      ResourceLocation.fromNamespaceAndPath(OraculumWorktablesMod.MOD_ID, "json"),
      INSTANCE
  );

  private static final Codec<JsonElement> CODEC = Codec.PASSTHROUGH.xmap(
      dynamic -> dynamic.convert(JsonOps.INSTANCE).getValue(),
      json -> new Dynamic<>(JsonOps.INSTANCE, json)
  );

  @Override
  public RecipeComponentType<?> type() {
    return TYPE;
  }

  @Override
  public Codec<JsonElement> codec() {
    return CODEC;
  }

  @Override
  public TypeInfo typeInfo() {
    return TypeInfo.of(JsonElement.class);
  }

  @Override
  public String toString() {
    return "json";
  }
}
