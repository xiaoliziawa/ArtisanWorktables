package com.lirxowo.oraculumworktables.common.network;

import com.lirxowo.oraculumworktables.common.container.BaseContainer;
import com.lirxowo.oraculum.network.spi.packet.IMessage;
import com.lirxowo.oraculum.network.spi.packet.PacketBlockPosBase;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SCPacketWorktableContainerJoinedBlockBreak
    extends PacketBlockPosBase<SCPacketWorktableContainerJoinedBlockBreak> {

  @SuppressWarnings("unused")
  public SCPacketWorktableContainerJoinedBlockBreak() {
    // serialization
  }

  public SCPacketWorktableContainerJoinedBlockBreak(BlockPos blockPos) {

    super(blockPos);
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public IMessage<SCPacketWorktableContainerJoinedBlockBreak> onMessage(
      SCPacketWorktableContainerJoinedBlockBreak message,
      Supplier<NetworkEvent.Context> supplier
  ) {

    Minecraft minecraft = Minecraft.getInstance();
    Player player = minecraft.player;
    Level world = minecraft.level;

    if (player != null
        && world != null
        && player.containerMenu instanceof BaseContainer) {
      ((BaseContainer) player.containerMenu).onJoinedBlockBreak(world, message.blockPos);
    }

    supplier.get().setPacketHandled(true);

    return null;
  }
}
