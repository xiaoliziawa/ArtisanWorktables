package com.lirxowo.oraculumworktables.common.event;

import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import com.lirxowo.oraculumworktables.common.block.*;
import com.lirxowo.oraculumworktables.common.reference.EnumTier;
import com.lirxowo.oraculumworktables.common.reference.EnumType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.Map;

public class BlockRegistrationEventHandler {

  private final List<Block> registeredWorktables;
  private final Map<EnumTier, List<Block>> registeredWorktablesByTier;

  public BlockRegistrationEventHandler(List<Block> registeredWorktables, Map<EnumTier, List<Block>> registeredWorktablesByTier) {
    this.registeredWorktables = registeredWorktables;
    this.registeredWorktablesByTier = registeredWorktablesByTier;
  }

  @SubscribeEvent
  public void on(RegisterEvent event) {
    if (!event.getRegistryKey().equals(Registries.BLOCK)) {
      return;
    }

    this.register(event, SoundType.WOOL, EnumType.TAILOR);
    this.register(event, SoundType.WOOD, EnumType.CARPENTER);
    this.register(event, SoundType.STONE, EnumType.MASON);
    this.register(event, SoundType.ANVIL, EnumType.BLACKSMITH);
    this.register(event, SoundType.STONE, EnumType.JEWELER);
    this.register(event, SoundType.WOOD, EnumType.BASIC);
    this.register(event, SoundType.ANVIL, EnumType.ENGINEER);
    this.register(event, SoundType.ANCIENT_DEBRIS, EnumType.MAGE);
    this.register(event, SoundType.WOOD, EnumType.SCRIBE);
    this.register(event, SoundType.STONE, EnumType.CHEMIST);
    this.register(event, SoundType.GRAVEL, EnumType.FARMER);
    this.register(event, SoundType.STONE, EnumType.CHEF);
    this.register(event, SoundType.STONE, EnumType.DESIGNER);
    this.register(event, SoundType.STONE, EnumType.TANNER);
    this.register(event, SoundType.STONE, EnumType.POTTER);

    ToolboxBlock toolboxBlock = new ToolboxBlock();
    ToolboxMechanicalBlock mechanicalToolboxBlock = new ToolboxMechanicalBlock();
    OraculumWorktablesMod.Blocks.TOOLBOX = toolboxBlock;
    OraculumWorktablesMod.Blocks.MECHANICAL_TOOLBOX = mechanicalToolboxBlock;

    event.register(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(OraculumWorktablesMod.MOD_ID, ToolboxBlock.NAME),
        () -> toolboxBlock);
    event.register(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(OraculumWorktablesMod.MOD_ID, ToolboxMechanicalBlock.NAME),
        () -> mechanicalToolboxBlock);
  }

  private void register(RegisterEvent event, SoundType soundType, EnumType type) {
    if (type == EnumType.MAGE) {
      this.register(event, EnumTier.WORKTABLE, type.getName(),
          new MageWorktableBlock(soundType, 2.0f, 3.0f));
      this.register(event, EnumTier.WORKSTATION, type.getName(),
          new MageWorkstationBlock(soundType, 3.0f, 6.0f));
      this.register(event, EnumTier.WORKSHOP, type.getName(),
          new MageWorkshopBlock(soundType, 4.0f, 12.0f));
    } else {
      this.register(event, EnumTier.WORKTABLE, type.getName(),
          new WorktableBlock(type, soundType, 2.0f, 3.0f));
      this.register(event, EnumTier.WORKSTATION, type.getName(),
          new WorkstationBlock(type, soundType, 3.0f, 6.0f));
      this.register(event, EnumTier.WORKSHOP, type.getName(),
          new WorkshopBlock(type, soundType, 4.0f, 12.0f));
    }
  }

  private void register(RegisterEvent event, EnumTier tier, String name, Block block) {
    String registryName = tier.getName() + "_" + name;
    event.register(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(OraculumWorktablesMod.MOD_ID, registryName), () -> block);
    this.registeredWorktables.add(block);
    this.registeredWorktablesByTier.computeIfAbsent(tier, enumTier -> new java.util.ArrayList<>()).add(block);
  }
}
