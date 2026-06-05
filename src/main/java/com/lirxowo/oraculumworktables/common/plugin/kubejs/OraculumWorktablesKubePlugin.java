package com.lirxowo.oraculumworktables.common.plugin.kubejs;

import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import com.lirxowo.oraculumworktables.common.reference.EnumType;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import net.minecraft.resources.ResourceLocation;

public class OraculumWorktablesKubePlugin
    extends KubeJSPlugin {

  @Override
  public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {

    for (EnumType type : EnumType.values()) {
      event.register(
          ResourceLocation.fromNamespaceAndPath(OraculumWorktablesMod.MOD_ID, type.getName() + "_shaped"),
          ArtisanRecipeSchemas.SHAPED
      );
      event.register(
          ResourceLocation.fromNamespaceAndPath(OraculumWorktablesMod.MOD_ID, type.getName() + "_shapeless"),
          ArtisanRecipeSchemas.SHAPELESS
      );
    }
  }
}
