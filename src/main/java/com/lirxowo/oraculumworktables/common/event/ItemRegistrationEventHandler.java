package com.lirxowo.oraculumworktables.common.event;

import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.List;

public class ItemRegistrationEventHandler {

  private final List<Block> registeredWorktables;

  public ItemRegistrationEventHandler(List<Block> registeredWorktables) {
    this.registeredWorktables = registeredWorktables;
  }

  @SubscribeEvent
  public void on(RegisterEvent event) {
    if (!event.getRegistryKey().equals(Registries.ITEM)) {
      return;
    }

    for (Block block : this.registeredWorktables) {
      this.registerBlockItem(event, block);
    }

    this.registerBlockItem(event, OraculumWorktablesMod.Blocks.TOOLBOX);
    this.registerBlockItem(event, OraculumWorktablesMod.Blocks.MECHANICAL_TOOLBOX);
  }

  private void registerBlockItem(RegisterEvent event, Block block) {
    ResourceLocation blockId = net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(block);
    if (blockId != null) {
      event.register(Registries.ITEM, blockId,
          () -> new BlockItem(block, new Item.Properties()));
    }
  }
}
