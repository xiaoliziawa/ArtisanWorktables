package com.lirxowo.artisanworktables.common.block;

import com.lirxowo.artisanworktables.client.screen.element.GuiElementTabs;
import com.lirxowo.artisanworktables.common.container.ContainerProvider;
import com.lirxowo.artisanworktables.common.recipe.ICraftingMatrixStackHandler;
import com.lirxowo.artisanworktables.common.reference.EnumTier;
import com.lirxowo.artisanworktables.common.reference.EnumType;
import com.lirxowo.artisanworktables.common.tile.BaseBlockEntity;
import com.lirxowo.oraculum.util.FluidHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

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
  @SuppressWarnings("deprecation")
  @Override
  public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult trace) {

    if (world.isClientSide) {
      GuiElementTabs.RECALCULATE_TAB_OFFSETS = true;

    } else {

      BlockEntity tileEntity = world.getBlockEntity(pos);

      if (tileEntity instanceof BaseBlockEntity) {

        FluidTank tank = ((BaseBlockEntity) tileEntity).getTank();

        if (FluidHelper.drainWaterFromBottle(player, tank)
            || FluidHelper.drainWaterIntoBottle(player, tank)
            || FluidUtil.interactWithFluidHandler(player, hand, tank)) {

          return InteractionResult.SUCCESS;
        }

        ContainerProvider containerProvider = new ContainerProvider(this.getTier(), this.getType(), world, pos);
        NetworkHooks.openScreen((ServerPlayer) player, containerProvider, tileEntity.getBlockPos());

      } else {
        throw new IllegalStateException("Invalid tile entity found!");
      }
    }

    return InteractionResult.SUCCESS;
  }

  @ParametersAreNonnullByDefault
  @Override
  public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {

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

  @SuppressWarnings("deprecation")
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
