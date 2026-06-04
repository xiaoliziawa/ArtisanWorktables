package com.lirxowo.artisanworktables.common.sound;

import com.lirxowo.artisanworktables.common.reference.Reference;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public final class ModSounds {

  public static final ResourceLocation CRAFT_MEME_ID =
      ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "craft_meme");

  public static final String[] CRAFT_SOUND_NAMES = {
      "chainsaw",
      "file",
      "forge_hammer",
      "macerator",
      "mortar",
      "portal_opening",
      "saw",
      "screwdriver",
      "wirecutter",
      "wrench"
  };

  public static SoundEvent CRAFT_MEME;

  public static ResourceLocation craftSoundId(String name) {

    return ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "craft." + name);
  }

  public static void init() {

    CRAFT_MEME = BuiltInRegistries.SOUND_EVENT.get(CRAFT_MEME_ID);
  }
}
