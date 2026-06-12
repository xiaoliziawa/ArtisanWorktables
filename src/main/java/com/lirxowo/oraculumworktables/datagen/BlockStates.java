package com.lirxowo.oraculumworktables.datagen;

import com.lirxowo.oraculumworktables.OraculumWorktablesMod;
import com.lirxowo.oraculumworktables.common.block.MageBaseBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Objects;

public class BlockStates
    extends BlockStateProvider {

  private static final String BASIC_TYPE = "basic";
  private static final String BASIC_MODEL_FOLDER = "worktable";

  public BlockStates(PackOutput packOutput, ExistingFileHelper existingFileHelper) {

    super(packOutput, OraculumWorktablesMod.MOD_ID, existingFileHelper);
  }

  @Override
  protected void registerStatesAndModels() {

    for (Block block : OraculumWorktablesMod.getProxy().getRegisteredWorktables()) {

      ResourceLocation registryName = BuiltInRegistries.BLOCK.getKey(block);
      String path = Objects.requireNonNull(registryName).getPath();
      String[] split = path.split("_");
      String tableTier = split[0];
      String tableType = split[1];

      if (block instanceof MageBaseBlock) {
        this.generateMageTable(block, path, tableTier, tableType);

      } else {
        this.generateSimpleBlock(block, this.getTableModel(tableTier, tableType, ""));
      }
    }

    this.generateSimpleBlock(OraculumWorktablesMod.Blocks.TOOLBOX, this.getExistingModel("block/toolbox/toolbox"));
    this.generateSimpleBlock(OraculumWorktablesMod.Blocks.MECHANICAL_TOOLBOX, this.getExistingModel("block/mechanical_toolbox/mechanical_toolbox"));
  }

  private void generateSimpleBlock(Block block, ModelFile.ExistingModelFile model) {

    this.simpleBlockItem(block, model);
    this.simpleBlock(block, model);
  }

  private void generateMageTable(Block block, String path, String tableTier, String tableType) {

    ModelFile.ExistingModelFile model = this.getTableModel(tableTier, tableType, "");
    ModelFile.ExistingModelFile modelActive = this.getTableModel(tableTier, tableType, "_active");

    this.itemModels().getBuilder(path).parent(model);

    this.getVariantBuilder(block)
        .partialState().with(MageBaseBlock.ACTIVE, false).addModels(new ConfiguredModel(model))
        .partialState().with(MageBaseBlock.ACTIVE, true).addModels(new ConfiguredModel(modelActive));
  }

  private ModelFile.ExistingModelFile getTableModel(String tableTier, String tableType, String suffix) {

    String folder = BASIC_TYPE.equals(tableType) ? BASIC_MODEL_FOLDER : tableType;
    return this.getExistingModel("block/" + folder + "/" + tableType + "_" + tableTier + suffix);
  }

  private ModelFile.ExistingModelFile getExistingModel(String path) {

    return this.models().getExistingFile(this.modLoc(path));
  }
}
