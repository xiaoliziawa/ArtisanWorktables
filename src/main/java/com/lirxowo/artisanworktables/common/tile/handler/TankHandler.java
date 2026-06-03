package com.lirxowo.artisanworktables.common.tile.handler;

import com.lirxowo.oraculum.inventory.spi.ObservableFluidTank;
import com.lirxowo.oraculum.network.spi.tile.ITileDataFluidTank;

public class TankHandler
    extends ObservableFluidTank
    implements ITileDataFluidTank {

  public TankHandler(int capacity) {

    super(capacity);
  }
}
