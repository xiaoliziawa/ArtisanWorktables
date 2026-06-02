package com.lirxowo.artisanworktables;

import com.lirxowo.artisanworktables.client.ClientProxy;
import com.lirxowo.artisanworktables.common.CommonProxy;
import com.lirxowo.artisanworktables.common.block.ToolboxBlock;
import com.lirxowo.artisanworktables.common.block.ToolboxMechanicalBlock;
import com.lirxowo.artisanworktables.common.container.*;
import com.lirxowo.artisanworktables.common.reference.Reference;
import com.lirxowo.artisanworktables.common.tile.ToolboxBlockEntity;
import com.lirxowo.artisanworktables.common.tile.WorkshopBlockEntity;
import com.lirxowo.artisanworktables.common.tile.WorkstationBlockEntity;
import com.lirxowo.artisanworktables.common.tile.WorktableBlockEntity;
import com.google.common.collect.Lists;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Mod(ArtisanWorktablesMod.MOD_ID)
public class ArtisanWorktablesMod {

  public static final String MOD_ID = Reference.MOD_ID;
  public static final Logger LOGGER = LogManager.getLogger();
  public static final String PACKET_SERVICE_PROTOCOL_VERSION = "1";

  public static final List<String> KNOWN_INCOMPATIBLE = Lists.newArrayList(
      "performant"
  );

  private static ArtisanWorktablesMod instance;

  private final IProxy proxy;

  public ArtisanWorktablesMod(FMLJavaModLoadingContext context) {
    ArtisanWorktablesMod.instance = this;

    this.proxy = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    this.proxy.initialize();
    this.proxy.registerModEventHandlers(context.getModEventBus());
    this.proxy.registerForgeEventHandlers(MinecraftForge.EVENT_BUS);
  }

  public static ArtisanWorktablesMod getInstance() {
    return ArtisanWorktablesMod.instance;
  }

  public static IProxy getProxy() {
    return ArtisanWorktablesMod.getInstance().proxy;
  }

  public static class Blocks {
    public static ToolboxBlock TOOLBOX;
    public static ToolboxMechanicalBlock MECHANICAL_TOOLBOX;

    public static void init() {
      TOOLBOX = (ToolboxBlock) BuiltInRegistries.BLOCK.get(
          ResourceLocation.fromNamespaceAndPath(MOD_ID, ToolboxBlock.NAME));
      MECHANICAL_TOOLBOX = (ToolboxMechanicalBlock) BuiltInRegistries.BLOCK.get(
          ResourceLocation.fromNamespaceAndPath(MOD_ID, ToolboxMechanicalBlock.NAME));
    }
  }

  public static class TileEntityTypes {
    public static BlockEntityType<?> WORKTABLE;
    public static BlockEntityType<?> WORKSTATION;
    public static BlockEntityType<?> WORKSHOP;
    public static BlockEntityType<?> TOOLBOX;

    public static void init() {
      WORKTABLE = BuiltInRegistries.BLOCK_ENTITY_TYPE.get(
          ResourceLocation.fromNamespaceAndPath(MOD_ID, WorktableBlockEntity.NAME));
      WORKSTATION = BuiltInRegistries.BLOCK_ENTITY_TYPE.get(
          ResourceLocation.fromNamespaceAndPath(MOD_ID, WorkstationBlockEntity.NAME));
      WORKSHOP = BuiltInRegistries.BLOCK_ENTITY_TYPE.get(
          ResourceLocation.fromNamespaceAndPath(MOD_ID, WorkshopBlockEntity.NAME));
      TOOLBOX = BuiltInRegistries.BLOCK_ENTITY_TYPE.get(
          ResourceLocation.fromNamespaceAndPath(MOD_ID, ToolboxBlockEntity.NAME));
    }
  }

  public static class ContainerTypes {
    public static MenuType<WorktableContainer> WORKTABLE;
    public static MenuType<WorkstationContainer> WORKSTATION;
    public static MenuType<WorkshopContainer> WORKSHOP;
    public static MenuType<ToolboxContainer> TOOLBOX;
    public static MenuType<ToolboxMechanicalContainer> MECHANICAL_TOOLBOX;

    @SuppressWarnings("unchecked")
    public static void init() {
      WORKTABLE = (MenuType<WorktableContainer>) BuiltInRegistries.MENU.get(
          ResourceLocation.fromNamespaceAndPath(MOD_ID, WorktableContainer.NAME));
      WORKSTATION = (MenuType<WorkstationContainer>) BuiltInRegistries.MENU.get(
          ResourceLocation.fromNamespaceAndPath(MOD_ID, WorkstationContainer.NAME));
      WORKSHOP = (MenuType<WorkshopContainer>) BuiltInRegistries.MENU.get(
          ResourceLocation.fromNamespaceAndPath(MOD_ID, WorkshopContainer.NAME));
      TOOLBOX = (MenuType<ToolboxContainer>) BuiltInRegistries.MENU.get(
          ResourceLocation.fromNamespaceAndPath(MOD_ID, ToolboxContainer.NAME));
      MECHANICAL_TOOLBOX = (MenuType<ToolboxMechanicalContainer>) BuiltInRegistries.MENU.get(
          ResourceLocation.fromNamespaceAndPath(MOD_ID, ToolboxMechanicalContainer.NAME));
    }
  }

  public static class ParticleTypes {
    public static SimpleParticleType MAGE;

    public static void init() {
      MAGE = (SimpleParticleType) BuiltInRegistries.PARTICLE_TYPE.get(
          ResourceLocation.fromNamespaceAndPath(MOD_ID, "mage"));
    }
  }
}