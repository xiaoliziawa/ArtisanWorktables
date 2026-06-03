package com.lirxowo.artisanworktables.client.screen.element;

import com.lirxowo.artisanworktables.ArtisanWorktablesMod;
import com.lirxowo.artisanworktables.client.ReferenceTexture;
import com.lirxowo.artisanworktables.client.screen.GuiTabOffset;
import com.lirxowo.artisanworktables.common.network.CSPacketWorktableTab;
import com.lirxowo.artisanworktables.common.tile.BaseBlockEntity;
import com.lirxowo.oraculum.gui.GuiContainerBase;
import com.lirxowo.oraculum.gui.GuiHelper;
import com.lirxowo.oraculum.gui.element.GuiElementBase;
import com.lirxowo.oraculum.gui.element.IGuiElementClickable;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class GuiElementTabs
    extends GuiElementBase
    implements IGuiElementClickable {

  private static final int TAB_WIDTH = 24;
  private static final int TAB_SPACING = 2;
  private static final int TAB_CURRENT_OFFSET = 1;
  private static final int TAB_HEIGHT = 21;
  private static final int TAB_LEFT_OFFSET = 4;
  private static final int TAB_ITEM_HORIZONTAL_OFFSET = 4;
  private static final int TAB_ITEM_VERTICAL_OFFSET = 4;
  private static final int BUTTON_WIDTH = 8;

  private static final int MAX_TAB_COUNT = 6;

  public static boolean RECALCULATE_TAB_OFFSETS = false;
  private static final GuiTabOffset GUI_TAB_OFFSET = new GuiTabOffset(0);

  private final BaseBlockEntity worktable;

  public GuiElementTabs(
      GuiContainerBase guiBase,
      BaseBlockEntity worktable,
      int elementWidth
  ) {

    super(guiBase, 0, -TAB_HEIGHT, elementWidth, TAB_HEIGHT);
    this.worktable = worktable;

    if (RECALCULATE_TAB_OFFSETS) {
      RECALCULATE_TAB_OFFSETS = false;
      this.calculateInitialTabOffset(worktable, GUI_TAB_OFFSET);
    }
  }

  @Override
  public void drawBackgroundLayer(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {

    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    int x = this.elementXModifiedGet();
    int y = this.elementYModifiedGet();

    int tabX = x + TAB_LEFT_OFFSET;
    int tabY = y;

    List<BaseBlockEntity> actualJoinedTables = this.worktable.getJoinedTables(
        new ArrayList<>(),
        Minecraft.getInstance().player,
        BaseBlockEntity::allowTabs
    );
    List<BaseBlockEntity> joinedTables = this.getJoinedTableOffsetView(
        actualJoinedTables,
        GUI_TAB_OFFSET.getOffset()
    );

    this.textureBind(ReferenceTexture.RESOURCE_LOCATION_GUI_ELEMENTS);

    // draw arrows

    if (GUI_TAB_OFFSET.getOffset() > 0) {
      // draw left button
      int textureX = (this.worktable.getWorktableGuiTabTextureYOffset() / 12) * (TAB_WIDTH + BUTTON_WIDTH * 2);

      GuiHelper.drawModalRectWithCustomSizedTexture(
          matrixStack,
          this.elementXModifiedGet() + TAB_LEFT_OFFSET + TAB_ITEM_HORIZONTAL_OFFSET - 18,
          tabY,
          0,
          TAB_WIDTH + textureX,
          (this.worktable.getWorktableGuiTabTextureYOffset() % 12) * TAB_HEIGHT,
          BUTTON_WIDTH,
          TAB_HEIGHT,
          ReferenceTexture.TEXTURE_GUI_ELEMENTS_WIDTH,
          ReferenceTexture.TEXTURE_GUI_ELEMENTS_HEIGHT
      );
    }

    if (GUI_TAB_OFFSET.getOffset() + MAX_TAB_COUNT < actualJoinedTables.size()) {
      // draw right button
      int textureX = (this.worktable.getWorktableGuiTabTextureYOffset() / 12) * (TAB_WIDTH + BUTTON_WIDTH * 2);

      GuiHelper.drawModalRectWithCustomSizedTexture(
          matrixStack,
          this.elementXModifiedGet() + this.elementWidthModifiedGet() - 12,
          tabY,
          0,
          TAB_WIDTH + BUTTON_WIDTH + textureX,
          (this.worktable.getWorktableGuiTabTextureYOffset() % 12) * TAB_HEIGHT,
          BUTTON_WIDTH,
          TAB_HEIGHT,
          ReferenceTexture.TEXTURE_GUI_ELEMENTS_WIDTH,
          ReferenceTexture.TEXTURE_GUI_ELEMENTS_HEIGHT
      );
    }

    // draw tabs

    for (BaseBlockEntity joinedTable : joinedTables) {
      int textureX = (joinedTable.getWorktableGuiTabTextureYOffset() / 12) * (TAB_WIDTH + BUTTON_WIDTH * 2);
      int textureY = (joinedTable.getWorktableGuiTabTextureYOffset() % 12) * TAB_HEIGHT;

      if (joinedTable == this.worktable) {
        GuiHelper.drawModalRectWithCustomSizedTexture(
            matrixStack,
            tabX,
            tabY + TAB_CURRENT_OFFSET,
            0,
            textureX,
            textureY,
            TAB_WIDTH,
            TAB_HEIGHT,
            ReferenceTexture.TEXTURE_GUI_ELEMENTS_WIDTH,
            ReferenceTexture.TEXTURE_GUI_ELEMENTS_HEIGHT
        );

      } else {
        GuiHelper.drawModalRectWithCustomSizedTexture(
            matrixStack,
            tabX,
            tabY,
            0,
            textureX,
            textureY,
            TAB_WIDTH,
            TAB_HEIGHT,
            ReferenceTexture.TEXTURE_GUI_ELEMENTS_WIDTH,
            ReferenceTexture.TEXTURE_GUI_ELEMENTS_HEIGHT
        );
      }

      tabX += TAB_WIDTH + TAB_SPACING;
    }

    // draw tab icons
    tabX = x + TAB_LEFT_OFFSET + TAB_ITEM_HORIZONTAL_OFFSET;
    tabY = y + TAB_ITEM_VERTICAL_OFFSET;

    GuiGraphics guiGraphics = new GuiGraphics(
        Minecraft.getInstance(),
        Minecraft.getInstance().renderBuffers().bufferSource()
    );

    for (BaseBlockEntity joinedTable : joinedTables) {
      BlockState blockState = joinedTable.getLevel().getBlockState(joinedTable.getBlockPos());
      ItemStack itemStack = joinedTable.getItemStackForTabDisplay(blockState);

      if (joinedTable == this.worktable) {
        this.guiBase.drawItemStack(guiGraphics, itemStack, tabX, tabY + TAB_CURRENT_OFFSET, null);

      } else {
        this.guiBase.drawItemStack(guiGraphics, itemStack, tabX, tabY, null);
      }

      tabX += TAB_WIDTH + TAB_SPACING;
    }
  }

  @Override
  public void drawForegroundLayer(PoseStack matrixStack, int mouseX, int mouseY) {
    //
  }

  @Override
  public void mouseClicked(double mouseX, double mouseY, int mouseButton) {

    if (mouseButton != 0) {
      return;
    }

    List<BaseBlockEntity> actualJoinedTables = this.worktable.getJoinedTables(
        new ArrayList<>(),
        Minecraft.getInstance().player,
        BaseBlockEntity::allowTabs
    );
    List<BaseBlockEntity> joinedTables = this.getJoinedTableOffsetView(
        actualJoinedTables,
        GUI_TAB_OFFSET.getOffset()
    );

    int yMin = this.elementYModifiedGet();
    int yMax = yMin + TAB_HEIGHT;

    for (int i = 0; i < joinedTables.size(); i++) {
      int xMin = this.elementXModifiedGet() + TAB_ITEM_HORIZONTAL_OFFSET + (TAB_WIDTH + TAB_SPACING) * i;
      int xMax = xMin + TAB_WIDTH;

      if (mouseX <= xMax
          && mouseX >= xMin
          && mouseY <= yMax
          && mouseY >= yMin) {
        BaseBlockEntity table = joinedTables.get(i);
        BlockPos pos = table.getBlockPos();
        ArtisanWorktablesMod.getProxy().getPacketService()
            .sendToServer(new CSPacketWorktableTab(pos));
        Minecraft.getInstance()
            .getSoundManager()
            .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1));
      }
    }

    int maximumDisplayedTabCount = MAX_TAB_COUNT;

    if (GUI_TAB_OFFSET.getOffset() > 0) {
      // check for left button click
      int xMin = this.elementXModifiedGet() + TAB_LEFT_OFFSET + TAB_ITEM_HORIZONTAL_OFFSET - 18;
      int xMax = xMin + 8;

      if (mouseX <= xMax
          && mouseX >= xMin
          && mouseY <= yMax
          && mouseY >= yMin) {
        int tabOffset = GUI_TAB_OFFSET.getOffset() - maximumDisplayedTabCount;
        GUI_TAB_OFFSET.setOffset(Math.max(0, tabOffset));
        Minecraft.getInstance()
            .getSoundManager()
            .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1));
      }
    }

    if (GUI_TAB_OFFSET.getOffset() + maximumDisplayedTabCount < actualJoinedTables.size()) {
      // check for right button click
      int xMin = this.elementXModifiedGet() + this.elementWidthModifiedGet() - 12;
      int xMax = xMin + 8;

      if (mouseX <= xMax
          && mouseX >= xMin
          && mouseY <= yMax
          && mouseY >= yMin) {
        GUI_TAB_OFFSET.setOffset(Math.min(
            actualJoinedTables.size() - maximumDisplayedTabCount,
            GUI_TAB_OFFSET.getOffset() + maximumDisplayedTabCount
        ));
        Minecraft.getInstance()
            .getSoundManager()
            .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1));
      }
    }
  }

  /**
   * Sets the initial tab offset when the gui is opened.
   *
   * @param worktableBase the worktable
   * @param guiTabOffset  the offset
   */
  private void calculateInitialTabOffset(BaseBlockEntity worktableBase, GuiTabOffset guiTabOffset) {

    guiTabOffset.setOffset(0);

    List<BaseBlockEntity> actualJoinedTables = worktableBase.getJoinedTables(
        new ArrayList<>(),
        Minecraft.getInstance().player,
        BaseBlockEntity::allowTabs
    );

    boolean tabInView = false;

    while (!tabInView && !actualJoinedTables.isEmpty()) {
      List<BaseBlockEntity> joinedTables = this.getJoinedTableOffsetView(
          actualJoinedTables,
          guiTabOffset.getOffset()
      );

      for (BaseBlockEntity joinedTable : joinedTables) {

        if (joinedTable == worktableBase) {
          tabInView = true;
          break;
        }
      }

      if (!tabInView) {
        guiTabOffset.setOffset(Math.min(
            actualJoinedTables.size() - MAX_TAB_COUNT,
            guiTabOffset.getOffset() + MAX_TAB_COUNT
        ));
      }
    }
  }

  private List<BaseBlockEntity> getJoinedTableOffsetView(
      List<BaseBlockEntity> list,
      int offset
  ) {

    List<BaseBlockEntity> result = new ArrayList<>(MAX_TAB_COUNT);

    if (offset + MAX_TAB_COUNT > list.size()) {
      offset = list.size() - MAX_TAB_COUNT;
    }

    if (offset < 0) {
      offset = 0;
    }

    int limit = Math.min(list.size(), offset + MAX_TAB_COUNT);

    for (int i = offset; i < limit; i++) {
      result.add(list.get(i));
    }

    return result;
  }

}
