package com.lirxowo.oraculumworktables.common.network;

import com.lirxowo.oraculum.network.spi.packet.IMessage;
import com.lirxowo.oraculum.network.spi.packet.IMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;

public class SCPacketIncompatible
    implements IMessage<SCPacketIncompatible>,
    IMessageHandler<SCPacketIncompatible, SCPacketIncompatible> {

  private List<String> modIdList;

  @SuppressWarnings("unused")
  public SCPacketIncompatible() {
    // serialization
  }

  public SCPacketIncompatible(List<String> modIdList) {

    this.modIdList = modIdList;
  }

  @Override
  public void encode(SCPacketIncompatible message, FriendlyByteBuf packetBuffer) {

    packetBuffer.writeInt(message.modIdList.size());

    for (String modId : message.modIdList) {
      packetBuffer.writeUtf(modId);
    }
  }

  @Override
  public SCPacketIncompatible decode(SCPacketIncompatible message, FriendlyByteBuf packetBuffer) {

    int size = packetBuffer.readInt();
    message.modIdList = new ArrayList<>(size);

    for (int i = 0; i < size; i++) {
      message.modIdList.add(packetBuffer.readUtf());
    }

    return message;
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public SCPacketIncompatible onMessage(
      SCPacketIncompatible message,
      IPayloadContext context
  ) {

    Minecraft minecraft = Minecraft.getInstance();

    String modList = String.join(", ", message.modIdList.toArray(new String[0]));

    String warningString = I18n.get("gui.oraculumworktables.warning");

    minecraft.setScreen(
        new ConfirmScreen(
            (result) -> minecraft.setScreen(null),
            Component.literal(warningString).withStyle(ChatFormatting.RED),
            Component.translatable("gui.oraculumworktables.incompatibility", modList),
            Component.translatable("gui.proceed"),
            Component.translatable("gui.cancel")
        )
    );


    return null;
  }
}
