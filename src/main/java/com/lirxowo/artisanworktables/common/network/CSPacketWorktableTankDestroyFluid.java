package com.lirxowo.artisanworktables.common.network;

import com.lirxowo.artisanworktables.ArtisanWorktablesMod;
import com.lirxowo.artisanworktables.common.tile.BaseBlockEntity;
import com.lirxowo.athenaeum.network.spi.packet.IMessage;
import com.lirxowo.athenaeum.network.spi.packet.SPacketTileEntityBase;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Sent from the client to the server to signal a fluid destroy.
 */
public class CSPacketWorktableTankDestroyFluid
    extends SPacketTileEntityBase<CSPacketWorktableTankDestroyFluid> {

  @SuppressWarnings("unused")
  public CSPacketWorktableTankDestroyFluid() {
    // Serialization
  }

  public CSPacketWorktableTankDestroyFluid(BlockPos blockPos) {

    super(blockPos);
  }

  @Override
  protected IMessage<CSPacketWorktableTankDestroyFluid> onMessage(
      CSPacketWorktableTankDestroyFluid message,
      Supplier<NetworkEvent.Context> supplier,
      BlockEntity tileEntity
  ) {

    if (tileEntity instanceof BaseBlockEntity) {
      FluidTank tank = ((BaseBlockEntity) tileEntity).getTank();
      tank.drain(tank.getCapacity(), IFluidHandler.FluidAction.EXECUTE);
      ArtisanWorktablesMod.getProxy().getPacketService().sendToNear(
          tileEntity,
          new SCPacketWorktableFluidUpdate(tileEntity.getBlockPos(), tank)
      );
    }

    supplier.get().setPacketHandled(true);

    return null;
  }
}
