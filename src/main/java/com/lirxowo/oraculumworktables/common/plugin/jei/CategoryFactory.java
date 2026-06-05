package com.lirxowo.oraculumworktables.common.plugin.jei;

import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import com.lirxowo.oraculumworktables.common.reference.EnumTier;
import com.lirxowo.oraculumworktables.common.reference.EnumType;
import com.lirxowo.oraculumworktables.common.util.Key;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

public class CategoryFactory {

  public BaseCategory<?> create(
      EnumTier tier,
      EnumType type,
      Block block,
      IGuiHelper guiHelper,
      CategorySetupHandler categorySetupHandler,
      CategoryDrawHandler categoryDrawHandler
  ) {

    return new Category(
        tier,
        type,
        String.format("block.%s.%s_%s", OraculumWorktablesMod.MOD_ID, tier.getName(), type.getName()),
        this.createBackground(tier, guiHelper, Key.from(String.format("textures/gui/%s_%s.png", tier.getName(), type.getName()))),
        guiHelper.createDrawableItemStack(new ItemStack(block)),
        Plugin.CATEGORY_KEYS.get(tier).get(type),
        categorySetupHandler,
        categoryDrawHandler
    );
  }

  private IDrawableStatic createBackground(EnumTier tier, IGuiHelper guiHelper, ResourceLocation resourceLocation) {

    switch (tier) {
      case WORKTABLE:
        return guiHelper.createDrawable(resourceLocation, 3, 3, 170, 80);
      case WORKSTATION:
        return guiHelper.createDrawable(resourceLocation, 3, 3, 170, 102);
      case WORKSHOP:
        return guiHelper.createDrawable(resourceLocation, 3, 13, 170, 128);
      default:
        throw new IllegalArgumentException("Unknown tier: " + tier.getName());
    }
  }
}
