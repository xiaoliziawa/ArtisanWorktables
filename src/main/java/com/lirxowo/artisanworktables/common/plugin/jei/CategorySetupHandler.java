package com.lirxowo.artisanworktables.common.plugin.jei;

import com.lirxowo.artisanworktables.common.recipe.ArtisanRecipe;
import com.lirxowo.artisanworktables.common.recipe.ArtisanRecipeShaped;
import com.lirxowo.artisanworktables.common.recipe.ToolEntry;
import com.lirxowo.artisanworktables.common.reference.EnumTier;
import com.google.common.collect.Lists;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategorySetupHandler {

  private static final int SLOT_OFFSET = 1;

  public static final String TOOL_SLOT_NAME_PREFIX = "aw.tool.";

  private final EnumTier tier;

  public CategorySetupHandler(EnumTier tier) {

    this.tier = tier;
  }

  private IRecipeSlotBuilder slot(IRecipeLayoutBuilder builder, RecipeIngredientRole role, int x, int y) {

    return builder.addSlot(role, x + SLOT_OFFSET, y + SLOT_OFFSET);
  }

  public void setup(ArtisanRecipe recipe, IRecipeLayoutBuilder builder) {

    this.setupOutput(recipe, builder, this.tier);
    this.setupCraftingGrid(recipe, builder, this.tier);
    this.setupTools(builder, recipe, this.tier);
    this.setupExtraOutputs(recipe, builder, this.tier);
    this.setupFluid(recipe, builder, this.tier);
    this.setupSecondaryIngredients(recipe, builder, this.tier);
    this.setupTransferButton(builder, this.tier);
  }

  private void setupTransferButton(IRecipeLayoutBuilder builder, EnumTier tier) {

    switch (tier) {
      case WORKTABLE:
        builder.moveRecipeTransferButton(157, 67);
        break;
      case WORKSTATION:
        builder.moveRecipeTransferButton(157, 89);
        break;
      case WORKSHOP:
        builder.moveRecipeTransferButton(157, 115);
        break;
    }
  }

  private void setupSecondaryIngredients(ArtisanRecipe recipe, IRecipeLayoutBuilder builder, EnumTier tier) {

    if (tier == EnumTier.WORKSTATION || tier == EnumTier.WORKSHOP) {
      int yPos = (tier == EnumTier.WORKSTATION) ? 71 : 97;
      NonNullList<Ingredient> secondaryIngredients = recipe.getSecondaryIngredients();

      for (int i = 0; i < 9; i++) {
        // Secondary ingredients use the CATALYST role so they are displayed and
        // searchable, but excluded from recipe transfer to avoid filling these slots.

        if (i + 1 <= secondaryIngredients.size()) {
          this.slot(builder, RecipeIngredientRole.CATALYST, 4 + (18 * i), yPos)
              .addRichTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(SecondaryIngredientDisplay.tooltip(recipe)))
              .addItemStacks(Arrays.asList(secondaryIngredients.get(i).getItems()));
        }
      }
    }
  }

  private void setupFluid(ArtisanRecipe recipe, IRecipeLayoutBuilder builder, EnumTier tier) {

    FluidStack fluidStack = recipe.getFluidIngredient();

    if (!fluidStack.isEmpty()) {

      long capacity = (long) fluidStack.getAmount() * 2;

      if (tier == EnumTier.WORKTABLE || tier == EnumTier.WORKSTATION) {
        // Fluid column keeps the original (un-offset) position; the +1 slot offset
        // would push the tall fluid renderer out of its background frame.
        builder.addSlot(RecipeIngredientRole.CATALYST, 5, 14)
            .setFluidRenderer(capacity, false, 6, 52)
            .addIngredient(ForgeTypes.FLUID_STACK, fluidStack);

      } else if (tier == EnumTier.WORKSHOP) {
        builder.addSlot(RecipeIngredientRole.CATALYST, 5, 4)
            .setFluidRenderer(capacity, false, 6, 88)
            .addIngredient(ForgeTypes.FLUID_STACK, fluidStack);
      }
    }
  }

  private void setupExtraOutputs(ArtisanRecipe recipe, IRecipeLayoutBuilder builder, EnumTier tier) {

    NonNullList<ArtisanRecipe.ExtraOutputChancePair> extraOutputs = recipe.getExtraOutputs();
    int size = Math.min(extraOutputs.size(), 3);

    if (tier == EnumTier.WORKTABLE || tier == EnumTier.WORKSTATION) {

      for (int i = 0; i < size; i++) {
        this.slot(builder, RecipeIngredientRole.OUTPUT, 148, 13 + 18 * i)
            .addItemStack(extraOutputs.get(i).getOutput());
      }

    } else if (tier == EnumTier.WORKSHOP) {

      for (int i = 0; i < size; i++) {
        this.slot(builder, RecipeIngredientRole.OUTPUT, 112 + 18 * i, 3)
            .addItemStack(extraOutputs.get(i).getOutput());
      }
    }
  }

  private void setupTools(IRecipeLayoutBuilder builder, ArtisanRecipe recipe, EnumTier tier) {

    List<List<ItemStack>> tools = new ArrayList<>();

    for (ToolEntry tool : recipe.getTools()) {
      tools.add(Lists.newArrayList(tool.getTool().getItems()));
    }

    if (tier == EnumTier.WORKTABLE) {

      if (tools.size() > 0) {
        this.toolSlot(builder, 0, 74, 31, tools.get(0));
      }

    } else if (tier == EnumTier.WORKSTATION) {

      if (tools.size() > 0) {
        this.toolSlot(builder, 0, 74, 20, tools.get(0));
      }

      if (tools.size() > 1) {
        this.toolSlot(builder, 1, 74, 20 + 22, tools.get(1));
      }

    } else if (tier == EnumTier.WORKSHOP) {

      if (tools.size() > 0) {
        this.toolSlot(builder, 0, 110, 26, tools.get(0));
      }

      if (tools.size() > 1) {
        this.toolSlot(builder, 1, 110, 26 + 22, tools.get(1));
      }

      if (tools.size() > 2) {
        this.toolSlot(builder, 2, 110, 26 + 44, tools.get(2));
      }
    }
  }

  private void toolSlot(IRecipeLayoutBuilder builder, int index, int x, int y, List<ItemStack> stacks) {

    this.slot(builder, RecipeIngredientRole.INPUT, x, y)
        .setSlotName(TOOL_SLOT_NAME_PREFIX + index)
        .addItemStacks(stacks);
  }

  private void setupCraftingGrid(ArtisanRecipe recipe, IRecipeLayoutBuilder builder, EnumTier tier) {

    int gridSize = (tier == EnumTier.WORKSHOP) ? 5 : 3;
    int baseY = (tier == EnumTier.WORKSHOP) ? 3 : 13;

    List<IRecipeSlotBuilder> gridSlots = new ArrayList<>(gridSize * gridSize);

    for (int y = 0; y < gridSize; y++) {
      for (int x = 0; x < gridSize; x++) {
        gridSlots.add(this.slot(builder, RecipeIngredientRole.INPUT, x * 18 + 16, y * 18 + baseY));
      }
    }

    List<Ingredient> ingredients = recipe.getIngredients();

    int recipeWidth;

    if (recipe instanceof ArtisanRecipeShaped) {
      recipeWidth = ((ArtisanRecipeShaped) recipe).getWidth();

    } else if (tier == EnumTier.WORKSHOP) {
      recipeWidth = shapelessWidth(ingredients.size());

    } else {
      recipeWidth = 0;
    }

    for (int i = 0; i < ingredients.size(); i++) {
      Ingredient ingredient = ingredients.get(i);

      if (ingredient == Ingredient.EMPTY || ingredient.isEmpty()) {
        continue;
      }

      int cell = (recipeWidth > 0)
          ? (i / recipeWidth) * gridSize + (i % recipeWidth)
          : i;

      if (cell < gridSlots.size()) {
        gridSlots.get(cell).addItemStacks(Arrays.asList(ingredient.getItems()));
      }
    }
  }

  private void setupOutput(ArtisanRecipe recipe, IRecipeLayoutBuilder builder, EnumTier tier) {

    if (tier == EnumTier.WORKTABLE || tier == EnumTier.WORKSTATION) {
      this.slot(builder, RecipeIngredientRole.OUTPUT, 111, 31).addItemStack(recipe.getResultItem());

    } else if (tier == EnumTier.WORKSHOP) {
      this.slot(builder, RecipeIngredientRole.OUTPUT, 139, 48).addItemStack(recipe.getResultItem());
    }
  }

  private static int shapelessWidth(int size) {

    if (size > 16) {
      return 5;
    } else if (size > 9) {
      return 4;
    } else if (size > 4) {
      return 3;
    } else if (size > 1) {
      return 2;
    } else {
      return 1;
    }
  }
}
