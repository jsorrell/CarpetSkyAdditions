package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.helpers.DeadCoralToSandHelper;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.CoralFanBlock;
import net.minecraft.block.CoralParentBlock;
import net.minecraft.block.DeadCoralBlock;
import net.minecraft.block.DeadCoralFanBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({DeadCoralBlock.class, DeadCoralFanBlock.class})
public abstract class DeadCoralMixin extends CoralParentBlock {
  public DeadCoralMixin(Settings settings) {
    super(settings);
  }

  @Override
  public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
    if (SkyAdditionsSettings.coralErosion) {
      world.createAndScheduleBlockTick(pos, this, DeadCoralToSandHelper.getSandDropDelay(world.getRandom()));
    }
    super.onBlockAdded(state, world, pos, oldState, notify);
  }

  @Override
  public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
    if (SkyAdditionsSettings.coralErosion && !((CoralParentBlock) this instanceof CoralFanBlock)) {
      world.createAndScheduleBlockTick(pos, this, DeadCoralToSandHelper.getSandDropDelay(world.getRandom()));
    }
    return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
  }

  @Override
  public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
    if (SkyAdditionsSettings.coralErosion) {
      if (DeadCoralToSandHelper.tryDropSand(state, world, pos, random)) {
        world.createAndScheduleBlockTick(pos, this, DeadCoralToSandHelper.getSandDropDelay(random));
      }
    }
  }
}
