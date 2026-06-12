package com.lirxowo.oraculumworktables.common.event;

import com.lirxowo.oraculumworktables.common.util.ToolValidationHelper;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.bus.api.SubscribeEvent;

public class TagsUpdatedEventHandler {

  @SubscribeEvent
  public void on(TagsUpdatedEvent event) {

    ToolValidationHelper.clear();
  }
}