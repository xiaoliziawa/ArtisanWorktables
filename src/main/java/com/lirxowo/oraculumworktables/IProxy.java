package com.lirxowo.oraculumworktables;

import com.lirxowo.oraculumworktables.common.recipe.ArtisanRecipe;
import com.lirxowo.oraculumworktables.common.reference.EnumType;
import com.lirxowo.oraculum.network.spi.packet.IPacketService;
import com.lirxowo.oraculum.network.spi.tile.data.service.ITileDataService;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.List;

public interface IProxy {

  void initialize(ModContainer modContainer);

  void registerModEventHandlers(IEventBus eventBus);

  void registerGameEventHandlers(IEventBus eventBus);

  List<Block> getRegisteredWorktables();

  ITileDataService getTileDataService();

  IPacketService getPacketService();

  boolean isIntegratedServerRunning();

  EnumMap<EnumType, RecipeSerializer<? extends ArtisanRecipe>> getRegisteredSerializersShaped();

  EnumMap<EnumType, RecipeSerializer<? extends ArtisanRecipe>> getRegisteredSerializersShapeless();

  @Nullable
  RecipeManager getRecipeManager();
}
