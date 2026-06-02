package com.lirxowo.artisanworktables.common.block;

import com.lirxowo.artisanworktables.client.screen.element.GuiElementTabs;
import com.lirxowo.artisanworktables.common.container.ToolboxContainerProvider;
import com.lirxowo.artisanworktables.common.tile.ToolboxBlockEntity;
import com.lirxowo.artisanworktables.common.tile.handler.ToolboxItemStackHandler;
import com.lirxowo.artisanworktables.common.util.Key;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public abstract class ToolboxBaseBlock
    extends Block
    implements EntityBlock {

  public static final VoxelShape VOXEL_SHAPE = Block.box(1, 0, 1, 15, 14, 15);
  public static final ResourceLocation CONTENTS = Key.from("contents");

  public ToolboxBaseBlock(float hardness, float resistance) {

    super(Properties.of()
        .sound(SoundType.WOOD)
        .strength(hardness, resistance)
        .noOcclusion()
    );
  }

  @ParametersAreNonnullByDefault
  @Nonnull
  @Override
  public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {

    return VOXEL_SHAPE;
  }

  @Nullable
  public abstract BlockEntity newBlockEntity(BlockPos pos, BlockState state);

  @Nonnull
  @ParametersAreNonnullByDefault
  @SuppressWarnings("deprecation")
  @Override
  public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult trace) {

    if (world.isClientSide) {
      GuiElementTabs.RECALCULATE_TAB_OFFSETS = true;

    } else {

      BlockEntity tileEntity = world.getBlockEntity(pos);

      if (tileEntity instanceof ToolboxBlockEntity) {

        ToolboxContainerProvider containerProvider = new ToolboxContainerProvider(world, pos);
        NetworkHooks.openScreen((ServerPlayer) player, containerProvider, tileEntity.getBlockPos());

      } else {
        throw new IllegalStateException("Invalid tile entity found!");
      }
    }

    return InteractionResult.SUCCESS;
  }

  @SuppressWarnings("deprecation")
  @Nonnull
  @Override
  public PushReaction getPistonPushReaction(@Nonnull BlockState state) {

    return PushReaction.DESTROY;
  }

  @Nonnull
  @Override
  public List<ItemStack> getDrops(@Nonnull BlockState blockState, LootParams.Builder builder) {

    BlockEntity tileentity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);

    if (tileentity instanceof ToolboxBlockEntity) {
      ToolboxBlockEntity tile = (ToolboxBlockEntity) tileentity;
      ToolboxItemStackHandler itemStackHandler = tile.getItemStackHandler();
      builder = builder.withDynamicDrop(CONTENTS, (consumer) -> {

        for (int i = 0; i < itemStackHandler.getSlots(); ++i) {
          consumer.accept(itemStackHandler.getStackInSlot(i));
        }
      });
    }

    return super.getDrops(blockState, builder);
  }
}
