package com.lirxowo.oraculumworktables.common.util;

import com.lirxowo.oraculumworktables.api.IToolHandler;
import com.lirxowo.oraculumworktables.common.recipe.ArtisanRecipe;
import com.lirxowo.oraculumworktables.common.recipe.ArtisanToolHandlers;
import com.lirxowo.oraculumworktables.common.recipe.RecipeTypes;
import com.lirxowo.oraculumworktables.common.recipe.ToolEntry;
import com.lirxowo.oraculumworktables.common.reference.EnumType;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Responsible for checking all AW recipes for the use of the given tool.
 * <p>
 * Caches results for performance.
 * <p>
 * Uses a synchronized block because it is called from both threads on the
 * same machine in a single player game.
 */
public final class ToolValidationHelper {

  private static final EnumMap<EnumType, Object2BooleanMap<String>> TYPE_CACHE;
  private static final Object2BooleanMap<String> ALL_CACHE;

  static {
    TYPE_CACHE = new EnumMap<>(EnumType.class);
    ALL_CACHE = new Object2BooleanOpenHashMap<>();
  }

  public static void clear() {

    synchronized (TYPE_CACHE) {
      TYPE_CACHE.clear();
      ALL_CACHE.clear();
    }
  }

  public static boolean isValidTool(ItemStack tool, RecipeManager recipeManager) {

    synchronized (ALL_CACHE) {

      String cacheKey = ToolValidationHelper.getCacheKey(tool);

      if (ALL_CACHE.containsKey(cacheKey)) {
        return ALL_CACHE.getBoolean(cacheKey);
      }

      for (EnumType type : EnumType.values()) {

        if (ToolValidationHelper.isValidTool(type, tool, recipeManager)) {
          ALL_CACHE.put(cacheKey, true);
          return true;
        }
      }

      ALL_CACHE.put(cacheKey, false);
      return false;
    }
  }

  public static boolean isValidTool(EnumType type, ItemStack tool, RecipeManager recipeManager) {

    synchronized (TYPE_CACHE) {
      String cacheKey = ToolValidationHelper.getCacheKey(tool);

      Object2BooleanMap<String> cache = TYPE_CACHE.computeIfAbsent(type, (t) -> new Object2BooleanOpenHashMap<>());

      if (cache.containsKey(cacheKey)) {
        return cache.getBoolean(cacheKey);
      }

      IToolHandler toolHandler = ArtisanToolHandlers.get(tool);

      boolean result = false;

      if (ToolValidationHelper.checkRecipeType(toolHandler, tool, recipeManager, RecipeTypes.SHAPED_RECIPE_TYPES.get(type))) {
        result = true;
      }

      if (!result) {

        if (ToolValidationHelper.checkRecipeType(toolHandler, tool, recipeManager, RecipeTypes.SHAPELESS_RECIPE_TYPES.get(type))) {
          result = true;
        }
      }

      cache.put(cacheKey, result);
      return result;
    }
  }

  private static boolean checkRecipeType(IToolHandler toolHandler, ItemStack tool, RecipeManager recipeManager, RecipeType<? extends ArtisanRecipe> recipeType) {

    return ToolValidationHelper.checkList(toolHandler, tool, recipeManager.getAllRecipesFor(recipeType));
  }

  private static boolean checkList(IToolHandler toolHandler, ItemStack tool, List<? extends RecipeHolder<? extends ArtisanRecipe>> recipeList) {

    for (RecipeHolder<? extends ArtisanRecipe> recipeHolder : recipeList) {
      NonNullList<ToolEntry> tools = recipeHolder.value().getTools();

      for (ToolEntry toolEntry : tools) {
        if (toolEntry.matches(toolHandler, tool)) {
          return true;
        }
      }
    }

    return false;
  }

  private static String getCacheKey(ItemStack tool) {

    ResourceLocation itemKey = BuiltInRegistries.ITEM.getKey(tool.getItem());
    DataComponentPatch patch = tool.getComponentsPatch();

    if (patch.isEmpty()) {
      return itemKey.toString();
    }

    StringBuilder builder = new StringBuilder(itemKey.toString());
    boolean appended = false;

    for (Map.Entry<DataComponentType<?>, Optional<?>> entry : patch.entrySet()) {

      if (entry.getKey() == DataComponents.DAMAGE) {
        continue;
      }

      builder.append('|')
          .append(BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(entry.getKey()))
          .append('=')
          .append(entry.getValue());
      appended = true;
    }

    return appended ? builder.toString() : itemKey.toString();
  }

  private ToolValidationHelper() {
    //
  }
}
