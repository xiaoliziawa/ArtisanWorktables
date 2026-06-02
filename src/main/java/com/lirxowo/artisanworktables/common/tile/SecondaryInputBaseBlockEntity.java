package com.lirxowo.artisanworktables.common.tile;

import com.lirxowo.artisanworktables.common.recipe.ISecondaryIngredientMatcher;
import com.lirxowo.artisanworktables.common.recipe.SecondaryIngredientMatcher;
import com.lirxowo.artisanworktables.common.reference.EnumType;
import com.lirxowo.artisanworktables.common.tile.handler.MutuallyExclusiveStackHandlerWrapper;
import com.lirxowo.athenaeum.inventory.spi.ObservableStackHandler;
import com.lirxowo.athenaeum.network.spi.tile.data.service.ITileDataService;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

public abstract class SecondaryInputBaseBlockEntity
    extends BaseBlockEntity {

  protected ObservableStackHandler secondaryIngredientHandler;
  protected MutuallyExclusiveStackHandlerWrapper wrapper;

  // ---------------------------------------------------------------------------
  // Initialization
  // ---------------------------------------------------------------------------

  protected SecondaryInputBaseBlockEntity(BlockEntityType<?> tileEntityType, BlockPos pos, BlockState state, ITileDataService tileDataService) {
    super(tileEntityType, pos, state, tileDataService);
  }

  protected SecondaryInputBaseBlockEntity(BlockEntityType<?> tileEntityType, BlockPos pos, BlockState state, ITileDataService tileDataService, EnumType type) {
    super(tileEntityType, pos, state, tileDataService, type);
  }

  @Override
  protected void initialize(EnumType type) {
    super.initialize(type);
    this.secondaryIngredientHandler = new ObservableStackHandler(9);
    this.secondaryIngredientHandler.addObserver((stackHandler, slotIndex) -> {
      this.setChanged();
      this.requiresRecipeUpdate = true;
    });
    this.wrapper = new MutuallyExclusiveStackHandlerWrapper(this.secondaryIngredientHandler);
  }

  // ---------------------------------------------------------------------------
  // Accessors
  // ---------------------------------------------------------------------------

  @Override
  public ISecondaryIngredientMatcher getSecondaryIngredientMatcher() {
    int slotCount = this.secondaryIngredientHandler.getSlots();
    List<ItemStack> inputs = new ArrayList<>(slotCount);

    for (int i = 0; i < slotCount; i++) {
      ItemStack itemStack = this.secondaryIngredientHandler.getStackInSlot(i);
      inputs.add(itemStack);
    }

    return new SecondaryIngredientMatcher(inputs);
  }

  @Override
  public IItemHandlerModifiable getSecondaryIngredientHandler() {
    return this.secondaryIngredientHandler;
  }

  // ---------------------------------------------------------------------------
  // Serialization
  // ---------------------------------------------------------------------------

  @Override
  public void load(@Nonnull CompoundTag nbt) {
    super.load(nbt);
    this.secondaryIngredientHandler.deserializeNBT(nbt.getCompound("secondaryIngredientHandler"));
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag nbt) {
    super.saveAdditional(nbt);
    nbt.put("secondaryIngredientHandler", this.secondaryIngredientHandler.serializeNBT());
  }
}
