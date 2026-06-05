package com.lirxowo.oraculumworktables.datagen;

import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import com.lirxowo.oraculumworktables.common.block.MageBaseBlock;
import com.lirxowo.oraculumworktables.common.block.ToolboxMechanicalBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class BlockStates
    extends BlockStateProvider {

  public BlockStates(PackOutput packOutput, ExistingFileHelper existingFileHelper) {

    super(packOutput, OraculumWorktablesMod.MOD_ID, existingFileHelper);
  }

  @Override
  protected void registerStatesAndModels() {

    for (Block block : OraculumWorktablesMod.getProxy().getRegisteredWorktables()) {

      ResourceLocation registryName = ForgeRegistries.BLOCKS.getKey(block);
      String path = Objects.requireNonNull(registryName).getPath();
      String[] split = path.split("_");
      String tableTier = split[0];
      String tableType = split[1];

      if (block instanceof MageBaseBlock) {
        this.generateMageTable(block, path, tableTier, tableType);

      } else {
        this.generateTable(block, path, tableTier, tableType);
      }
    }

    {
      ModelFile.ExistingModelFile existingFile = this.models()
          .getExistingFile(this.modLoc("block/toolbox"));

      this.simpleBlockItem(OraculumWorktablesMod.Blocks.TOOLBOX, existingFile);
      this.simpleBlock(OraculumWorktablesMod.Blocks.TOOLBOX, existingFile);
    }

    {
      BlockModelBuilder blockModelBuilder = this.models()
          .withExistingParent(ToolboxMechanicalBlock.NAME, this.modLoc("block/toolbox"))
          .texture("horizontal", this.modLoc("block/mechanical_toolbox"))
          .texture("vertical", this.modLoc("block/mechanical_toolbox_top"))
          .texture("particle", this.modLoc("block/mechanical_toolbox"));

      this.simpleBlockItem(OraculumWorktablesMod.Blocks.MECHANICAL_TOOLBOX, blockModelBuilder);
      this.simpleBlock(OraculumWorktablesMod.Blocks.MECHANICAL_TOOLBOX, blockModelBuilder);
    }
  }

  private void generateTable(Block block, String path, String tableTier, String tableType) {

    BlockModelBuilder blockModelBuilder = this.getTableBlockModelBuilder(path, tableTier, tableType, "");

    this.simpleBlockItem(block, blockModelBuilder);
    this.simpleBlock(block, blockModelBuilder);
  }

  private void generateMageTable(Block block, String path, String tableTier, String tableType) {

    BlockModelBuilder blockModelBuilder = this.getTableBlockModelBuilder(path, tableTier, tableType, "");
    BlockModelBuilder blockModelBuilderActive = this.getTableBlockModelBuilder(path + "_active", tableTier, tableType, "_active");

    this.itemModels().getBuilder(path).parent(blockModelBuilder);

    this.getVariantBuilder(block).partialState().with(MageBaseBlock.ACTIVE, false).addModels(new ConfiguredModel(blockModelBuilder));
    this.getVariantBuilder(block).partialState().with(MageBaseBlock.ACTIVE, true).addModels(new ConfiguredModel(blockModelBuilderActive));
  }

  private BlockModelBuilder getTableBlockModelBuilder(String path, String tableTier, String tableType, String suffix) {

    return this.models()
        .withExistingParent(path, this.modLoc("block/" + tableTier))
        .texture("side", this.modLoc("block/" + tableType + "_side" + suffix))
        .texture("top", this.modLoc("block/" + tableType + "_top" + suffix))
        .texture("particle", this.modLoc("block/" + tableType + "_top" + suffix));
  }
}
