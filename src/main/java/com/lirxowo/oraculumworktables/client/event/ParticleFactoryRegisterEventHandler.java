package com.lirxowo.oraculumworktables.client.event;

import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import com.lirxowo.oraculumworktables.common.particle.MageParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ParticleFactoryRegisterEventHandler {

  @SubscribeEvent
  public void on(RegisterParticleProvidersEvent event) {

    event.registerSpriteSet(OraculumWorktablesMod.ParticleTypes.MAGE, MageParticle.Factory::new);
  }
}