package com.lirxowo.artisanworktables.client.screen.element;

import com.lirxowo.artisanworktables.ArtisanWorktablesMod;
import com.lirxowo.artisanworktables.common.network.CSPacketWorktableTankDestroyFluid;
import com.lirxowo.athenaeum.gui.GuiContainerBase;
import com.lirxowo.athenaeum.gui.element.GuiElementFluidTankVertical;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.List;

public abstract class GuiElementFluidTankBase
    extends GuiElementFluidTankVertical {

  protected final BlockPos blockPos;
  protected final int overlayColor;

  public GuiElementFluidTankBase(
      GuiContainerBase guiBase,
      FluidTank fluidTank,
      int elementX,
      int elementY,
      int elementWidth,
      int elementHeight,
      int overlayColor,
      BlockPos blockPos
  ) {

    super(guiBase, fluidTank, elementX, elementY, elementWidth, elementHeight);
    this.overlayColor = overlayColor;
    this.blockPos = blockPos;
  }

  public void elementClicked(double mouseX, double mouseY, int mouseButton) {

    if (mouseButton == 0 && Screen.hasShiftDown()) {
      ArtisanWorktablesMod.getProxy().getPacketService()
          .sendToServer(new CSPacketWorktableTankDestroyFluid(this.blockPos));
    }
  }

  public List<Component> tooltipTextGet(List<Component> list) {

    if (this.fluidTank.getFluid() == FluidStack.EMPTY || this.fluidTank.getFluidAmount() == 0) {
      list.add(Component.translatable("gui.artisanworktables.tooltip.fluid.empty"));
      list.add(Component.literal(this.fluidTank.getFluidAmount() + " / " + this.fluidTank.getCapacity() + " mB")
          .withStyle(ChatFormatting.GRAY));

    } else {
      Fluid fluid = this.fluidTank.getFluid().getFluid();
      list.add(fluid.getFluidType().getDescription(this.fluidTank.getFluid()));
      list.add(Component.literal(this.fluidTank.getFluidAmount() + " / " + this.fluidTank.getCapacity() + " mB")
          .withStyle(ChatFormatting.GRAY));
      list.add(Component.translatable(
          "gui.artisanworktables.tooltip.fluid.destroy"
      ).withStyle(ChatFormatting.DARK_GRAY));
    }

    return list;
  }
}
