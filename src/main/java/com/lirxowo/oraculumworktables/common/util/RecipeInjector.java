package com.lirxowo.oraculumworktables.common.util;

import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import com.lirxowo.oraculumworktables.common.recipe.ArtisanRecipe;
import com.lirxowo.oraculumworktables.common.recipe.ArtisanRecipeBuilder;
import com.lirxowo.oraculumworktables.common.reference.EnumType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public final class RecipeInjector {

  public static void inject(Consumer<ArtisanRecipe> recipeConsumer) {

    Ingredient[] inputs = new Ingredient[]{
        Ingredient.of(new ItemStack(Items.APPLE)),
        Ingredient.of(new ItemStack(Items.POTATO)),
        Ingredient.of(new ItemStack(Items.BLAZE_ROD))
    };

    int id = 0;

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        for (int k = 0; k < 3; k++) {
          for (int l = 0; l < 3; l++) {
            for (int m = 0; m < 3; m++) {
              for (int n = 0; n < 3; n++) {
                for (int o = 0; o < 3; o++) {

                  Ingredient pickaxe = Ingredient.of(
                      new ItemStack(Items.DIAMOND_PICKAXE),
                      new ItemStack(Items.IRON_PICKAXE),
                      new ItemStack(Items.GOLDEN_PICKAXE),
                      new ItemStack(Items.NETHERITE_PICKAXE),
                      new ItemStack(Items.STONE_PICKAXE),
                      new ItemStack(Items.WOODEN_PICKAXE)
                  );

                  Ingredient axe = Ingredient.of(
                      new ItemStack(Items.DIAMOND_AXE),
                      new ItemStack(Items.IRON_AXE),
                      new ItemStack(Items.GOLDEN_AXE),
                      new ItemStack(Items.NETHERITE_AXE),
                      new ItemStack(Items.STONE_AXE),
                      new ItemStack(Items.WOODEN_AXE)
                  );

                  Ingredient shovel = Ingredient.of(
                      new ItemStack(Items.DIAMOND_SHOVEL),
                      new ItemStack(Items.IRON_SHOVEL),
                      new ItemStack(Items.GOLDEN_SHOVEL),
                      new ItemStack(Items.NETHERITE_SHOVEL),
                      new ItemStack(Items.STONE_SHOVEL),
                      new ItemStack(Items.WOODEN_SHOVEL)
                  );

                  try {
                    ArtisanRecipeBuilder builder = new ArtisanRecipeBuilder();
                    ArtisanRecipe artisanRecipe = builder
                        .setRecipeId(ResourceLocation.fromNamespaceAndPath(OraculumWorktablesMod.MOD_ID, "test_recipe_" + id))
                        .setWidth(5)
                        .setHeight(2)
                        .setIngredients(NonNullList.of(inputs[i], inputs[j], inputs[k], inputs[l], inputs[m], inputs[n], inputs[o]))
                        .setResult(new ItemStack(Blocks.GRAVEL))
                        .addTool(pickaxe, 10)
                        .addTool(axe, 10)
                        .addTool(shovel, 10)
                        .buildShaped(EnumType.BLACKSMITH);
                    recipeConsumer.accept(artisanRecipe);
                    id += 1;

                  } catch (Exception e) {
                    throw new RuntimeException(e);
                  }

                }
              }
            }
          }
        }
      }
    }

  }

  private RecipeInjector() {
    //
  }

}
