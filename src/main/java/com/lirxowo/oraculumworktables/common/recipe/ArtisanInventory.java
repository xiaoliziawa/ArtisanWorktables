package com.lirxowo.oraculumworktables.common.recipe;

import com.lirxowo.oraculumworktables.api.IToolHandler;
import com.lirxowo.oraculumworktables.common.reference.EnumTier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class ArtisanInventory
    implements Container {

  private final EnumTier tableTier;
  private final PlayerData playerData;
  private final ICraftingMatrixStackHandler craftingMatrix;
  private final FluidTank fluidTank;
  private final ItemStack[] tools;
  private final IToolHandler[] toolHandlers;
  private final IItemHandlerModifiable toolHandler;
  private final IItemHandlerModifiable toolReplacementHandler;
  private final ISecondaryIngredientMatcher secondaryIngredientMatcher;
  private final IItemHandlerModifiable secondaryIngredientHandler;
  private final IItemHandlerModifiable secondaryOutputHandler;
  private final int width;
  private final int height;

  public ArtisanInventory(
      EnumTier tableTier,
      @Nullable PlayerData playerData,
      ICraftingMatrixStackHandler craftingMatrix,
      FluidTank fluidTank,
      ItemStack[] tools,
      IToolHandler[] toolHandlers,
      IItemHandlerModifiable toolHandler,
      IItemHandlerModifiable toolReplacementHandler,
      ISecondaryIngredientMatcher secondaryIngredientMatcher,
      IItemHandlerModifiable secondaryIngredientHandler,
      IItemHandlerModifiable secondaryOutputHandler,
      int width,
      int height
  ) {

    this.tableTier = tableTier;
    this.playerData = playerData;
    this.craftingMatrix = craftingMatrix;
    this.fluidTank = fluidTank;
    this.tools = tools;
    this.toolHandlers = toolHandlers;
    this.toolHandler = toolHandler;
    this.toolReplacementHandler = toolReplacementHandler;
    this.secondaryIngredientMatcher = secondaryIngredientMatcher;
    this.secondaryIngredientHandler = secondaryIngredientHandler;
    this.secondaryOutputHandler = secondaryOutputHandler;
    this.width = width;
    this.height = height;
  }

  // ---------------------------------------------------------------------------
  // Accessors
  // ---------------------------------------------------------------------------

  public EnumTier getTableTier() {

    return this.tableTier;
  }

  public ICraftingMatrixStackHandler getCraftingMatrix() {

    return this.craftingMatrix;
  }

  public FluidTank getFluidTank() {

    return this.fluidTank;
  }

  public FluidStack getFluidStack() {

    return this.fluidTank.getFluid();
  }

  public Optional<PlayerData> getPlayerData() {

    return Optional.ofNullable(this.playerData);
  }

  public ItemStack[] getTools() {

    return this.tools;
  }

  public IToolHandler[] getToolHandlers() {

    return this.toolHandlers;
  }

  public IItemHandlerModifiable getToolHandler() {

    return this.toolHandler;
  }

  @Nullable
  public IItemHandlerModifiable getToolReplacementHandler() {

    return this.toolReplacementHandler;
  }

  public ISecondaryIngredientMatcher getSecondaryIngredientMatcher() {

    return this.secondaryIngredientMatcher;
  }

  public IItemHandlerModifiable getSecondaryIngredientHandler() {

    return this.secondaryIngredientHandler;
  }

  public IItemHandlerModifiable getSecondaryOutputHandler() {

    return this.secondaryOutputHandler;
  }

  // ---------------------------------------------------------------------------
  // Implementation
  // ---------------------------------------------------------------------------

  @Override
  public int getContainerSize() {
    return this.width * this.height;
  }

  @Override
  public boolean isEmpty() {
    for (int i = 0; i < this.craftingMatrix.getSlots(); i++) {
      ItemStack itemStack = this.craftingMatrix.getStackInSlot(i);

      if (!itemStack.isEmpty()) {
        return false;
      }
    }

    return true;
  }

  @Nonnull
  @Override
  public ItemStack getItem(int index) {
    if (index < 0 || index >= this.craftingMatrix.getSlots()) {
      return ItemStack.EMPTY;
    }

    return this.craftingMatrix.getStackInSlot(index);
  }

  @Nonnull
  @Override
  public ItemStack removeItem(int index, int count) {
    if (index < 0 || index >= this.craftingMatrix.getSlots()) {
      return ItemStack.EMPTY;
    }

    ItemStack stackInSlot = this.craftingMatrix.getStackInSlot(index);
    ItemStack splitStack = stackInSlot.copy().split(count);
    stackInSlot.setCount(stackInSlot.getCount() - splitStack.getCount());
    this.craftingMatrix.setStackInSlot(index, stackInSlot);
    return splitStack;
  }

  @Nonnull
  @Override
  public ItemStack removeItemNoUpdate(int index) {
    if (index < 0 || index >= this.craftingMatrix.getSlots()) {
      return ItemStack.EMPTY;
    }

    ItemStack stackInSlot = this.craftingMatrix.getStackInSlot(index);
    this.craftingMatrix.setStackInSlot(index, ItemStack.EMPTY);
    return stackInSlot;
  }

  @Override
  public void setItem(int index, @Nonnull ItemStack stack) {
    if (index < 0 || index >= this.craftingMatrix.getSlots()) {
      return;
    }

    this.craftingMatrix.setStackInSlot(index, stack);
  }

  @Override
  public int getMaxStackSize() {
    return 64;
  }

  @Override
  public void setChanged() {
    //
  }

  @Override
  public boolean stillValid(@Nonnull Player player) {
    return true;
  }

  @Override
  public void startOpen(@Nonnull Player player) {
    //
  }

  @Override
  public void stopOpen(@Nonnull Player player) {
    //
  }

  @Override
  public boolean canPlaceItem(int index, @Nonnull ItemStack stack) {
    return true;
  }

  @Override
  public void clearContent() {
    for (int i = 0; i < this.craftingMatrix.getSlots(); i++) {
      this.craftingMatrix.setStackInSlot(i, ItemStack.EMPTY);
    }
  }

  public static class PlayerData {

    public final boolean isCreative;
    public final int experience;
    public final int levels;

    public PlayerData(boolean isCreative, int experience, int levels) {

      this.isCreative = isCreative;
      this.experience = experience;
      this.levels = levels;
    }
  }
}