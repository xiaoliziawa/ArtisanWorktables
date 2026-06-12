package com.lirxowo.oraculumworktables.common.util;

import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import net.minecraft.resources.ResourceLocation;

public final class Key {

  public static ResourceLocation from(String path) {

    return ResourceLocation.fromNamespaceAndPath(OraculumWorktablesMod.MOD_ID, path);
  }

  private Key() {
    //
  }
}
