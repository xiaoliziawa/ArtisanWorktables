package com.lirxowo.artisanworktables.common.recipe.serializer;

import com.lirxowo.artisanworktables.common.recipe.ArtisanRecipeBuilder;
import com.lirxowo.artisanworktables.common.recipe.ArtisanRecipeShapeless;
import com.lirxowo.artisanworktables.common.reference.EnumType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RecipeSerializerShapelessPacketReader
    extends RecipeSerializerPacketReader<ArtisanRecipeShapeless> {

  private final EnumType type;

  public RecipeSerializerShapelessPacketReader(EnumType type) {

    this.type = type;
  }

  @Nullable
  @Override
  public ArtisanRecipeShapeless read(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer) {

    ArtisanRecipeBuilder builder = new ArtisanRecipeBuilder();

    this.read(builder, recipeId, buffer);

    try {
      return builder.buildShapeless(this.type);

    } catch (Exception e) {
      throw new RuntimeException("Error building recipe: " + recipeId, e);
    }
  }
}