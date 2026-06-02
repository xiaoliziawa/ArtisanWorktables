package com.lirxowo.artisanworktables.common.block;

import com.lirxowo.artisanworktables.ArtisanWorktablesMod;
import com.lirxowo.artisanworktables.common.reference.EnumType;
import com.lirxowo.artisanworktables.common.tile.BaseBlockEntity;
import com.lirxowo.artisanworktables.common.util.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.ParametersAreNonnullByDefault;

public abstract class MageBaseBlock
    extends BaseBlock {

  public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

  protected MageBaseBlock(EnumType type, SoundType soundType, float hardness, float resistance) {

    super(type, soundType, hardness, resistance);
    this.registerDefaultState(this.stateDefinition.any().setValue(ACTIVE, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {

    builder.add(ACTIVE);
  }

  @ParametersAreNonnullByDefault
  @OnlyIn(Dist.CLIENT)
  @Override
  public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rand) {

    BlockEntity tileEntity = world.getBlockEntity(pos);

    if (tileEntity instanceof BaseBlockEntity) {

      ItemStackHandler toolHandler = ((BaseBlockEntity) tileEntity).getToolHandler();

      boolean hasTool = false;

      for (int i = 0; i < toolHandler.getSlots(); i++) {

        if (!toolHandler.getStackInSlot(i).isEmpty()) {
          hasTool = true;
          break;
        }
      }

      if (hasTool) {
        world.addParticle(
            ParticleTypes.PORTAL,
            pos.getX() + 0.5 + Util.RANDOM.nextFloat() * 0.5 - 0.25,
            pos.getY() + 0.5,
            pos.getZ() + 0.5 + Util.RANDOM.nextFloat() * 0.5 - 0.25,
            0,
            Util.RANDOM.nextFloat(),
            0
        );

        world.addParticle(
            ArtisanWorktablesMod.ParticleTypes.MAGE,
            pos.getX() + 0.5 + Util.RANDOM.nextFloat() * 0.5 - 0.25,
            pos.getY() + 1.5,
            pos.getZ() + 0.5 + Util.RANDOM.nextFloat() * 0.5 - 0.25,
            0,
            Util.RANDOM.nextFloat() * 0.5,
            0
        );
      }
    }
  }
}