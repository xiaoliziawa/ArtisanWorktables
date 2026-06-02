package com.lirxowo.artisanworktables.common.tile.handler;

import com.lirxowo.athenaeum.inventory.spi.ObservableFluidTank;
import com.lirxowo.athenaeum.network.spi.tile.ITileDataFluidTank;

public class TankHandler
    extends ObservableFluidTank
    implements ITileDataFluidTank {

  public TankHandler(int capacity) {

    super(capacity);
  }
}
