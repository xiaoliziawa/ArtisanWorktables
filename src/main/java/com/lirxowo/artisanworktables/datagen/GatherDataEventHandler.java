package com.lirxowo.artisanworktables.datagen;

import com.lirxowo.artisanworktables.ArtisanWorktablesMod;
import com.google.gson.GsonBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = ArtisanWorktablesMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class GatherDataEventHandler {

  private static final Logger LOGGER = LogManager.getLogger(GatherDataEventHandler.class);

  @SubscribeEvent
  public static void on(GatherDataEvent event) {

    DataGenerator dataGenerator = event.getGenerator();
    PackOutput packOutput = dataGenerator.getPackOutput();
    ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

    dataGenerator.addProvider(event.includeClient(), new BlockStates(packOutput, existingFileHelper));
    dataGenerator.addProvider(event.includeServer(),
        new LootTables(
            packOutput,
            new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create(),
            LOGGER
        )
    );
  }

  private GatherDataEventHandler() {
    //
  }
}
