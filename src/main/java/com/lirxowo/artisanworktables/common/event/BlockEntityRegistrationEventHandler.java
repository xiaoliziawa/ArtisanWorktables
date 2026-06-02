package com.lirxowo.artisanworktables.common.event;

import com.lirxowo.artisanworktables.ArtisanWorktablesMod;
import com.lirxowo.artisanworktables.common.reference.EnumTier;
import com.lirxowo.artisanworktables.common.tile.ToolboxBlockEntity;
import com.lirxowo.artisanworktables.common.tile.WorkshopBlockEntity;
import com.lirxowo.artisanworktables.common.tile.WorkstationBlockEntity;
import com.lirxowo.artisanworktables.common.tile.WorktableBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.Map;

public class BlockEntityRegistrationEventHandler {

  private final Map<EnumTier, List<Block>> registeredWorktablesByTier;

  public BlockEntityRegistrationEventHandler(Map<EnumTier, List<Block>> registeredWorktablesByTier) {
    this.registeredWorktablesByTier = registeredWorktablesByTier;
  }

  @SubscribeEvent
  public void on(RegisterEvent event) {
    if (!event.getRegistryKey().equals(Registries.BLOCK_ENTITY_TYPE)) {
      return;
    }

    this.register(event, EnumTier.WORKTABLE);
    this.register(event, EnumTier.WORKSTATION);
    this.register(event, EnumTier.WORKSHOP);

    event.register(Registries.BLOCK_ENTITY_TYPE,
        ResourceLocation.fromNamespaceAndPath("artisanworktables", "toolbox"),
        () -> BlockEntityType.Builder.of(ToolboxBlockEntity::new,
            ArtisanWorktablesMod.Blocks.TOOLBOX,
            ArtisanWorktablesMod.Blocks.MECHANICAL_TOOLBOX).build(null));
  }

  private void register(RegisterEvent event, EnumTier tier) {
    List<Block> blockList = this.registeredWorktablesByTier.get(tier);
    Block[] blockArray = blockList.toArray(new Block[0]);

    event.register(Registries.BLOCK_ENTITY_TYPE,
        ResourceLocation.fromNamespaceAndPath("artisanworktables", tier.getName()),
        () -> {
          switch (tier) {
            case WORKTABLE:
              return BlockEntityType.Builder.of(WorktableBlockEntity::new, blockArray).build(null);
            case WORKSTATION:
              return BlockEntityType.Builder.of(WorkstationBlockEntity::new, blockArray).build(null);
            case WORKSHOP:
              return BlockEntityType.Builder.of(WorkshopBlockEntity::new, blockArray).build(null);
            default:
              throw new IllegalArgumentException("Unknown tier: " + tier);
          }
        });
  }
}