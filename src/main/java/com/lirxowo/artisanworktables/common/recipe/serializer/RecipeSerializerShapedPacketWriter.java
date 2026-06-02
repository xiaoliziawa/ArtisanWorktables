package com.lirxowo.artisanworktables.common.recipe.serializer;

import com.lirxowo.artisanworktables.common.recipe.ArtisanRecipeShaped;
import net.minecraft.network.FriendlyByteBuf;

import javax.annotation.Nonnull;

public class RecipeSerializerShapedPacketWriter
    extends RecipeSerializerPacketWriter<ArtisanRecipeShaped> {

  @Override
  public void write(@Nonnull FriendlyByteBuf buffer, @Nonnull ArtisanRecipeShaped recipe) {

    super.write(buffer, recipe);

    buffer.writeBoolean(recipe.isMirrored());
    buffer.writeInt(recipe.getWidth());
    buffer.writeInt(recipe.getHeight());
  }
}