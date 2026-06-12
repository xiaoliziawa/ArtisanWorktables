package com.lirxowo.oraculumworktables.client.event;

import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import com.lirxowo.oraculumworktables.client.screen.ToolboxMechanicalScreen;
import com.lirxowo.oraculumworktables.client.screen.ToolboxScreen;
import com.lirxowo.oraculumworktables.client.screen.WorkshopScreen;
import com.lirxowo.oraculumworktables.client.screen.WorkstationScreen;
import com.lirxowo.oraculumworktables.client.screen.WorktableScreen;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class ClientSetupEventHandler {

  @SubscribeEvent
  public void on(RegisterMenuScreensEvent event) {

    OraculumWorktablesMod.ContainerTypes.init();

    event.register(OraculumWorktablesMod.ContainerTypes.WORKTABLE, WorktableScreen::new);
    event.register(OraculumWorktablesMod.ContainerTypes.WORKSTATION, WorkstationScreen::new);
    event.register(OraculumWorktablesMod.ContainerTypes.WORKSHOP, WorkshopScreen::new);
    event.register(OraculumWorktablesMod.ContainerTypes.TOOLBOX, ToolboxScreen::new);
    event.register(OraculumWorktablesMod.ContainerTypes.MECHANICAL_TOOLBOX, ToolboxMechanicalScreen::new);
  }
}
