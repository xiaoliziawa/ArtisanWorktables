package com.lirxowo.oraculumworktables.common.event;

import com.lirxowo.oraculumworktables.common.util.ToolValidationHelper;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TagsUpdatedEventHandler {

  @SubscribeEvent
  public void on(TagsUpdatedEvent event) {

    ToolValidationHelper.clear();
  }
}