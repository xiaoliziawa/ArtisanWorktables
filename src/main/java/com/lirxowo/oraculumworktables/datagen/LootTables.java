package com.lirxowo.oraculumworktables.datagen;

import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import com.google.gson.Gson;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.data.PackOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class LootTables
    implements DataProvider {

  private final PackOutput output;
  private final Gson gson;
  private final Logger logger;

  public LootTables(PackOutput output, Gson gson, Logger logger) {

    this.output = output;
    this.gson = gson;
    this.logger = logger;
  }

  @Override
  public CompletableFuture<?> run(@Nonnull CachedOutput cache) {

    return CompletableFuture.runAsync(() -> {
      for (Block block : OraculumWorktablesMod.getProxy().getRegisteredWorktables()) {
        ResourceLocation resourceLocation = BuiltInRegistries.BLOCK.getKey(block);
        String resourceLocationPath = Objects.requireNonNull(resourceLocation).getPath();
        this.createAndSaveTable(cache, block, resourceLocationPath);
      }
    });

//    this.createAndSaveTable(cache, OraculumWorktablesMod.Blocks.TOOLBOX, ToolboxBlock.NAME);
//    this.createAndSaveTable(cache, OraculumWorktablesMod.Blocks.MECHANICAL_TOOLBOX, ToolboxMechanicalBlock.NAME);
  }

  private void createAndSaveTable(@Nonnull CachedOutput cache, Block block, String resourceLocationPath) {

    LootTable.Builder lootTableBuilder = this.createTable(resourceLocationPath, block);
    LootTable lootTable = lootTableBuilder.setParamSet(LootContextParamSets.BLOCK).build();
    Path path = this.output.getOutputFolder().resolve("data/" + OraculumWorktablesMod.MOD_ID + "/loot_tables/blocks/" + resourceLocationPath + ".json");

    try {
      DataProvider.saveStable(cache, LootDataType.TABLE.parser().toJsonTree(lootTable), path);

    } catch (Exception e) {
      this.logger.error("Couldn't write loot table ", path, e);
    }
  }

  private LootTable.Builder createTable(String name, Block block) {

    LootPool.Builder builder = LootPool.lootPool()
        .name(name)
        .when(ExplosionCondition.survivesExplosion())
        .setRolls(ConstantValue.exactly(1))
        .add(LootItem.lootTableItem(block));
    return LootTable.lootTable().withPool(builder);
  }

  @Nonnull
  @Override
  public String getName() {
    return "OraculumWorktables LootTables";
  }
}
