package com.lirxowo.oraculumworktables.common.network;

import com.lirxowo.oraculumworktables.common.tile.BaseBlockEntity;
import com.lirxowo.oraculum.network.spi.packet.IMessage;
import com.lirxowo.oraculum.network.spi.packet.SPacketTileEntityBase;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.network.handling.IPayloadContext;


public class CSPacketWorktableCreativeToggle
    extends SPacketTileEntityBase<CSPacketWorktableCreativeToggle> {

  @SuppressWarnings("unused")
  public CSPacketWorktableCreativeToggle() {
    // Serialization
  }

  public CSPacketWorktableCreativeToggle(BlockPos blockPos) {

    super(blockPos);
  }

  @Override
  protected IMessage<CSPacketWorktableCreativeToggle> onMessage(
      CSPacketWorktableCreativeToggle message,
      IPayloadContext context,
      BlockEntity tileEntity
  ) {

    if (tileEntity instanceof BaseBlockEntity) {
      BaseBlockEntity table = (BaseBlockEntity) tileEntity;
//      table.setCreative(!table.isCreative());
//      table.setLocked(false);
      CSPacketWorktableClear.clear(table, CSPacketWorktableClear.CLEAR_ALL);
      table.setChanged();
    }


    return null;
  }
}
