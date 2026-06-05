package com.lirxowo.oraculumworktables.common.recipe.serializer;

import com.lirxowo.oraculumworktables.common.recipe.ArtisanRecipeShapeless;
import net.minecraft.network.FriendlyByteBuf;

import javax.annotation.Nonnull;

public class RecipeSerializerShapelessPacketWriter
    extends RecipeSerializerPacketWriter<ArtisanRecipeShapeless> {

  @Override
  public void write(@Nonnull FriendlyByteBuf buffer, @Nonnull ArtisanRecipeShapeless recipe) {

    super.write(buffer, recipe);
  }
}