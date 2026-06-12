package com.lirxowo.oraculumworktables.common.network;

import com.lirxowo.oraculumworktables.common.tile.BaseBlockEntity;
import com.lirxowo.oraculum.network.spi.packet.CPacketTileEntityBase;
import com.lirxowo.oraculum.network.spi.packet.IMessage;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.network.handling.IPayloadContext;


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

    HolderLookup.Provider registries = ((RegistryFriendlyByteBuf) packetBuffer).registryAccess();
    CompoundTag compound = packetBuffer.readNbt();
    message.fluidTank = new FluidTank(0);

    if (compound != null) {
      message.fluidTank.readFromNBT(registries, compound);
    }

    return message;
  }

  @Override
  public void encode(SCPacketWorktableFluidUpdate message, FriendlyByteBuf packetBuffer) {

    super.encode(message, packetBuffer);

    HolderLookup.Provider registries = ((RegistryFriendlyByteBuf) packetBuffer).registryAccess();
    packetBuffer.writeNbt(message.fluidTank.writeToNBT(registries, new CompoundTag()));
  }

  @Override
  protected IMessage<SCPacketWorktableFluidUpdate> onMessage(
      SCPacketWorktableFluidUpdate message,
      IPayloadContext context,
      BlockEntity tileEntity
  ) {

    if (tileEntity != null) {
      BaseBlockEntity tileEntityBase = (BaseBlockEntity) tileEntity;
      tileEntityBase.getTank().setFluid(message.fluidTank.getFluid());

      // We don't force a container recipe update here because it's triggered
      // when the tank's onContentsChanged() method is called.
    }


    return null;
  }
}
