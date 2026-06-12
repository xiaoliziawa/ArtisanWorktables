package com.lirxowo.oraculumworktables.common.recipe;

import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

public interface ICraftingMatrixStackHandler
    extends IItemHandler,
    IItemHandlerModifiable {

  int getWidth();

  int getHeight();
}
