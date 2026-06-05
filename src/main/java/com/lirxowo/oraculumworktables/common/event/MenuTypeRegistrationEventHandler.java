package com.lirxowo.oraculumworktables.common.event;

import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import com.lirxowo.oraculumworktables.common.container.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MenuTypeRegistrationEventHandler {

  @SubscribeEvent
  public void on(RegisterEvent event) {
    if (!event.getRegistryKey().equals(Registries.MENU)) {
      return;
    }

    event.register(Registries.MENU,
        ResourceLocation.fromNamespaceAndPath(OraculumWorktablesMod.MOD_ID, WorktableContainer.NAME),
        () -> IForgeMenuType.create((id, playerInventory, data) -> {
          BlockPos blockPos = data.readBlockPos();
          Player player = playerInventory.player;
          Level world = player.level();
          return new WorktableContainer(id, world, blockPos, playerInventory, player);
        }));

    event.register(Registries.MENU,
        ResourceLocation.fromNamespaceAndPath(OraculumWorktablesMod.MOD_ID, WorkstationContainer.NAME),
        () -> IForgeMenuType.create((id, playerInventory, data) -> {
          BlockPos blockPos = data.readBlockPos();
          Player player = playerInventory.player;
          Level world = player.level();
          return new WorkstationContainer(id, world, blockPos, playerInventory, player);
        }));

    event.register(Registries.MENU,
        ResourceLocation.fromNamespaceAndPath(OraculumWorktablesMod.MOD_ID, WorkshopContainer.NAME),
        () -> IForgeMenuType.create((id, playerInventory, data) -> {
          BlockPos blockPos = data.readBlockPos();
          Player player = playerInventory.player;
          Level world = player.level();
          return new WorkshopContainer(id, world, blockPos, playerInventory, player);
        }));

    event.register(Registries.MENU,
        ResourceLocation.fromNamespaceAndPath(OraculumWorktablesMod.MOD_ID, ToolboxContainer.NAME),
        () -> IForgeMenuType.create((id, playerInventory, data) -> {
          BlockPos blockPos = data.readBlockPos();
          Player player = playerInventory.player;
          Level world = player.level();
          return new ToolboxContainer(id, world, blockPos, playerInventory, player);
        }));

    event.register(Registries.MENU,
        ResourceLocation.fromNamespaceAndPath(OraculumWorktablesMod.MOD_ID, ToolboxMechanicalContainer.NAME),
        () -> IForgeMenuType.create((id, playerInventory, data) -> {
          BlockPos blockPos = data.readBlockPos();
          Player player = playerInventory.player;
          Level world = player.level();
          return new ToolboxMechanicalContainer(id, world, blockPos, playerInventory, player);
        }));
  }
}
