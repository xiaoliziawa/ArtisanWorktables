package com.lirxowo.oraculumworktables.client;

import com.lirxowo.oraculumworktables.client.event.ClientCommandRegistrationEventHandler;
import com.lirxowo.oraculumworktables.client.event.ClientSetupEventHandler;
import com.lirxowo.oraculumworktables.client.event.ParticleFactoryRegisterEventHandler;
import com.lirxowo.oraculumworktables.common.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.bus.api.IEventBus;

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
  public void registerGameEventHandlers(IEventBus eventBus) {

    super.registerGameEventHandlers(eventBus);

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
