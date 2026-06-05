package com.lirxowo.oraculumworktables.common.recipe.serializer;

import com.lirxowo.oraculumworktables.common.recipe.ArtisanRecipeBuilder;
import com.lirxowo.oraculumworktables.common.recipe.ArtisanRecipeShaped;
import com.lirxowo.oraculumworktables.common.reference.EnumType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RecipeSerializerShapedPacketReader
    extends RecipeSerializerPacketReader<ArtisanRecipeShaped> {

  private final EnumType type;

  public RecipeSerializerShapedPacketReader(EnumType type) {

    this.type = type;
  }

  @Nullable
  @Override
  public ArtisanRecipeShaped read(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer) {

    ArtisanRecipeBuilder builder = new ArtisanRecipeBuilder();

    this.read(builder, recipeId, buffer);

    boolean mirrored = buffer.readBoolean();
    int width = buffer.readInt();
    int height = buffer.readInt();

    try {
      return builder
          .setMirrored(mirrored)
          .setWidth(width)
          .setHeight(height)
          .buildShaped(this.type);

    } catch (Exception e) {
      throw new RuntimeException("Error building recipe: " + recipeId, e);
    }
  }
}