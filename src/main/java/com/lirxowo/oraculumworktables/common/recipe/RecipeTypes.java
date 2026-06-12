package com.lirxowo.oraculumworktables.common.recipe;

import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import com.lirxowo.oraculumworktables.common.reference.EnumType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.EnumMap;

public final class RecipeTypes {

  public static final EnumMap<EnumType, RecipeType<ArtisanRecipe>> SHAPED_RECIPE_TYPES = new EnumMap<>(EnumType.class);
  public static final EnumMap<EnumType, RecipeType<ArtisanRecipe>> SHAPELESS_RECIPE_TYPES = new EnumMap<>(EnumType.class);

  public static void register(RegisterEvent event) {

    for (EnumType type : EnumType.values()) {
      SHAPED_RECIPE_TYPES.put(type, register(event, type.getName() + "_shaped"));
      SHAPELESS_RECIPE_TYPES.put(type, register(event, type.getName() + "_shapeless"));
    }
  }

  private static RecipeType<ArtisanRecipe> register(RegisterEvent event, String path) {

    ResourceLocation id = ResourceLocation.fromNamespaceAndPath(OraculumWorktablesMod.MOD_ID, path);

    RecipeType<ArtisanRecipe> recipeType = new RecipeType<>() {
      @Override
      public String toString() {
        return id.toString();
      }
    };

    event.register(Registries.RECIPE_TYPE, id, () -> recipeType);
    return recipeType;
  }

  private RecipeTypes() {
    //
  }
}
