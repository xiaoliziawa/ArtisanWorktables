package com.lirxowo.oraculumworktables.client.screen;

import com.lirxowo.oraculumworktables.client.ReferenceTexture;
import com.lirxowo.oraculumworktables.common.container.ToolboxMechanicalContainer;
import com.lirxowo.oraculum.gui.Texture;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;

public class ToolboxMechanicalScreen
    extends ToolboxBaseScreen<ToolboxMechanicalContainer> {

  private static final int WIDTH = 176;
  private static final int HEIGHT = 166;

  public ToolboxMechanicalScreen(ToolboxMechanicalContainer container, Inventory playerInventory, Component title) {

    super(container, playerInventory, title, WIDTH, HEIGHT);
  }

  @Override
  protected Texture getTexture() {

    return ReferenceTexture.TEXTURE_TOOLBOX_MECHANICAL;
  }
}
