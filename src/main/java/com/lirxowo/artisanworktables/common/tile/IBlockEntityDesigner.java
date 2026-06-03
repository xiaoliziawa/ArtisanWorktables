package com.lirxowo.artisanworktables.common.tile;

import com.lirxowo.oraculum.gui.Texture;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;

public interface IBlockEntityDesigner {

  ItemStackHandler getPatternStackHandler();

  BlockEntity getTileEntity();

  boolean canPlayerUse(Player player);

  Texture getTexturePatternSide();
}
