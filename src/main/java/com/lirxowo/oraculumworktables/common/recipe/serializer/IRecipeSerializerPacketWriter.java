package com.lirxowo.oraculumworktables.common.recipe.serializer;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.network.FriendlyByteBuf;

import javax.annotation.Nonnull;

public interface IRecipeSerializerPacketWriter<T extends Recipe<?>> {

  void write(@Nonnull FriendlyByteBuf buffer, @Nonnull T recipe);
}
