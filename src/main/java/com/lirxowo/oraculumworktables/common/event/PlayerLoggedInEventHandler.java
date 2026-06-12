package com.lirxowo.oraculumworktables.common.event;

import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import com.lirxowo.oraculumworktables.OraculumWorktablesModCommonConfig;
import com.lirxowo.oraculumworktables.common.network.SCPacketIncompatible;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PlayerLoggedInEventHandler {

  private final List<String> incompatibleModList;

  public PlayerLoggedInEventHandler(List<String> incompatibleModList) {

    this.incompatibleModList = incompatibleModList;
  }

  @SubscribeEvent
  public void on(PlayerEvent.PlayerLoggedInEvent event) {

    if (OraculumWorktablesModCommonConfig.hideIncompatibilityMessage) {
      return;
    }

    List<String> result = new ArrayList<>();

    for (String modId : this.incompatibleModList) {

      if (ModList.get().isLoaded(modId)) {
        result.add(modId);
      }
    }

    if (!result.isEmpty()) {
      OraculumWorktablesMod.getProxy().getPacketService().sendToPlayer((ServerPlayer) event.getEntity(), new SCPacketIncompatible(result));
    }
  }
}