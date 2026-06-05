package com.lirxowo.oraculumworktables.common.recipe.serializer;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IRecipeSerializerPacketReader<T extends Recipe<?>> {

  @Nullable
  T read(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer);
}
