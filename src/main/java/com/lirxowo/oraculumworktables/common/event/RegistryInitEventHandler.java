package com.lirxowo.oraculumworktables.common.event;

import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import com.lirxowo.oraculumworktables.common.sound.ModSounds;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class RegistryInitEventHandler {

  @SubscribeEvent
  public void on(FMLCommonSetupEvent event) {
    event.enqueueWork(() -> {
      OraculumWorktablesMod.TileEntityTypes.init();
      OraculumWorktablesMod.ContainerTypes.init();
      OraculumWorktablesMod.ParticleTypes.init();
      ModSounds.init();
    });
  }
}
