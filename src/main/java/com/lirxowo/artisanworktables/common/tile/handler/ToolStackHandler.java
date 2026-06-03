package com.lirxowo.artisanworktables.common.tile.handler;

import com.lirxowo.oraculum.inventory.spi.ObservableStackHandler;
import com.lirxowo.oraculum.network.spi.tile.ITileDataItemStackHandler;

public class ToolStackHandler
    extends ObservableStackHandler
    implements ITileDataItemStackHandler {

  public ToolStackHandler(int size) {

    super(size);
  }
}
