package com.lirxowo.artisanworktables.client.screen;

import com.lirxowo.artisanworktables.common.container.BaseContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;

public class WorktableScreen
    extends BaseScreen {

  private static final int WIDTH = 176;
  private static final int HEIGHT = 166;

  public WorktableScreen(BaseContainer container, Inventory playerInventory, Component title) {

    super(container, playerInventory, title, WIDTH, HEIGHT);
  }
}
