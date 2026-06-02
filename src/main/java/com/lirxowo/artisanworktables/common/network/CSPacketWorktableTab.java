package com.lirxowo.artisanworktables.common.network;

import com.lirxowo.artisanworktables.common.container.ContainerProvider;
import com.lirxowo.artisanworktables.common.tile.BaseBlockEntity;
import com.lirxowo.athenaeum.network.spi.packet.IMessage;
import com.lirxowo.athenaeum.network.spi.packet.SPacketTileEntityBase;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Sent from the client to the server to signal a worktable tab change.
 */
public class CSPacketWorktableTab
    extends SPacketTileEntityBase<CSPacketWorktableTab> {

  @SuppressWarnings("unused")
  public CSPacketWorktableTab() {
    // Serialization
  }

  public CSPacketWorktableTab(BlockPos blockPos) {

    super(blockPos);
  }

  @Override
  protected IMessage<CSPacketWorktableTab> onMessage(
      CSPacketWorktableTab message,
      Supplier<NetworkEvent.Context> supplier,
      BlockEntity tileEntity
  ) {

    NetworkEvent.Context context = supplier.get();
    ServerPlayer player = context.getSender();
    ItemStack heldStack = Objects.requireNonNull(player).containerMenu.getCarried();

    if (!heldStack.isEmpty()) {
      player.containerMenu.setCarried(ItemStack.EMPTY);
    }

    if (tileEntity instanceof BaseBlockEntity) {
      BaseBlockEntity table = (BaseBlockEntity) tileEntity;
      ContainerProvider containerProvider = new ContainerProvider(table.getTableTier(), table.getTableType(), table.getLevel(), table.getBlockPos());
      NetworkHooks.openScreen(player, containerProvider, tileEntity.getBlockPos());
    }

    if (!heldStack.isEmpty()) {
      player.containerMenu.setCarried(heldStack);
      player.connection.send(new ClientboundContainerSetSlotPacket(-1, player.containerMenu.incrementStateId(), -1, heldStack));
    }

    context.setPacketHandled(true);

    return null;
  }
}
