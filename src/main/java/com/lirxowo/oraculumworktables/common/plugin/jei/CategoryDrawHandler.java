package com.lirxowo.oraculumworktables.common.plugin.jei;

import com.lirxowo.oraculumworktables.common.recipe.ArtisanRecipe;
import com.lirxowo.oraculumworktables.common.recipe.ArtisanRecipeShapeless;
import com.lirxowo.oraculumworktables.common.recipe.ToolEntry;
import com.lirxowo.oraculumworktables.common.reference.EnumTier;
import com.lirxowo.oraculum.gui.GuiHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.NonNullList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CategoryDrawHandler {

  private static final int TEXT_MARGIN = 5;
  private static final int SECONDARY_STATUS_RIGHT_EDGE = 150;

  private final EnumTier tier;

  public CategoryDrawHandler(EnumTier tier) {

    this.tier = tier;
  }

  public void draw(@Nonnull ArtisanRecipe recipe, GuiGraphics guiGraphics, int backgroundHeight) {

    Minecraft minecraft = Minecraft.getInstance();
    Font font = minecraft.font;

    guiGraphics.pose().pushPose();
    guiGraphics.pose().translate(0, 0, 200);

    this.drawToolDamageStrings(recipe, guiGraphics, font);

    String experienceString = this.getExperienceString(recipe);
    this.drawExperienceString(font, guiGraphics, backgroundHeight, experienceString);
    this.drawSecondaryIngredientString(recipe, guiGraphics, backgroundHeight, font, experienceString);

    guiGraphics.pose().pushPose();
    guiGraphics.pose().scale(0.5f, 0.5f, 1);

    this.drawExtraOutputChanceStrings(recipe, guiGraphics, font);
    this.drawShapelessIndicator(recipe, guiGraphics, minecraft);

    guiGraphics.pose().popPose();
    guiGraphics.pose().popPose();
  }

  @Nullable
  private String getExperienceString(@Nonnull ArtisanRecipe recipe) {

    if (recipe.getExperienceRequired() > 0) {

      if (recipe.consumeExperience()) {
        return I18n.get("jei.oraculumworktables.xp.cost", recipe.getExperienceRequired());

      } else {
        return I18n.get(
            "jei.oraculumworktables.xp.required",
            recipe.getExperienceRequired()
        );
      }

    } else if (recipe.getLevelRequired() > 0) {

      if (recipe.consumeExperience()) {
        return I18n.get("jei.oraculumworktables.level.cost", recipe.getLevelRequired());

      } else {
        return I18n.get("jei.oraculumworktables.level.required", recipe.getLevelRequired());
      }
    }

    return null;
  }

  private void drawToolDamageStrings(@Nonnull ArtisanRecipe recipe, GuiGraphics guiGraphics, Font font) {

    switch (this.tier) {
      case WORKTABLE:
        this.drawToolDamageString(recipe, font, guiGraphics, 83, 52);
        break;
      case WORKSTATION:
        this.drawToolDamageString(recipe, font, guiGraphics, 83, 33);
        break;
      case WORKSHOP:
        this.drawToolDamageString(recipe, font, guiGraphics, 119, 39);
        break;
    }
  }

  private void drawExtraOutputChanceStrings(@Nonnull ArtisanRecipe recipe, GuiGraphics guiGraphics, Font font) {

    int size = Math.min(recipe.getExtraOutputs().size(), 3);

    if (this.tier == EnumTier.WORKSHOP) {

      int xPos = 256;
      int yPos = 12;

      for (int i = 0; i < size; i++) {
        float chance = recipe.getExtraOutputs().get(i).getChance();
        this.drawExtraOutputChanceString(font, guiGraphics, chance, xPos + 36 * i, yPos);
      }

    } else {

      int xPos = 331;
      int yPos = 32;

      for (int i = 0; i < size; i++) {
        float chance = recipe.getExtraOutputs().get(i).getChance();
        this.drawExtraOutputChanceString(font, guiGraphics, chance, xPos, yPos + 36 * i);
      }
    }
  }

  private void drawShapelessIndicator(@Nonnull ArtisanRecipe recipe, GuiGraphics guiGraphics, Minecraft minecraft) {

    if (recipe instanceof ArtisanRecipeShapeless) {

      RenderSystem.enableBlend();

      if (this.tier == EnumTier.WORKSHOP) {
        GuiHelper.drawTexturedRect(minecraft, Plugin.RECIPE_BACKGROUND, guiGraphics.pose(), 288, 58, 18, 17, 0, 0, 0, 1, 1);

      } else {
        GuiHelper.drawTexturedRect(minecraft, Plugin.RECIPE_BACKGROUND, guiGraphics.pose(), 234, 8, 18, 17, 0, 0, 0, 1, 1);
      }
    }
  }

  private void drawExtraOutputChanceString(
      Font font,
      GuiGraphics guiGraphics,
      float secondaryOutputChance,
      int positionX,
      int positionY
  ) {

    String label = (int) (secondaryOutputChance * 100) + "%";
    guiGraphics.drawString(font, label, (int) (positionX - font.width(label) * 0.5f), positionY, 0xFFFFFFFF, false);
  }

  private void drawToolDamageString(ArtisanRecipe recipe, Font font, GuiGraphics guiGraphics, int offsetX, int offsetY) {

    NonNullList<ToolEntry> tools = recipe.getTools();
    int toolCount = tools.size();

    for (int i = 0; i < toolCount; i++) {
      String label = "-" + tools.get(i).getDamage();
      guiGraphics.drawString(
          font,
          label,
          (int) (offsetX - font.width(label) * 0.5f),
          offsetY + (22 * i),
          0xFFFFFFFF,
          false
      );
    }
  }

  private void drawExperienceString(Font font, GuiGraphics guiGraphics, int backgroundHeight, @Nullable String experienceString) {

    if (experienceString == null) {
      return;
    }

    guiGraphics.drawString(
        font,
        experienceString,
        TEXT_MARGIN,
        backgroundHeight - 10,
        0xFF80FF20,
        false
    );
  }

  private void drawSecondaryIngredientString(
      @Nonnull ArtisanRecipe recipe,
      GuiGraphics guiGraphics,
      int backgroundHeight,
      Font font,
      @Nullable String experienceString
  ) {

    if (recipe.getSecondaryIngredients().isEmpty()) {
      return;
    }

    String label = SecondaryIngredientDisplay.label(recipe);
    int x = TEXT_MARGIN;
    int y = backgroundHeight - 10;

    if (experienceString != null) {
      int nextX = TEXT_MARGIN + font.width(experienceString) + 8;

      if (nextX + font.width(label) <= SECONDARY_STATUS_RIGHT_EDGE) {
        x = nextX;
      } else {
        y -= 10;
      }
    }

    guiGraphics.drawString(
        font,
        label,
        x,
        y,
        SecondaryIngredientDisplay.color(recipe),
        false
    );
  }
}
