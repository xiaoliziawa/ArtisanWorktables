package com.lirxowo.artisanworktables.common.container;

import com.lirxowo.artisanworktables.ArtisanWorktablesMod;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class ToolboxMechanicalContainer
    extends ToolboxBaseContainer {

  public static final String NAME = "mechanical_toolbox";

  public ToolboxMechanicalContainer(int id, Level world, BlockPos blockPos, Inventory playerInventory, Player player) {

    super(ArtisanWorktablesMod.ContainerTypes.MECHANICAL_TOOLBOX, id, world, blockPos, playerInventory, player);
  }
}
