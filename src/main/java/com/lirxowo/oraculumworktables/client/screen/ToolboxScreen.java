package com.lirxowo.oraculumworktables.client.screen;

import com.lirxowo.oraculumworktables.client.ReferenceTexture;
import com.lirxowo.oraculumworktables.common.container.ToolboxContainer;
import com.lirxowo.oraculum.gui.Texture;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;

public class ToolboxScreen
    extends ToolboxBaseScreen<ToolboxContainer> {

  private static final int WIDTH = 176;
  private static final int HEIGHT = 166;

  public ToolboxScreen(ToolboxContainer container, Inventory playerInventory, Component title) {

    super(container, playerInventory, title, WIDTH, HEIGHT);
  }

  @Override
  protected Texture getTexture() {

    return ReferenceTexture.TEXTURE_TOOLBOX;
  }
}
