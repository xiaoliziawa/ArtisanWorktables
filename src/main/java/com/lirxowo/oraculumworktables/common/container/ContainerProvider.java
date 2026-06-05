package com.lirxowo.oraculumworktables.common.container;

import com.lirxowo.oraculumworktables.common.reference.EnumTier;
import com.lirxowo.oraculumworktables.common.reference.EnumType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class ContainerProvider
    implements MenuProvider {

  private final EnumTier tier;
  private final EnumType type;
  private final Level world;
  private final BlockPos pos;

  public ContainerProvider(EnumTier tier, EnumType type, Level world, BlockPos pos) {

    this.tier = tier;
    this.type = type;
    this.world = world;
    this.pos = pos;
  }

  @Nonnull
  @Override
  public Component getDisplayName() {

    return Component.translatable("block.oraculumworktables." + this.tier.getName() + "_" + this.type.getName());
  }

  @Override
  public AbstractContainerMenu createMenu(int id, @Nonnull Inventory playerInventory, @Nonnull Player playerEntity) {

    switch (this.tier) {

      case WORKTABLE:
        return new WorktableContainer(id, this.world, this.pos, playerInventory, playerEntity);

      case WORKSTATION:
        return new WorkstationContainer(id, this.world, this.pos, playerInventory, playerEntity);

      case WORKSHOP:
        return new WorkshopContainer(id, this.world, this.pos, playerInventory, playerEntity);

      default:
        throw new IllegalArgumentException("Unknown table tier: " + this.tier.getName());
    }
  }
}