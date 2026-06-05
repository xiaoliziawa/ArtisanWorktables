package com.lirxowo.oraculumworktables.common.container;

import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import com.lirxowo.oraculumworktables.common.block.ToolboxBaseBlock;
import com.lirxowo.oraculumworktables.common.block.ToolboxBlock;
import com.lirxowo.oraculumworktables.common.block.ToolboxMechanicalBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class ToolboxContainerProvider
    implements MenuProvider {

  private final ToolboxBaseBlock block;
  private final Level world;
  private final BlockPos pos;

  public ToolboxContainerProvider(Level world, BlockPos pos) {

    this.block = (ToolboxBaseBlock) world.getBlockState(pos).getBlock();
    this.world = world;
    this.pos = pos;
  }

  @Nonnull
  @Override
  public Component getDisplayName() {

    return Component.translatable(
        "block." + OraculumWorktablesMod.MOD_ID + "." + BuiltInRegistries.BLOCK.getKey(this.block).getPath()
    );
  }

  @Override
  public AbstractContainerMenu createMenu(int id, @Nonnull Inventory playerInventory, @Nonnull Player playerEntity) {

    if (this.block instanceof ToolboxBlock) {
      return new ToolboxContainer(id, this.world, this.pos, playerInventory, playerEntity);

    } else if (this.block instanceof ToolboxMechanicalBlock) {
      return new ToolboxMechanicalContainer(id, this.world, this.pos, playerInventory, playerEntity);

    } else {
      throw new IllegalArgumentException("Unknown block type: " + BuiltInRegistries.BLOCK.getKey(this.block));
    }
  }
}
