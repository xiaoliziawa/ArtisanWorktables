package com.lirxowo.artisanworktables.client.event;

import com.lirxowo.artisanworktables.ArtisanWorktablesMod;
import com.lirxowo.artisanworktables.common.particle.MageParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ParticleFactoryRegisterEventHandler {

  @SubscribeEvent
  public void on(RegisterParticleProvidersEvent event) {

    event.registerSpriteSet(ArtisanWorktablesMod.ParticleTypes.MAGE, MageParticle.Factory::new);
  }
}