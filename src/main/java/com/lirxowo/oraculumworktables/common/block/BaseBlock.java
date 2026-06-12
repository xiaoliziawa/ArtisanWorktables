package com.lirxowo.oraculumworktables.common.block;

import com.lirxowo.oraculumworktables.client.screen.element.GuiElementTabs;
import com.lirxowo.oraculumworktables.common.container.ContainerProvider;
import com.lirxowo.oraculumworktables.common.recipe.ICraftingMatrixStackHandler;
import com.lirxowo.oraculumworktables.common.reference.EnumTier;
import com.lirxowo.oraculumworktables.common.reference.EnumType;
import com.lirxowo.oraculumworktables.common.tile.BaseBlockEntity;
import com.lirxowo.oraculum.util.FluidHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Containers;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public abstract class BaseBlock
    extends Block
    implements EntityBlock {

  private final EnumType type;

  protected BaseBlock(EnumType type, SoundType soundType, float hardness, float resistance) {

    super(Properties.of()
        .sound(soundType)
        .strength(hardness, resistance)
        .noOcclusion()
    );

    this.type = type;
  }

  public EnumType getType() {

    return this.type;
  }

  protected abstract EnumTier getTier();

  @Nonnull
  @ParametersAreNonnullByDefault
  @Override
  protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult trace) {

    if (world.isClientSide) {
      return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    BlockEntity tileEntity = world.getBlockEntity(pos);

    if (tileEntity instanceof BaseBlockEntity baseBlockEntity) {

      FluidTank tank = baseBlockEntity.getTank();

      if (FluidHelper.drainWaterFromBottle(player, tank)
          || FluidHelper.drainWaterIntoBottle(player, tank)
          || FluidUtil.interactWithFluidHandler(player, hand, tank)) {

        return ItemInteractionResult.SUCCESS;
      }
    }

    return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
  }

  @Nonnull
  @ParametersAreNonnullByDefault
  @Override
  protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult trace) {

    if (world.isClientSide) {
      GuiElementTabs.RECALCULATE_TAB_OFFSETS = true;

    } else {

      BlockEntity tileEntity = world.getBlockEntity(pos);

      if (tileEntity instanceof BaseBlockEntity) {
        ContainerProvider containerProvider = new ContainerProvider(this.getTier(), this.getType(), world, pos);
        player.openMenu(containerProvider, tileEntity.getBlockPos());

      } else {
        throw new IllegalStateException("Invalid tile entity found!");
      }
    }

    return InteractionResult.SUCCESS;
  }

  @ParametersAreNonnullByDefault
  @Override
  protected void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {

    if (!state.is(newState.getBlock())) {
      BlockEntity tileEntity = world.getBlockEntity(pos);

      if (tileEntity instanceof BaseBlockEntity) {
        BaseBlockEntity baseTileEntity = (BaseBlockEntity) tileEntity;

        ItemStackHandler toolHandler = baseTileEntity.getToolHandler();
        ICraftingMatrixStackHandler craftingMatrixHandler = baseTileEntity.getCraftingMatrixHandler();
        ItemStackHandler secondaryOutputHandler = baseTileEntity.getSecondaryOutputHandler();
        IItemHandlerModifiable secondaryIngredientHandler = baseTileEntity.getSecondaryIngredientHandler();

        this.dropItems(world, pos, toolHandler);
        this.dropItems(world, pos, craftingMatrixHandler);
        this.dropItems(world, pos, secondaryOutputHandler);
        this.dropItems(world, pos, secondaryIngredientHandler);
      }

      super.onRemove(state, world, pos, newState, isMoving);
    }
  }

  private void dropItems(Level world, BlockPos pos, IItemHandler handler) {

    for (int i = 0; i < handler.getSlots(); i++) {
      ItemStack itemStack = handler.getStackInSlot(i);

      if (!itemStack.isEmpty()) {
        Containers.dropItemStack(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, itemStack);
      }
    }
  }

  @Nonnull
  @Override
  public PushReaction getPistonPushReaction(@Nonnull BlockState state) {

    return PushReaction.DESTROY;
  }

  @Nullable
  @ParametersAreNonnullByDefault
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> blockEntityType) {

    return (level, pos, blockState, blockEntity) -> {
      if (blockEntity instanceof BaseBlockEntity baseBlockEntity) {
        baseBlockEntity.tick();
      }
    };
  }
}
