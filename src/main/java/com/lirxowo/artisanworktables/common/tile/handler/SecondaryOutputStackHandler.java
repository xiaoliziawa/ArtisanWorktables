package com.lirxowo.artisanworktables.common.tile.handler;

import com.lirxowo.oraculum.inventory.spi.ObservableStackHandler;
import com.lirxowo.oraculum.network.spi.tile.ITileDataItemStackHandler;

public class SecondaryOutputStackHandler
    extends ObservableStackHandler
    implements ITileDataItemStackHandler {

  public SecondaryOutputStackHandler(int size) {

    super(size);
  }
}
