package com.lirxowo.artisanworktables.common.recipe.serializer;

import com.lirxowo.artisanworktables.common.recipe.ArtisanRecipe;
import com.lirxowo.artisanworktables.common.recipe.ArtisanRecipeBuilder;
import com.lirxowo.artisanworktables.common.recipe.ToolEntry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.fluids.FluidStack;

public abstract class RecipeSerializerPacketReader<R extends ArtisanRecipe>
    implements IRecipeSerializerPacketReader<R> {

  protected void read(ArtisanRecipeBuilder builder, ResourceLocation recipeId, FriendlyByteBuf buffer) {

    String group = buffer.readUtf();
    NonNullList<ToolEntry> tools = this.readTools(buffer);
    ItemStack result = buffer.readItem();
    NonNullList<Ingredient> ingredients = this.readIngredientList(buffer);
    NonNullList<Ingredient> secondaryIngredients = this.readIngredientList(buffer);
    boolean consumeSecondaryIngredients = buffer.readBoolean();
    FluidStack fluidIngredient = FluidStack.readFromPacket(buffer);
    NonNullList<ArtisanRecipe.ExtraOutputChancePair> extraOutput = this.readExtraOutputs(buffer);
    int minimumTier = buffer.readInt();
    int maximumTier = buffer.readInt();
    int experienceRequired = buffer.readInt();
    int levelRequired = buffer.readInt();
    boolean consumeExperience = buffer.readBoolean();
    String craftSound = buffer.readUtf();

    builder
        .setRecipeId(recipeId)
        .setGroup(group)
        .setResult(result)
        .setIngredients(ingredients)
        .setTools(tools)
        .setSecondaryIngredients(secondaryIngredients)
        .setConsumeSecondaryIngredients(consumeSecondaryIngredients)
        .setFluidIngredient(fluidIngredient)
        .setExtraOutputs(extraOutput)
        .setMinimumTier(minimumTier)
        .setMaximumTier(maximumTier)
        .setExperienceRequired(experienceRequired)
        .setLevelRequired(levelRequired)
        .setConsumeExperience(consumeExperience)
        .setCraftSound(craftSound);
  }

  protected NonNullList<ArtisanRecipe.ExtraOutputChancePair> readExtraOutputs(FriendlyByteBuf buffer) {

    int size = buffer.readInt();
    NonNullList<ArtisanRecipe.ExtraOutputChancePair> result = NonNullList.create();

    for (int i = 0; i < size; i++) {
      ItemStack itemStack = buffer.readItem();
      float chance = buffer.readFloat();
      result.add(new ArtisanRecipe.ExtraOutputChancePair(itemStack, chance));
    }

    return result;
  }

  protected NonNullList<Ingredient> readIngredientList(FriendlyByteBuf buffer) {

    int size = buffer.readInt();
    NonNullList<Ingredient> result = NonNullList.create();

    for (int i = 0; i < size; i++) {
      Ingredient ingredient = Ingredient.fromNetwork(buffer);
      result.add(ingredient);
    }

    return result;
  }

  protected NonNullList<ToolEntry> readTools(FriendlyByteBuf buffer) {

    int size = buffer.readInt();
    NonNullList<ToolEntry> result = NonNullList.create();

    for (int i = 0; i < size; i++) {
      Ingredient ingredient = Ingredient.fromNetwork(buffer);
      int damage = buffer.readInt();
      boolean matchNbt = buffer.readBoolean();
      ToolAction toolAction = buffer.readBoolean() ? ToolAction.get(buffer.readUtf()) : null;
      result.add(new ToolEntry(ingredient, damage, matchNbt, toolAction));
    }

    return result;
  }
}
