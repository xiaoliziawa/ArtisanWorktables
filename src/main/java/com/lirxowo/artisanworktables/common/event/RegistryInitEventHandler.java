package com.lirxowo.artisanworktables.common.event;

import com.lirxowo.artisanworktables.ArtisanWorktablesMod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class RegistryInitEventHandler {

  @SubscribeEvent
  public void on(FMLCommonSetupEvent event) {
    event.enqueueWork(() -> {
      ArtisanWorktablesMod.TileEntityTypes.init();
      ArtisanWorktablesMod.ContainerTypes.init();
      ArtisanWorktablesMod.ParticleTypes.init();
    });
  }
}
