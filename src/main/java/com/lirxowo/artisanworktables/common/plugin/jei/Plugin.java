package com.lirxowo.artisanworktables.common.plugin.jei;

import com.lirxowo.artisanworktables.ArtisanWorktablesMod;
import com.lirxowo.artisanworktables.common.block.BaseBlock;
import com.lirxowo.artisanworktables.common.container.BaseContainer;
import com.lirxowo.artisanworktables.common.container.WorkshopContainer;
import com.lirxowo.artisanworktables.common.container.WorkstationContainer;
import com.lirxowo.artisanworktables.common.container.WorktableContainer;
import com.lirxowo.artisanworktables.common.recipe.ArtisanRecipe;
import com.lirxowo.artisanworktables.common.recipe.RecipeTypes;
import com.lirxowo.artisanworktables.common.reference.EnumTier;
import com.lirxowo.artisanworktables.common.reference.EnumType;
import com.lirxowo.artisanworktables.common.util.Key;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.*;

@JeiPlugin
public class Plugin
    implements IModPlugin {

  public static final ResourceLocation RECIPE_BACKGROUND = Key.from("textures/gui/recipe_background.png");

  public static final Map<EnumTier, Map<EnumType, RecipeType<ArtisanRecipe>>> CATEGORY_KEYS;

  static {
    CATEGORY_KEYS = new EnumMap<>(EnumTier.class);
    for (EnumTier tier : EnumTier.values()) {
      for (EnumType type : EnumType.values()) {
        Map<EnumType, RecipeType<ArtisanRecipe>> map = CATEGORY_KEYS.computeIfAbsent(tier, t -> new EnumMap<>(EnumType.class));
        map.put(type, RecipeType.create(
            ArtisanWorktablesMod.MOD_ID,
            String.format("%s_%s", tier.getName(), type.getName()),
            ArtisanRecipe.class
        ));
      }
    }
  }

  @Nonnull
  @Override
  public ResourceLocation getPluginUid() {

    return Key.from("jei_plugin");
  }

  @Override
  public void registerCategories(@Nonnull IRecipeCategoryRegistration registry) {

    IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
    CategoryFactory categoryFactory = new CategoryFactory();
    List<Block> registeredWorktables = ArtisanWorktablesMod.getProxy().getRegisteredWorktables();
    List<IRecipeCategory<?>> recipeCategoryList = new ArrayList<>(registeredWorktables.size());

    Map<EnumTier, CategoryDrawHandler> categoryDrawHandlerMap = new EnumMap<>(EnumTier.class);
    Map<EnumTier, CategorySetupHandler> categorySetupHandlerMap = new EnumMap<>(EnumTier.class);

    for (EnumTier tier : EnumTier.values()) {
      categoryDrawHandlerMap.put(tier, new CategoryDrawHandler(tier));
      categorySetupHandlerMap.put(tier, new CategorySetupHandler(tier));
    }

    for (Block block : registeredWorktables) {
      ResourceLocation registryName = ForgeRegistries.BLOCKS.getKey(block);
      String path = Objects.requireNonNull(registryName).getPath();
      String[] split = path.split("_");
      EnumTier tier = EnumTier.fromName(split[0]);
      EnumType type = EnumType.fromName(split[1]);
      CategorySetupHandler categorySetupHandler = categorySetupHandlerMap.get(tier);
      CategoryDrawHandler categoryDrawHandler = categoryDrawHandlerMap.get(tier);

      BaseCategory<?> category = categoryFactory.create(tier, type, block, guiHelper, categorySetupHandler, categoryDrawHandler);
      recipeCategoryList.add(category);
    }

    registry.addRecipeCategories(recipeCategoryList.toArray(new IRecipeCategory[0]));
  }

  @Override
  public void registerRecipes(@Nonnull IRecipeRegistration registry) {

    RecipeManager recipeManager = ArtisanWorktablesMod.getProxy().getRecipeManager();

    if (recipeManager == null) {
      throw new RuntimeException("Null recipe manager");
    }

    for (EnumTier tier : EnumTier.values()) {
      for (EnumType type : EnumType.values()) {
        List<ArtisanRecipe> recipes = this.getRecipes(recipeManager, tier, type);
        registry.addRecipes(CATEGORY_KEYS.get(tier).get(type), recipes);
      }
    }
  }

  @Override
  public void registerRecipeCatalysts(@Nonnull IRecipeCatalystRegistration registry) {

    List<Block> registeredWorktables = ArtisanWorktablesMod.getProxy().getRegisteredWorktables();

    for (Block block : registeredWorktables) {
      BaseBlock baseBlock = (BaseBlock) block;
      EnumType type = baseBlock.getType();
      EnumTier tier = EnumTier.fromName(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath().split("_")[0]);

      registry.addRecipeCatalyst(new ItemStack(block), CATEGORY_KEYS.get(tier).get(type));
    }
  }

  @Override
  public void registerRecipeTransferHandlers(@Nonnull IRecipeTransferRegistration registry) {

    for (EnumTier tier : EnumTier.values()) {

      Class<? extends BaseContainer> containerClass;

      switch (tier) {
        case WORKTABLE:
          containerClass = WorktableContainer.class;
          break;
        case WORKSTATION:
          containerClass = WorkstationContainer.class;
          break;
        case WORKSHOP:
          containerClass = WorkshopContainer.class;
          break;
        default:
          throw new RuntimeException("Unknown tier: " + tier);
      }

      for (EnumType type : EnumType.values()) {
        registry.addRecipeTransferHandler(new RecipeTransferInfo<>(containerClass, tier, type, CATEGORY_KEYS.get(tier).get(type)));
      }
    }
  }

  private List<ArtisanRecipe> getRecipes(RecipeManager recipeManager, EnumTier tier, EnumType type) {

    List<ArtisanRecipe> result = new ArrayList<>();

    {
      List<ArtisanRecipe> recipesForType = recipeManager.getAllRecipesFor(RecipeTypes.SHAPED_RECIPE_TYPES.get(type));

      for (ArtisanRecipe recipe : recipesForType) {
        if (recipe.matchTier(tier)) {
          result.add(recipe);
        }
      }
    }

    {
      List<ArtisanRecipe> recipesForType = recipeManager.getAllRecipesFor(RecipeTypes.SHAPELESS_RECIPE_TYPES.get(type));

      for (ArtisanRecipe recipe : recipesForType) {
        if (recipe.matchTier(tier)) {
          result.add(recipe);
        }
      }
    }

    result.sort(Comparator.comparing(recipe -> recipe.getId().toString()));

    return result;
  }
}
