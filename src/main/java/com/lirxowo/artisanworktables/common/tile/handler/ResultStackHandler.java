package com.lirxowo.artisanworktables.common.tile.handler;

import com.lirxowo.athenaeum.inventory.spi.ObservableStackHandler;
import com.lirxowo.athenaeum.network.spi.tile.ITileDataItemStackHandler;

public class ResultStackHandler
    extends ObservableStackHandler
    implements ITileDataItemStackHandler {

  public ResultStackHandler(int size) {

    super(size);
  }
}
