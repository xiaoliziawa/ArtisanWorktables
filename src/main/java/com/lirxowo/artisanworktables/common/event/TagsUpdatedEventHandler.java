package com.lirxowo.artisanworktables.common.event;

import com.lirxowo.artisanworktables.common.util.ToolValidationHelper;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TagsUpdatedEventHandler {

  @SubscribeEvent
  public void on(TagsUpdatedEvent event) {

    ToolValidationHelper.clear();
  }
}