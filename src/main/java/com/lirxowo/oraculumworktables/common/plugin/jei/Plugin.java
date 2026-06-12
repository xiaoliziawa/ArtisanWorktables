package com.lirxowo.oraculumworktables.common.plugin.jei;

import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import com.lirxowo.oraculumworktables.common.block.BaseBlock;
import com.lirxowo.oraculumworktables.common.container.BaseContainer;
import com.lirxowo.oraculumworktables.common.container.WorkshopContainer;
import com.lirxowo.oraculumworktables.common.container.WorkstationContainer;
import com.lirxowo.oraculumworktables.common.container.WorktableContainer;
import com.lirxowo.oraculumworktables.common.recipe.ArtisanRecipe;
import com.lirxowo.oraculumworktables.common.recipe.RecipeTypes;
import com.lirxowo.oraculumworktables.common.reference.EnumTier;
import com.lirxowo.oraculumworktables.common.reference.EnumType;
import com.lirxowo.oraculumworktables.common.util.Key;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.resources.ResourceLocation;

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
            OraculumWorktablesMod.MOD_ID,
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
    List<Block> registeredWorktables = OraculumWorktablesMod.getProxy().getRegisteredWorktables();
    List<IRecipeCategory<?>> recipeCategoryList = new ArrayList<>(registeredWorktables.size());

    Map<EnumTier, CategoryDrawHandler> categoryDrawHandlerMap = new EnumMap<>(EnumTier.class);
    Map<EnumTier, CategorySetupHandler> categorySetupHandlerMap = new EnumMap<>(EnumTier.class);

    for (EnumTier tier : EnumTier.values()) {
      categoryDrawHandlerMap.put(tier, new CategoryDrawHandler(tier));
      categorySetupHandlerMap.put(tier, new CategorySetupHandler(tier));
    }

    for (Block block : registeredWorktables) {
      ResourceLocation registryName = BuiltInRegistries.BLOCK.getKey(block);
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

    RecipeManager recipeManager = OraculumWorktablesMod.getProxy().getRecipeManager();

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

    List<Block> registeredWorktables = OraculumWorktablesMod.getProxy().getRegisteredWorktables();

    for (Block block : registeredWorktables) {
      BaseBlock baseBlock = (BaseBlock) block;
      EnumType type = baseBlock.getType();
      EnumTier tier = EnumTier.fromName(Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block)).getPath().split("_")[0]);

      registry.addRecipeCatalyst(new ItemStack(block), CATEGORY_KEYS.get(tier).get(type));
    }
  }

  @Override
  public void registerRecipeTransferHandlers(@Nonnull IRecipeTransferRegistration registry) {

    IRecipeTransferHandlerHelper transferHelper = registry.getTransferHelper();
    IIngredientManager ingredientManager = registry.getJeiHelpers().getIngredientManager();

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
        Plugin.registerToolAwareTransfer(registry, transferHelper, ingredientManager, containerClass, tier, type, CATEGORY_KEYS.get(tier).get(type));
      }
    }
  }

  private static <C extends BaseContainer> void registerToolAwareTransfer(
      IRecipeTransferRegistration registry,
      IRecipeTransferHandlerHelper transferHelper,
      IIngredientManager ingredientManager,
      Class<C> containerClass,
      EnumTier tier,
      EnumType type,
      RecipeType<ArtisanRecipe> recipeType
  ) {

    RecipeTransferInfo<C> transferInfo = new RecipeTransferInfo<>(containerClass, tier, type, recipeType);
    registry.addRecipeTransferHandler(new ArtisanRecipeTransferHandler<>(transferInfo, transferHelper, ingredientManager), recipeType);
  }

  private List<ArtisanRecipe> getRecipes(RecipeManager recipeManager, EnumTier tier, EnumType type) {

    List<RecipeHolder<ArtisanRecipe>> holders = new ArrayList<>();
    holders.addAll(recipeManager.getAllRecipesFor(RecipeTypes.SHAPED_RECIPE_TYPES.get(type)));
    holders.addAll(recipeManager.getAllRecipesFor(RecipeTypes.SHAPELESS_RECIPE_TYPES.get(type)));

    holders.sort(Comparator.comparing(holder -> holder.id().toString()));

    List<ArtisanRecipe> result = new ArrayList<>(holders.size());

    for (RecipeHolder<ArtisanRecipe> holder : holders) {
      ArtisanRecipe recipe = holder.value();

      if (recipe.matchTier(tier)) {
        result.add(recipe);
      }
    }

    return result;
  }
}
