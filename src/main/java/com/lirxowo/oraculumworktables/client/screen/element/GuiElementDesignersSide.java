package com.lirxowo.oraculumworktables.client.screen.element;

import com.lirxowo.oraculumworktables.common.tile.IBlockEntityDesigner;
import com.lirxowo.oraculum.gui.GuiContainerBase;
import com.lirxowo.oraculum.gui.Texture;
import com.lirxowo.oraculum.gui.element.GuiElementTextureRectangle;
import com.mojang.blaze3d.vertex.PoseStack;

public class GuiElementDesignersSide
    extends GuiElementTextureRectangle {

  private final IBlockEntityDesigner tile;

  public GuiElementDesignersSide(
      GuiContainerBase guiBase,
      IBlockEntityDesigner tile,
      Texture texture,
      int elementX,
      int elementY
  ) {

    super(guiBase, texture, elementX, elementY, 68, 176);
    this.tile = tile;
  }

  @Override
  public void drawBackgroundLayer(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {

    if (this.tile != null && !this.tile.getTileEntity().isRemoved()) {
      super.drawBackgroundLayer(matrixStack, partialTicks, mouseX, mouseY);
    }
  }
}
