package com.lirxowo.artisanworktables.common.recipe.serializer;

import com.google.gson.JsonObject;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public interface IRecipeSerializerJsonReader<T extends Recipe<?>> {

  @Nonnull
  T read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json);
}
