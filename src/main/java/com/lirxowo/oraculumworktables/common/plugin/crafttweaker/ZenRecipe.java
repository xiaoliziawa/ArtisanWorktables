package com.lirxowo.oraculumworktables.common.plugin.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.blamejared.crafttweaker.api.zencode.scriptrun.IScriptRunInfo;
import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import com.lirxowo.oraculumworktables.common.recipe.ArtisanRecipe;
import com.lirxowo.oraculumworktables.common.recipe.ArtisanRecipeBuilder;
import com.lirxowo.oraculumworktables.common.recipe.ArtisanRecipeShaped;
import com.lirxowo.oraculumworktables.common.recipe.RecipeTypes;
import com.lirxowo.oraculumworktables.common.reference.EnumType;
import com.lirxowo.oraculumworktables.common.util.RecipeInjector;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.oraculum_worktables.Recipe")
public class ZenRecipe {

  private static final Logger LOGGER = CraftTweakerAPI.getLogger(OraculumWorktablesMod.MOD_ID);

  private static IScriptRunInfo lastRunInfo;
  private static IntSet usedHashSet = new IntOpenHashSet();

  private final ZenEnumType type;
  private final ArtisanRecipeBuilder builder;

  private boolean isShaped;

  public ZenRecipe(ZenEnumType type, ArtisanRecipeBuilder builder) {

    this.type = type;
    this.builder = builder;
  }

  @ZenCodeType.Method
  public static ZenRecipe type(ZenEnumType type) {

    return new ZenRecipe(type, new ArtisanRecipeBuilder());
  }

  @ZenCodeType.Method
  public ZenRecipe shaped(IIngredient[][] ingredients) {

    NonNullList<Ingredient> result = NonNullList.create();

    int maxWidth = 0;

    for (IIngredient[] iIngredientRow : ingredients) {
      maxWidth = Math.max(iIngredientRow.length, maxWidth);
      for (IIngredient iIngredient : iIngredientRow) {
        result.add(iIngredient.asVanillaIngredient());
      }
    }

    this.builder.setIngredients(result);
    this.builder.setWidth(maxWidth);
    this.builder.setHeight(ingredients.length);
    this.isShaped = true;
    return this;
  }

  @ZenCodeType.Method
  public ZenRecipe shapeless(IIngredient[] ingredients) {

    NonNullList<Ingredient> result = NonNullList.create();

    for (IIngredient iIngredient : ingredients) {
      result.add(iIngredient.asVanillaIngredient());
    }

    this.builder.setIngredients(result);
    this.builder.setWidth(0);
    this.builder.setHeight(0);
    this.isShaped = false;
    return this;
  }

  @ZenCodeType.Method
  public ZenRecipe tool(IIngredient tool, int damage) {

    return this.tool(tool, damage, false);
  }

  @ZenCodeType.Method
  public ZenRecipe tool(IIngredient tool, int damage, boolean matchNbt) {

    this.builder.addTool(tool.asVanillaIngredient(), damage, matchNbt);
    return this;
  }

  @ZenCodeType.Method
  public ZenRecipe fluid(IFluidStack fluid) {

    this.builder.setFluidIngredient(fluid.getInternal());
    return this;
  }

  @ZenCodeType.Method
  public ZenRecipe secondary(IIngredient[] ingredients) {

    return this.secondary(ingredients, true);
  }

  @ZenCodeType.Method
  public ZenRecipe secondary(IIngredient[] ingredients, boolean consume) {

    NonNullList<Ingredient> result = NonNullList.create();

    for (IIngredient iIngredient : ingredients) {
      result.add(iIngredient.asVanillaIngredient());
    }

    this.builder.setSecondaryIngredients(result);
    this.builder.setConsumeSecondaryIngredients(consume);
    return this;
  }

  @ZenCodeType.Method
  public ZenRecipe mirrored(boolean mirrored) {

    this.builder.setMirrored(mirrored);
    return this;
  }

  @ZenCodeType.Method
  public ZenRecipe restrict(ZenEnumTier minimum) {

    return this.restrict(minimum, ZenEnumTier.WORKSHOP);
  }

  @ZenCodeType.Method
  public ZenRecipe restrict(ZenEnumTier minimum, ZenEnumTier maximum) {

    this.builder.setMinimumTier(minimum.getTier().getId());
    this.builder.setMaximumTier(maximum.getTier().getId());
    return this;
  }

  @ZenCodeType.Method
  public ZenRecipe experience(int amount) {

    return this.experience(amount, true);
  }

  @ZenCodeType.Method
  public ZenRecipe experience(int amount, boolean consume) {

    this.builder.setLevelRequired(0);
    this.builder.setExperienceRequired(amount);
    this.builder.setConsumeExperience(consume);
    return this;
  }

  @ZenCodeType.Method
  public ZenRecipe level(int amount) {

    return this.level(amount, true);
  }

  @ZenCodeType.Method
  public ZenRecipe level(int amount, boolean consume) {

    this.builder.setExperienceRequired(0);
    this.builder.setLevelRequired(amount);
    this.builder.setConsumeExperience(consume);
    return this;
  }

  @ZenCodeType.Method
  public ZenRecipe craftSound(String craftSound) {

    this.builder.setCraftSound(craftSound);
    return this;
  }

  @ZenCodeType.Method
  public ZenRecipe output(IItemStack output) {

    this.builder.setResult(output.getInternal());
    return this;
  }

  @ZenCodeType.Method
  public ZenRecipe extra(IItemStack extra, float chance) {

    this.builder.addExtraOutput(extra.getInternal(), chance);
    return this;
  }

  @ZenCodeType.Method
  public void register() {

    IScriptRunInfo runInfo = CraftTweakerAPI.getScriptRunManager().currentRunInfo();

    if (ZenRecipe.lastRunInfo != runInfo) {
      ZenRecipe.lastRunInfo = runInfo;
      ZenRecipe.usedHashSet = new IntOpenHashSet();
    }

    String generatedName = this.builder.getGeneratedName(ZenRecipe.usedHashSet, LOGGER::error);
    this.register(generatedName);
  }

  @ZenCodeType.Method
  public void register(String name) {

    CraftTweakerUtil.validateRecipeName(name);
    ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath("crafttweaker", name);
    this.builder.setRecipeId(resourceLocation);

    try {

      ArtisanRecipe recipe = this.isShaped
          ? this.builder.buildShaped(this.type.getType())
          : this.builder.buildShapeless(this.type.getType());

      CraftTweakerAPI.apply(new ActionAddRecipe<>(managerFor(recipe), recipe, this.isShaped ? "shaped" : "shapeless"));

    } catch (Exception e) {
      LOGGER.error("Error registering recipe: " + resourceLocation, e);
    }
  }

  @ZenCodeType.Method
  public static void injectTestRecipes() {

    RecipeInjector.inject(recipe -> CraftTweakerAPI.apply(new ActionAddRecipe<>(managerFor(recipe), recipe)));
  }

  private static IRecipeManager<ArtisanRecipe> managerFor(ArtisanRecipe recipe) {

    EnumType tableType = recipe.getTableType();
    RecipeType<ArtisanRecipe> recipeType = (recipe instanceof ArtisanRecipeShaped)
        ? RecipeTypes.SHAPED_RECIPE_TYPES.get(tableType)
        : RecipeTypes.SHAPELESS_RECIPE_TYPES.get(tableType);

    return () -> recipeType;
  }
}
