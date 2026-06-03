package com.lirxowo.artisanworktables.common.tile;

import com.lirxowo.artisanworktables.ArtisanWorktablesMod;
import com.lirxowo.artisanworktables.ArtisanWorktablesModCommonConfig;
import com.lirxowo.artisanworktables.common.block.ToolboxMechanicalBlock;
import com.lirxowo.artisanworktables.common.tile.handler.ToolboxItemStackHandler;
import com.lirxowo.artisanworktables.common.util.ToolValidationHelper;
import com.lirxowo.oraculum.network.spi.tile.ITileData;
import com.lirxowo.oraculum.network.spi.tile.BlockEntityDataBase;
import com.lirxowo.oraculum.network.spi.tile.data.TileDataItemStackHandler;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;

public class ToolboxBlockEntity
    extends BlockEntityDataBase {

  public static final String NAME = "toolbox";

  private final ToolboxItemStackHandler itemStackHandler;
  private final LazyOptional<ItemStackHandler> itemCapability;

  public ToolboxBlockEntity(BlockPos pos, BlockState state) {
    super(
        ArtisanWorktablesMod.TileEntityTypes.TOOLBOX,
        pos,
        state,
        ArtisanWorktablesMod.getProxy().getTileDataService()
    );

    Predicate<ItemStack> predicate = itemStack -> itemStack.isEmpty()
        || this.allowNonToolItems()
        || ToolValidationHelper.isValidTool(itemStack, ArtisanWorktablesMod.getProxy().getRecipeManager());

    this.itemStackHandler = new ToolboxItemStackHandler(predicate, 27);
    this.itemStackHandler.addObserver((stackHandler, slotIndex) -> this.setChanged());

    this.itemCapability = LazyOptional.of(() -> this.itemStackHandler);

    this.registerTileDataForNetwork(new ITileData[]{
        new TileDataItemStackHandler<>(this.itemStackHandler)
    });
  }

  // ---------------------------------------------------------------------------
  // Accessors
  // ---------------------------------------------------------------------------

  public boolean isMechanical() {
    return this.level != null
        && this.level.getBlockState(this.getBlockPos()).getBlock() instanceof ToolboxMechanicalBlock;
  }

  public ToolboxItemStackHandler getItemStackHandler() {
    return this.itemStackHandler;
  }

  public boolean canPlayerUse(Player player) {
    return this.level != null
        && this.level.getBlockEntity(this.getBlockPos()) == this
        && player.distanceToSqr(this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 0.5, this.getBlockPos().getZ() + 0.5) <= 64;
  }

  private boolean allowNonToolItems() {
    return ArtisanWorktablesModCommonConfig.allowNonToolItemsInToolboxes;
  }

  // ---------------------------------------------------------------------------
  // Capability
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
    if (capability == ForgeCapabilities.ITEM_HANDLER) {
      return this.itemCapability.cast();
    }

    return super.getCapability(capability, side);
  }

  // ---------------------------------------------------------------------------
  // Serialization
  // ---------------------------------------------------------------------------

  @Override
  public void load(@Nonnull CompoundTag nbt) {
    super.load(nbt);
    this.itemStackHandler.deserializeNBT(nbt.getCompound("itemStackHandler"));

    if (nbt.contains("Items", Tag.TAG_LIST)) {
      NonNullList<ItemStack> itemStacks = NonNullList.withSize(27, ItemStack.EMPTY);
      ContainerHelper.loadAllItems(nbt, itemStacks);

      for (int i = 0; i < 27; i++) {
        this.itemStackHandler.forceStackInSlot(i, itemStacks.get(i));
      }
    }
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag nbt) {
    super.saveAdditional(nbt);
    nbt.put("itemStackHandler", this.itemStackHandler.serializeNBT());
  }
}