package com.lirxowo.oraculumworktables.common.event;

import com.lirxowo.oraculumworktables.common.sound.ModSounds;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

public class SoundEventRegistrationEventHandler {

  @SubscribeEvent
  public void on(RegisterEvent event) {

    if (!event.getRegistryKey().equals(Registries.SOUND_EVENT)) {
      return;
    }

    event.register(Registries.SOUND_EVENT,
        ModSounds.CRAFT_MEME_ID,
        () -> SoundEvent.createVariableRangeEvent(ModSounds.CRAFT_MEME_ID));

    for (String name : ModSounds.CRAFT_SOUND_NAMES) {
      ResourceLocation id = ModSounds.craftSoundId(name);
      event.register(Registries.SOUND_EVENT, id, () -> SoundEvent.createVariableRangeEvent(id));
    }
  }
}
