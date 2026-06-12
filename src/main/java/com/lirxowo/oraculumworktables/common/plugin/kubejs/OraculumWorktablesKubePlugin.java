package com.lirxowo.oraculumworktables.common.plugin.kubejs;

import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import com.lirxowo.oraculumworktables.common.reference.EnumType;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import net.minecraft.resources.ResourceLocation;

public class OraculumWorktablesKubePlugin
    implements KubeJSPlugin {

  @Override
  public void registerRecipeSchemas(RecipeSchemaRegistry registry) {

    for (EnumType type : EnumType.values()) {
      registry.register(
          ResourceLocation.fromNamespaceAndPath(OraculumWorktablesMod.MOD_ID, type.getName() + "_shaped"),
          ArtisanRecipeSchemas.SHAPED
      );
      registry.register(
          ResourceLocation.fromNamespaceAndPath(OraculumWorktablesMod.MOD_ID, type.getName() + "_shapeless"),
          ArtisanRecipeSchemas.SHAPELESS
      );
    }
  }
}
