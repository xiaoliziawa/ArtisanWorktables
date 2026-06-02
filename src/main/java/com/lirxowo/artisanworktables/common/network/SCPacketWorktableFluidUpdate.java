package com.lirxowo.artisanworktables.common.network;

import com.lirxowo.artisanworktables.common.tile.BaseBlockEntity;
import com.lirxowo.athenaeum.network.spi.packet.CPacketTileEntityBase;
import com.lirxowo.athenaeum.network.spi.packet.IMessage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SCPacketWorktableFluidUpdate
    extends CPacketTileEntityBase<SCPacketWorktableFluidUpdate> {

  private FluidTank fluidTank;

  @SuppressWarnings("unused")
  public SCPacketWorktableFluidUpdate() {
    // serialization
  }

  public SCPacketWorktableFluidUpdate(BlockPos blockPos, FluidTank fluidTank) {

    super(blockPos);
    this.fluidTank = fluidTank;
  }

  @Override
  public SCPacketWorktableFluidUpdate decode(SCPacketWorktableFluidUpdate message, FriendlyByteBuf packetBuffer) {

    super.decode(message, packetBuffer);

    CompoundTag compound = packetBuffer.readNbt();
    message.fluidTank = new FluidTank(0);

    if (compound != null) {
      message.fluidTank.readFromNBT(compound);
    }

    return message;
  }

  @Override
  public void encode(SCPacketWorktableFluidUpdate message, FriendlyByteBuf packetBuffer) {

    super.encode(message, packetBuffer);

    packetBuffer.writeNbt(message.fluidTank.writeToNBT(new CompoundTag()));
  }

  @Override
  protected IMessage<SCPacketWorktableFluidUpdate> onMessage(
      SCPacketWorktableFluidUpdate message,
      Supplier<NetworkEvent.Context> supplier,
      BlockEntity tileEntity
  ) {

    if (tileEntity != null) {
      BaseBlockEntity tileEntityBase = (BaseBlockEntity) tileEntity;
      tileEntityBase.getTank().setFluid(message.fluidTank.getFluid());

      // We don't force a container recipe update here because it's triggered
      // when the tank's onContentsChanged() method is called.
    }

    supplier.get().setPacketHandled(true);

    return null;
  }
}
