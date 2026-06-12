package com.lirxowo.oraculumworktables.client.event;

import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import com.lirxowo.oraculumworktables.common.particle.MageParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.bus.api.SubscribeEvent;

public class ParticleFactoryRegisterEventHandler {

  @SubscribeEvent
  public void on(RegisterParticleProvidersEvent event) {

    OraculumWorktablesMod.ParticleTypes.init();

    event.registerSpriteSet(OraculumWorktablesMod.ParticleTypes.MAGE, MageParticle.Factory::new);
  }
}