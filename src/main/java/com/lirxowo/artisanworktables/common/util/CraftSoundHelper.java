package com.lirxowo.artisanworktables.common.util;

import com.lirxowo.artisanworktables.ArtisanWorktablesModCommonConfig;
import com.lirxowo.artisanworktables.common.recipe.ArtisanRecipe;
import com.lirxowo.artisanworktables.common.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public final class CraftSoundHelper {

  private static final float VOLUME = 1.0f;
  private static final float MEME_VOLUME = 0.2f;
  private static final float BASE_PITCH = 1.0f;
  private static final float PITCH_VARIANCE = 0.2f;

  private CraftSoundHelper() {

  }

  public static void playCraftSound(Player player, ArtisanRecipe recipe, BlockPos pos) {

    Level level = player.level();

    if (!level.isClientSide) {
      return;
    }

    String craftSound = recipe.getCraftSound();

    if (craftSound != null && !craftSound.isEmpty()) {
      ResourceLocation id = ResourceLocation.tryParse(craftSound);
      SoundEvent sound = (id == null) ? null : BuiltInRegistries.SOUND_EVENT.get(id);

      if (sound != null) {
        level.playLocalSound(pos, sound, SoundSource.PLAYERS, VOLUME, randomForgePitch(), false);
      }

      return;
    }

    if (!ArtisanWorktablesModCommonConfig.enableMemeCraftSound) {
      return;
    }

    if (Util.RANDOM.nextDouble() >= ArtisanWorktablesModCommonConfig.memeCraftSoundChance) {
      return;
    }

    level.playLocalSound(pos, ModSounds.CRAFT_MEME, SoundSource.PLAYERS, MEME_VOLUME, BASE_PITCH, false);
  }

  private static float randomForgePitch() {

    return BASE_PITCH + (Util.RANDOM.nextFloat() * 2.0f - 1.0f) * PITCH_VARIANCE;
  }
}
