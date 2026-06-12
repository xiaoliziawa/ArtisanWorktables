package com.lirxowo.oraculumworktables.client.screen.element;

import com.lirxowo.oraculumworktables.common.tile.ToolboxBlockEntity;
import com.lirxowo.oraculum.gui.GuiContainerBase;
import com.lirxowo.oraculum.gui.Texture;
import com.lirxowo.oraculum.gui.element.GuiElementTextureRectangle;
import com.mojang.blaze3d.vertex.PoseStack;

import java.util.function.IntSupplier;

public class GuiElementToolboxSide
    extends GuiElementTextureRectangle {

  private final ToolboxBlockEntity toolbox;
  private final IntSupplier elementY;

  public GuiElementToolboxSide(
      GuiContainerBase guiBase,
      ToolboxBlockEntity toolbox,
      Texture texture,
      int elementX,
      IntSupplier elementY
  ) {

    super(guiBase, texture, elementX, 0, 68, 176);
    this.toolbox = toolbox;
    this.elementY = elementY;
  }

  @Override
  public void drawBackgroundLayer(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {

    if (this.toolbox != null && !this.toolbox.isRemoved()) {
      super.drawBackgroundLayer(matrixStack, partialTicks, mouseX, mouseY);
    }
  }

  @Override
  protected int elementYModifiedGet() {

    return super.elementYModifiedGet() + this.elementY.getAsInt();
  }
}
