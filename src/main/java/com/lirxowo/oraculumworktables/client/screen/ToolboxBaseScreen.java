package com.lirxowo.oraculumworktables.client.screen;

import com.lirxowo.oraculumworktables.common.container.ToolboxBaseContainer;
import com.lirxowo.oraculum.gui.GuiContainerBase;
import com.lirxowo.oraculum.gui.GuiHelper;
import com.lirxowo.oraculum.gui.Texture;
import com.lirxowo.oraculum.gui.element.GuiElementTextureRectangle;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.GuiGraphics;

import javax.annotation.Nonnull;
import java.awt.*;

public abstract class ToolboxBaseScreen<C extends ToolboxBaseContainer>
    extends GuiContainerBase<C> {

  private static final int TEXT_SHADOW_COLOR = new Color(103, 69, 29).getRGB();

  public ToolboxBaseScreen(C container, Inventory playerInventory, Component title, int width, int height) {

    super(container, playerInventory, title, width, height);

    this.guiContainerElementAdd(new GuiElementTextureRectangle(
        this,
        this.getTexture(),
        0,
        0,
        176,
        166
    ));
  }

  protected abstract Texture getTexture();

  @Override
  public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {

    this.renderBackground(guiGraphics);
    super.render(guiGraphics, mouseX, mouseY, partialTicks);
    this.renderTooltip(guiGraphics, mouseX, mouseY);
  }

  @Override
  protected void renderLabels(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY) {

    super.renderLabels(guiGraphics, mouseX, mouseY);

    PoseStack matrixStack = guiGraphics.pose();
    GuiHelper.drawStringOutlined(matrixStack, this.title, this.titleLabelX, this.titleLabelY, this.font, TEXT_SHADOW_COLOR, false);
    GuiHelper.drawStringOutlined(matrixStack, Component.translatable("container.inventory"), this.inventoryLabelX, this.inventoryLabelY, this.font, TEXT_SHADOW_COLOR, false);
  }
}
