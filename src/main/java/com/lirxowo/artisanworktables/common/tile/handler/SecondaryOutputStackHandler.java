package com.lirxowo.artisanworktables.common.tile.handler;

import com.lirxowo.athenaeum.inventory.spi.ObservableStackHandler;
import com.lirxowo.athenaeum.network.spi.tile.ITileDataItemStackHandler;

public class SecondaryOutputStackHandler
    extends ObservableStackHandler
    implements ITileDataItemStackHandler {

  public SecondaryOutputStackHandler(int size) {

    super(size);
  }
}
