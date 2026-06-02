package com.lirxowo.artisanworktables.common.plugin.jei;

import com.lirxowo.artisanworktables.common.container.BaseContainer;
import com.lirxowo.artisanworktables.common.recipe.ArtisanRecipe;
import com.lirxowo.artisanworktables.common.reference.EnumTier;
import com.lirxowo.artisanworktables.common.reference.EnumType;
import com.lirxowo.artisanworktables.common.tile.BaseBlockEntity;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RecipeTransferInfo<C extends BaseContainer>
    implements IRecipeTransferInfo<C, ArtisanRecipe> {

  private final Class<C> containerClass;
  private final EnumTier tier;
  private final EnumType type;
  private final RecipeType<ArtisanRecipe> recipeType;

  public RecipeTransferInfo(
      Class<C> containerClass,
      EnumTier tier,
      EnumType type,
      RecipeType<ArtisanRecipe> recipeType
  ) {

    this.containerClass = containerClass;
    this.type = type;
    this.recipeType = recipeType;
    this.tier = tier;
  }

  @Nonnull
  @Override
  public Class<? extends C> getContainerClass() {

    return this.containerClass;
  }

  @Nonnull
  @Override
  public Optional<MenuType<C>> getMenuType() {

    return Optional.empty();
  }

  @Nonnull
  @Override
  public RecipeType<ArtisanRecipe> getRecipeType() {

    return this.recipeType;
  }

  @Override
  public boolean canHandle(C container, ArtisanRecipe recipe) {

    BaseBlockEntity tile = container.getTile();
    EnumType type = tile.getTableType();
    int tableTierId = tile.getTableTier().getId();
    return this.type == type
        && this.tier != null
        && this.tier.getId() <= tableTierId;
  }

  @Nonnull
  @Override
  public List<Slot> getRecipeSlots(C container, ArtisanRecipe recipe) {

    // grid

    /*
    2019-10-15 - Issue #196
    A smaller grid size is returned if the transfer handler making the request
    passes a smaller transferGridSize parameter. This prevents the smaller
    transfer handlers from making a mess in the larger tables.
     */

    List<Slot> result = new ArrayList<>();
    BaseBlockEntity tile = container.getTile();
    int rowLength = (tile.getTableTier() == EnumTier.WORKSHOP) ? 5 : 3;
    int transferGridSize = (this.tier == EnumTier.WORKSHOP) ? 5 : 3;

    for (int row = 0; row < transferGridSize; row++) {
      for (int col = 0; col < transferGridSize; col++) {
        result.add(container.slots.get(container.slotIndexCraftingMatrixStart + col + (row * rowLength)));
      }
    }

    // tool
    for (int i = container.slotIndexToolsStart; i <= container.slotIndexToolsEnd; i++) {
      result.add(container.slots.get(i));
    }

    // Intentionally left out the secondary ingredient slots to prevent JEI from
    // transferring items to these slots when the transfer button is clicked.

    return result;
  }

  @Nonnull
  @Override
  public List<Slot> getInventorySlots(C container, ArtisanRecipe recipe) {

    List<Slot> result = new ArrayList<>();

    for (int i = container.slotIndexInventoryStart; i <= container.slotIndexInventoryEnd; i++) {
      result.add(container.slots.get(i));
    }

    for (int i = container.slotIndexHotbarStart; i <= container.slotIndexHotbarEnd; i++) {
      result.add(container.slots.get(i));
    }

    if (container.canPlayerUseToolbox()) {

      for (int i = container.slotIndexToolboxStart; i <= container.slotIndexToolboxEnd; i++) {
        result.add(container.slots.get(i));
      }
    }

    for (int i = container.slotIndexSecondaryInputStart; i < container.slotIndexSecondaryInputEnd; i++) {
      result.add(container.slots.get(i));
    }

    return result;
  }

  @Override
  public boolean requireCompleteSets(C container, ArtisanRecipe recipe) {

    return false;
  }
}
