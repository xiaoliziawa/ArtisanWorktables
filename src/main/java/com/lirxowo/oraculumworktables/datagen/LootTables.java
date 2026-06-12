package com.lirxowo.oraculumworktables.datagen;

import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.data.PackOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class LootTables
    implements DataProvider {

  private final PackOutput output;
  private final CompletableFuture<HolderLookup.Provider> registries;
  private final Logger logger;

  public LootTables(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, Logger logger) {

    this.output = output;
    this.registries = registries;
    this.logger = logger;
  }

  @Override
  public CompletableFuture<?> run(@Nonnull CachedOutput cache) {

    return this.registries.thenCompose(provider -> {
      List<CompletableFuture<?>> futures = new ArrayList<>();

      for (Block block : OraculumWorktablesMod.getProxy().getRegisteredWorktables()) {
        ResourceLocation resourceLocation = BuiltInRegistries.BLOCK.getKey(block);
        String resourceLocationPath = Objects.requireNonNull(resourceLocation).getPath();
        futures.add(this.createAndSaveTable(cache, provider, block, resourceLocationPath));
      }

      return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    });
  }

  private CompletableFuture<?> createAndSaveTable(CachedOutput cache, HolderLookup.Provider provider, Block block, String resourceLocationPath) {

    LootTable lootTable = this.createTable(block).setParamSet(LootContextParamSets.BLOCK).build();
    Path path = this.output.getOutputFolder().resolve("data/" + OraculumWorktablesMod.MOD_ID + "/loot_table/blocks/" + resourceLocationPath + ".json");

    return DataProvider.saveStable(cache, provider, LootTable.DIRECT_CODEC, lootTable, path)
        .exceptionally(e -> {
          this.logger.error("Couldn't write loot table {}", path, e);
          return null;
        });
  }

  private LootTable.Builder createTable(Block block) {

    LootPool.Builder builder = LootPool.lootPool()
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
