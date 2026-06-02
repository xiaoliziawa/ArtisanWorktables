package com.lirxowo.artisanworktables.common.plugin.jei;

import com.lirxowo.artisanworktables.common.recipe.ArtisanRecipe;
import com.lirxowo.artisanworktables.common.recipe.ArtisanRecipeShapeless;
import com.lirxowo.artisanworktables.common.recipe.ToolEntry;
import com.lirxowo.artisanworktables.common.reference.EnumTier;
import com.lirxowo.athenaeum.gui.GuiHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.NonNullList;

import javax.annotation.Nonnull;

public class CategoryDrawHandler {

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
    this.drawExperienceString(recipe, guiGraphics, backgroundHeight, font);

    guiGraphics.pose().pushPose();
    guiGraphics.pose().scale(0.5f, 0.5f, 1);

    this.drawExtraOutputChanceStrings(recipe, guiGraphics, font);
    this.drawShapelessIndicator(recipe, guiGraphics, minecraft);

    guiGraphics.pose().popPose();
    guiGraphics.pose().popPose();
  }

  private void drawExperienceString(@Nonnull ArtisanRecipe recipe, GuiGraphics guiGraphics, int backgroundHeight, Font font) {

    String experienceString = null;

    if (recipe.getExperienceRequired() > 0) {

      if (recipe.consumeExperience()) {
        experienceString = I18n.get("jei.artisanworktables.xp.cost", recipe.getExperienceRequired());

      } else {
        experienceString = I18n.get(
            "jei.artisanworktables.xp.required",
            recipe.getExperienceRequired()
        );
      }

    } else if (recipe.getLevelRequired() > 0) {

      if (recipe.consumeExperience()) {
        experienceString = I18n.get("jei.artisanworktables.level.cost", recipe.getLevelRequired());

      } else {
        experienceString = I18n.get("jei.artisanworktables.level.required", recipe.getLevelRequired());
      }
    }

    if (experienceString != null) {
      this.drawExperienceString(font, guiGraphics, backgroundHeight, experienceString);
    }
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

  private void drawExperienceString(Font font, GuiGraphics guiGraphics, int backgroundHeight, String experienceString) {

    guiGraphics.drawString(
        font,
        experienceString,
        5,
        backgroundHeight - 10,
        0xFF80FF20,
        false
    );
  }
}
