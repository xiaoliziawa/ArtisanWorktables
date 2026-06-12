package com.lirxowo.oraculumworktables.common.event;

import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.bus.api.SubscribeEvent;

public class ParticleTypeRegistrationEventHandler {

  @SubscribeEvent
  public void on(RegisterEvent event) {
    if (!event.getRegistryKey().equals(Registries.PARTICLE_TYPE)) {
      return;
    }

    event.register(Registries.PARTICLE_TYPE,
        ResourceLocation.fromNamespaceAndPath(OraculumWorktablesMod.MOD_ID, "mage"),
        () -> new SimpleParticleType(false));
  }

}
