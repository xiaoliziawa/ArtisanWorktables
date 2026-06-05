package com.lirxowo.oraculumworktables;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = OraculumWorktablesMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class OraculumWorktablesModCommonConfig {

  public static ForgeConfigSpec CONFIG_SPEC;
  public static ConfigCommon CONFIG;

  static {
    ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    CONFIG = new ConfigCommon(builder);
    CONFIG_SPEC = builder.build();
  }

  @SubscribeEvent
  public static void onModConfigEvent(final ModConfigEvent.Loading configEvent) {

    if (configEvent.getConfig().getSpec() == OraculumWorktablesModCommonConfig.CONFIG_SPEC) {
      OraculumWorktablesModCommonConfig.bake();
    }
  }

  public static boolean enableDuplicateRecipeHashWarnings;
  public static boolean restrictCraftMinimumDurability;
  public static int fluidCapacityWorktable;
  public static int fluidCapacityWorkstation;
  public static int fluidCapacityWorkshop;
  public static boolean allowNonToolItemsInToolboxes;
  public static boolean hideIncompatibilityMessage;
  public static boolean enableMemeCraftSound;
  public static double memeCraftSoundChance;

  public static void bake() {

    enableDuplicateRecipeHashWarnings = CONFIG.enableDuplicateRecipeHashWarnings.get();
    restrictCraftMinimumDurability = CONFIG.restrictCraftMinimumDurability.get();
    fluidCapacityWorktable = CONFIG.fluidCapacityWorktable.get();
    fluidCapacityWorkstation = CONFIG.fluidCapacityWorkstation.get();
    fluidCapacityWorkshop = CONFIG.fluidCapacityWorkshop.get();
    allowNonToolItemsInToolboxes = CONFIG.allowNonToolItemsInToolboxes.get();
    hideIncompatibilityMessage = CONFIG.hideIncompatibilityMessage.get();
    enableMemeCraftSound = CONFIG.enableMemeCraftSound.get();
    memeCraftSoundChance = CONFIG.memeCraftSoundChance.get();
  }

  public static class ConfigCommon {

    public final ForgeConfigSpec.BooleanValue enableDuplicateRecipeHashWarnings;
    public final ForgeConfigSpec.BooleanValue restrictCraftMinimumDurability;
    public final ForgeConfigSpec.IntValue fluidCapacityWorktable;
    public final ForgeConfigSpec.IntValue fluidCapacityWorkstation;
    public final ForgeConfigSpec.IntValue fluidCapacityWorkshop;
    public final ForgeConfigSpec.BooleanValue allowNonToolItemsInToolboxes;
    public final ForgeConfigSpec.BooleanValue hideIncompatibilityMessage;
    public final ForgeConfigSpec.BooleanValue enableMemeCraftSound;
    public final ForgeConfigSpec.DoubleValue memeCraftSoundChance;

    public ConfigCommon(ForgeConfigSpec.Builder builder) {

      this.enableDuplicateRecipeHashWarnings = builder
          .comment(
              "Set to true to enable log warnings for duplicate auto-generated recipe names.",
              "Default: " + false
          )
          .define("enableDuplicateRecipeHashWarnings", false);

      this.restrictCraftMinimumDurability = builder
          .comment(
              "If set to true, crafting tools must have sufficient durability remaining to perform the craft.",
              "If set to false, this restriction is ignored.",
              "Default: " + true
          )
          .define("restrictCraftMinimumDurability", true);

      this.fluidCapacityWorktable = builder
          .comment(
              "Worktable fluid capacity in millibuckets.",
              "Default: " + 4000
          )
          .defineInRange("fluidCapacityWorktable", 4000, 0, Integer.MAX_VALUE);

      this.fluidCapacityWorkstation = builder
          .comment(
              "Workstation fluid capacity in millibuckets.",
              "Default: " + 8000
          )
          .defineInRange("fluidCapacityWorkstation", 8000, 0, Integer.MAX_VALUE);

      this.fluidCapacityWorkshop = builder
          .comment(
              "Workshop fluid capacity in millibuckets.",
              "Default: " + 16000
          )
          .defineInRange("fluidCapacityWorkshop", 16000, 0, Integer.MAX_VALUE);

      this.allowNonToolItemsInToolboxes = builder
          .comment(
              "Set to true to allow non-tool items in toolboxes.",
              "Default: " + false
          )
          .define("allowNonToolItemsInToolboxes", false);

      this.hideIncompatibilityMessage = builder
          .comment(
              "Set to true to hide the mod incompatibility message.",
              "Default: " + false
          )
          .define("hideIncompatibilityMessage", false);

      this.enableMemeCraftSound = builder
          .comment(
              "Set to true to enable the default 'meme' craft sound for recipes that don't specify a 'craftSound'.",
              "When enabled, crafting such a recipe has a chance (see memeCraftSoundChance) to play the meme sound.",
              "Recipes that explicitly set 'craftSound' are unaffected by this option.",
              "Default: " + false
          )
          .define("enableMemeCraftSound", false);

      this.memeCraftSoundChance = builder
          .comment(
              "The probability (0.0 - 1.0) to play the default meme craft sound when enableMemeCraftSound is true.",
              "Default: " + 0.03
          )
          .defineInRange("memeCraftSoundChance", 0.03, 0.0, 1.0);
    }
  }
}
