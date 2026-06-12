package com.lirxowo.oraculumworktables.common.network;

import com.lirxowo.oraculumworktables.common.container.BaseContainer;
import com.lirxowo.oraculum.network.spi.packet.IMessage;
import com.lirxowo.oraculum.network.spi.packet.PacketBlockPosBase;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;


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
      IPayloadContext context
  ) {

    Minecraft minecraft = Minecraft.getInstance();
    Player player = minecraft.player;
    Level world = minecraft.level;

    if (player != null
        && world != null
        && player.containerMenu instanceof BaseContainer) {
      ((BaseContainer) player.containerMenu).onJoinedBlockBreak(world, message.blockPos);
    }


    return null;
  }
}
