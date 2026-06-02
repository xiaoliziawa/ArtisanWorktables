package com.lirxowo.artisanworktables.common.event;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ParticleTypeRegistrationEventHandler {

  @SubscribeEvent
  public void on(RegisterEvent event) {
    if (!event.getRegistryKey().equals(Registries.PARTICLE_TYPE)) {
      return;
    }

    event.register(Registries.PARTICLE_TYPE,
        ResourceLocation.fromNamespaceAndPath("artisanworktables", "mage"),
        () -> new SimpleParticleType(false));
  }

}