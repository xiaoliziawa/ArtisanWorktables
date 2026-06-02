package com.lirxowo.artisanworktables.client.screen.element;

import com.lirxowo.artisanworktables.common.container.BaseContainer;
import com.lirxowo.athenaeum.gui.GuiContainerBase;
import com.lirxowo.athenaeum.gui.element.GuiElementBase;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.util.Mth;

public class GuiElementMageEffect
    extends GuiElementBase {

  private static final double TWO_PI = Math.PI * 2;
  private static final String[] LETTERS = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

  private final BaseContainer container;

  public GuiElementMageEffect(
      GuiContainerBase guiBase,
      BaseContainer container,
      int elementX,
      int elementY
  ) {

    super(guiBase, elementX, elementY, 16, 16);
    this.container = container;
  }

  @Override
  public void drawBackgroundLayer(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
    //
  }

  @Override
  public void drawForegroundLayer(PoseStack matrixStack, int mouseX, int mouseY) {

    if (this.elementIsMouseInside(mouseX, mouseY)
        && !this.container.slots.get(0).getItem().isEmpty()) {

      float radius = 21f;
      int count = 12;
      float angleIncrement = (float) (TWO_PI / (float) count);
      float offset = (float) ((System.currentTimeMillis() / 12 * (Math.PI / 180f)) % TWO_PI);

      for (int i = 0; i < count; i++) {
        int x = Math.round(Mth.cos(i * angleIncrement + offset) * radius) + this.elementX + this.elementWidth / 2 - 3;
        int y = Math.round(Mth.sin(i * angleIncrement + offset) * radius) + this.elementY + this.elementHeight / 2 - 4;
        this.guiBase.drawString(matrixStack, LETTERS[i], x, y);
      }

    }
  }
}
