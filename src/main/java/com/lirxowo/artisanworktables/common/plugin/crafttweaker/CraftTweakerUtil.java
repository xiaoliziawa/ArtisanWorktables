package com.lirxowo.artisanworktables.common.plugin.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.lirxowo.artisanworktables.ArtisanWorktablesMod;
import org.apache.logging.log4j.Logger;

public final class CraftTweakerUtil {

  private static final Logger LOGGER = CraftTweakerAPI.getLogger(ArtisanWorktablesMod.MOD_ID);

  public static String validateRecipeName(String name) {

    name = CraftTweakerUtil.fixRecipeName(name);
    if(!name.chars().allMatch((ch) -> ch == 95 || ch == 45 || ch >= 97 && ch <= 122 || ch >= 48 && ch <= 57 || ch == 47 || ch == 46)) {
      throw new IllegalArgumentException("Given name does not fit the \"[a-z0-9/._-]\" regex! Name: \"" + name + "\"");
    }
    return name;
  }

  public static String fixRecipeName(String name) {

    String fixed = name;
    if (fixed.indexOf(':') >= 0) {
      String temp = fixed.replace(":", ".");
      LOGGER.warn("Invalid recipe name \"{}\", recipe names cannot have a \":\"! New recipe name: \"{}\"", fixed, temp);
      fixed = temp;
    }
    if (fixed.indexOf(' ') >= 0) {
      String temp = fixed.replace(" ", ".");
      LOGGER.warn("Invalid recipe name \"{}\", recipe names cannot have a \" \"! New recipe name: \"{}\"", fixed, temp);
      fixed = temp;
    }
    if (!fixed.toLowerCase().equals(fixed)) {
      String temp = fixed.toLowerCase();
      LOGGER.warn("Invalid recipe name \"{}\", recipe names have to be lowercase! New recipe name: \"{}\"", fixed, temp);
      fixed = temp;
    }
    return fixed;
  }

  private CraftTweakerUtil() {
    //
  }

}
