package com.lirxowo.artisanworktables.common.container;

import com.lirxowo.artisanworktables.ArtisanWorktablesMod;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class WorkstationContainer
    extends BaseContainer {

  public static final String NAME = "workstation";

  public WorkstationContainer(int id, Level world, BlockPos blockPos, Inventory playerInventory, Player player) {

    super(ArtisanWorktablesMod.ContainerTypes.WORKSTATION, id, world, blockPos, playerInventory, player);
  }
}
