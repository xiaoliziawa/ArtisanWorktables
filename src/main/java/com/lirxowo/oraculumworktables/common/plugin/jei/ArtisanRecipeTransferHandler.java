package com.lirxowo.oraculumworktables.common.plugin.jei;

import com.lirxowo.oraculumworktables.api.IToolHandler;
import com.lirxowo.oraculumworktables.common.container.BaseContainer;
import com.lirxowo.oraculumworktables.common.recipe.ArtisanRecipe;
import com.lirxowo.oraculumworktables.common.recipe.ArtisanToolHandlers;
import com.lirxowo.oraculumworktables.common.recipe.ToolEntry;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Recipe transfer that understands the mod's tool-handler matching (Tinkers, Tetra, ...).
 * <p>
 * JEI's stock transfer matches inventory items against the literal ingredients shown in a
 * tool slot, so modular tools - which never appear in those ingredient lists - are reported
 * as missing even though crafting accepts them. For each tool slot we locate an actual
 * matching tool in the player inventory via {@link IToolHandler}, swap it into the slot's
 * displayed ingredient, then delegate the real transfer to JEI so its networking is reused.
 */
public class ArtisanRecipeTransferHandler<C extends BaseContainer>
    implements IRecipeTransferHandler<C, ArtisanRecipe> {

  private final RecipeTransferInfo<C> transferInfo;
  private final IRecipeTransferHandlerHelper helper;
  private final IIngredientManager ingredientManager;
  private final IRecipeTransferHandler<C, ArtisanRecipe> delegate;

  public ArtisanRecipeTransferHandler(
      RecipeTransferInfo<C> transferInfo,
      IRecipeTransferHandlerHelper helper,
      IIngredientManager ingredientManager
  ) {

    this.transferInfo = transferInfo;
    this.helper = helper;
    this.ingredientManager = ingredientManager;
    this.delegate = helper.createUnregisteredRecipeTransferHandler(transferInfo);
  }

  @Override
  public Class<? extends C> getContainerClass() {

    return this.transferInfo.getContainerClass();
  }

  @Override
  public Optional<MenuType<C>> getMenuType() {

    return this.transferInfo.getMenuType();
  }

  @Override
  public RecipeType<ArtisanRecipe> getRecipeType() {

    return this.transferInfo.getRecipeType();
  }

  @Nullable
  @Override
  public IRecipeTransferError transferRecipe(C container, ArtisanRecipe recipe, IRecipeSlotsView recipeSlots, Player player, boolean maxTransfer, boolean doTransfer) {

    List<ToolEntry> toolEntries = recipe.getTools();

    if (toolEntries.isEmpty()) {
      return this.delegate.transferRecipe(container, recipe, recipeSlots, player, maxTransfer, doTransfer);
    }

    List<Slot> inventorySlots = this.transferInfo.getInventorySlots(container, recipe);
    Set<Integer> usedSlots = new HashSet<>();
    List<IRecipeSlotView> adjustedViews = new ArrayList<>();
    List<IRecipeSlotView> missingTools = new ArrayList<>();
    boolean replaced = false;

    for (IRecipeSlotView slotView : recipeSlots.getSlotViews()) {

      int toolIndex = ArtisanRecipeTransferHandler.toolIndex(slotView);

      if (toolIndex < 0 || toolIndex >= toolEntries.size()) {
        adjustedViews.add(slotView);
        continue;
      }

      ItemStack matched = this.findInventoryTool(toolEntries.get(toolIndex), recipe, inventorySlots, usedSlots);

      if (matched.isEmpty()) {
        adjustedViews.add(slotView);
        missingTools.add(slotView);
        continue;
      }

      Optional<ITypedIngredient<ItemStack>> typed = this.ingredientManager.createTypedIngredient(VanillaTypes.ITEM_STACK, matched);

      if (typed.isPresent()) {
        adjustedViews.add(new ToolSlotView(slotView, typed.get()));
        replaced = true;

      } else {
        adjustedViews.add(slotView);
      }
    }

    if (!missingTools.isEmpty()) {
      return this.helper.createUserErrorForMissingSlots(
          Component.translatable("jei.oraculum_worktables.transfer.missing_tool"), missingTools);
    }

    IRecipeSlotsView effectiveView = replaced
        ? this.helper.createRecipeSlotsView(adjustedViews)
        : recipeSlots;

    return this.delegate.transferRecipe(container, recipe, effectiveView, player, maxTransfer, doTransfer);
  }

  private ItemStack findInventoryTool(ToolEntry toolEntry, ArtisanRecipe recipe, List<Slot> inventorySlots, Set<Integer> usedSlots) {

    for (Slot slot : inventorySlots) {

      if (usedSlots.contains(slot.index)) {
        continue;
      }

      ItemStack stack = slot.getItem();

      if (stack.isEmpty()) {
        continue;
      }

      IToolHandler handler = ArtisanToolHandlers.get(stack);

      if (toolEntry.matches(handler, stack)
          && recipe.hasSufficientToolDurability(handler, stack)) {
        usedSlots.add(slot.index);
        return stack;
      }
    }

    return ItemStack.EMPTY;
  }

  private static int toolIndex(IRecipeSlotView slotView) {

    Optional<String> slotName = slotView.getSlotName();

    if (slotName.isEmpty() || !slotName.get().startsWith(CategorySetupHandler.TOOL_SLOT_NAME_PREFIX)) {
      return -1;
    }

    try {
      return Integer.parseInt(slotName.get().substring(CategorySetupHandler.TOOL_SLOT_NAME_PREFIX.length()));

    } catch (NumberFormatException e) {
      return -1;
    }
  }

  private static final class ToolSlotView
      implements IRecipeSlotView {

    private final IRecipeSlotView delegate;
    private final ITypedIngredient<?> ingredient;

    private ToolSlotView(IRecipeSlotView delegate, ITypedIngredient<?> ingredient) {

      this.delegate = delegate;
      this.ingredient = ingredient;
    }

    @Override
    public Stream<ITypedIngredient<?>> getAllIngredients() {

      return Stream.of(this.ingredient);
    }

    @Override
    public Optional<ITypedIngredient<?>> getDisplayedIngredient() {

      return Optional.of(this.ingredient);
    }

    @Override
    public RecipeIngredientRole getRole() {

      return this.delegate.getRole();
    }

    @Override
    public void drawHighlight(GuiGraphics guiGraphics, int color) {

      this.delegate.drawHighlight(guiGraphics, color);
    }

    @Override
    public Optional<String> getSlotName() {

      return this.delegate.getSlotName();
    }
  }
}
