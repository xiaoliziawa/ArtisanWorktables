package com.lirxowo.oraculumworktables.common.recipe.serializer;

import com.lirxowo.oraculumworktables.common.recipe.ArtisanRecipe;
import com.lirxowo.oraculumworktables.common.recipe.ArtisanRecipeShaped;
import com.lirxowo.oraculumworktables.common.recipe.ArtisanRecipeShapeless;
import com.lirxowo.oraculumworktables.common.reference.EnumType;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ArtisanRecipeSerializer<R extends ArtisanRecipe>
    implements RecipeSerializer<R> {

  private final MapCodec<R> codec;
  private final StreamCodec<RegistryFriendlyByteBuf, R> streamCodec;

  public ArtisanRecipeSerializer(MapCodec<R> codec, StreamCodec<RegistryFriendlyByteBuf, R> streamCodec) {

    this.codec = codec;
    this.streamCodec = streamCodec;
  }

  @Override
  public MapCodec<R> codec() {

    return this.codec;
  }

  @Override
  public StreamCodec<RegistryFriendlyByteBuf, R> streamCodec() {

    return this.streamCodec;
  }

  public static ArtisanRecipeSerializer<ArtisanRecipeShaped> shaped(EnumType type) {

    return new ArtisanRecipeSerializer<>(ArtisanRecipeCodecs.shapedCodec(type), ArtisanRecipeCodecs.shapedStreamCodec(type));
  }

  public static ArtisanRecipeSerializer<ArtisanRecipeShapeless> shapeless(EnumType type) {

    return new ArtisanRecipeSerializer<>(ArtisanRecipeCodecs.shapelessCodec(type), ArtisanRecipeCodecs.shapelessStreamCodec(type));
  }
}
