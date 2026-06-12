package com.lirxowo.oraculumworktables.datagen;

import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@EventBusSubscriber(modid = OraculumWorktablesMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class GatherDataEventHandler {

  private static final Logger LOGGER = LogManager.getLogger(GatherDataEventHandler.class);

  @SubscribeEvent
  public static void on(GatherDataEvent event) {

    DataGenerator dataGenerator = event.getGenerator();
    PackOutput packOutput = dataGenerator.getPackOutput();
    ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

    dataGenerator.addProvider(event.includeClient(), new BlockStates(packOutput, existingFileHelper));
    dataGenerator.addProvider(event.includeServer(), new LootTables(packOutput, event.getLookupProvider(), LOGGER));
  }

  private GatherDataEventHandler() {
    //
  }
}
