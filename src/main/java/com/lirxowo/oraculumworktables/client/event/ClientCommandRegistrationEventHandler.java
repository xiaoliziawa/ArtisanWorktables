package com.lirxowo.oraculumworktables.client.event;

import com.lirxowo.oraculumworktables.client.command.ScreenshotCommand;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientCommandRegistrationEventHandler {

  @SubscribeEvent
  public void on(RegisterClientCommandsEvent event) {

    ScreenshotCommand.register(event.getDispatcher());
  }
}
