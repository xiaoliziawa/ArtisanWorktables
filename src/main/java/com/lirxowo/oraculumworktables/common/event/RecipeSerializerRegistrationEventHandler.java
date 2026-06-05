package com.lirxowo.oraculumworktables.common.event;

import com.lirxowo.oraculumworktables.common.recipe.ArtisanRecipe;
import com.lirxowo.oraculumworktables.common.recipe.ArtisanRecipeShaped;
import com.lirxowo.oraculumworktables.common.recipe.ArtisanRecipeShapeless;
import com.lirxowo.oraculumworktables.common.recipe.RecipeTypes;
import com.lirxowo.oraculumworktables.common.recipe.serializer.*;
import com.lirxowo.oraculumworktables.common.reference.EnumType;
import com.lirxowo.oraculumworktables.common.reference.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.EnumMap;

public class RecipeSerializerRegistrationEventHandler {

  private final EnumMap<EnumType, RecipeSerializer<? extends ArtisanRecipe>> registeredSerializersShaped;
  private final EnumMap<EnumType, RecipeSerializer<? extends ArtisanRecipe>> registeredSerializersShapeless;

  public RecipeSerializerRegistrationEventHandler(
      EnumMap<EnumType, RecipeSerializer<? extends ArtisanRecipe>> registeredSerializersShaped,
      EnumMap<EnumType, RecipeSerializer<? extends ArtisanRecipe>> registeredSerializersShapeless
  ) {
    this.registeredSerializersShaped = registeredSerializersShaped;
    this.registeredSerializersShapeless = registeredSerializersShapeless;
  }

  @SubscribeEvent
  public void on(RegisterEvent event) {
    if (event.getRegistryKey().equals(Registries.RECIPE_TYPE)) {
      RecipeTypes.register(event);
      return;
    }

    if (!event.getRegistryKey().equals(Registries.RECIPE_SERIALIZER)) {
      return;
    }

    for (EnumType type : EnumType.values()) {
      String name = type.getName();

      ArtisanRecipeSerializer<ArtisanRecipeShaped> shapedSerializer = new ArtisanRecipeSerializer<>(
          new RecipeSerializerShapedJsonReader(type, Reference.MAX_RECIPE_WIDTH, Reference.MAX_RECIPE_HEIGHT),
          new RecipeSerializerShapedPacketReader(type),
          new RecipeSerializerShapedPacketWriter()
      );
      this.registeredSerializersShaped.put(type, shapedSerializer);
      event.register(Registries.RECIPE_SERIALIZER,
          ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, name + "_shaped"),
          () -> shapedSerializer);

      ArtisanRecipeSerializer<ArtisanRecipeShapeless> shapelessSerializer = new ArtisanRecipeSerializer<>(
          new RecipeSerializerShapelessJsonReader(type, Reference.MAX_RECIPE_WIDTH, Reference.MAX_RECIPE_HEIGHT),
          new RecipeSerializerShapelessPacketReader(type),
          new RecipeSerializerShapelessPacketWriter()
      );
      this.registeredSerializersShapeless.put(type, shapelessSerializer);
      event.register(Registries.RECIPE_SERIALIZER,
          ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, name + "_shapeless"),
          () -> shapelessSerializer);
    }
  }
}
