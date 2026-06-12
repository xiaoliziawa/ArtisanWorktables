package com.lirxowo.oraculumworktables.common.event;

import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import com.lirxowo.oraculumworktables.common.tile.BaseBlockEntity;
import com.lirxowo.oraculumworktables.common.tile.ToolboxBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class CapabilityRegistrationEventHandler {

  @SubscribeEvent
  @SuppressWarnings("unchecked")
  public void register(RegisterCapabilitiesEvent event) {

    OraculumWorktablesMod.TileEntityTypes.init();

    this.registerFluidHandler(event, OraculumWorktablesMod.TileEntityTypes.WORKTABLE);
    this.registerFluidHandler(event, OraculumWorktablesMod.TileEntityTypes.WORKSTATION);
    this.registerFluidHandler(event, OraculumWorktablesMod.TileEntityTypes.WORKSHOP);

    event.registerBlockEntity(
        Capabilities.ItemHandler.BLOCK,
        (BlockEntityType<ToolboxBlockEntity>) OraculumWorktablesMod.TileEntityTypes.TOOLBOX,
        (blockEntity, side) -> blockEntity.getItemStackHandler()
    );
  }

  @SuppressWarnings("unchecked")
  private void registerFluidHandler(RegisterCapabilitiesEvent event, BlockEntityType<?> type) {

    event.registerBlockEntity(
        Capabilities.FluidHandler.BLOCK,
        (BlockEntityType<BaseBlockEntity>) type,
        (blockEntity, side) -> blockEntity.getTank()
    );
  }
}
