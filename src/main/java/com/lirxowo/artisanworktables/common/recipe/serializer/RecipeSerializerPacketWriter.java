package com.lirxowo.artisanworktables.common.recipe.serializer;

import com.lirxowo.artisanworktables.common.recipe.ArtisanRecipe;
import com.lirxowo.artisanworktables.common.recipe.ToolEntry;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.NonNullList;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class RecipeSerializerPacketWriter<R extends ArtisanRecipe>
    implements IRecipeSerializerPacketWriter<R> {

  @Override
  public void write(@Nonnull FriendlyByteBuf buffer, @Nonnull R recipe) {

    buffer.writeUtf(recipe.getGroup());
    this.writeTools(buffer, recipe.getTools());
    buffer.writeItem(recipe.getResultItem());
    this.writeIngredientList(buffer, recipe.getIngredients());
    this.writeIngredientList(buffer, recipe.getSecondaryIngredients());
    buffer.writeBoolean(recipe.consumeSecondaryIngredients());
    recipe.getFluidIngredient().writeToPacket(buffer);
    this.writeExtraOutputs(buffer, recipe.getExtraOutputs());
    buffer.writeInt(recipe.getMinimumTier());
    buffer.writeInt(recipe.getMaximumTier());
    buffer.writeInt(recipe.getExperienceRequired());
    buffer.writeInt(recipe.getLevelRequired());
    buffer.writeBoolean(recipe.consumeExperience());
  }

  protected void writeExtraOutputs(FriendlyByteBuf buffer, NonNullList<ArtisanRecipe.ExtraOutputChancePair> extraOutputs) {

    buffer.writeInt(extraOutputs.size());

    for (ArtisanRecipe.ExtraOutputChancePair extraOutput : extraOutputs) {
      buffer.writeItem(extraOutput.getOutput());
      buffer.writeFloat(extraOutput.getChance());
    }
  }

  protected void writeIngredientList(FriendlyByteBuf buffer, List<Ingredient> ingredients) {

    buffer.writeInt(ingredients.size());

    for (Ingredient ingredient : ingredients) {
      ingredient.toNetwork(buffer);
    }
  }

  protected void writeTools(FriendlyByteBuf buffer, NonNullList<ToolEntry> tools) {

    buffer.writeInt(tools.size());

    for (ToolEntry tool : tools) {
      tool.getTool().toNetwork(buffer);
      buffer.writeInt(tool.getDamage());
      buffer.writeBoolean(tool.matchNbt());
      buffer.writeBoolean(tool.getToolAction() != null);

      if (tool.getToolAction() != null) {
        buffer.writeUtf(tool.getToolAction().name());
      }
    }
  }
}
