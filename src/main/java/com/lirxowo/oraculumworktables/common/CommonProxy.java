package com.lirxowo.oraculumworktables.common;

import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import com.lirxowo.oraculumworktables.OraculumWorktablesModCommonConfig;
import com.lirxowo.oraculumworktables.IProxy;
import com.lirxowo.oraculumworktables.common.event.*;
import com.lirxowo.oraculumworktables.common.network.*;
import com.lirxowo.oraculumworktables.common.plugin.tconstruct.TinkersToolHandler;
import com.lirxowo.oraculumworktables.common.plugin.tetra.TetraToolHandler;
import com.lirxowo.oraculumworktables.common.recipe.ArtisanRecipe;
import com.lirxowo.oraculumworktables.common.recipe.ArtisanToolHandlers;
import com.lirxowo.oraculumworktables.common.reference.EnumTier;
import com.lirxowo.oraculumworktables.common.reference.EnumType;
import com.lirxowo.oraculum.network.api.NetworkAPI;
import com.lirxowo.oraculum.network.spi.packet.IPacketService;
import com.lirxowo.oraculum.network.spi.tile.data.service.ITileDataService;
import com.lirxowo.oraculum.network.spi.tile.data.service.SCPacketTileData;
import com.lirxowo.oraculum.util.ConfigHelper;
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

    String modId = OraculumWorktablesMod.MOD_ID;
    Path configPath = FMLPaths.CONFIGDIR.get();
    Path modConfigPath = configPath.resolve(modId);

    try {
      Files.createDirectories(modConfigPath);

    } catch (IOException e) {
      OraculumWorktablesMod.LOGGER.error("Error creating folder: " + modConfigPath, e);
    }

    String configFilenameCommon = modId + "-common.toml";
    modLoadingContext.registerConfig(ModConfig.Type.COMMON, OraculumWorktablesModCommonConfig.CONFIG_SPEC, modId + "/" + configFilenameCommon);
    ConfigHelper.loadConfig(OraculumWorktablesModCommonConfig.CONFIG_SPEC, modConfigPath.resolve(configFilenameCommon));

    this.packetService = NetworkAPI.createPacketService(modId, modId, OraculumWorktablesMod.PACKET_SERVICE_PROTOCOL_VERSION);
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
      OraculumWorktablesMod.LOGGER.info("Tinkers' Construct detected; registered Tinkers tool handler.");
    }

    if (ModList.get().isLoaded("tetra")) {
      ArtisanToolHandlers.register(new TetraToolHandler());
      OraculumWorktablesMod.LOGGER.info("Tetra detected; registered Tetra tool handler.");
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
    eventBus.register(new SoundEventRegistrationEventHandler());
    eventBus.register(new CreativeTabRegistrationEventHandler());
    eventBus.register(new RegistryInitEventHandler());
  }

  @Override
  public void registerForgeEventHandlers(IEventBus eventBus) {

    eventBus.register(new TagsUpdatedEventHandler());
    eventBus.register(new PlayerLoggedInEventHandler(OraculumWorktablesMod.KNOWN_INCOMPATIBLE));
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
