package com.lirxowo.oraculumworktables.common.recipe.serializer;

import com.lirxowo.oraculumworktables.common.recipe.ArtisanRecipe;
import com.lirxowo.oraculumworktables.common.recipe.ArtisanRecipeBuilder;
import com.lirxowo.oraculumworktables.common.recipe.ArtisanRecipeShaped;
import com.lirxowo.oraculumworktables.common.recipe.ArtisanRecipeShapeless;
import com.lirxowo.oraculumworktables.common.recipe.ToolEntry;
import com.lirxowo.oraculumworktables.common.reference.EnumType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;
import java.util.Optional;

public final class ArtisanRecipeCodecs {

  private static final Codec<ItemAbility> ITEM_ABILITY_CODEC = Codec.STRING.xmap(ItemAbility::get, ItemAbility::name);

  private static final Codec<ToolEntry> TOOL_ENTRY_CODEC = RecordCodecBuilder.create(instance -> instance.group(
      Ingredient.CODEC.optionalFieldOf("ingredient", Ingredient.EMPTY).forGetter(ToolEntry::getTool),
      ItemStack.CODEC.optionalFieldOf("item", ItemStack.EMPTY).forGetter(tool -> tool.getMatchStack() != null ? tool.getMatchStack() : ItemStack.EMPTY),
      Codec.INT.optionalFieldOf("damage", 1).forGetter(ToolEntry::getDamage),
      Codec.BOOL.optionalFieldOf("matchNbt", false).forGetter(ToolEntry::matchNbt),
      ITEM_ABILITY_CODEC.optionalFieldOf("toolAction").forGetter(tool -> Optional.ofNullable(tool.getItemAbility()))
  ).apply(instance, (ingredient, item, damage, matchNbt, ability) ->
      new ToolEntry(ingredient, item.isEmpty() ? null : item, damage, matchNbt, ability.orElse(null))));

  private static final Codec<ArtisanRecipe.ExtraOutputChancePair> EXTRA_OUTPUT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
      ItemStack.CODEC.fieldOf("result").forGetter(ArtisanRecipe.ExtraOutputChancePair::getOutput),
      Codec.FLOAT.optionalFieldOf("chance", 1.0F).forGetter(ArtisanRecipe.ExtraOutputChancePair::getChance)
  ).apply(instance, ArtisanRecipe.ExtraOutputChancePair::new));

  // ---------------------------------------------------------------------------
  // JSON map codecs
  // ---------------------------------------------------------------------------

  public static MapCodec<ArtisanRecipeShapeless> shapelessCodec(EnumType type) {

    return RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.STRING.optionalFieldOf("group", "").forGetter(ArtisanRecipe::getGroup),
        ItemStack.STRICT_CODEC.fieldOf("result").forGetter(ArtisanRecipe::getResultItem),
        TOOL_ENTRY_CODEC.listOf().optionalFieldOf("tools", List.of()).forGetter(ArtisanRecipe::getTools),
        Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").forGetter(ArtisanRecipe::getIngredients),
        Ingredient.CODEC_NONEMPTY.listOf().optionalFieldOf("secondaryIngredients", List.of()).forGetter(ArtisanRecipe::getSecondaryIngredients),
        Codec.BOOL.optionalFieldOf("consumeSecondaryIngredients", true).forGetter(ArtisanRecipe::consumeSecondaryIngredients),
        FluidStack.OPTIONAL_CODEC.optionalFieldOf("fluidIngredient", FluidStack.EMPTY).forGetter(ArtisanRecipe::getFluidIngredient),
        EXTRA_OUTPUT_CODEC.listOf().optionalFieldOf("extraOutput", List.of()).forGetter(ArtisanRecipe::getExtraOutputs),
        Codec.INT.optionalFieldOf("minimumTier", 0).forGetter(ArtisanRecipe::getMinimumTier),
        Codec.INT.optionalFieldOf("maximumTier", 2).forGetter(ArtisanRecipe::getMaximumTier),
        Codec.INT.optionalFieldOf("experienceRequired", 0).forGetter(ArtisanRecipe::getExperienceRequired),
        Codec.INT.optionalFieldOf("levelRequired", 0).forGetter(ArtisanRecipe::getLevelRequired),
        Codec.BOOL.optionalFieldOf("consumeExperience", true).forGetter(ArtisanRecipe::consumeExperience),
        Codec.STRING.optionalFieldOf("craftSound", "").forGetter(ArtisanRecipe::getCraftSound)
    ).apply(instance, (group, result, tools, ingredients, secondaryIngredients, consumeSecondary, fluid, extraOutputs,
                       minimumTier, maximumTier, experienceRequired, levelRequired, consumeExperience, craftSound) -> {
      try {
        return commonBuilder(group, result, tools, secondaryIngredients, consumeSecondary, fluid, extraOutputs,
            minimumTier, maximumTier, experienceRequired, levelRequired, consumeExperience, craftSound)
            .setIngredients(nonNull(ingredients))
            .buildShapeless(type);

      } catch (Exception e) {
        throw new IllegalStateException("Invalid artisan shapeless recipe", e);
      }
    }));
  }

  public static MapCodec<ArtisanRecipeShaped> shapedCodec(EnumType type) {

    return RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.STRING.optionalFieldOf("group", "").forGetter(ArtisanRecipe::getGroup),
        ItemStack.STRICT_CODEC.fieldOf("result").forGetter(ArtisanRecipe::getResultItem),
        TOOL_ENTRY_CODEC.listOf().optionalFieldOf("tools", List.of()).forGetter(ArtisanRecipe::getTools),
        ShapedRecipePattern.MAP_CODEC.forGetter(ArtisanRecipeShaped::getPattern),
        Ingredient.CODEC_NONEMPTY.listOf().optionalFieldOf("secondaryIngredients", List.of()).forGetter(ArtisanRecipe::getSecondaryIngredients),
        Codec.BOOL.optionalFieldOf("consumeSecondaryIngredients", true).forGetter(ArtisanRecipe::consumeSecondaryIngredients),
        FluidStack.OPTIONAL_CODEC.optionalFieldOf("fluidIngredient", FluidStack.EMPTY).forGetter(ArtisanRecipe::getFluidIngredient),
        EXTRA_OUTPUT_CODEC.listOf().optionalFieldOf("extraOutput", List.of()).forGetter(ArtisanRecipe::getExtraOutputs),
        Codec.BOOL.optionalFieldOf("mirrored", true).forGetter(ArtisanRecipeShaped::isMirrored),
        Codec.INT.optionalFieldOf("minimumTier", 0).forGetter(ArtisanRecipe::getMinimumTier),
        Codec.INT.optionalFieldOf("maximumTier", 2).forGetter(ArtisanRecipe::getMaximumTier),
        Codec.INT.optionalFieldOf("experienceRequired", 0).forGetter(ArtisanRecipe::getExperienceRequired),
        Codec.INT.optionalFieldOf("levelRequired", 0).forGetter(ArtisanRecipe::getLevelRequired),
        Codec.BOOL.optionalFieldOf("consumeExperience", true).forGetter(ArtisanRecipe::consumeExperience),
        Codec.STRING.optionalFieldOf("craftSound", "").forGetter(ArtisanRecipe::getCraftSound)
    ).apply(instance, (group, result, tools, pattern, secondaryIngredients, consumeSecondary, fluid, extraOutputs,
                       mirrored, minimumTier, maximumTier, experienceRequired, levelRequired, consumeExperience, craftSound) -> {
      try {
        return commonBuilder(group, result, tools, secondaryIngredients, consumeSecondary, fluid, extraOutputs,
            minimumTier, maximumTier, experienceRequired, levelRequired, consumeExperience, craftSound)
            .setShapedPattern(pattern)
            .setMirrored(mirrored)
            .buildShaped(type);

      } catch (Exception e) {
        throw new IllegalStateException("Invalid artisan shaped recipe", e);
      }
    }));
  }

  // ---------------------------------------------------------------------------
  // Network stream codecs
  // ---------------------------------------------------------------------------

  public static StreamCodec<RegistryFriendlyByteBuf, ArtisanRecipeShapeless> shapelessStreamCodec(EnumType type) {

    return StreamCodec.of(
        (buffer, recipe) -> {
          writeCommon(buffer, recipe);
          writeIngredients(buffer, recipe.getIngredients());
        },
        buffer -> {
          ArtisanRecipeBuilder builder = readCommon(buffer);
          builder.setIngredients(readIngredients(buffer));

          try {
            return builder.buildShapeless(type);

          } catch (Exception e) {
            throw new IllegalStateException("Invalid artisan shapeless recipe", e);
          }
        }
    );
  }

  public static StreamCodec<RegistryFriendlyByteBuf, ArtisanRecipeShaped> shapedStreamCodec(EnumType type) {

    return StreamCodec.of(
        (buffer, recipe) -> {
          writeCommon(buffer, recipe);
          ShapedRecipePattern.STREAM_CODEC.encode(buffer, recipe.getPattern());
          buffer.writeBoolean(recipe.isMirrored());
        },
        buffer -> {
          ArtisanRecipeBuilder builder = readCommon(buffer);
          builder.setShapedPattern(ShapedRecipePattern.STREAM_CODEC.decode(buffer));
          builder.setMirrored(buffer.readBoolean());

          try {
            return builder.buildShaped(type);

          } catch (Exception e) {
            throw new IllegalStateException("Invalid artisan shaped recipe", e);
          }
        }
    );
  }

  // ---------------------------------------------------------------------------
  // Helpers
  // ---------------------------------------------------------------------------

  private static ArtisanRecipeBuilder commonBuilder(
      String group,
      ItemStack result,
      List<ToolEntry> tools,
      List<Ingredient> secondaryIngredients,
      boolean consumeSecondaryIngredients,
      FluidStack fluidIngredient,
      List<ArtisanRecipe.ExtraOutputChancePair> extraOutputs,
      int minimumTier,
      int maximumTier,
      int experienceRequired,
      int levelRequired,
      boolean consumeExperience,
      String craftSound
  ) {

    return new ArtisanRecipeBuilder()
        .setGroup(group)
        .setResult(result)
        .setTools(nonNull(tools))
        .setSecondaryIngredients(nonNull(secondaryIngredients))
        .setConsumeSecondaryIngredients(consumeSecondaryIngredients)
        .setFluidIngredient(fluidIngredient)
        .setExtraOutputs(nonNull(extraOutputs))
        .setMinimumTier(minimumTier)
        .setMaximumTier(maximumTier)
        .setExperienceRequired(experienceRequired)
        .setLevelRequired(levelRequired)
        .setConsumeExperience(consumeExperience)
        .setCraftSound(craftSound);
  }

  private static void writeCommon(RegistryFriendlyByteBuf buffer, ArtisanRecipe recipe) {

    buffer.writeUtf(recipe.getGroup());
    writeTools(buffer, recipe.getTools());
    ItemStack.STREAM_CODEC.encode(buffer, recipe.getResultItem());
    writeIngredients(buffer, recipe.getSecondaryIngredients());
    buffer.writeBoolean(recipe.consumeSecondaryIngredients());
    FluidStack.OPTIONAL_STREAM_CODEC.encode(buffer, recipe.getFluidIngredient());
    writeExtraOutputs(buffer, recipe.getExtraOutputs());
    buffer.writeVarInt(recipe.getMinimumTier());
    buffer.writeVarInt(recipe.getMaximumTier());
    buffer.writeVarInt(recipe.getExperienceRequired());
    buffer.writeVarInt(recipe.getLevelRequired());
    buffer.writeBoolean(recipe.consumeExperience());
    buffer.writeUtf(recipe.getCraftSound());
  }

  private static ArtisanRecipeBuilder readCommon(RegistryFriendlyByteBuf buffer) {

    String group = buffer.readUtf();
    NonNullList<ToolEntry> tools = readTools(buffer);
    ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
    NonNullList<Ingredient> secondaryIngredients = readIngredients(buffer);
    boolean consumeSecondary = buffer.readBoolean();
    FluidStack fluid = FluidStack.OPTIONAL_STREAM_CODEC.decode(buffer);
    NonNullList<ArtisanRecipe.ExtraOutputChancePair> extraOutputs = readExtraOutputs(buffer);
    int minimumTier = buffer.readVarInt();
    int maximumTier = buffer.readVarInt();
    int experienceRequired = buffer.readVarInt();
    int levelRequired = buffer.readVarInt();
    boolean consumeExperience = buffer.readBoolean();
    String craftSound = buffer.readUtf();

    return new ArtisanRecipeBuilder()
        .setGroup(group)
        .setResult(result)
        .setTools(tools)
        .setSecondaryIngredients(secondaryIngredients)
        .setConsumeSecondaryIngredients(consumeSecondary)
        .setFluidIngredient(fluid)
        .setExtraOutputs(extraOutputs)
        .setMinimumTier(minimumTier)
        .setMaximumTier(maximumTier)
        .setExperienceRequired(experienceRequired)
        .setLevelRequired(levelRequired)
        .setConsumeExperience(consumeExperience)
        .setCraftSound(craftSound);
  }

  private static void writeTools(RegistryFriendlyByteBuf buffer, List<ToolEntry> tools) {

    buffer.writeVarInt(tools.size());

    for (ToolEntry tool : tools) {
      Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, tool.getTool());
      buffer.writeVarInt(tool.getDamage());
      buffer.writeBoolean(tool.matchNbt());

      ItemAbility ability = tool.getItemAbility();
      buffer.writeBoolean(ability != null);

      if (ability != null) {
        buffer.writeUtf(ability.name());
      }
    }
  }

  private static NonNullList<ToolEntry> readTools(RegistryFriendlyByteBuf buffer) {

    int size = buffer.readVarInt();
    NonNullList<ToolEntry> tools = NonNullList.create();

    for (int i = 0; i < size; i++) {
      Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
      int damage = buffer.readVarInt();
      boolean matchNbt = buffer.readBoolean();
      ItemAbility ability = buffer.readBoolean() ? ItemAbility.get(buffer.readUtf()) : null;
      tools.add(new ToolEntry(ingredient, damage, matchNbt, ability));
    }

    return tools;
  }

  private static void writeIngredients(RegistryFriendlyByteBuf buffer, List<Ingredient> ingredients) {

    buffer.writeVarInt(ingredients.size());

    for (Ingredient ingredient : ingredients) {
      Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
    }
  }

  private static NonNullList<Ingredient> readIngredients(RegistryFriendlyByteBuf buffer) {

    int size = buffer.readVarInt();
    NonNullList<Ingredient> ingredients = NonNullList.create();

    for (int i = 0; i < size; i++) {
      ingredients.add(Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
    }

    return ingredients;
  }

  private static void writeExtraOutputs(RegistryFriendlyByteBuf buffer, List<ArtisanRecipe.ExtraOutputChancePair> extraOutputs) {

    buffer.writeVarInt(extraOutputs.size());

    for (ArtisanRecipe.ExtraOutputChancePair extraOutput : extraOutputs) {
      ItemStack.STREAM_CODEC.encode(buffer, extraOutput.getOutput());
      buffer.writeFloat(extraOutput.getChance());
    }
  }

  private static NonNullList<ArtisanRecipe.ExtraOutputChancePair> readExtraOutputs(RegistryFriendlyByteBuf buffer) {

    int size = buffer.readVarInt();
    NonNullList<ArtisanRecipe.ExtraOutputChancePair> extraOutputs = NonNullList.create();

    for (int i = 0; i < size; i++) {
      ItemStack output = ItemStack.STREAM_CODEC.decode(buffer);
      float chance = buffer.readFloat();
      extraOutputs.add(new ArtisanRecipe.ExtraOutputChancePair(output, chance));
    }

    return extraOutputs;
  }

  private static <T> NonNullList<T> nonNull(List<T> list) {

    NonNullList<T> result = NonNullList.create();
    result.addAll(list);
    return result;
  }

  private ArtisanRecipeCodecs() {
    //
  }
}
