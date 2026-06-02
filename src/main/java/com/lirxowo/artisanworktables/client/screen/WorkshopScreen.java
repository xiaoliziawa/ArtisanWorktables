package com.lirxowo.artisanworktables.client.screen;

import com.lirxowo.artisanworktables.client.screen.element.GuiElementFluidTankLarge;
import com.lirxowo.artisanworktables.common.container.BaseContainer;
import com.lirxowo.artisanworktables.common.tile.BaseBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;

public class WorkshopScreen
    extends BaseScreen {

  private static final int WIDTH = 176;
  private static final int HEIGHT = 225;

  public WorkshopScreen(BaseContainer container, Inventory playerInventory, Component title) {

    super(container, playerInventory, title, WIDTH, HEIGHT);
  }

  @Override
  protected void addFluidTankElement(BaseBlockEntity tile, int overlayColor) {

    this.guiContainerElementAdd(new GuiElementFluidTankLarge(
        this,
        tile.getTank(),
        tile.getBlockPos(),
        overlayColor,
        8,
        17
    ));
  }
}
