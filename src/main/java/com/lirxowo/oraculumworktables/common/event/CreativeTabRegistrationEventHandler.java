package com.lirxowo.oraculumworktables.common.event;

import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import com.lirxowo.oraculumworktables.common.reference.EnumTier;
import com.lirxowo.oraculumworktables.common.reference.EnumType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.bus.api.SubscribeEvent;

public class CreativeTabRegistrationEventHandler {

  @SubscribeEvent
  public void on(RegisterEvent event) {
    if (!event.getRegistryKey().equals(Registries.CREATIVE_MODE_TAB)) {
      return;
    }

    event.register(Registries.CREATIVE_MODE_TAB,
        ResourceLocation.fromNamespaceAndPath(OraculumWorktablesMod.MOD_ID, "general"),
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + OraculumWorktablesMod.MOD_ID))
            .icon(() -> new ItemStack(OraculumWorktablesMod.Blocks.TOOLBOX))
            .displayItems((parameters, output) -> {
              for (EnumType type : EnumType.values()) {
                for (EnumTier tier : EnumTier.values()) {
                  ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                      OraculumWorktablesMod.MOD_ID, tier.getName() + "_" + type.getName());
                  Item item = BuiltInRegistries.ITEM.get(id);

                  if (item != Items.AIR) {
                    output.accept(item);
                  }
                }
              }

              output.accept(OraculumWorktablesMod.Blocks.TOOLBOX);
              output.accept(OraculumWorktablesMod.Blocks.MECHANICAL_TOOLBOX);
            })
            .build());
  }
}
