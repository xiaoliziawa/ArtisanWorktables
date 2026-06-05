package com.lirxowo.oraculumworktables.common.container;

import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class ToolboxMechanicalContainer
    extends ToolboxBaseContainer {

  public static final String NAME = "mechanical_toolbox";

  public ToolboxMechanicalContainer(int id, Level world, BlockPos blockPos, Inventory playerInventory, Player player) {

    super(OraculumWorktablesMod.ContainerTypes.MECHANICAL_TOOLBOX, id, world, blockPos, playerInventory, player);
  }
}
