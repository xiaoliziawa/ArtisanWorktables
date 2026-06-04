package com.lirxowo.artisanworktables.client.command;

import com.lirxowo.artisanworktables.ArtisanWorktablesMod;
import com.lirxowo.artisanworktables.client.render.ItemIconRenderer;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.io.File;
import java.util.Optional;

public final class ScreenshotCommand {

  private static final int DEFAULT_SIZE = 32;
  private static final int MIN_SIZE = 16;
  private static final int MAX_SIZE = 4096;

  private static final String OUTPUT_DIR = "screenshots/" + ArtisanWorktablesMod.MOD_ID;

  private static final SuggestionProvider<CommandSourceStack> ITEM_SUGGESTIONS =
      (context, builder) -> SharedSuggestionProvider.suggestResource(BuiltInRegistries.ITEM.keySet(), builder);

  public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

    dispatcher.register(
        Commands.literal("screenshot")
            .then(Commands.literal("hand")
                .executes(context -> captureHand(DEFAULT_SIZE))
                .then(Commands.argument("size", IntegerArgumentType.integer(MIN_SIZE, MAX_SIZE))
                    .executes(context -> captureHand(IntegerArgumentType.getInteger(context, "size")))))
            .then(Commands.literal("item")
                .then(Commands.argument("itemid", ResourceLocationArgument.id())
                    .suggests(ITEM_SUGGESTIONS)
                    .executes(context -> captureItem(ResourceLocationArgument.getId(context, "itemid"), DEFAULT_SIZE))
                    .then(Commands.argument("size", IntegerArgumentType.integer(MIN_SIZE, MAX_SIZE))
                        .executes(context -> captureItem(ResourceLocationArgument.getId(context, "itemid"), IntegerArgumentType.getInteger(context, "size"))))))
    );
  }

  private static int captureHand(int size) {

    Minecraft minecraft = Minecraft.getInstance();

    if (minecraft.player == null) {
      return 0;
    }

    ItemStack stack = minecraft.player.getMainHandItem();

    if (stack.isEmpty()) {
      sendFailure(Component.translatable("commands.artisanworktables.screenshot.empty_hand"));
      return 0;
    }

    return capture(stack.copy(), size);
  }

  private static int captureItem(ResourceLocation itemId, int size) {

    Optional<Item> item = BuiltInRegistries.ITEM.getOptional(itemId);

    if (item.isEmpty() || item.get() == Items.AIR) {
      sendFailure(Component.translatable("commands.artisanworktables.screenshot.unknown_item", itemId.toString()));
      return 0;
    }

    return capture(new ItemStack(item.get()), size);
  }

  private static int capture(ItemStack stack, int size) {

    Minecraft minecraft = Minecraft.getInstance();
    ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
    File outFile = uniqueFile(minecraft.gameDirectory, itemId);

    try {
      ItemIconRenderer.renderToFile(stack, size, outFile);

    } catch (Exception e) {
      ArtisanWorktablesMod.LOGGER.error("Failed to render item screenshot for " + itemId, e);
      sendFailure(Component.translatable("commands.artisanworktables.screenshot.failure", e.getMessage()));
      return 0;
    }

    Component fileLink = Component.literal(outFile.getName())
        .withStyle(ChatFormatting.UNDERLINE)
        .withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, outFile.getAbsolutePath())));
    sendSuccess(Component.translatable("commands.artisanworktables.screenshot.success", fileLink));
    return 1;
  }

  private static File uniqueFile(File gameDirectory, ResourceLocation itemId) {

    File directory = new File(gameDirectory, OUTPUT_DIR);
    String baseName = itemId.toString().replace(':', '_').replace('/', '_');

    File file = new File(directory, baseName + ".png");

    for (int i = 1; file.exists(); i++) {
      file = new File(directory, baseName + "_" + i + ".png");
    }

    return file;
  }

  private static void sendSuccess(Component component) {

    if (Minecraft.getInstance().player != null) {
      Minecraft.getInstance().player.displayClientMessage(component, false);
    }
  }

  private static void sendFailure(Component component) {

    if (Minecraft.getInstance().player != null) {
      Minecraft.getInstance().player.displayClientMessage(component.copy().withStyle(ChatFormatting.RED), false);
    }
  }
}
