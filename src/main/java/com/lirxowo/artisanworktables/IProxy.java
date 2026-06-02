package com.lirxowo.artisanworktables;

import com.lirxowo.artisanworktables.common.recipe.ArtisanRecipe;
import com.lirxowo.artisanworktables.common.reference.EnumType;
import com.lirxowo.athenaeum.network.spi.packet.IPacketService;
import com.lirxowo.athenaeum.network.spi.tile.data.service.ITileDataService;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.eventbus.api.IEventBus;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.List;

public interface IProxy {

  void initialize();

  void registerModEventHandlers(IEventBus eventBus);

  void registerForgeEventHandlers(IEventBus eventBus);

  List<Block> getRegisteredWorktables();

  ITileDataService getTileDataService();

  IPacketService getPacketService();

  boolean isIntegratedServerRunning();

  EnumMap<EnumType, RecipeSerializer<? extends ArtisanRecipe>> getRegisteredSerializersShaped();

  EnumMap<EnumType, RecipeSerializer<? extends ArtisanRecipe>> getRegisteredSerializersShapeless();

  @Nullable
  RecipeManager getRecipeManager();
}
