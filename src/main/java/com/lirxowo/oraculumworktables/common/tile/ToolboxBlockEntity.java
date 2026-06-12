package com.lirxowo.oraculumworktables.common.tile;

import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import com.lirxowo.oraculumworktables.OraculumWorktablesModCommonConfig;
import com.lirxowo.oraculumworktables.common.block.ToolboxMechanicalBlock;
import com.lirxowo.oraculumworktables.common.tile.handler.ToolboxItemStackHandler;
import com.lirxowo.oraculumworktables.common.util.ToolValidationHelper;
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
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class ToolboxBlockEntity
    extends BlockEntityDataBase {

  public static final String NAME = "toolbox";

  private final ToolboxItemStackHandler itemStackHandler;

  public ToolboxBlockEntity(BlockPos pos, BlockState state) {
    super(
        OraculumWorktablesMod.TileEntityTypes.TOOLBOX,
        pos,
        state,
        OraculumWorktablesMod.getProxy().getTileDataService()
    );

    Predicate<ItemStack> predicate = itemStack -> itemStack.isEmpty()
        || this.allowNonToolItems()
        || ToolValidationHelper.isValidTool(itemStack, OraculumWorktablesMod.getProxy().getRecipeManager());

    this.itemStackHandler = new ToolboxItemStackHandler(predicate, 27);
    this.itemStackHandler.addObserver((stackHandler, slotIndex) -> this.setChanged());

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
    return OraculumWorktablesModCommonConfig.allowNonToolItemsInToolboxes;
  }

  // ---------------------------------------------------------------------------
  // Serialization
  // ---------------------------------------------------------------------------

  @Override
  protected void read(@Nonnull CompoundTag nbt, @Nonnull HolderLookup.Provider registries) {
    super.read(nbt, registries);
    this.itemStackHandler.deserializeNBT(registries, nbt.getCompound("itemStackHandler"));

    if (nbt.contains("Items", Tag.TAG_LIST)) {
      NonNullList<ItemStack> itemStacks = NonNullList.withSize(27, ItemStack.EMPTY);
      ContainerHelper.loadAllItems(nbt, itemStacks, registries);

      for (int i = 0; i < 27; i++) {
        this.itemStackHandler.forceStackInSlot(i, itemStacks.get(i));
      }
    }
  }

  @Override
  protected void write(@Nonnull CompoundTag nbt, @Nonnull HolderLookup.Provider registries) {
    super.write(nbt, registries);
    nbt.put("itemStackHandler", this.itemStackHandler.serializeNBT(registries));
  }
}
