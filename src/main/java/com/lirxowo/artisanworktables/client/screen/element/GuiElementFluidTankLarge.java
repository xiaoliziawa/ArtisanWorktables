package com.lirxowo.artisanworktables.client.screen.element;

import com.lirxowo.artisanworktables.client.ReferenceTexture;
import com.lirxowo.oraculum.gui.GuiContainerBase;
import com.lirxowo.oraculum.gui.GuiHelper;
import com.lirxowo.oraculum.gui.element.IGuiElementClickable;
import com.lirxowo.oraculum.gui.element.IGuiElementTooltipProvider;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.core.BlockPos;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class GuiElementFluidTankLarge
    extends GuiElementFluidTankBase
    implements IGuiElementClickable,
    IGuiElementTooltipProvider {

  private static final int ELEMENT_WIDTH = 6;
  private static final int ELEMENT_HEIGHT = 88;
  private static final int ELEMENT_OVERLAY_WIDTH = 6;
  private static final int ELEMENT_OVERLAY_HEIGHT = 52;

  public GuiElementFluidTankLarge(
      GuiContainerBase guiBase,
      FluidTank fluidTank,
      BlockPos blockPos,
      int overlayColor,
      int elementX,
      int elementY
  ) {

    super(guiBase, fluidTank, elementX, elementY, ELEMENT_WIDTH, ELEMENT_HEIGHT, overlayColor, blockPos);
  }

  @Override
  public void drawForegroundLayer(PoseStack matrixStack, int mouseX, int mouseY) {

    super.drawForegroundLayer(matrixStack, mouseX, mouseY);

    this.textureBind(ReferenceTexture.TEXTURE_FLUID_OVERLAY);

    RenderSystem.enableBlend();
    RenderSystem.setShaderColor(
        ((this.overlayColor >> 16) & 0xFF) / 255f,
        ((this.overlayColor >> 8) & 0xFF) / 255f,
        (this.overlayColor & 0xFF) / 255f,
        1f
    );

    GuiHelper.drawModalRectWithCustomSizedTexture(
        matrixStack,
        this.elementX,
        this.elementY,
        0,
        ReferenceTexture.TEXTURE_FLUID_OVERLAY.getPositionX(),
        ReferenceTexture.TEXTURE_FLUID_OVERLAY.getPositionY(),
        ELEMENT_OVERLAY_WIDTH,
        ELEMENT_OVERLAY_HEIGHT,
        ReferenceTexture.TEXTURE_FLUID_OVERLAY.getWidth(),
        ReferenceTexture.TEXTURE_FLUID_OVERLAY.getHeight()
    );

    GuiHelper.drawModalRectWithCustomSizedTexture(
        matrixStack,
        this.elementX,
        this.elementY + ELEMENT_OVERLAY_HEIGHT - 16,
        0,
        ReferenceTexture.TEXTURE_FLUID_OVERLAY.getPositionX(),
        ReferenceTexture.TEXTURE_FLUID_OVERLAY.getPositionY(),
        ELEMENT_OVERLAY_WIDTH,
        ELEMENT_OVERLAY_HEIGHT,
        ReferenceTexture.TEXTURE_FLUID_OVERLAY.getWidth(),
        ReferenceTexture.TEXTURE_FLUID_OVERLAY.getHeight()
    );

    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
  }

}
