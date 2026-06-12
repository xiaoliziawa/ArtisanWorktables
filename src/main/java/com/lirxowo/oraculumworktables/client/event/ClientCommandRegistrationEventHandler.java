package com.lirxowo.oraculumworktables.client.event;

import com.lirxowo.oraculumworktables.client.command.ScreenshotCommand;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.bus.api.SubscribeEvent;

public class ClientCommandRegistrationEventHandler {

  @SubscribeEvent
  public void on(RegisterClientCommandsEvent event) {

    ScreenshotCommand.register(event.getDispatcher());
  }
}
