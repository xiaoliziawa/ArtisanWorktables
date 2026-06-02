package com.lirxowo.artisanworktables.common.recipe.serializer;

import com.lirxowo.artisanworktables.common.recipe.ArtisanRecipe;
import com.google.gson.JsonObject;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ArtisanRecipeSerializer<R extends ArtisanRecipe>
    implements RecipeSerializer<R> {

  private final IRecipeSerializerJsonReader<R> recipeSerializerJsonReader;
  private final IRecipeSerializerPacketReader<R> recipeSerializerPacketReader;
  private final IRecipeSerializerPacketWriter<R> recipeSerializerPacketWriter;

  public ArtisanRecipeSerializer(
      IRecipeSerializerJsonReader<R> recipeSerializerJsonReader,
      IRecipeSerializerPacketReader<R> recipeSerializerPacketReader,
      IRecipeSerializerPacketWriter<R> recipeSerializerPacketWriter
  ) {

    this.recipeSerializerJsonReader = recipeSerializerJsonReader;
    this.recipeSerializerPacketReader = recipeSerializerPacketReader;
    this.recipeSerializerPacketWriter = recipeSerializerPacketWriter;
  }

  @Nonnull
  @Override
  public R fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {

    return this.recipeSerializerJsonReader.read(recipeId, json);
  }

  @Nullable
  @Override
  public R fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer) {

    return this.recipeSerializerPacketReader.read(recipeId, buffer);
  }

  @Override
  public void toNetwork(@Nonnull FriendlyByteBuf buffer, @Nonnull R recipe) {

    this.recipeSerializerPacketWriter.write(buffer, recipe);
  }
}