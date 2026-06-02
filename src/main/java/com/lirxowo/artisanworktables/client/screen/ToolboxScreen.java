package com.lirxowo.artisanworktables.client.screen;

import com.lirxowo.artisanworktables.client.ReferenceTexture;
import com.lirxowo.artisanworktables.common.container.ToolboxContainer;
import com.lirxowo.athenaeum.gui.Texture;
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
