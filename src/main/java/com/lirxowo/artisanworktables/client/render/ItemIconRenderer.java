package com.lirxowo.artisanworktables.client.render;

import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexSorting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import org.joml.Matrix4f;

import java.io.File;
import java.io.IOException;

public final class ItemIconRenderer {

  private static final int GUI_CELL = 16;
  private static final float GUI_NEAR_PLANE = 1000.0F;

  public static void renderToFile(ItemStack stack, int size, File outFile) throws IOException {

    Minecraft minecraft = Minecraft.getInstance();
    Matrix4f previousProjection = new Matrix4f(RenderSystem.getProjectionMatrix());

    TextureTarget target = new TextureTarget(size, size, true, Minecraft.ON_OSX);
    target.setClearColor(0.0F, 0.0F, 0.0F, 0.0F);
    target.clear(Minecraft.ON_OSX);
    target.bindWrite(true);

    float guiFarPlane = ForgeHooksClient.getGuiFarPlane();
    Matrix4f projection = new Matrix4f().setOrtho(0.0F, GUI_CELL, GUI_CELL, 0.0F, GUI_NEAR_PLANE, guiFarPlane);
    RenderSystem.setProjectionMatrix(projection, VertexSorting.ORTHOGRAPHIC_Z);

    PoseStack modelViewStack = RenderSystem.getModelViewStack();
    modelViewStack.pushPose();
    modelViewStack.setIdentity();
    modelViewStack.translate(0.0F, 0.0F, GUI_NEAR_PLANE - guiFarPlane);
    RenderSystem.applyModelViewMatrix();

    Lighting.setupFor3DItems();

    GuiGraphics guiGraphics = new GuiGraphics(minecraft, minecraft.renderBuffers().bufferSource());
    guiGraphics.renderItem(stack, 0, 0);
    guiGraphics.flush();

    modelViewStack.popPose();
    RenderSystem.applyModelViewMatrix();
    target.unbindWrite();

    NativeImage image = new NativeImage(size, size, false);
    RenderSystem.bindTexture(target.getColorTextureId());
    image.downloadTexture(0, false);
    image.flipY();

    minecraft.getMainRenderTarget().bindWrite(true);
    RenderSystem.setProjectionMatrix(previousProjection, VertexSorting.ORTHOGRAPHIC_Z);

    try {
      File parent = outFile.getParentFile();

      if (parent != null) {
        parent.mkdirs();
      }

      image.writeToFile(outFile);

    } finally {
      image.close();
      target.destroyBuffers();
    }
  }
}
