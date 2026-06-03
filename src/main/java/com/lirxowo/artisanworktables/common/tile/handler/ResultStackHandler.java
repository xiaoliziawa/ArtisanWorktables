package com.lirxowo.artisanworktables.common.tile.handler;

import com.lirxowo.oraculum.inventory.spi.ObservableStackHandler;
import com.lirxowo.oraculum.network.spi.tile.ITileDataItemStackHandler;

public class ResultStackHandler
    extends ObservableStackHandler
    implements ITileDataItemStackHandler {

  public ResultStackHandler(int size) {

    super(size);
  }
}
