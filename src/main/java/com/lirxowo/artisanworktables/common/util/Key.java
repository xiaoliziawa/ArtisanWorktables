package com.lirxowo.artisanworktables.common.util;

import com.lirxowo.artisanworktables.ArtisanWorktablesMod;
import net.minecraft.resources.ResourceLocation;

public final class Key {

  public static ResourceLocation from(String path) {

    return ResourceLocation.fromNamespaceAndPath(ArtisanWorktablesMod.MOD_ID, path);
  }

  private Key() {
    //
  }
}
