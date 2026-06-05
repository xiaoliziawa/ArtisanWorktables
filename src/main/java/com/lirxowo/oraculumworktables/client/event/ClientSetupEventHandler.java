package com.lirxowo.oraculumworktables.client.event;

import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import com.lirxowo.oraculumworktables.client.screen.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetupEventHandler {

  @SubscribeEvent
  public void on(FMLClientSetupEvent event) {

    MenuScreens.register(OraculumWorktablesMod.ContainerTypes.WORKTABLE, WorktableScreen::new);
    MenuScreens.register(OraculumWorktablesMod.ContainerTypes.WORKSTATION, WorkstationScreen::new);
    MenuScreens.register(OraculumWorktablesMod.ContainerTypes.WORKSHOP, WorkshopScreen::new);

    MenuScreens.register(OraculumWorktablesMod.ContainerTypes.TOOLBOX, ToolboxScreen::new);
    MenuScreens.register(OraculumWorktablesMod.ContainerTypes.MECHANICAL_TOOLBOX, ToolboxMechanicalScreen::new);
  }

}
