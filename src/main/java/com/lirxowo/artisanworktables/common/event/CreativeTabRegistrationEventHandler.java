package com.lirxowo.artisanworktables.common.event;

import com.lirxowo.artisanworktables.ArtisanWorktablesMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Map;

public class CreativeTabRegistrationEventHandler {

  @SubscribeEvent
  public void on(RegisterEvent event) {
    if (!event.getRegistryKey().equals(Registries.CREATIVE_MODE_TAB)) {
      return;
    }

    event.register(Registries.CREATIVE_MODE_TAB,
        ResourceLocation.fromNamespaceAndPath(ArtisanWorktablesMod.MOD_ID, "general"),
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + ArtisanWorktablesMod.MOD_ID))
            .icon(() -> new ItemStack(ArtisanWorktablesMod.Blocks.TOOLBOX))
            .displayItems((parameters, output) -> {
              for (Map.Entry<ResourceKey<Item>, Item> entry : BuiltInRegistries.ITEM.entrySet()) {
                if (entry.getKey().location().getNamespace().equals(ArtisanWorktablesMod.MOD_ID)) {
                  output.accept(entry.getValue());
                }
              }
            })
            .build());
  }
}
