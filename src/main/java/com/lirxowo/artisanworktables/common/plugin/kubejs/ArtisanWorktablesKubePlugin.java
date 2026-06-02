package com.lirxowo.artisanworktables.common.plugin.kubejs;

import com.lirxowo.artisanworktables.ArtisanWorktablesMod;
import com.lirxowo.artisanworktables.common.reference.EnumType;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import net.minecraft.resources.ResourceLocation;

public class ArtisanWorktablesKubePlugin
    extends KubeJSPlugin {

  @Override
  public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {

    for (EnumType type : EnumType.values()) {
      event.register(
          ResourceLocation.fromNamespaceAndPath(ArtisanWorktablesMod.MOD_ID, type.getName() + "_shaped"),
          ArtisanRecipeSchemas.SHAPED
      );
      event.register(
          ResourceLocation.fromNamespaceAndPath(ArtisanWorktablesMod.MOD_ID, type.getName() + "_shapeless"),
          ArtisanRecipeSchemas.SHAPELESS
      );
    }
  }
}
