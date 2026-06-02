package com.lirxowo.artisanworktables.client.screen;

import com.lirxowo.artisanworktables.common.container.BaseContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;

public class WorkstationScreen
    extends BaseScreen {

  private static final int WIDTH = 176;
  private static final int HEIGHT = 189;

  public WorkstationScreen(BaseContainer container, Inventory playerInventory, Component title) {

    super(container, playerInventory, title, WIDTH, HEIGHT);
  }
}
