package com.lirxowo.oraculumworktables.common.network;

import com.lirxowo.oraculumworktables.common.tile.BaseBlockEntity;
import com.lirxowo.oraculum.network.spi.packet.IMessage;
import com.lirxowo.oraculum.network.spi.packet.SPacketTileEntityBase;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.network.handling.IPayloadContext;


public class CSPacketWorktableLockedModeToggle
    extends SPacketTileEntityBase<CSPacketWorktableLockedModeToggle> {

  @SuppressWarnings("unused")
  public CSPacketWorktableLockedModeToggle() {
    // Serialization
  }

  public CSPacketWorktableLockedModeToggle(BlockPos blockPos) {

    super(blockPos);
  }

  @Override
  protected IMessage<CSPacketWorktableLockedModeToggle> onMessage(
      CSPacketWorktableLockedModeToggle message,
      IPayloadContext context,
      BlockEntity tileEntity
  ) {

    if (tileEntity instanceof BaseBlockEntity) {
      BaseBlockEntity table = (BaseBlockEntity) tileEntity;

//      if (ModuleWorktablesConfig.allowSlotLockingForTier(table.getTableTier())) {
//        table.setLocked(!table.isLocked());
//        table.setChanged();
//      }
    }


    return null;
  }
}
