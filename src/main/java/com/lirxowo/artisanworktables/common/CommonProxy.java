package com.lirxowo.artisanworktables.common;

import com.lirxowo.artisanworktables.ArtisanWorktablesMod;
import com.lirxowo.artisanworktables.ArtisanWorktablesModCommonConfig;
import com.lirxowo.artisanworktables.IProxy;
import com.lirxowo.artisanworktables.common.event.*;
import com.lirxowo.artisanworktables.common.network.*;
import com.lirxowo.artisanworktables.common.plugin.tconstruct.TinkersToolHandler;
import com.lirxowo.artisanworktables.common.recipe.ArtisanRecipe;
import com.lirxowo.artisanworktables.common.recipe.ArtisanToolHandlers;
import com.lirxowo.artisanworktables.common.reference.EnumTier;
import com.lirxowo.artisanworktables.common.reference.EnumType;
import com.lirxowo.athenaeum.network.api.NetworkAPI;
import com.lirxowo.athenaeum.network.spi.packet.IPacketService;
import com.lirxowo.athenaeum.network.spi.tile.data.service.ITileDataService;
import com.lirxowo.athenaeum.network.spi.tile.data.service.SCPacketTileData;
import com.lirxowo.athenaeum.util.ConfigHelper;
import com.google.common.collect.Lists;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class CommonProxy
    implements IProxy {

  protected final List<Block> registeredWorktables;
  protected final Map<EnumTier, List<Block>> registeredWorktablesByTier;
  protected final EnumMap<EnumType, RecipeSerializer<? extends ArtisanRecipe>> registeredSerializersShaped;
  protected final EnumMap<EnumType, RecipeSerializer<? extends ArtisanRecipe>> registeredSerializersShapeless;

  protected IPacketService packetService;
  protected ITileDataService tileDataService;

  public CommonProxy() {

    this.registeredWorktables = new ArrayList<>();
    this.registeredWorktablesByTier = new EnumMap<>(EnumTier.class);
    this.registeredSerializersShaped = new EnumMap<>(EnumType.class);
    this.registeredSerializersShapeless = new EnumMap<>(EnumType.class);
  }

  @Override
  public void initialize(ModLoadingContext modLoadingContext) {

    String modId = ArtisanWorktablesMod.MOD_ID;
    Path configPath = FMLPaths.CONFIGDIR.get();
    Path modConfigPath = configPath.resolve(modId);

    try {
      Files.createDirectories(modConfigPath);

    } catch (IOException e) {
      ArtisanWorktablesMod.LOGGER.error("Error creating folder: " + modConfigPath, e);
    }

    String configFilenameCommon = modId + "-common.toml";
    modLoadingContext.registerConfig(ModConfig.Type.COMMON, ArtisanWorktablesModCommonConfig.CONFIG_SPEC, modId + "/" + configFilenameCommon);
    ConfigHelper.loadConfig(ArtisanWorktablesModCommonConfig.CONFIG_SPEC, modConfigPath.resolve(configFilenameCommon));

    this.packetService = NetworkAPI.createPacketService(modId, modId, ArtisanWorktablesMod.PACKET_SERVICE_PROTOCOL_VERSION);
    this.tileDataService = NetworkAPI.createTileDataService(modId, modId, this.packetService);

    this.packetService.registerMessage(SCPacketTileData.class, SCPacketTileData.class);
    this.packetService.registerMessage(CSPacketWorktableClear.class, CSPacketWorktableClear.class);
    this.packetService.registerMessage(CSPacketWorktableCreativeToggle.class, CSPacketWorktableCreativeToggle.class);
    this.packetService.registerMessage(CSPacketWorktableLockedModeToggle.class, CSPacketWorktableLockedModeToggle.class);
    this.packetService.registerMessage(CSPacketWorktableTab.class, CSPacketWorktableTab.class);
    this.packetService.registerMessage(CSPacketWorktableTankDestroyFluid.class, CSPacketWorktableTankDestroyFluid.class);
    this.packetService.registerMessage(SCPacketWorktableContainerJoinedBlockBreak.class, SCPacketWorktableContainerJoinedBlockBreak.class);
    this.packetService.registerMessage(SCPacketWorktableFluidUpdate.class, SCPacketWorktableFluidUpdate.class);
    this.packetService.registerMessage(SCPacketIncompatible.class, SCPacketIncompatible.class);

    this.registerToolHandlers();
  }

  private void registerToolHandlers() {

    if (ModList.get().isLoaded("tconstruct")) {
      ArtisanToolHandlers.register(new TinkersToolHandler());
      ArtisanWorktablesMod.LOGGER.info("Tinkers' Construct detected; registered Tinkers tool handler.");
    }
  }

  @Override
  public void registerModEventHandlers(IEventBus eventBus) {

    eventBus.register(new BlockRegistrationEventHandler(this.registeredWorktables, this.registeredWorktablesByTier));
    eventBus.register(new ItemRegistrationEventHandler(this.registeredWorktables));
    eventBus.register(new BlockEntityRegistrationEventHandler(this.registeredWorktablesByTier));
    eventBus.register(new MenuTypeRegistrationEventHandler());
    eventBus.register(new RecipeSerializerRegistrationEventHandler(this.registeredSerializersShaped, this.registeredSerializersShapeless));
    eventBus.register(new ParticleTypeRegistrationEventHandler());
    eventBus.register(new CreativeTabRegistrationEventHandler());
    eventBus.register(new RegistryInitEventHandler());
  }

  @Override
  public void registerForgeEventHandlers(IEventBus eventBus) {

    eventBus.register(new TagsUpdatedEventHandler());
    eventBus.register(new PlayerLoggedInEventHandler(ArtisanWorktablesMod.KNOWN_INCOMPATIBLE));
  }

  // ---------------------------------------------------------------------------
  // Accessors
  // ---------------------------------------------------------------------------

  @Override
  public List<Block> getRegisteredWorktables() {

    return Collections.unmodifiableList(this.registeredWorktables);
  }

  @Override
  public ITileDataService getTileDataService() {

    return this.tileDataService;
  }

  @Override
  public IPacketService getPacketService() {

    return this.packetService;
  }

  @Override
  public boolean isIntegratedServerRunning() {

    return false;
  }

  @Override
  public EnumMap<EnumType, RecipeSerializer<? extends ArtisanRecipe>> getRegisteredSerializersShaped() {

    return this.registeredSerializersShaped;
  }

  @Override
  public EnumMap<EnumType, RecipeSerializer<? extends ArtisanRecipe>> getRegisteredSerializersShapeless() {

    return this.registeredSerializersShapeless;
  }

  @Nullable
  @Override
  public RecipeManager getRecipeManager() {

    MinecraftServer minecraftServer = ServerLifecycleHooks.getCurrentServer();

    if (minecraftServer != null) {
      return minecraftServer.getRecipeManager();
    }

    return null;
  }
}
