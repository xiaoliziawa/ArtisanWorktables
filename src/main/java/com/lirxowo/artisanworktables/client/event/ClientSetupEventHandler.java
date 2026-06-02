package com.lirxowo.artisanworktables.client.event;

import com.lirxowo.artisanworktables.ArtisanWorktablesMod;
import com.lirxowo.artisanworktables.client.screen.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetupEventHandler {

  @SubscribeEvent
  public void on(FMLClientSetupEvent event) {

    MenuScreens.register(ArtisanWorktablesMod.ContainerTypes.WORKTABLE, WorktableScreen::new);
    MenuScreens.register(ArtisanWorktablesMod.ContainerTypes.WORKSTATION, WorkstationScreen::new);
    MenuScreens.register(ArtisanWorktablesMod.ContainerTypes.WORKSHOP, WorkshopScreen::new);

    MenuScreens.register(ArtisanWorktablesMod.ContainerTypes.TOOLBOX, ToolboxScreen::new);
    MenuScreens.register(ArtisanWorktablesMod.ContainerTypes.MECHANICAL_TOOLBOX, ToolboxMechanicalScreen::new);
  }

}
