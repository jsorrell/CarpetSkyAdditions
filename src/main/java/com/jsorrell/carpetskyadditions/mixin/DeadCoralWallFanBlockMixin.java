package com.jsorrell.carpetskyadditions.mixin;

import com.jsorrell.carpetskyadditions.helpers.DeadCoralToSandHelper;
import com.jsorrell.carpetskyadditions.settings.SkyAdditionsSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.CoralWallFanBlock;
import net.minecraft.block.DeadCoralFanBlock;
import net.minecraft.block.DeadCoralWallFanBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DeadCoralWallFanBlock.class)
public class DeadCoralWallFanBlockMixin extends DeadCoralFanBlock {
  public DeadCoralWallFanBlockMixin(Settings settings) {
    super(settings);
  }

  @Inject(method = "getStateForNeighborUpdate", at = @At(value = "HEAD"))
  private void scheduleTickOnBlockUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos, CallbackInfoReturnable<BlockState> cir) {
    if (SkyAdditionsSettings.coralErosion && !((DeadCoralWallFanBlock) (Object) this instanceof CoralWallFanBlock)) {
      world.scheduleBlockTick(pos, this, DeadCoralToSandHelper.getSandDropDelay(world.getRandom()));
    }
  }
}
