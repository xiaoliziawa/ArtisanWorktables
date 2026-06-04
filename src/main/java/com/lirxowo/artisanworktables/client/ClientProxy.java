package com.lirxowo.artisanworktables.client;

import com.lirxowo.artisanworktables.client.event.ClientCommandRegistrationEventHandler;
import com.lirxowo.artisanworktables.client.event.ClientSetupEventHandler;
import com.lirxowo.artisanworktables.client.event.ParticleFactoryRegisterEventHandler;
import com.lirxowo.artisanworktables.common.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.eventbus.api.IEventBus;

import javax.annotation.Nullable;

public class ClientProxy
    extends CommonProxy {

  @Override
  public void registerModEventHandlers(IEventBus eventBus) {

    super.registerModEventHandlers(eventBus);

    eventBus.register(new ClientSetupEventHandler());
    eventBus.register(new ParticleFactoryRegisterEventHandler());
  }

  @Override
  public void registerForgeEventHandlers(IEventBus eventBus) {

    super.registerForgeEventHandlers(eventBus);

    eventBus.register(new ClientCommandRegistrationEventHandler());
  }

  @Override
  public boolean isIntegratedServerRunning() {

    return Minecraft.getInstance().hasSingleplayerServer();
  }

  @Nullable
  @Override
  public RecipeManager getRecipeManager() {

    LocalPlayer player = Minecraft.getInstance().player;

    if (player != null) {
      return player.level().getRecipeManager();
    }

    return null;
  }
}
